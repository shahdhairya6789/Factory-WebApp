package com.example.demo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**") // Apply to all endpoints
//                        .allowedOrigins("http://localhost:5173", "http://127.0.0.1:8080") // Allow localhost
//                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allowed HTTP methods
//                        .allowedHeaders("*") // Allow all headers
//                        .allowedHeaders("Authorization", "Cache-Control", "Content-Type")
//                        .allowCredentials(true); // Allow cookies/auth headers
//            }
//        };
//    }
}
