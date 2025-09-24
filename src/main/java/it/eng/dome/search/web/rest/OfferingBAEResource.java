package it.eng.dome.search.web.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class OfferingBAEResource {
//
//	@Autowired
//	private OfferingProcessor offeringProcessor;
//
//	/*
//	 * @PostMapping("/indexing") public ResponseEntity<IndexingObject>
//	 * save(IndexingObject obj){
//	 *
//	 * obj = offeringProcessor.save(obj); return ResponseEntity.ok(obj);
//	 *
//	 * }
//	 */
//
//
//	@PostMapping("/offerings/processProductOffering")
//	public ResponseEntity<IndexingObject> processProductOffering(@RequestBody ProductOffering product){
//
//		IndexingObject obj = offeringProcessor.processProductOffering(product);
//		return ResponseEntity.ok(obj);
//
//	}
//
//
//	@PostMapping("/offerings/processListProductOffering")
//	public ResponseEntity<List<IndexingObject>> processListProductOffering(@RequestBody ProductOffering[] product){
//
//		List<IndexingObject> obj = null;
//		try {
//			obj = offeringProcessor.processListProductOffering(product);
//		} catch (JsonProcessingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return ResponseEntity.ok(obj);
//
//	}
//
//	@GetMapping("/offerings/processProductOfferingsInMarketplace")
//	public ResponseEntity<List<IndexingObject>> processProductOfferingsInMarketplace(){
//
//		List<IndexingObject> obj = offeringProcessor.processListProductOffering();
//		return ResponseEntity.ok(obj);
//
//	}
//
//
//
//
//
//	/*
//	 * @GetMapping("/offerings/processProductOffering/{productOfferingId}") public
//	 * ResponseEntity<IndexingObject> processProductOfferingById(@PathVariable
//	 * String productOfferingId){
//	 *
//	 * IndexingObject obj =
//	 * offeringProcessor.processProductOfferingById(productOfferingId); return
//	 * ResponseEntity.ok(obj);
//	 *
//	 * }
//	 */
//
//	/*
//	 * @GetMapping("/offerings/analyzeToken") public ResponseEntity<String>
//	 * analyzeToken(){
//	 *
//	 * String obj = offeringProcessor.analyzeToken(); return ResponseEntity.ok(obj);
//	 *
//	 * }
//	 */
//
//
//	@GetMapping("/offerings/clearRepository")
//	public ResponseEntity<?> clearRepository() {
//
//		offeringProcessor.clearRepository();
//		//return (ResponseEntity<?>) ResponseEntity.ok();
//		return  ResponseEntity.noContent().build();
//	}
//
//
//
//
//
//

}
