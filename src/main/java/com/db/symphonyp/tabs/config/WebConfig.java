package com.db.symphonyp.tabs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/bundle-local.json").allowedOrigins("*");
                registry.addMapping("/bundle-linode.json").allowedOrigins("*");
                registry.addMapping("/bundle-admin.json").allowedOrigins("*");
            }
        };
    }
}
