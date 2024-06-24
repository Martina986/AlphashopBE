package com.xantrix.webapp;

import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class FiltersCorsConfig implements WebMvcConfigurer {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry
                        .addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("PUT","DELETE","GET","POST","OPTIONS","HEAD","PATCH")
                        .allowedHeaders("*")
                        .exposedHeaders("header1","header2","Authorization")
                        .allowCredentials(false)
                        .maxAge(3600);
            }
        };
    }
}
