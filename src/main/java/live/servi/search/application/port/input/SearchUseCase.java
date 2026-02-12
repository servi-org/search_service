package live.servi.search.application.port.input;

import java.util.List;
import java.util.UUID;

import live.servi.search.domain.model.Service;

public interface SearchUseCase {
    void indexService(Service serviceData);
    void deleteService(UUID serviceId);
    List<Service> searchServices(String query, Integer hitsPerPage, Integer page);
}
