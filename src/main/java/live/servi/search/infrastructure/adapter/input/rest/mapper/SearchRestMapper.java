package live.servi.search.infrastructure.adapter.input.rest.mapper;

import org.springframework.stereotype.Component;

import live.servi.search.domain.model.Service;
import live.servi.search.infrastructure.adapter.input.rest.dto.SearchResponse;

@Component
public class SearchRestMapper {
    public SearchResponse toResponse(Service service) {
        return SearchResponse.builder()
                .serviceId(service.getId())
                .title(service.getTitle())
                .description(service.getDescription())
                .price(service.getPrice())
                .priceType(service.getPriceType())
                .supplierId(service.getSupplierId().toString())
                .supplierName(service.getSupplierName())
                .rating(service.getRating())
                .category(service.getCategory().name())
                .assetUrls(service.getAssetUrls())
                .build();
    }
}
