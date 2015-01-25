package me.photomap.web.service;

import me.photomap.web.data.repo.UserRepo;
import me.photomap.web.data.repo.model.Session;
import me.photomap.web.data.repo.model.User;
import me.photomap.web.service.exceptions.AuthenticationFailure;
import me.photomap.web.service.exceptions.LoginFailedException;
import me.photomap.web.service.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


@Service
public class UserService {


    private static final String PASSWORD_SALT = "password.salt";
    private static final String SESSION_TIMOUT = "session.timeout";

    @Autowired
    Environment env;

    @Autowired
    UserRepo userRepo;

    @Autowired
    @Qualifier(value = "sessionTemplate")
    RedisTemplate<String,Session> redis;

    Logger log = LoggerFactory.getLogger(UserService.class);

    public User registerUser(User user)throws Exception{
        String hashPass = generateHashedSaltedPassword(user.getPassword());
        user.setPassword(hashPass);
        return userRepo.save(user);
    }

    private String generateHashedSaltedPassword(String pass)throws Exception{
        String salt = env.getProperty(PASSWORD_SALT);
        MessageDigest m = MessageDigest.getInstance("SHA-256");
        String sb = salt+pass+salt+salt;
        m.update(sb.getBytes());
        return javax.xml.bind.DatatypeConverter.printHexBinary(m.digest());
    }

    public Session loginUser(User user) throws Exception{
        User u = userRepo.findByEmail(user.getEmail());
        Session s = new Session();
        if(null == u){
            throw new ResourceNotFoundException();
        }
        if(u.getPassword().equals(generateHashedSaltedPassword(user.getPassword()))){
            String sessionId = UUID.randomUUID().toString();
            String userId = u.getId().toString();
            s.setSessionId(sessionId);
            s.setUserId(userId);
        }else{
            throw new LoginFailedException("incorrect userid password");
        }
        int timeout = env.getProperty(SESSION_TIMOUT,Integer.TYPE);
        redis.delete(u.getId().toString());
        redis.opsForValue().set(u.getId().toString(), s);
        redis.expire(u.getId().toString(),timeout, TimeUnit.SECONDS);
        return s;


    }

    public void logoutUser(User u){
        redis.delete(u.getId().toString());
    }

    public User verifyUserAndSession(String userId, String sessionId)throws AuthenticationFailure{
        if(null == userId){return null;}
        if(null == sessionId){return null;}
        Session s = new Session();
        s.setSessionId(sessionId);
        s.setUserId(userId);

        Session redisSession  = (Session) redis.opsForValue().get(userId);

        log.info("redis session is " + redisSession + " userid " + userId);


        if(null != redisSession && sessionId.equals(redisSession.getSessionId())){
            int timeout = env.getProperty(SESSION_TIMOUT,Integer.TYPE);
            redis.expire(userId, timeout, TimeUnit.SECONDS);
            return userRepo.findOne(userId);
        }
        return null;

    }



}
