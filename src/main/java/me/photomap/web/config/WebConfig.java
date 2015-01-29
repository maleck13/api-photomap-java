

package me.photomap.web.config;


import me.photomap.web.http.interceptors.AccessInterceptor;
import me.photomap.web.http.interceptors.UserResolver;
import org.apache.commons.codec.Charsets;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@EnableWebMvc
@Configuration
class WebConfig extends WebMvcConfigurerAdapter {


    @Bean
    public CommonsMultipartResolver multipartResolver(){
        CommonsMultipartResolver res = new CommonsMultipartResolver();
        res.setMaxUploadSize(5000000);
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
}
