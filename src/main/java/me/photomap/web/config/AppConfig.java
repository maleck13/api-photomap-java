

package me.photomap.web.config;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.MediaType;

@Configuration
@ComponentScan(basePackages = "me.photomap.web")
@PropertySource({
        "classpath:/photomap/me/config/common.properties",
        "classpath:/photomap/me/config/services.${environment.name}.properties"})
public class AppConfig {


        /**
         *
         * note this is required to be registered so that you can use ${} @Value for properties
         */

        @Bean
        public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer(){
                return new PropertySourcesPlaceholderConfigurer();
        }


        public static final String JSON = MediaType.APPLICATION_JSON_VALUE;

}
