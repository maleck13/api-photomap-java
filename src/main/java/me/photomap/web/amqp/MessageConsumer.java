package me.photomap.web.amqp;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Envelope;

import java.io.IOException;

/**
 * Created by craigbrookes on 14/02/15.
 */
public interface MessageConsumer {

  public void setChannel(Channel ch);

  public void setTopic(String topic);

  public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException;
}
