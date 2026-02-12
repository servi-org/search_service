package live.servi.search.application.service;

import com.algolia.api.SearchClient;
import com.algolia.model.search.*;

import live.servi.search.application.port.input.SearchUseCase;
import live.servi.search.domain.exception.DomainException;
import live.servi.search.domain.model.Service;
import live.servi.search.infrastructure.adapter.input.messaging.dto.ServiceSearchResult;
import live.servi.search.infrastructure.adapter.input.messaging.mapper.ServiceEventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Servicio encargado de interactuar con Algolia
 */
@Slf4j
@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class SearchService implements SearchUseCase{
    
    private final ServiceEventMapper serviceEventMapper;
    private final SearchClient searchClient;
    
    @Value("${algolia.index-name:services}")
    private String indexName;
    
    /**
     * Indexa o actualiza un servicio en Algolia.
     * 
     * Si el servicio ya existe (mismo objectID), se actualiza.
     * Si no existe, se crea.
     */
    @Override
    public void indexService(Service serviceData) {
        try {
            // Convertir ServiceData a un Map que Algolia pueda entender
            Map<String, Object> record = new HashMap<>();
            record.put("objectID", serviceData.getId().toString());
            record.put("title", serviceData.getTitle());
            record.put("description", serviceData.getDescription());
            record.put("price", serviceData.getPrice());
            record.put("priceType", serviceData.getPriceType());
            record.put("supplierId", serviceData.getSupplierId());
            record.put("supplierName", serviceData.getSupplierName());
            record.put("categories", serviceData.getCategories());
            record.put("assetUrls", serviceData.getAssetUrls());
            
            // Guardar en Algolia
            SaveObjectResponse response = searchClient.saveObject(indexName, record);
            
            // Esperar a que Algolia termine de indexar
            searchClient.waitForTask(indexName, response.getTaskID());
            
            log.info("Servicio indexado en Algolia: objectID={}, title='{}'", 
                serviceData.getId(), serviceData.getTitle());
                
        } catch (Exception e) {
            log.error("Error al indexar servicio en Algolia: {}", e.getMessage(), e);
            throw new DomainException("INTERNAL_SERVER_ERROR", "Error al indexar en Algolia", 500);
        }
    }
    
    /**
     * Elimina un servicio de Algolia.
     */
    @Override
    public void deleteService(UUID serviceId) {
        try {
            DeletedAtResponse response = searchClient.deleteObject(indexName, serviceId.toString());
            
            // Esperar a que Algolia termine de procesar
            searchClient.waitForTask(indexName, response.getTaskID());
            
            log.info("Servicio eliminado de Algolia: objectID={}", serviceId);
            
        } catch (Exception e) {
            log.error("Error al eliminar servicio de Algolia: {}", e.getMessage(), e);
            throw new DomainException("INTERNAL_SERVER_ERROR", "Error al eliminar de Algolia", 500);
        }
    }
    
    /**
     * Busca servicios en Algolia.
     * 
     * Esta búsqueda es la que usará el frontend cuando el usuario escriba en el buscador.
     */
    @Override
    public List<Service> searchServices(String query, Integer hitsPerPage, Integer page) {
        try {
            // Configurar parámetros de búsqueda
            Integer pageSize = hitsPerPage != null ? hitsPerPage : 20;
            Integer pageNumber = page != null ? page : 0;
            
            // Realizar búsqueda en Algolia
            SearchResponses<ServiceSearchResult> response = searchClient.search(
                new SearchMethodParams().addRequests(
                    new SearchForHits()
                        .setIndexName(indexName)
                        .setQuery(query)
                        .setHitsPerPage(pageSize)
                        .setPage(pageNumber)
                ),
                ServiceSearchResult.class
            );
            
            Integer hitCount = response.getResults().isEmpty() ? 0 : 
                ((SearchResponse<ServiceSearchResult>) response.getResults().get(0)).getHits().size();
            log.info("Búsqueda en Algolia: query='{}', hits={}", 
                query, hitCount);
            
            return response.getResults().isEmpty() ? List.of() : 
                ((SearchResponse<ServiceSearchResult>) response.getResults().get(0)).getHits().stream()
                    .map(serviceEventMapper::toDomain)
                    .toList();
            
        } catch (Exception e) {
            log.error("Error al buscar en Algolia: {}", e.getMessage(), e);
            throw new DomainException("INTERNAL_SERVER_ERROR", "Error al buscar en Algolia", 500);
        }
    }
}
