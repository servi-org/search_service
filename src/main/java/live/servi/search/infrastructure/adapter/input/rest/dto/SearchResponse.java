package live.servi.search.infrastructure.adapter.input.rest.dto;

import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchResponse {
    private UUID serviceId;
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
