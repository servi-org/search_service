package com.template.hexagonal.infrastructure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Clase de configuración para las propiedades JWT personalizadas.
 * Mapea las propiedades del archivo application.yml con el prefijo "jwt".
 */
@Configuration
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
public class JwtProperties {
    
    /**
     * Clave secreta utilizada para firmar los tokens JWT.
     */
    private String secret;
    
    /**
     * Tiempo de expiración del token en milisegundos.
     */
    private Long expiration;
}
