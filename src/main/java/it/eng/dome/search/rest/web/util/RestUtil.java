package it.eng.dome.search.rest.web.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author mardetom
 *
 */
@Component
public class RestUtil {

	@Value("${dome.tmforum.url}")
	private String getTMForumUrl;

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
		String url = getBAEUrl + "/catalog/productOffering";
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
		String url = getTMForumUrl + "/productCatalogManagement/v4/productSpecification/" + id;
		log.debug("Call getTMFProductSpecificationById to URL {}", url);
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

		String result = response.getBody();
		log.debug("Response for getTMFProductSpecificationById with status code {}", response.getStatusCode().name());
		return result;
	}

	public String getAllProductOfferingsFromTMForum() {
		String url = getTMForumUrl + "/serviceCatalogManagement/v4/serviceSpecification";
		log.debug("Call getAllProductOfferingsFromTMForum to URL {}", url);
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

		String result = response.getBody();
		log.debug("Response for getAllProductOfferingsFromTMForum with status code {}",
				response.getStatusCode().name());
		return result;
	}

	public String getTMFProductOfferingById(String id) {
		String url = getTMForumUrl + "/productCatalogManagement/v4/productOffering/" + id;
		log.debug("Call getTMFProductOfferingById to URL {}", url);
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

		String result = response.getBody();
		log.debug("Response for getTMFProductOfferingById with status code {}", response.getStatusCode().name());
		return result;
	}

	public String getTMFServiceSpecificationById(String id) {
		String url = getTMForumUrl + "/productCatalogManagement/v4/productOffering/" + id;
		log.debug("Call getTMFServiceSpecificationById to URL {}", url);
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

		String result = response.getBody();
		log.debug("Response for getTMFServiceSpecificationById with status code {}", response.getStatusCode().name());
		return result;
	}

	public String getTMFResourceSpecificationById(String id) {
		String url = getTMForumUrl + "/resourceCatalog/v4/resourceSpecification/" + id;
		log.debug("Call getTMFServiceSpecificationById to URL {}", url);
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

		String result = response.getBody();
		log.debug("Response for getTMFResourceSpecificationById with status code {}", response.getStatusCode().name());
		return result;
	}

}