package me.photomap.web.service;

import me.photomap.web.config.WebConfig;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by craigbrookes on 23/12/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextHierarchy({
        @ContextConfiguration(name="dispatcher",  classes = WebConfig.class,loader = AnnotationConfigWebContextLoader.class)
})
public abstract class IntegrateBase {
    public static final String FILE_PATH = "file.disk.location";
    static {
        System.setProperty("environment.name", "dev");
        System.setProperty(FILE_PATH, "/tmp");
    }
}
