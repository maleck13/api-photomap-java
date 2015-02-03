/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.photomap.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.photomap.web.data.repo.PicturesRepo;
import me.photomap.web.data.repo.model.Picture;
import me.photomap.web.data.repo.model.User;
import me.photomap.web.http.filters.UserAwareHttpRequest;
import me.photomap.web.service.AmqpService;
import me.photomap.web.service.FileService;
import me.photomap.web.service.exceptions.FileException;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author craigbrookes
 */
@Controller
public class PictureController {
    
    @Autowired PicturesRepo picRepo;
    @Autowired AmqpService amqpService;
    @Autowired private FileService fileService;


    Logger log = LoggerFactory.getLogger(PictureController.class);


    @RequestMapping(value = "/pictures/range",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody public List<Integer> range(UserAwareHttpRequest req){
        User u = req.getUser();
        return picRepo.yearRange(u.getUserName());
    }
    

    @RequestMapping(value = "/pictures",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody public List<Picture> list(UserAwareHttpRequest req){
        User u = req.getUser();
        return picRepo.findAllByUser(u.getUserName());
    }
    

    @RequestMapping("/pictures/{from}/{to}")
    @ResponseBody public List<Picture> listFromTo(@PathVariable("from") int  from , @PathVariable("to") int to ,UserAwareHttpRequest req){
        User u = req.getUser();
        return picRepo.finAllInDateRange(from, to, u.getUserName());
    }


    @ResponseStatus(HttpStatus.OK)
    @RequestMapping("pictures/upload")
    @ResponseBody public Map<String,String> upload(MultipartFile file, MultipartHttpServletRequest req)throws FileException{

        User u = (User) req.getAttribute(UserAwareHttpRequest.USER_ATTRIBUTE);

        String jobKey = fileService.saveMultipartFileToDisk(file,u.getUserName());
        Map<String,String> res = new HashMap<>();
        res.put("key",jobKey);
        return res;

    }

    @RequestMapping(value = "/picture",method = RequestMethod.GET,produces = MediaType.IMAGE_JPEG_VALUE )
    @ResponseBody public byte[] serveImg(UserAwareHttpRequest request, @RequestParam("file")String file)throws Exception{
        User u = request.getUser();
        File f = fileService.loadFile(file,u);
        FileInputStream in = new FileInputStream(f);
        return IOUtils.toByteArray(in);

    }
    
}
