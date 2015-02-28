package me.photomap.web.amqp;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;


public class ProxyConsumer extends DefaultConsumer {

  private MessageConsumer messageConsumer;

  public ProxyConsumer(Channel channel,MessageConsumer mc) {
    super(channel);
    this.messageConsumer = mc;
  }

  @Override
  public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
    messageConsumer.handleDelivery(consumerTag,envelope,properties,body);
  }
}
