package live.servi.search.infrastructure.adapter.input.messaging.mapper;

import live.servi.search.domain.model.Category;
import live.servi.search.domain.model.Service;
import live.servi.search.infrastructure.adapter.input.messaging.dto.ServiceData;
import live.servi.search.infrastructure.adapter.input.messaging.dto.ServiceSearchResult;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class ServiceEventMapper {
    public Service toDomain(ServiceSearchResult result) {
        return Service.builder()
                .id(UUID.fromString(result.getObjectID()))
                .title(result.getTitle())
                .description(result.getDescription())
                .price(result.getPrice())
                .priceType(result.getPriceType())
                .supplierId(UUID.fromString(result.getSupplierId()))
                .supplierName(result.getSupplierName())
                .categories(result.getCategories().stream()
                        .map(Category::valueOf)
                        .collect(Collectors.toList()))
                .assetUrls(result.getAssetUrls())
                .build();
    }

    public Service toDomain(ServiceData data) {
        return Service.builder()
                .id(data.getId())
                .title(data.getTitle())
                .description(data.getDescription())
                .price(data.getPrice())
                .priceType(data.getPriceType())
                .supplierId(data.getSupplierId())
                .supplierName(data.getSupplierName())
                .categories(data.getCategories().stream()
                        .map(Category::valueOf)
                        .collect(Collectors.toList()))
                .assetUrls(data.getAssetUrls())
                .build();
    }
}
