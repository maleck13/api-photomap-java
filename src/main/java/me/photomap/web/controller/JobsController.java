package me.photomap.web.controller;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import me.photomap.web.service.AmqpService;
import me.photomap.web.service.exceptions.AmqpMessagingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;


@Controller
public class JobsController {

  @Autowired
  private AmqpService amqpService;

  ObjectMapper mapper = new ObjectMapper();

  Logger log = LoggerFactory.getLogger(JobsController.class);

  private static final String STATUS_KEY = "Status";

  @Autowired SimpMessagingTemplate simpMessagingTemplate;



  @MessageMapping("/update/{jobid}")
  public void greeting(@DestinationVariable("jobid") String jobid, Map<String,String> message) throws Exception {
    try {
      amqpService.consumeJobQue(jobid);
    }catch (Exception e){
      e.printStackTrace();
      simpMessagingTemplate.convertAndSend("/queue/jobupdate/" + jobid, "{\"error\":\"unable to get job update\"}");
    }
  }

  @MessageExceptionHandler
  public void messageException(Throwable exc){
    exc.printStackTrace();
    System.out.println("exception with messages "+exc.getMessage());
  }

}
