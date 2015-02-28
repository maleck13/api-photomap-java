package me.photomap.web.amqp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;
import me.photomap.web.service.AmqpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;


@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class JobMessageConsumer extends Observable implements MessageConsumer {

  @Autowired
  private SimpMessagingTemplate simpMessagingTemplate;
  private Channel channel;
  private String topic;

  Logger log = LoggerFactory.getLogger(JobMessageConsumer.class);

  private ObjectMapper mapper = new ObjectMapper();

  @Override
  public void setChannel(Channel ch) {
    channel = ch;
  }

  @Override
  public void setTopic(String topic) {
    this.topic = topic;
  }

  private double id;

  public JobMessageConsumer() {
    id = Math.random();
    System.out.print("\n\n\n I WAS CREATED " + id + " \n\n\n");
  }

  @Override
  public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
    Map<String, String> message = mapper.readValue(body, HashMap.class);

    String jobid = message.get("Jobid");
    String status = message.get("Status");
    log.info(mapper.writeValueAsString(message));
    long deliveryTag = envelope.getDeliveryTag();
    simpMessagingTemplate.convertAndSend("/queue/jobupdate/" + jobid, message);
    channel.basicAck(deliveryTag, false);
    if ("complete".equalsIgnoreCase(status) || "error".equalsIgnoreCase(status)) {
      channel.queueUnbind(topic, "amq.topic", topic);
      channel.queueDelete(topic, false, true);
      channel.close();
    }
  }
}
