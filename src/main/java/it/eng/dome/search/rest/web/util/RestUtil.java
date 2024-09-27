package it.eng.dome.search.rest.web.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


/**
 * @author mardetom
 *
 */
@Component
public class RestUtil {

	@Value("${dome.tmforum.service-catalog-url}")
	private String getTMForumServiceCatalogUrl;

	@Value("${dome.tmforum.product-catalog-url}")
	private String getTMForumProductCatalogUrl;

	@Value("${dome.tmforum.resource-catalog-url}")
	private String getTMForumResourceCatalogUrl;
	
	
	@Value("${dome.bae.url}")
	private String getBAEUrl;


	private static final Logger log = LoggerFactory.getLogger(RestUtil.class);
	private static RestTemplate restTemplate = new RestTemplate();


	private final String classifyUrl =
			"https://deployenv6.expertcustomers.ai:8086/services/dome/classify"; //to change
	private final String analyzeUrl =
			"https://deployenv6.expertcustomers.ai:8086/services/dome/analyze";


	/*****************
	 * BAE Endpoints *
	 *****************/

	public String getProductOfferingById(String id) {
		String url = getBAEUrl + "/catalog/productOffering/" + id;
		log.debug("Call getProductOfferingById to URL {}", url);

		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

		String result = response.getBody();
		log.debug("Response for getProductOfferingById with status code {}", response.getStatusCode().name());
		return result;
	}

	public String getProductSpecificationById(String id) {
		String url = getBAEUrl + "/catalog/productSpecification/" + id;
		log.debug("Call getProductSpecificationById to URL {}", url);

		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

		String result = response.getBody();
		log.debug("Response for getProductSpecificationById with status code {}", response.getStatusCode().name());
		return result;
	}

	public String getServiceSpecificationById(String id) {
		String url = getBAEUrl + "/service/serviceSpecification/" + id;
		log.debug("Call getServiceSpecificationById to URL {}", url);
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

		String result = response.getBody();
		log.debug("Response for getServiceSpecificationById with status code {}", response.getStatusCode().name());
		return result;
	}

	public String getResourceSpecificationById(String id) {
		String url = getBAEUrl + "/resource/resourceSpecification/" + id;
		log.debug("Call getResourceSpecificationById to URL {}", url);
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

		String result = response.getBody();
		log.debug("Response for getResourceSpecificationById with status code {}", response.getStatusCode().name());
		return result;
	}

	public String getAllProductOfferings() {		
		String url = getBAEUrl + "/catalog/productOffering?limit=1000";
		log.debug("Call getAllProductOfferings to URL {}", url); 
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

		String result = response.getBody();
		log.debug("Response for getAllProductOfferings with status code {}", response.getStatusCode().name());
		return result;
	}

	/*********************
	 * TMForum Endpoints *
	 *********************/

	public String getTMFProductSpecificationById(String id) {
		String url = getTMForumProductCatalogUrl + "/productSpecification/" + id;
		log.debug("Call getTMFProductSpecificationById to URL {}", url);
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

		String result = response.getBody();
		log.debug("Response for getTMFProductSpecificationById with status code {}", response.getStatusCode().name());
		return result;
	}

	public String getAllProductOfferingsFromTMForum() {
		String url = getTMForumProductCatalogUrl + "/productOffering";
		log.debug("Call getAllProductOfferingsFromTMForum to URL {}", url);
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

		String result = response.getBody();
		log.debug("Response for getAllProductOfferingsFromTMForum with status code {}",
				response.getStatusCode().name());
		return result;
	}

	public String getTMFProductOfferingById(String id) {
		String url = getTMForumProductCatalogUrl + "/productOffering/" + id;
		log.debug("Call getTMFProductOfferingById to URL {}", url);
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

		String result = response.getBody();
		log.debug("Response for getTMFProductOfferingById with status code {}", response.getStatusCode().name());
		return result;
	}

	public String getTMFServiceSpecificationById(String id) {
		String url = getTMForumServiceCatalogUrl + "/serviceSpecification/" + id;
		log.debug("Call getTMFServiceSpecificationById to URL {}", url);
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

		String result = response.getBody();
		log.debug("Response for getTMFServiceSpecificationById with status code {}", response.getStatusCode().name());
		return result;
	}

