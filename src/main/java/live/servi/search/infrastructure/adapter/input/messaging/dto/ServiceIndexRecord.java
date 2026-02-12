package live.servi.search.infrastructure.adapter.input.messaging.dto;

import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ServiceIndexRecord{
    private UUID objectID;
    private String title;
    private String description;
    private Double price;
    private String priceType;
    private UUID supplierId;
    private String supplierName;
    private Integer rating;
    private String category;
    private List<String> assetUrls;
}