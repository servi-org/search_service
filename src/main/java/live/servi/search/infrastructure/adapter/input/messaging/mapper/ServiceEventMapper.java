package live.servi.search.infrastructure.adapter.input.messaging.mapper;

import live.servi.search.domain.model.Category;
import live.servi.search.domain.model.Service;
import live.servi.search.infrastructure.adapter.input.messaging.dto.ServiceData;
import live.servi.search.infrastructure.adapter.input.messaging.dto.ServiceIndexRecord;

import org.springframework.stereotype.Component;

@Component
public class ServiceEventMapper {
    public Service toDomain(ServiceIndexRecord result) {
        return Service.builder()
                .id(result.getObjectID())
                .title(result.getTitle())
                .description(result.getDescription())
                .price(result.getPrice())
                .priceType(result.getPriceType())
                .supplierId(result.getSupplierId())
                .supplierName(result.getSupplierName())
                .rating(result.getRating())
                .category(Category.valueOf(result.getCategory()))
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
                .rating(data.getRating())
                .category(Category.valueOf(data.getCategory()))
                .assetUrls(data.getAssetUrls())
                .build();
    }
}
