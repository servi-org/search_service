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
                .categories(service.getCategories().stream()
                        .map(Enum::name)
                        .toList())
                .assetUrls(service.getAssetUrls())
                .build();
    }
}
