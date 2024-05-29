package it.eng.dome.search.web.rest;

import java.net.URI;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.eng.dome.search.service.SemanticProcessor;

@RestController
@RequestMapping("/api")
public class TestSemanticService {

	/*
	 * @Autowired SemanticProcessor semanticProcessor;
	 * 
	 * @PostMapping("/test/processEntityExtraction") public ResponseEntity<String>
	 * processEntityExtraction(@RequestBody String body) throws Exception{
	 * 
	 * 
	 * String result = semanticProcessor.processEntityExtraction(body); return
	 * ResponseEntity.ok(result);
	 * 
	 * 
	 * }
	 */
}
