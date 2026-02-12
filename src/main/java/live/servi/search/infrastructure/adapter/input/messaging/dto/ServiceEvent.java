package live.servi.search.infrastructure.adapter.input.messaging.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO que representa un evento recibido desde Kafka
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceEvent {
    
    private EventAction action; 
    private UUID serviceId;
    private ServiceData serviceData;
    private Long timestamp;
    
    public enum EventAction {
        CREATE,
        UPDATE,
        DELETE
    }
}
