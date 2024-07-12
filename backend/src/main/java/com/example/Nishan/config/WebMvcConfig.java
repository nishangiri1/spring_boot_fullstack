package com.example.Nishan.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

//    @Value("#{'${cors.allowed-origin}'.split(',')}")
//    private List<String> allowedOrigin;
//
//    @Value("#{'${cors.allowed-method}'.split(',')}")
//    private List<String> allowedMethod;
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        CorsRegistration corsRegistration = registry.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedMethods("*");

    }
}
