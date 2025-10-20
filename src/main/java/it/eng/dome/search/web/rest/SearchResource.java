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

	//search 2.0 - Improvement Feb 2025
	@PostMapping(value = "/SearchProduct/{query}")
	public ResponseEntity<List<ProductOffering>> searchProduct (@PathVariable String query, @RequestBody SearchRequest request, Pageable pageable){
		Map<Page<IndexingObject>, Map<IndexingObject, Float>> resultPage = searchProcessor.searchAllFields(query, request, pageable);
		Page<ProductOffering> pageProduct = resultProcessor.processResultsWithScore(resultPage, pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageProduct, "/api/SearchProduct/" + query);
		return new ResponseEntity<>(pageProduct.getContent(), headers, HttpStatus.OK);
	}
	
	@PostMapping(value = "/SearchProductByFilterCategory")
	public ResponseEntity<List<ProductOffering>> searchProductByFilterCategory(@RequestBody SearchRequest request, Pageable pageable){
		Page<IndexingObject> page = searchProcessor.search(request, pageable);
		Page<ProductOffering> pageProduct = resultProcessor.processResults(page, pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageProduct, "/api/SearchProductByFilterCategory");
		return new ResponseEntity<>(pageProduct.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/offerings/clearRepository")
	public ResponseEntity<?> clearRepository() {
		indexingService.clearRepository();
		//return (ResponseEntity<?>) ResponseEntity.ok();
		return  ResponseEntity.noContent().build();
	}
}
