package com.kht.backend.config;

import com.kht.backend.filter.ResponseInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    private final long MAX_AGE_SECS = 36000;
    @Bean
    public ResponseInterceptor responseInterceptor(){
        return new ResponseInterceptor();
    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("HEAD", "OPTIONS", "GET", "POST", "PUT", "PATCH", "DELETE")
                .maxAge(MAX_AGE_SECS);
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(responseInterceptor())
                .addPathPatterns("/**")
                .order(1);//指定执行顺序，数值越小越优先
        /*registry.addInterceptor(new SecondInterceptor())
                .addPathPatterns("/hello")
                .order(2);//指定执行顺序，数值越小越优先*/
    }
}
