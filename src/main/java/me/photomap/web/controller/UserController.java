package me.photomap.web.controller;

import me.photomap.web.annotations.OpenAccess;
import me.photomap.web.config.AppConfig;
import me.photomap.web.data.repo.UserRepo;
import me.photomap.web.data.repo.model.LoginDetails;
import me.photomap.web.data.repo.model.Session;
import me.photomap.web.data.repo.model.User;
import me.photomap.web.http.filters.UserAwareHttpRequest;
import me.photomap.web.service.FileService;
import me.photomap.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;


@Controller
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepo userRepo;
    @Autowired private FileService fileService;


    @RequestMapping(value = "/user/register",method = RequestMethod.POST, produces = AppConfig.JSON, consumes = AppConfig.JSON)
    @OpenAccess
    @ResponseBody public User register(@RequestBody @Valid User user) throws Exception{
        User newUser = userService.registerUser(user);
        fileService.setUpUserDirs(newUser);
        return newUser;

    }

    @RequestMapping(value = "/user/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @OpenAccess
    @ResponseBody public Session login(@RequestBody @Valid LoginDetails login)throws Exception{
        User user = new User();
        user.setEmail(login.getEmail());
        user.setPassword(login.getPassword());
        return userService.loginUser(user);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/user/logout",method = RequestMethod.POST)
    @OpenAccess
    @ResponseBody public void logOut(UserAwareHttpRequest req){
        if(null != req.getUser()){
            userService.logoutUser(req.getUser());
        }
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public Map handleDuplicate(DuplicateKeyException e){
        Map<String,String> res = new HashMap<String, String>();
        res.put("error",e.getMessage());
        return res;
    }

}
