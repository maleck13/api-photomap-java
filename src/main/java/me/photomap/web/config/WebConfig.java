

package me.photomap.web.config;


import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3Client;
import me.photomap.web.http.interceptors.AccessInterceptor;
import me.photomap.web.http.interceptors.UserResolver;
import me.photomap.web.me.photomap.web.task.TaskFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@EnableWebMvc
@Configuration
class WebConfig extends WebMvcConfigurerAdapter {

    @Value("${s3.enabled}") Boolean S3_ENABLED;


    @Bean
    public CommonsMultipartResolver multipartResolver(){
        CommonsMultipartResolver res = new CommonsMultipartResolver();
        res.setMaxUploadSize(10000000); //10Mb
        return res;
    }


    @Bean
    public UserResolver userResolver(){
        return new UserResolver();
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userResolver());
        registry.addInterceptor(new AccessInterceptor());
    }

    @Bean
    public AmazonS3Client amazonS3Client(){

        AmazonS3Client s3 = null;
        if(S3_ENABLED) {
         s3 = new AmazonS3Client(new ProfileCredentialsProvider("/etc/photomap/.aws/credentials", "default"));
        }
        return s3;
    }

    @Bean
    public TaskFactory taskFactory(){
        TaskFactory.registerTask(TaskFactory.QUEUE_CLEANER);
        TaskFactory t = new TaskFactory();
        return t;
    }

}
