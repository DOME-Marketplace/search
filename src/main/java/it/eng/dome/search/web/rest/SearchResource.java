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
import org.springframework.web.bind.annotation.*;

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
	@PostMapping("/SearchProduct/{query}")
	public ResponseEntity<List<ProductOffering>> searchProductWithQuery(
			@PathVariable String query,
			@RequestBody SearchRequest request,
			Pageable pageable) {
		return executeSearch(query, request, pageable);
	}

	// Caso senza query
	@PostMapping("/SearchProduct")
	public ResponseEntity<List<ProductOffering>> searchProductNoQuery(
			@RequestBody SearchRequest request,
			Pageable pageable) {
		return executeSearch(null, request, pageable);
	}

	// Metodo privato di supporto per non duplicare la logica
	private ResponseEntity<List<ProductOffering>> executeSearch(String query, SearchRequest request,
			Pageable pageable) {
		Map<Page<IndexingObject>, Map<IndexingObject, Float>> resultPage = searchProcessor.searchAllFields(query,
				request, pageable);
		Page<ProductOffering> pageProduct = resultProcessor.processResultsWithScore(resultPage, pageable);

		String path = (query != null) ? "/api/SearchProduct/" + query : "/api/SearchProduct";
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
