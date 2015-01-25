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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


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



    public void publishPicUploadedMessage(String filePath , String fileName)throws IOException{
        Map<String,String> message = new HashMap<String, String>();
        String resKey = "key" +  new Date().getTime() + Math.floor(Math.random() * 10000000)+"key";
        message.put("file",filePath);
        message.put("name",fileName);
        message.put("resKey",resKey);

        Channel ch = null;
        try {
            String jsonMessage = mapper.writeValueAsString(message);
            ch = amqpConnection.createChannel();
            ch.queueDeclare(PICS_QUE, true, false, false, null);
            ch.basicPublish("", PICS_QUE, MessageProperties.PERSISTENT_TEXT_PLAIN, jsonMessage.getBytes());
        }catch (IOException e){
            log.warn("failed to declare or publish message ",e);
            throw new AmqpMessagingException("service unreachable. picture could not be processed. try again later",e);
        }finally {
            if(null != ch){
                ch.close();
            }
        }


    }



}
