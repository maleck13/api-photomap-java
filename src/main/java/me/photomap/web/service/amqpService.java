package me.photomap.web.service;

import com.rabbitmq.client.*;
import me.photomap.web.service.exceptions.AmqpMessagingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.*;


@Service
public class AmqpService {

    @Autowired
    ConnectionFactory amqpConnectionFactory;

    Connection amqpConnection;

    Logger log = LoggerFactory.getLogger(AmqpService.class);

    ObjectMapper mapper = new ObjectMapper();

    private static final  String PICS_QUE = "pics";


    @PostConstruct
    public void setUp(){
        try{
            System.out.println("setting up rabbitmq connection");
            log.info("setting up rabbitmq connection");
            amqpConnection = amqpConnectionFactory.newConnection();
//            Channel ch = amqpConnection.createChannel();
//            ch.queueDeclare("jobResponsesQueue",true,false,false,null);
//            ch.queueBind()

        }catch (IOException e){
            System.out.println("failed setting up rabbitmq connection");
            e.printStackTrace();
            log.warn("failed to setup connection to rabbit ", e);
        }
    }

    @PreDestroy
    public void tearDown(){
        if(null != amqpConnection){
            try {
                log.info("closing connection to rabbit ");
                amqpConnection.close();
            }catch(IOException e){
                log.warn("failed to close connection to rabbit ", e);
            }
        }
    }



    public String publishPicUploadedMessage(String filePath , String fileName, String userName)throws IOException{
        Map<String,String> message = new HashMap<String, String>();
        String resKey = UUID.randomUUID().toString();
        message.put("file",filePath);
        message.put("name",fileName);
        message.put("resKey",resKey);
        message.put("user",userName);

        Channel ch = null;
        try {
            String jsonMessage = mapper.writeValueAsString(message);
            ch = amqpConnection.createChannel();
            ch.queueDeclare(PICS_QUE, true, false, false, null);
            ch.queueDeclare(resKey,true,false,false,null);

            ch.basicPublish("", PICS_QUE, MessageProperties.PERSISTENT_TEXT_PLAIN, jsonMessage.getBytes());
        }catch (IOException e){
            log.warn("failed to declare or publish message ",e);
            throw new AmqpMessagingException("service unreachable. picture could not be processed. try again later",e);
        }finally {
            if(null != ch){
                ch.close();
            }
        }

        return resKey;

    }

    public void removeQueue(String queueName){
        Channel ch = null;
        try {
            ch = amqpConnection.createChannel();
            ch.queuePurge(queueName);
            ch.queueDelete(queueName);
        }catch (Exception e){
            log.warn("failed to remove  que " + queueName ,e);
        }finally {
            try {
                if(null != ch) {
                    ch.close();
                }
            }catch(IOException e){
                log.warn("failed to close rabbitmq connection" , e);
            }
        }
    }

    public List<QueueingConsumer.Delivery> readResponseQue(String jobId)throws AmqpMessagingException{

        Channel ch = null;
        QueueingConsumer.Delivery d = null;
        List<QueueingConsumer.Delivery> lm = new ArrayList<>();
        try {
            ch = amqpConnection.createChannel();
            QueueingConsumer c = new QueueingConsumer(ch);
            ch.basicConsume(jobId,true,c);

            while (null != ( d = c.nextDelivery(1000))){
                lm.add(d);
            }

        }catch(Exception e){
            log.warn("failed to consume message from que ",e);
            throw new AmqpMessagingException("service unreachable. Unexpected error",e);
        }finally {

            if(null != ch) {
                try {
                    ch.close();
                }catch(IOException e){
                    //dont report to client just log to be investigated
                    log.warn("failed to close rabbitmq connection" , e);
                }
            }

        }
        return lm;
    }



}
