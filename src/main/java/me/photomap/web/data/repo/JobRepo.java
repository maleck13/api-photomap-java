package me.photomap.web.data.repo;

import me.photomap.web.data.repo.model.Job;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by craigbrookes on 14/02/15.
 */
@Repository
public interface JobRepo extends MongoRepository<Job,String>{
}
