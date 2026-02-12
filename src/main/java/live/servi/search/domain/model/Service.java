package live.servi.search.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * DTO con los datos del servicio
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Service {
    
    private UUID id;
    private String title;
    private String description;
    private Double price;
    private String priceType;
    private UUID supplierId;
    private String supplierName;
    private List<Category> categories;
    private List<String> assetUrls;
    
}
