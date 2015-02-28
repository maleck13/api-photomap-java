package me.photomap.web.config;

import me.photomap.web.data.repo.model.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by craigbrookes on 21/12/14.
 */
@Configuration
public class RedisConfig {

  @Autowired
  Environment env;

  @Bean(name = "sessionTemplate")
  RedisTemplate<String, Session> jedisTemplate() {
    RedisTemplate<String, Session> jt = new RedisTemplate<>();
    jt.setConnectionFactory(jedisConnectionFactory());
    jt.setValueSerializer(new Jackson2JsonRedisSerializer<Session>(Session.class));
    return jt;

  }

  @Bean(name = "amqpRedisMessageTemplate")
  RedisTemplate<String, HashMap> jedisMapTemplate() {
    RedisTemplate<String, HashMap> jt = new RedisTemplate<>();
    jt.setConnectionFactory(jedisConnectionFactory());
    jt.setValueSerializer(new Jackson2JsonRedisSerializer<>(HashMap.class));
    return jt;
  }

  @Bean
  JedisConnectionFactory jedisConnectionFactory() {
    String redisHost = env.getProperty("redis.host");
    String redisPass = env.getProperty("redis.pass");
    JedisConnectionFactory jdc = new JedisConnectionFactory();
    jdc.setUsePool(true);
    jdc.setHostName(redisHost);
    jdc.setPassword(redisPass);
    return jdc;

  }


}
