package it.eng.dome.search.web.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class OfferingTMForumResource {
	/*
	@Autowired
	private OfferingProcessor offeringProcessor;
	
	
	@GetMapping("/offering/processProductOfferingsFromTMForumAPI")
	public ResponseEntity<List<IndexingObject>> processProductOffetingsFromTMForumAPI(){
		List<IndexingObject> obj = offeringProcessor.processListProductOfferingFromTMForumAPI();
		return ResponseEntity.ok(obj);

	}
	
	
	
	@PostMapping("/offering/processProductOffering")
	public ResponseEntity<IndexingObject> processProductOffering(@RequestBody ProductOffering product){

		IndexingObject obj = offeringProcessor.processProductOfferingFromTMForumAPI(product);
		return ResponseEntity.ok(obj);

	}
	
	@PostMapping("/offering/callbackProductOffering")
    public ResponseEntity<IndexingObject> callbackProductOffering(@RequestParam String message)
        throws ExecutionException, InterruptedException {
     
		IndexingObject obj = offeringProcessor.processProductOfferingFromCallback(message);
		return ResponseEntity.ok(obj);
    
    }*/
}
