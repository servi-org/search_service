package live.servi.search.infrastructure.adapter.input.messaging.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * DTO con los datos del servicio que se indexará en Algolia
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceData {
    
    private UUID id;
    private String title;
    private String description;
    private Double price;
    private String priceType;
    private UUID supplierId;
    private String supplierName;
    private List<String> categories;
    private List<String> assetUrls;
    
}
