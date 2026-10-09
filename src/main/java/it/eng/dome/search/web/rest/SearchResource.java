package it.eng.dome.search.web.rest;

import it.eng.dome.search.domain.IndexingObject;
import it.eng.dome.search.rest.web.util.PaginationUtil;
import it.eng.dome.search.service.IndexingService;
import it.eng.dome.search.service.ResultProcessor;
import it.eng.dome.search.service.SearchProcessor;
import it.eng.dome.search.service.dto.SearchRequest;
import it.eng.dome.tmforum.tmf620.v4.model.ProductOffering;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SearchResource {

	@Autowired
	private SearchProcessor searchProcessor;

	@Autowired
	private ResultProcessor resultProcessor;

	@Autowired
	private IndexingService indexingService;

	// search 2.0 - Improvement Feb 2026
	// Caso con query
	@PostMapping("/SearchProduct")
	public ResponseEntity<List<ProductOffering>> searchProduct(
			@RequestParam(value = "query", required = false) String query,
			@RequestParam(value = "lifecycleStatus", defaultValue = "Launched") String lifecycleStatus,
			@RequestBody SearchRequest request,
			Pageable pageable) {
		return executeSearch(query, lifecycleStatus, request, pageable);
	}

	// // Caso senza query
	// @PostMapping("/SearchProduct")
	// public ResponseEntity<List<ProductOffering>> searchProductNoQuery(
	// 		@RequestParam(value = "lifecycleStatus", defaultValue = "Launched") String lifecycleStatus,
	// 		@RequestBody SearchRequest request,
	// 		Pageable pageable) {
	// 	return executeSearch(null, lifecycleStatus, request, pageable);
	// }

	// Metodo privato di supporto per non duplicare la logica
	private ResponseEntity<List<ProductOffering>> executeSearch(String query, String lifecycleStatus, SearchRequest request, Pageable pageable) {
		
		String effectiveStatus = (lifecycleStatus == null || lifecycleStatus.trim().isEmpty())
            ? "Launched"
            : lifecycleStatus.trim();
			
		Map<Page<IndexingObject>, Map<IndexingObject, Float>> resultPage = searchProcessor.searchAllFields(query,
				effectiveStatus, request, pageable);
		
		Page<ProductOffering> pageProduct = resultProcessor.processResultsWithScore(resultPage, pageable);

		UriComponentsBuilder pathBuilder = UriComponentsBuilder
		.fromPath("/api/SearchProduct")
		.queryParam("lifecycleStatus", effectiveStatus);

		if (query != null && !query.trim().isEmpty()) {
			pathBuilder.queryParam("query", query);
		}

		String path = pathBuilder.build().encode().toUriString();

		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageProduct, path);

		return new ResponseEntity<>(pageProduct.getContent(), headers, HttpStatus.OK);
	}

	// @PostMapping("/SearchProduct")
    // public ResponseEntity<List<ProductOffering>> searchProduct(
    //         @RequestParam(name = "query", required = false) String query, // Usa ?query=...
    //         @RequestBody SearchRequest request,
    //         Pageable pageable) {
        
    //     Map<Page<IndexingObject>, Map<IndexingObject, Float>> resultPage = searchProcessor.searchAllFields(query, request, pageable);
    //     // ... resto del codice identico ...
    // }

	// //search 2.0 - Improvement Feb 2025
	// @PostMapping(value = "/SearchProduct/{query}")
	// public ResponseEntity<List<ProductOffering>> searchProduct (@PathVariable
	// String query, @RequestBody SearchRequest request, Pageable pageable){
	// Map<Page<IndexingObject>, Map<IndexingObject, Float>> resultPage =
	// searchProcessor.searchAllFields(query, request, pageable);
	// Page<ProductOffering> pageProduct =
	// resultProcessor.processResultsWithScore(resultPage, pageable);
	// HttpHeaders headers =
	// PaginationUtil.generatePaginationHttpHeaders(pageProduct,
	// "/api/SearchProduct/" + query);
	// return new ResponseEntity<>(pageProduct.getContent(), headers,
	// HttpStatus.OK);
	// }

	// @PostMapping(value = "/SearchProductByFilterCategory")
	// public ResponseEntity<List<ProductOffering>>
	// searchProductByFilterCategory(@RequestBody SearchRequest request,
	// Pageable pageable) {
	// Page<IndexingObject> page = searchProcessor.search(request, pageable);
	// Page<ProductOffering> pageProduct = resultProcessor.processResults(page,
	// pageable);
	// HttpHeaders headers =
	// PaginationUtil.generatePaginationHttpHeaders(pageProduct,
	// "/api/SearchProductByFilterCategory");
	// return new ResponseEntity<>(pageProduct.getContent(), headers,
	// HttpStatus.OK);
	// }

	@GetMapping("/offerings/clearRepository")
	public ResponseEntity<?> clearRepository() {
		indexingService.clearRepository();
		// return (ResponseEntity<?>) ResponseEntity.ok();
		return ResponseEntity.noContent().build();
	}
}
