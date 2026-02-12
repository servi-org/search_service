package live.servi.search.infrastructure.adapter.input.messaging;

import live.servi.search.application.port.input.SearchUseCase;
import live.servi.search.domain.model.Service;
import live.servi.search.infrastructure.adapter.input.messaging.dto.ServiceEvent;
import live.servi.search.infrastructure.adapter.input.messaging.mapper.ServiceEventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Listener de Kafka que escucha eventos de servicios
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ServiceEventListener {
    
    private final SearchUseCase searchService;
    private final ServiceEventMapper eventMapper;
    
    /**
     * Este método se ejecuta automaticamente cada vez que llega un mensaje al topic.
     * Deserializar el JSON a ServiceEvent
     * Llamar a este método con el evento
     * Confirmar (commit) el offset si todo sale bien
     */
    @KafkaListener(
        topics = "${kafka.topic.service-events:service-events}",
        groupId = "${spring.kafka.consumer.group-id:search-service-group}",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleServiceEvent(ServiceEvent event) {
        log.info("Evento recibido de Kafka: action={}, serviceId={}", 
            event.getAction(), event.getServiceId());
            Service service = eventMapper.toDomain(event.getServiceData()); 
        
        try {
            switch (event.getAction()) {
                case CREATE:
                    log.info("Procesando CREATE -> indexando servicio en Algolia");
                    searchService.indexService(service);
                    break;
                    
                case UPDATE:
                    log.info("Procesando UPDATE -> actualizando servicio en Algolia");
                    searchService.indexService(service);
                    break;
                    
                case DELETE:
                    log.info("Procesando DELETE -> eliminando servicio de Algolia");
                    searchService.deleteService(event.getServiceId());
                    break;
                    
                default:
                    log.warn("Acción desconocida: {}", event.getAction());
            }
            
            log.info("Evento procesado exitosamente");
            
        } catch (Exception e) {
            log.error("Error al procesar evento de Kafka: {}", e.getMessage(), e);
            // implementar logica de retry o enviar a un DLQ (Dead Letter Queue)
            throw e; // Re-lanzar la excepción para que Kafka no haga commit del offset
        }
    }
}
