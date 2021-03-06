/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.photomap.web.config;

import com.mongodb.*;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoFactoryBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Arrays;

/**
 * @author craigbrookes
 */

@Configuration
@EnableMongoRepositories(basePackages = {"me.photomap.web.data.repo"})
public class MongoConfig extends AbstractMongoConfiguration {

  Logger log = LoggerFactory.getLogger(getClass());
  @Autowired
  Environment env;


  @PostConstruct
  public void init() {
    System.out.println("");
  }


  @Bean
  @Override
  public Mongo mongo() throws Exception {

    int port = env.getProperty("mongodb.dbport", Integer.class);
    String host = env.getProperty("mongodb.dbhostname");
    String user = env.getProperty("mongodb.dbusername");
    String pass = env.getProperty("mongodb.dbpassword");
    MongoCredential credential = MongoCredential.createMongoCRCredential(user, "admin", pass.toCharArray());
    MongoClient m = new MongoClient(new ServerAddress(host), Arrays.asList(credential));
    return m;
  }

  @Override
  protected String getDatabaseName() {
    return env.getProperty("mongodb.name");
  }

  @Override
  protected UserCredentials getUserCredentials() {
    String user = env.getProperty("mongodb.dbusername");
    String pass = env.getProperty("mongodb.dbpassword");
    System.out.println("user : " + user + " pass : " + pass);

    return new UserCredentials(user, pass);
  }

  @Bean
  @Override
  public MongoTemplate mongoTemplate() throws Exception {


    MongoTemplate m;

      m = new MongoTemplate(mongo(), getDatabaseName());

    return m;

  }


}

