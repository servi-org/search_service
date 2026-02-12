package live.servi.search.infrastructure.config;

import com.algolia.api.SearchClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Algolia Search Client
 */
@Configuration
public class AlgoliaConfig {
    
    @Value("${algolia.application-id}")
    private String applicationId;
    
    @Value("${algolia.api-key}")
    private String apiKey;
    
    /**
     * Crea el cliente de Algolia
     * Este cliente se inyecta en los servicios que necesiten interactuar con Algolia
     */
    @Bean
    public SearchClient searchClient() {
        return new SearchClient(applicationId, apiKey);
    }
}
