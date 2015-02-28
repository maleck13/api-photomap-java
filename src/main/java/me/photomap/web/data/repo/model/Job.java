package me.photomap.web.data.repo.model;

import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by craigbrookes on 14/02/15.
 */
@Document
public class Job {

  @NotEmpty
  private String jobType;
  @NotEmpty
  private String jobId;
  @Id
  private ObjectId id;
  @NotEmpty
  private HashMap<String,String> message;

  private Date created = new Date();

  public String getJobType() {
    return jobType;
  }

  public void setJobType(String jobType) {
    this.jobType = jobType;
  }

  public String getJobId() {
    return jobId;
  }

  public void setJobId(String jobId) {
    this.jobId = jobId;
  }

  public ObjectId getId() {
    return id;
  }

  public void setId(ObjectId id) {
    this.id = id;
  }

  public HashMap<String, String> getMessage() {
    return message;
  }

  public void setMessage(HashMap<String, String> message) {
    this.message = message;
  }

  public Date getCreated() {
    return created;
  }

  public void setCreated(Date created) {
    this.created = created;
  }
}
