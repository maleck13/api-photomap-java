

package me.photomap.web.config;


import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3Client;
import me.photomap.web.http.interceptors.AccessInterceptor;
import me.photomap.web.http.interceptors.UserResolver;
import me.photomap.web.task.TaskFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@ComponentScan(basePackages = "me.photomap.web")
@PropertySource({
    "classpath:/photomap/me/config/common.properties",
    "classpath:/photomap/me/config/services.${environment.name}.properties"})
@EnableWebMvc
@Import({WebsocketConfig.class,RedisConfig.class,RabbitmqConfig.class})
public class WebConfig extends WebMvcConfigurerAdapter {


  @Bean
  public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
    return new PropertySourcesPlaceholderConfigurer();
  }


  public static final String JSON = MediaType.APPLICATION_JSON_VALUE;

  @Value("${s3.enabled}")
  Boolean S3_ENABLED;


  @Bean
  public CommonsMultipartResolver multipartResolver() {
    CommonsMultipartResolver res = new CommonsMultipartResolver();
    res.setMaxUploadSize(10000000); //10Mb
    return res;
  }


  @Bean
  public UserResolver userResolver() {
    return new UserResolver();
  }


  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(userResolver());
    registry.addInterceptor(new AccessInterceptor());
  }

  @Bean
  public AmazonS3Client amazonS3Client() {

    AmazonS3Client s3 = null;
    if (S3_ENABLED) {
      s3 = new AmazonS3Client(new ProfileCredentialsProvider("/etc/photomap/.aws/credentials", "default"));
    }
    return s3;
  }

  @Bean
  public TaskFactory taskFactory() {
    TaskFactory.registerTask(TaskFactory.QUEUE_CLEANER);
    TaskFactory t = new TaskFactory();
    return t;
  }


}
