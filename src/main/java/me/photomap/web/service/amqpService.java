package me.photomap.web.service;

import com.rabbitmq.client.*;
import me.photomap.web.amqp.JobMessageConsumer;
import me.photomap.web.amqp.MessageConsumer;
import me.photomap.web.amqp.ProxyConsumer;
import me.photomap.web.amqp.ReplyMessageCollector;
import me.photomap.web.data.repo.QueueRepo;
import me.photomap.web.data.repo.model.*;
import me.photomap.web.data.repo.model.Queue;
import me.photomap.web.service.exceptions.AmqpMessagingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.*;


@Service
public class AmqpService {

  @Autowired
  ConnectionFactory amqpConnectionFactory;

  private
  @Autowired
  ReplyMessageCollector replyMessageCollector;

  private
  @Autowired
  ApplicationContext ctx;

  private
  @Autowired
  JobMessageConsumer jobMessageConsumer;

  Connection amqpConnection;

  Logger log = LoggerFactory.getLogger(AmqpService.class);

  ObjectMapper mapper = new ObjectMapper();

  private Map<String, Channel> jobChannels = new HashMap<>(1024);

  private static final String PICS_QUE = "pics";
  public static final String JOB_LOG_QUE = "jobs_log";


  @PostConstruct
  public void setUp() {
    try {
      System.out.println("setting up rabbitmq connection");
      log.info("setting up rabbitmq connection");

      amqpConnection = amqpConnectionFactory.newConnection();
      Channel ch = amqpConnection.createChannel();
      ch.queueDeclare(PICS_QUE, true, false, false, null);
      ch.queueDeclare(JOB_LOG_QUE, true, false, false, null);
      setUpSubscribers();
    } catch (IOException e) {
      System.out.println("failed setting up rabbitmq connection");
      e.printStackTrace();
      log.warn("failed to setup connection to rabbit ", e);
    }
  }


  public void setUpSubscribers() throws IOException {

    Channel ch = amqpConnection.createChannel();
    ch.queueBind(JOB_LOG_QUE, "amq.topic", "picjob.#");
    ProxyConsumer consumer = new ProxyConsumer(ch, replyMessageCollector);
    ch.basicConsume(JOB_LOG_QUE, true, consumer);
  }

  @PreDestroy
  public void tearDown() {
    if (null != amqpConnection) {
      try {
        log.info("closing connection to rabbit ");
        amqpConnection.close();
      } catch (IOException e) {
        log.warn("failed to close connection to rabbit ", e);
      }
    }
  }


  public String publishPicUploadedMessage(String filePath, String fileName, String userName, String resKey) throws IOException {
    Map<String, String> message = new HashMap<String, String>();
    message.put("file", filePath);
    message.put("name", fileName);
    message.put("resKey", resKey);
    message.put("user", userName);

    Channel ch = null;
    try {
      String jsonMessage = mapper.writeValueAsString(message);
      ch = amqpConnection.createChannel();
      ch.basicPublish("", PICS_QUE, MessageProperties.PERSISTENT_TEXT_PLAIN, jsonMessage.getBytes());
    } catch (IOException e) {
      log.warn("failed to declare or publish message ", e);
      throw new AmqpMessagingException("service unreachable. picture could not be processed. try again later", e);
    } finally {
      if (null != ch) {
        ch.close();
      }
    }

    return resKey;

  }

  public void setUpJobQueue(String jobId) throws IOException {
    Channel ch = null;
    ch = amqpConnection.createChannel();
    String topic = "picjob.update." + jobId;
    Map<String, Object> args = new HashMap<>();
    args.put("x-expires", 60000 * 5); // if the queue is left idle for 5mns it will be deleted
    ch.queueDeclare(topic, true, false, false, args);
    ch.queueBind(topic, "amq.topic", topic);
    jobChannels.put(jobId, ch);


  }

  private Channel getJobChannel(String jobId) throws IOException {
    Channel ch;
    if (jobChannels.containsKey(jobId)) {
      ch = jobChannels.get(jobId);
      jobChannels.remove(jobId);
      if (!ch.isOpen()) {
        ch = amqpConnection.createChannel();
      }
    } else {
      ch = amqpConnection.createChannel();
    }
    return ch;
  }

  public void consumeJobQue(String jobId) throws IOException {

    Channel ch = getJobChannel(jobId);
    String topic = "picjob.update." + jobId;
    MessageConsumer mc = ctx.getBean(JobMessageConsumer.class);
    mc.setChannel(ch);
    mc.setTopic(topic);
    ProxyConsumer pc = new ProxyConsumer(ch, mc);
    ch.basicConsume(topic, pc);
  }


}
