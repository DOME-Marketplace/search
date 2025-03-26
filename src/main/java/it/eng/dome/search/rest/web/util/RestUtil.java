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
import org.springframework.web.client.HttpStatusCodeException;
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


	/*****************
	 * BAE Endpoints *
	 *****************/

	public String getProductOfferingById(String id) {
		String url = getBAEUrl + "/catalog/productOffering/" + id;
		log.debug("Call getProductOfferingById to URL {}", url);

		try {
			ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

			String result = response.getBody();
			log.debug("Response for getProductOfferingById with status: {} - {}", response.getStatusCode().value(), response.getStatusCode().name());
			return result;
		} catch (HttpStatusCodeException exception) {
			log.error("Error for getProductOfferingById with status: {} - {}", exception.getStatusCode().value(), exception.getStatusCode().name());
			return null;
		}
	}

	public String getProductSpecificationById(String id) {
		String url = getBAEUrl + "/catalog/productSpecification/" + id;
		log.debug("Call getProductSpecificationById to URL {}", url);

		try {
			ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

			String result = response.getBody();
			log.debug("Response for getProductSpecificationById with status: {} - {}", response.getStatusCode().value(), response.getStatusCode().name());
			return result;
		} catch (HttpStatusCodeException exception) {
			log.error("Error for getProductSpecificationById with status: {} - {}", exception.getStatusCode().value(), exception.getStatusCode().name());
			return null;
		}
	}

	public String getServiceSpecificationById(String id) {
		String url = getBAEUrl + "/service/serviceSpecification/" + id;
		log.debug("Call getServiceSpecificationById to URL {}", url);

		try {
			ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

			String result = response.getBody();
			log.debug("Response for getServiceSpecificationById with status: {} - {}", response.getStatusCode().value(), response.getStatusCode().name());
			return result;
		} catch (HttpStatusCodeException exception) {
			log.error("Error for getServiceSpecificationById with status: {} - {}", exception.getStatusCode().value(), exception.getStatusCode().name());
			return null;
		}
	}

	public String getResourceSpecificationById(String id) {
		String url = getBAEUrl + "/resource/resourceSpecification/" + id;
		log.debug("Call getResourceSpecificationById to URL {}", url);

		try {
			ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

			String result = response.getBody();
			log.debug("Response for getResourceSpecificationById with status: {} - {}", response.getStatusCode().value(), response.getStatusCode().name());
			return result;
		} catch (HttpStatusCodeException exception) {
			log.error("Error for getResourceSpecificationById with status: {} - {}", exception.getStatusCode().value(), exception.getStatusCode().name());
			return null;
		}
	}

	public String getAllProductOfferings() {		
		String url = getBAEUrl + "/catalog/productOffering?limit=1000";
		log.debug("Call getAllProductOfferings to URL {}", url); 

		try {
			ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

			String result = response.getBody();
			log.debug("Response for getAllProductOfferings with status: {} - {}", response.getStatusCode().value(), response.getStatusCode().name());
			return result;
		} catch (HttpStatusCodeException exception) {
			log.error("Error for getAllProductOfferings with status: {} - {}", exception.getStatusCode().value(), exception.getStatusCode().name());
			return null;
		}
	}

	public String getAllProductOfferingsPaginated() {
		int limit = 10;
		int offset = 0;
		boolean hasMore = true;
		StringBuilder jsonResult = new StringBuilder("[");

		while (hasMore) {
			String url = getBAEUrl + "/catalog/productOffering?limit=" + limit + "&offset=" + offset;
			log.debug("Fetching product offerings from URL: {}", url);

			try {
				ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

				if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
					String jsonChunk = response.getBody();

					// Rimuoviamo le parentesi quadre [] per unire i risultati
					if (!"[]".equals(jsonChunk)) {
						if (jsonResult.length() > 1) {
							jsonResult.append(","); // Aggiunge una virgola tra i blocchi
						}
						jsonResult.append(jsonChunk.substring(1, jsonChunk.length() - 1));
					}

					// Contiamo il numero di elementi nel batch corrente
					long count = jsonChunk.chars().filter(ch -> ch == '{').count();

					if (count < limit) {
						hasMore = false; // Se riceviamo meno elementi del limite, abbiamo finito
					} else {
						offset += limit; // Aumentiamo l'offset per la prossima richiesta
					}
				} else {
					hasMore = false;
				}
			} catch (HttpStatusCodeException exception) {
				log.error("Error retrieving product offerings: {}", exception.getMessage());
				hasMore = false;
			}
		}

		jsonResult.append("]"); // Chiudiamo il JSON array
		return jsonResult.toString();
	}




	/*********************
	 * TMForum Endpoints *
	 *********************/

	public String getTMFProductSpecificationById(String id) {
		String url = getTMForumProductCatalogUrl + "/productSpecification/" + id;
		log.debug("Call getTMFProductSpecificationById to URL {}", url);

		try {
			ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

			String result = response.getBody();
			log.debug("Response for getTMFProductSpecificationById with status: {} - {}", response.getStatusCode().value(), response.getStatusCode().name());
			return result;
		} catch (HttpStatusCodeException exception) {
			log.error("Error for getTMFProductSpecificationById with status: {} - {}", exception.getStatusCode().value(), exception.getStatusCode().name());
			return null;
		}
	}

	public String getAllProductOfferingsFromTMForum() {
		String url = getTMForumProductCatalogUrl + "/productOffering";
		log.debug("Call getAllProductOfferingsFromTMForum to URL {}", url);

		try {
			ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

			String result = response.getBody();
			log.debug("Response for getAllProductOfferingsFromTMForum with status: {} - {}", response.getStatusCode().value(), response.getStatusCode().name());
			return result;
		} catch (HttpStatusCodeException exception) {
			log.error("Error for getAllProductOfferingsFromTMForum with status: {} - {}", exception.getStatusCode().value(), exception.getStatusCode().name());
			return null;
		}
	}

	public String getTMFProductOfferingById(String id) {
		String url = getTMForumProductCatalogUrl + "/productOffering/" + id;
		log.debug("Call getTMFProductOfferingById to URL {}", url);

		try {
			ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

			String result = response.getBody();
			log.debug("Response for getTMFProductOfferingById with status: {} - {}", response.getStatusCode().value(), response.getStatusCode().name());
			return result;
		} catch (HttpStatusCodeException exception) {
			log.error("Error for getTMFProductOfferingById with status: {} - {}", exception.getStatusCode().value(), exception.getStatusCode().name());
			return null;
		}
	}

	public String getTMFServiceSpecificationById(String id) {
		String url = getTMForumServiceCatalogUrl + "/serviceSpecification/" + id;
		log.debug("Call getTMFServiceSpecificationById to URL {}", url);
		
		try {
			ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

			String result = response.getBody();
			log.debug("Response for getTMFProductOfferingById with status: {} - {}", response.getStatusCode().value(), response.getStatusCode().name());
			return result;
		} catch (HttpStatusCodeException exception) {
			log.error("Error for getTMFProductOfferingById with status: {} - {}", exception.getStatusCode().value(), exception.getStatusCode().name());
			return null;
		}
	}

	public String getTMFResourceSpecificationById(String id) {
		String url = getTMForumResourceCatalogUrl + "/resourceSpecification/" + id;
		log.debug("Call getTMFServiceSpecificationById to URL {}", url);

		try {
			ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

			String result = response.getBody();
			log.debug("Response for getTMFResourceSpecificationById with status: {} - {}", response.getStatusCode().value(), response.getStatusCode().name());
			return result;
		} catch (HttpStatusCodeException exception) {
			log.error("Error for getTMFResourceSpecificationById with status: {} - {}", exception.getStatusCode().value(), exception.getStatusCode().name());
			return null;
		}
	}

	public String getOrganizationById(String id) {
		String url = getBAEUrl + "/party/organization/" + id;
		log.debug("Call getOrganizationById to URL {}", url);

		try {
			ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

			String result = response.getBody();
			log.debug("Response for getOrganizationById with status: {} - {}", response.getStatusCode().value(), response.getStatusCode().name());
			return result;
		} catch (HttpStatusCodeException exception) {
			log.error("Error for getOrganizationById with status: {} - {}", exception.getStatusCode().value(), exception.getStatusCode().name());
			return null;
		}
	}


}