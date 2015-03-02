/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.photomap.web.data.repo;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.util.List;

import me.photomap.web.data.repo.model.Picture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

/**
 * @author craigbrookes
 */
@Repository
public class PicturesRepo {
  @Autowired
  MongoTemplate mongoTemp;

  private static final String COLLECTION = "pictures";

  public List<Integer> yearRange(String user) {
    DBObject q = new BasicDBObject("user", user);
    return mongoTemp.getCollection(COLLECTION).distinct("year", q);
  }

  public List<Picture> findAllByUser(String user) {
    DBObject q = new BasicDBObject("user", user);
    Query query = new BasicQuery(q);
    return mongoTemp.find(query, Picture.class);
  }

  public List<Picture> finAllInDateRange(long from, long to, String user) {

    Query q = new Query(Criteria.where("timestamp").gte(from).andOperator(Criteria.where("timestamp").lt(to),Criteria.where("user").is(user)));
    return mongoTemp.find(q, Picture.class);
  }
}