	public String getTMFResourceSpecificationById(String id) {
		String url = getTMForumResourceCatalogUrl + "/resourceSpecification/" + id;
		log.debug("Call getTMFServiceSpecificationById to URL {}", url);
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

		String result = response.getBody();
		log.debug("Response for getTMFResourceSpecificationById with status code {}", response.getStatusCode().name());
		return result;
	}
	
	
	/*********************
	 * Semantic Endpoints *
	 *********************/
	
//	public String getClassificationResult(String bodyClassification) {
//		String url = getSemanticUrl + "/classify";
//		log.debug("Call getClassificationResult to URL {}", url);
//		
//		HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.TEXT_PLAIN);
//        headers.setAccept(Collections.singletonList(MediaType.ALL));
//        
//        // Prepare request entity with text body
//        HttpEntity<String> requestEntity = new HttpEntity<>(bodyClassification, headers);
//
//        log.debug("Sending POST request to URL {}", url);
//        
//     // Send POST request
//        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
//        
//		log.debug("Response for getClassificationResult with status code {}", response.getStatusCode().name());
//		if (response.getStatusCode() == HttpStatus.OK) {
//            return response.getBody();
//        } else {
//            throw new RuntimeException("Failed to Classify text: " + response.getStatusCode().name());
//        }
//
//	}
//	
//	
//	public String getAnalyzeResult(String bodyAnalyze) {
//		String url = getSemanticUrl + "/analyze";
//		log.debug("Call getAnalyzeResult to URL {}", url);
//		
//		HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.TEXT_PLAIN);
//        headers.setAccept(Collections.singletonList(MediaType.ALL));
//        
//        // Prepare request entity with text body
//        HttpEntity<String> requestEntity = new HttpEntity<>(bodyAnalyze, headers);
//
//        log.debug("Sending POST request to URL {}", url);
//        
//     // Send POST request
//        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
//        
//     // Log response status
//        log.debug("Response for POST-getAnalyzeResult request with status code {}", response.getStatusCode().name());
//		
//		if (response.getStatusCode() == HttpStatus.OK) {
//            return response.getBody();
//        } else {
//            throw new RuntimeException("Failed to Analyze text: " + response.getStatusCode().name());
//        }
//	}


	/*******************************
	 * Semantic services Endpoints *
	 *******************************/

	/*
	 * public String classifyText(String contentToClassify) {
	 * 
	 * String url = classifyUrl; log.info("Call classifyText to URL {}", url);
	 * 
	 * // Crea le intestazioni della richiesta HttpHeaders headers = new
	 * HttpHeaders(); headers.setContentType(MediaType.TEXT_PLAIN);
	 * headers.setAccept(Collections.singletonList(MediaType.ALL));
	 * 
	 * // Crea il corpo della richiesta HttpEntity<String> entity = new
	 * HttpEntity<>(contentToClassify, headers);
	 * 
	 * // Invia la richiesta POST ResponseEntity<String> response =
	 * restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
	 * 
	 * // Ottieni il corpo della risposta String result = response.getBody();
	 * log.info("Response for classifyText with status code {}",
	 * response.getStatusCode().name()); return result; }
	 * 
	 * 
	 * public String analyzeText(String contentToAnalyze) {
	 * 
	 * String url = analyzeUrl; log.info("Call analyzeText to URL {}", url);
	 * 
	 * // Crea le intestazioni della richiesta HttpHeaders headers = new
	 * HttpHeaders(); headers.setContentType(MediaType.TEXT_PLAIN);
	 * headers.setAccept(Collections.singletonList(MediaType.ALL));
	 * 
	 * // Crea il corpo della richiesta HttpEntity<String> entity = new
	 * HttpEntity<>(contentToAnalyze, headers);
	 * 
	 * // Invia la richiesta POST ResponseEntity<String> response =
	 * restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
	 * 
	 * // Ottieni il corpo della risposta String result = response.getBody();
	 * log.info("Response for analyzeText with status code {}",
	 * response.getStatusCode().name()); return result; }
	 */

}