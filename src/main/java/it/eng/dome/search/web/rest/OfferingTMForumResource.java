package it.eng.dome.search.web.rest;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import it.eng.dome.search.domain.IndexingObject;
import it.eng.dome.search.service.OfferingProcessor;

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
