package it.eng.dome.search.web.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.eng.dome.search.domain.IndexingObject;
import it.eng.dome.search.domain.ProductOffering;
import it.eng.dome.search.service.OfferingProcessor;

@RestController
@RequestMapping("/api")
public class OfferingResource {

	@Autowired
	private OfferingProcessor offeringProcessor;

	/*
	 * @PostMapping("/indexing") public ResponseEntity<IndexingObject>
	 * save(IndexingObject obj){
	 * 
	 * obj = offeringProcessor.save(obj); return ResponseEntity.ok(obj);
	 * 
	 * }
	 */


	@PostMapping("/offerings/processProductOffering")
	public ResponseEntity<IndexingObject> processProductOffering(@RequestBody ProductOffering product){

		IndexingObject obj = offeringProcessor.processProductOffering(product);
		return ResponseEntity.ok(obj);

	}
	
	
	@PostMapping("/offerings/processListProductOffering")
	public ResponseEntity<IndexingObject> processListProductOffering(@RequestBody ProductOffering[] product){

		IndexingObject obj = offeringProcessor.processListProductOffering(product);
		return ResponseEntity.ok(obj);

	}
	
	@GetMapping("/offerings/processProductOfferingsInMarketplace")
	public ResponseEntity<List<IndexingObject>> processProductOfferingsInMarketplace(){

		List<IndexingObject> obj = offeringProcessor.processListProductOffering();
		return ResponseEntity.ok(obj);

	}
	
	
	@GetMapping("/offering/processProductOfferingsFromTMForumAPI")
	public ResponseEntity<List<IndexingObject>> processProductOffetingsFromTMForumAPI(){
		List<IndexingObject> obj = offeringProcessor.processListProductOfferingFromTMForumAPI();
		return ResponseEntity.ok(obj);

	}
	
	
	


	/*
	 * @GetMapping("/offerings/processProductOffering/{productOfferingId}") public
	 * ResponseEntity<IndexingObject> processProductOfferingById(@PathVariable
	 * String productOfferingId){
	 * 
	 * IndexingObject obj =
	 * offeringProcessor.processProductOfferingById(productOfferingId); return
	 * ResponseEntity.ok(obj);
	 * 
	 * }
	 */
	
	/*
	 * @GetMapping("/offerings/analyzeToken") public ResponseEntity<String>
	 * analyzeToken(){
	 * 
	 * String obj = offeringProcessor.analyzeToken(); return ResponseEntity.ok(obj);
	 * 
	 * }
	 */
	
	
	@GetMapping("/offerings/clearRepository")
	public ResponseEntity clearRepository() {
		
		offeringProcessor.clearRepository();
		return (ResponseEntity) ResponseEntity.ok();
	}
	
	
	
	
	
	

}
