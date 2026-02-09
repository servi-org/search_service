package live.servi.search.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * Clase de configuración para las propiedades CORS personalizadas.
 * Mapea las propiedades del archivo application.yml con el prefijo "cors".
 */
@Configuration
@ConfigurationProperties(prefix = "cors")
@Data
public class CorsProperties {
    @NotEmpty
    private String allowedOrigins;
}
