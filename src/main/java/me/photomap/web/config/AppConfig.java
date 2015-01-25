

package me.photomap.web.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.MediaType;

@Configuration
@ComponentScan(basePackages = "me.photomap.web")
@PropertySource({
        "classpath:/photomap/me/config/common.properties",
        "classpath:/photomap/me/config/services.${environment.name}.properties"})
public class AppConfig {

        public static final String JSON = MediaType.APPLICATION_JSON_VALUE;

}
