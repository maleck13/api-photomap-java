package me.photomap.web.service;

import me.photomap.web.service.exceptions.FileException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * Created by craigbrookes on 21/12/14.
 */
@Service
public class FileService {

    @Autowired private Environment env;
    @Autowired AmqpService amqpService;


    private static final String FILE_DISK_PATH = "file.disk.location";

    public String saveMultipartFileToDisk(MultipartFile file,String userName) throws FileException{
        if(file.isEmpty()){
            throw new FileException("file is empty no content received");
        }

        String location = env.getProperty(FILE_DISK_PATH);
        String fileName = file.getName();
        location += "/" + userName + "/" + fileName;
        File fileDest = new File(location);

        try {
            if(! fileDest.getParentFile().exists()){
                fileDest.getParentFile().mkdirs();
            }
            file.transferTo(fileDest);
            //send out the message over rabbitmq
            amqpService.publishPicUploadedMessage(location,fileName);
        }catch (Exception e){
            //a number of checked exceptions can be thrown so convert to simple file exception
            throw new FileException(e.getMessage(),e);
        }
        return location;
    }

}
