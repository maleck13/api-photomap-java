package me.photomap.web.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import me.photomap.web.data.repo.model.User;
import me.photomap.web.service.exceptions.FileException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Created by craigbrookes on 21/12/14.
 */
@Service
public class FileService {

  @Autowired
  private Environment env;
  @Autowired
  private AmqpService amqpService;
  @Autowired
  private AmazonS3Client amazonS3Client;
  @Value("${file.disk.location}")
  String DISK_FILE_LOC;
  @Value("${s3.enabled}")
  Boolean S3_ENABLED;


  private static final String PIC_DIR = "pictures";
  private static final String COMPLETED_DIR = "completed";
  private static final String THUMBS_DIR = "thumbs";

  public Map<String, String> getUserDirs(User user) {
    Map<String, String> dirMap = new HashMap<>();
    dirMap.put(PIC_DIR, DISK_FILE_LOC + "/" + user.getUserName() + "/" + PIC_DIR);
    dirMap.put(COMPLETED_DIR, DISK_FILE_LOC + "/" + user.getUserName() + "/" + COMPLETED_DIR);
    dirMap.put(THUMBS_DIR, DISK_FILE_LOC + "/" + user.getUserName() + "/" + THUMBS_DIR);
    return dirMap;
  }

  public String getUserDir(User user, String dirName) {
    return getUserDirs(user).get(dirName);
  }

  public Boolean userDirsExist(User user) {
    Map<String, String> userDirs = getUserDirs(user);
    Set<String> keySet = userDirs.keySet();
    for (String dir : keySet) {
      File userDir = new File(userDirs.get(dir));
      if (!userDir.exists()) {
        return false;
      }
    }
    return true;
  }

  public void setUpUserDirs(User user) throws FileException {
    if (userDirsExist(user)) return;
    Map<String, String> dirMap = getUserDirs(user);
    Set<String> keySet = dirMap.keySet();
    for (String dir : keySet) {
      File f = new File(dirMap.get(dir));
      if (!f.exists()) {
        f.mkdirs();
      }
    }
  }

  public String saveMultipartFileToDisk(MultipartFile file, String userName) throws FileException {
    if (file.isEmpty()) {
      throw new FileException("file is empty no content received");
    }


    String fileName = file.getOriginalFilename();
    String shortPath = userName + "/" + fileName;
    String location = DISK_FILE_LOC + "/" + shortPath;
    File fileDest = new File(location);
    String resKey = UUID.randomUUID().toString();

    try {
      file.transferTo(fileDest);
      //send out the message over rabbitmq
      amqpService.setUpJobQueue(resKey);
      amqpService.publishPicUploadedMessage(shortPath, fileName, userName,resKey);
    } catch (Exception e) {
      //a number of checked exceptions can be thrown so convert to simple file exception
      throw new FileException(e.getMessage(), e);
    }
    return resKey;
  }

  public FileResource loadFile(String file, User user) throws FileNotFoundException {

    if (S3_ENABLED) {
      String s3Path = user.getUserName() + "/thumbs/" + file;
      S3Object obj = amazonS3Client.getObject("photo-map", s3Path);
      FileResource res = new FileResource(obj.getObjectContent(), obj.getObjectMetadata().getContentLength(), obj.getObjectMetadata().getContentType());
      return res;
    } else {
      String dirLoc = getUserDir(user, THUMBS_DIR);
      String location = dirLoc + "/" + file;
      File f = new File(location);
      if (!f.exists() && f.canRead()) {
        throw new FileNotFoundException(file);
      }
      FileResource res = new FileResource(new FileInputStream(f), f.length(), MediaType.IMAGE_JPEG_VALUE);
      return res;
    }


  }

  public class FileResource {
    private InputStream in;
    private long contentLengh;
    private String contentType;

    public FileResource(InputStream in, long contentLengh, String contentType) {
      this.in = in;
      this.contentLengh = contentLengh;
      this.contentType = contentType;
    }

    public InputStream getContentStream() {
      return in;
    }

    public long getContentLengh() {
      return contentLengh;
    }

    public String getContentType() {
      return contentType;
    }
  }

}
