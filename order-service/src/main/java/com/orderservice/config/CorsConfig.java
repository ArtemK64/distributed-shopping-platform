package com.orderservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {

            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins(
                            "http://localhost",
                            "http://localhost:5000",
                            "http://localhost:5002"
                        )
                        .allowedMethods("POST", "OPTIONS")
                        .allowedHeaders(
                            "Origin",
                            "Content-Type",
                            "Accept",
                            "X-Requested-With"
                        )
                        .allowCredentials(false)
                        .maxAge(1800);
            }
        };
    }
}
