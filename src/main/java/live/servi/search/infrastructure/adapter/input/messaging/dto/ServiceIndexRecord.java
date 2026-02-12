package live.servi.search.infrastructure.adapter.input.messaging.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ServiceIndexRecord{
    private String objectID;
    private String title;
    private String description;
    private Double price;
    private String priceType;
    private String supplierId;
    private String supplierName;
    private Integer rating;
    private String category;
    private List<String> assetUrls;
}