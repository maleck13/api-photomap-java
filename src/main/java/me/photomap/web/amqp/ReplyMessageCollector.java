package me.photomap.web.amqp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import me.photomap.web.data.repo.JobRepo;
import me.photomap.web.data.repo.model.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;


@Component
public class ReplyMessageCollector implements MessageConsumer {

  private @Autowired JobRepo jobRepo;


  private ObjectMapper mapper = new ObjectMapper();

  @Override
  public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
     HashMap<String,String> message = mapper.readValue(body,HashMap.class);
    System.out.println("recieved message to ReplyMessageCollector");
//    Job j = new Job();
//    j.setJobId(message.get("Jobid"));
//    j.setJobType(message.get("Type"));
//    j.setMessage(message);
//    jobRepo.save(j);
  }

  @Override
  public void setChannel(Channel ch) {

  }

  @Override
  public void setTopic(String topic) {

  }
}
