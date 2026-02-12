package live.servi.search.infrastructure.adapter.input.rest;

import live.servi.search.application.port.input.SearchUseCase;
import live.servi.search.domain.model.Service;
import live.servi.search.infrastructure.adapter.input.rest.dto.SearchResponse;
import live.servi.search.infrastructure.adapter.input.rest.mapper.SearchRestMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para búsqueda de servicios
 */
@Slf4j
@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {
    
    private final SearchUseCase searchService;
    private final SearchRestMapper restMapper;
    
    /**
     * Endpoint para buscar servicios
     * 
     * GET /search?q=plomero&hitsPerPage=20&page=0
     */
    @GetMapping
    public ResponseEntity<List<SearchResponse>> searchServices(
        @RequestParam(value = "q", required = false, defaultValue = "") String query,
        @RequestParam(value = "hitsPerPage", required = false) Integer hitsPerPage,
        @RequestParam(value = "page", required = false) Integer page
    ) {
        log.info("Búsqueda solicitada: query='{}', hitsPerPage={}, page={}", 
            query, hitsPerPage, page);
        
        List<Service> response = searchService.searchServices(query, hitsPerPage, page);
        
        List<SearchResponse> rest = new ArrayList<SearchResponse>();
        for (Service service : response) {
            rest.add(restMapper.toResponse(service));
        }
        
        return ResponseEntity.ok(rest);
    }
}
