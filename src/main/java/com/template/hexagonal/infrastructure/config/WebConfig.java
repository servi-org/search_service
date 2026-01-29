package com.template.hexagonal.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * config de CORS
 */
@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // aplicar a todos los endpoints
                        .allowedOrigins("http://localhost:3000", "http://localhost:4200") // origenes permitidos
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS") // metodos permitidos
                        .allowedHeaders("*") // headers permitidos
                        .allowCredentials(true); // permite enviar cookies
            }
        };
    }
}
