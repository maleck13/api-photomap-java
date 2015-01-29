package me.photomap.web.service;

import com.sun.org.apache.xpath.internal.operations.Bool;
import me.photomap.web.data.repo.model.User;
import me.photomap.web.service.exceptions.FileException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by craigbrookes on 21/12/14.
 */
@Service
public class FileService {

    @Autowired private Environment env;
    @Autowired private AmqpService amqpService;
    @Value("${file.disk.location}") String diskLocation;


    private static final String FILE_DISK_PATH = "file.disk.location";

    private static final String PIC_DIR = "pictures";
    private static final String COMPLETED_DIR = "completed";
    private static final String THUMBS_DIR = "thumbs";

    public Map<String,String> getUserDirs(User user){
        Map<String,String> dirMap = new HashMap<>();
        dirMap.put(PIC_DIR,diskLocation + "/" + user.getUserName() + "/" + PIC_DIR);
        dirMap.put(COMPLETED_DIR,diskLocation + "/" + user.getUserName() + "/" + COMPLETED_DIR);
        dirMap.put(THUMBS_DIR,diskLocation + "/" + user.getUserName() + "/" + THUMBS_DIR);
        return dirMap;
    }

    public Boolean userDirsExist(User user){
        Map<String,String> userDirs = getUserDirs(user);
        Set<String> keySet = userDirs.keySet();
        for(String dir : keySet){
            File userDir = new File(userDirs.get(dir));
            if(! userDir.exists()){
                return false;
            }
        }
        return true;
    }

    public void setUpUserDirs(User user)throws FileException{
        if(userDirsExist(user))return;
        Map<String, String> dirMap = getUserDirs(user);
        Set<String> keySet = dirMap.keySet();
        for(String dir : keySet){
            File f = new File(dirMap.get(dir));
            if(!f.exists()){
                f.mkdirs();
            }
        }
    }

    public String saveMultipartFileToDisk(MultipartFile file,String userName) throws FileException{
        if(file.isEmpty()){
            throw new FileException("file is empty no content received");
        }

        String location = env.getProperty(FILE_DISK_PATH);
        String fileName = file.getOriginalFilename();
        String shortPath = userName + "/" + fileName;
        location += "/" + shortPath;
        File fileDest = new File(location);

        try {
            if(! fileDest.getParentFile().exists()){
                fileDest.getParentFile().mkdirs();
            }
            file.transferTo(fileDest);
            //send out the message over rabbitmq
            amqpService.publishPicUploadedMessage(shortPath,fileName);
        }catch (Exception e){
            //a number of checked exceptions can be thrown so convert to simple file exception
            throw new FileException(e.getMessage(),e);
        }
        return location;
    }

}
