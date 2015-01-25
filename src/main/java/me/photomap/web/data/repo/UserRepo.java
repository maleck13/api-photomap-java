package me.photomap.web.data.repo;

import me.photomap.web.data.repo.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by craigbrookes on 21/12/14.
 */

@Repository
public interface UserRepo extends MongoRepository<User,String> {

    public User findByEmail(String email);

}
