package me.photomap.web.config;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;


@Configuration
public class RabbitmqConfig {

  @Autowired
  Environment env;

  @Bean(name = "rabbitMqConnectionFactory")
  public ConnectionFactory rabbitMqConnectionFactory() {
    ConnectionFactory cf = new ConnectionFactory();
    String host = env.getProperty("rabbitmq.host");
    String user = env.getProperty("rabbitmq.pics.user");
    String pass = env.getProperty("rabbitmq.pics.pass");
    cf.setHost(host);
    cf.setUsername(user);
    cf.setPassword(pass);
    cf.setAutomaticRecoveryEnabled(true);
    cf.setNetworkRecoveryInterval(2000);
    return cf;
  }

}
