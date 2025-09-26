package it.eng.dome.search.service;

import java.net.URI;
import java.net.URISyntaxException;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SemanticProcessor {
	
	/*
	 * public String processEntityExtraction(String body) throws URISyntaxException
	 * {
	 * RestTemplate restTemplate = new RestTemplate(); URI uri = new URI ("https://deployenv6.expertcustomers.ai:8086/services/dome/analyze");
	 * 
	 * HttpHeaders headers = new HttpHeaders();
	 * headers.setContentType(MediaType.TEXT_PLAIN);
	 * 
	 * HttpEntity<String> httpEntity = new HttpEntity<>(body, headers);
	 * ResponseEntity<String> result = restTemplate.postForEntity(uri, httpEntity, String.class);
	 * 
	 * return result.getBody(); }
	 */
}
