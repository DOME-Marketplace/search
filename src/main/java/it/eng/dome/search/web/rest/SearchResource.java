package it.eng.dome.search.web.rest;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.eng.dome.search.domain.IndexingObject;
import it.eng.dome.search.domain.ProductOffering;
import it.eng.dome.search.rest.web.util.PaginationUtil;
import it.eng.dome.search.service.ResultProcessor;
import it.eng.dome.search.service.SearchProcessor;
import it.eng.dome.search.service.dto.SearchRequest;

@RestController
@RequestMapping("/api")
public class SearchResource {

	@Autowired
	private SearchProcessor searchProcessor;
	
	@Autowired
	private ResultProcessor resultProcessor;

//	// @ApiPageable
//	@GetMapping(value = "/SearchByKeywords/{query}")
//	public ResponseEntity<List<IndexingObject>> search(@PathVariable String query,
//			/*
//			 * @PageableDefault(sort = {"productOfferingName.keyword"}, direction =
//			 * Sort.Direction.ASC, size = 50)
//			 */ Pageable pageable) {
//
//		Page<IndexingObject> page = searchProcessor.search(query, pageable);
//		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/SearchByKeywords/" + query);
//		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
//
//		// List<IndexingObject> objList = page.getContent();
//		// return ResponseEntity.ok(objList);
//
//	}

	//search 0.1 - PoC March 2024
	@GetMapping(value = "/SearchProductsByKeywords/{query}")
	public ResponseEntity<List<ProductOffering>> searchProductOfferings(@PathVariable String query,
			/*
			 * @PageableDefault(sort = {"productOfferingName.keyword"}, direction =
			 * Sort.Direction.ASC, size = 50)
			 */ Pageable pageable) {

		Page<IndexingObject> page = searchProcessor.search(query, pageable);
		Page<ProductOffering> pageProduct = resultProcessor.processResults(page, pageable);
		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageProduct, "/api/SearchProductsByKeywords/" + query);
		return new ResponseEntity<>(pageProduct.getContent(), headers, HttpStatus.OK);

		// List<IndexingObject> objList = page.getContent();
		// return ResponseEntity.ok(objList);

	}

	//search 1.0 - First Review July 2024
//	@PostMapping(value = "/SearchProduct/{query}")
//	public ResponseEntity<List<ProductOffering>> searchProduct(@PathVariable String query, @RequestBody SearchRequest request, Pageable pageable){
//
//
//		Page<IndexingObject> page = searchProcessor.search(query, request, pageable);
//		Page<ProductOffering> pageProduct = resultProcessor.processResults(page, pageable);
//		HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageProduct, "/api/SearchProduct/" + query);
//		return new ResponseEntity<>(pageProduct.getContent(), headers, HttpStatus.OK);
//	}

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
	

	/*
	 *
	 * 
	 * @GetMapping(value="/FuzzySearchByKeywords/{query}") public
	 * ResponseEntity<List<IndexingObject>> fuzzySearch(@PathVariable String query,
	 * 
	 * @PageableDefault(sort = {"productOfferingName.keyword"}, direction =
	 * Sort.Direction.ASC, size = 50) Pageable pageable){
	 * 
	 * 
	 * Page<IndexingObject> page = searchProcessor.fuzzySearch(query, pageable);
	 * HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page,
	 * "/api/FuzzySearchByKeywords/"+query); return new
	 * ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	 * 
	 * // List<IndexingObject> objList = page.getContent(); // return
	 * ResponseEntity.ok(objList);
	 * 
	 * }
	 */

	/*
	 * // @ApiPageable
	 * 
	 * @GetMapping(value = "/TestSearchByKeywords/{query}") public
	 * ResponseEntity<List<IndexingObject>> testSearch(@PathVariable String query,
	 * Pageable pageable) {
	 * 
	 * Page<IndexingObject> page = searchProcessor.testSearch(query, pageable);
	 * HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page,
	 * "/api/testSearchByKeywords/" + query); return new
	 * ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	 * 
	 * }
	 */

}
