package me.photomap.web.data.repo.model;

import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "queues")
public class Queue {
  @Id
  private ObjectId id;

  @Indexed(unique = true)
  @NotEmpty
  private String queueId;

  private Date created = new Date();

  public ObjectId getId() {
    return id;
  }

  public void setId(ObjectId id) {
    this.id = id;
  }

  public String getQueueId() {
    return queueId;
  }

  public void setQueueId(String queueId) {
    this.queueId = queueId;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }
}
