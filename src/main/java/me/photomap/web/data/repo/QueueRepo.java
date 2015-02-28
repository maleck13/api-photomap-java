package me.photomap.web.data.repo;

import me.photomap.web.data.repo.model.Queue;
import me.photomap.web.data.repo.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface QueueRepo extends MongoRepository<Queue, String> {

  public List<Queue> findByCreatedLessThan(Date date);

  public List<Queue> deleteQueueByCreatedLessThan(Date date);
}
