package me.photomap.web.service;

import org.junit.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.InputStream;

/**
 * Created by craigbrookes on 23/12/14.
 */
public class FileServiceTest extends IntegrateBase {


    @Mock AmqpService amqpService;
    @Autowired @InjectMocks FileService fileService;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown(){
        File f = new File("/opt/pictures/testuser/test.jpg");
        f.delete();
        f = new File("/opt/pictures/testuser/test.zip");
        f.delete();
    }

    @Test
    public void testSaveMultipartFile()throws Exception{
        InputStream fs = FileService.class.getResourceAsStream("/files/IMG_TEST.JPG");
        MockMultipartFile file = new MockMultipartFile("test.jpg", "test.jpg", MediaType.IMAGE_JPEG_VALUE,fs);
        Assert.assertNotNull(file);
        String path = fileService.saveMultipartFileToDisk(file,"testuser");
        Assert.assertNotNull(path);
        Assert.assertTrue(path.equals("/opt/pictures/testuser/test.jpg"));
    }

    @Test
    public void testSaveMultipartZipFile()throws Exception{
        InputStream fs = FileService.class.getResourceAsStream("/files/IMG_TEST.JPG.zip");
        MockMultipartFile file = new MockMultipartFile("test.zip", "test.zip", MediaType.APPLICATION_OCTET_STREAM_VALUE,fs);
        Assert.assertNotNull(file);
        String path = fileService.saveMultipartFileToDisk(file,"testuser");
        Assert.assertNotNull(path);
        Assert.assertTrue(path.equals("/opt/pictures/testuser/test.zip"));
    }
}


