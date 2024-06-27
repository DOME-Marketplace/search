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

	@Value("${dome.tmforum.host}")
	private String getTMForumHost;
	
	@Value("${dome.tmforum.port}")
	private String getTMForumPort;

	@Value("${dome.bae.host}")
	private String getBAEHost;

	@Value("${dome.bae.port}")
	private String getBAEPort;

	private static final Logger log = LoggerFactory.getLogger(RestUtil.class);
	private static RestTemplate restTemplate = new RestTemplate();

	/*****************
	 * BAE Endpoints *
	 *****************/

	public String getProductOfferingById(String id) {
		String url = getBAEHost + ":" + getBAEPort + "/catalog/productOffering/" + id;
		log.debug("Call getProductOfferingById to URL {}", url);

		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

		String result = response.getBody();
		log.debug("Response for getProductOfferingById with status code {}", response.getStatusCode().name());
		return result;
	}

	public String getProductSpecificationById(String id) {
		String url = getBAEHost + ":" + getBAEPort + "/catalog/productSpecification/" + id;
		log.debug("Call getProductSpecificationById to URL {}", url);

		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

		String result = response.getBody();
		log.debug("Response for getProductSpecificationById with status code {}", response.getStatusCode().name());
		return result;
	}

	public String getServiceSpecificationById(String id) {
		String url = getBAEHost + ":" + getBAEPort + "/service/serviceSpecification/" + id;
		log.debug("Call getServiceSpecificationById to URL {}", url);
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

		String result = response.getBody();
		log.debug("Response for getServiceSpecificationById with status code {}", response.getStatusCode().name());
		return result;
	}

	public String getResourceSpecificationById(String id) {
		String url = getBAEHost + ":" + getBAEPort + "/resource/resourceSpecification/" + id;
		log.debug("Call getResourceSpecificationById to URL {}", url);
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

		String result = response.getBody();
		log.debug("Response for getResourceSpecificationById with status code {}", response.getStatusCode().name());
		return result;
	}

	public String getAllProductOfferings() {		
		String url = getBAEHost + ":" + getBAEPort + "/catalog/productOffering";
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
		String url = getTMForumHost + ":" + getTMForumPort + "/productCatalogManagement/v4/productSpecification/" + id;
		log.debug("Call getTMFProductSpecificationById to URL {}", url);
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

		String result = response.getBody();
		log.debug("Response for getTMFProductSpecificationById with status code {}", response.getStatusCode().name());
		return result;
	}

	public String getAllProductOfferingsFromTMForum() {
		String url = getTMForumHost + ":" + getTMForumPort + "/serviceCatalogManagement/v4/serviceSpecification";
		log.debug("Call getAllProductOfferingsFromTMForum to URL {}", url);
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

		String result = response.getBody();
		log.debug("Response for getAllProductOfferingsFromTMForum with status code {}",
				response.getStatusCode().name());
		return result;
	}

	public String getTMFProductOfferingById(String id) {
		String url = getTMForumHost + ":" + getTMForumPort + "/productCatalogManagement/v4/productOffering/" + id;
		log.debug("Call getTMFProductOfferingById to URL {}", url);
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

		String result = response.getBody();
		log.debug("Response for getTMFProductOfferingById with status code {}", response.getStatusCode().name());
		return result;
	}

	public String getTMFServiceSpecificationById(String id) {
		String url = getTMForumHost + ":" + getTMForumPort + "/productCatalogManagement/v4/productOffering/" + id;
		log.debug("Call getTMFServiceSpecificationById to URL {}", url);
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

		String result = response.getBody();
		log.debug("Response for getTMFServiceSpecificationById with status code {}", response.getStatusCode().name());
		return result;
	}

	public String getTMFResourceSpecificationById(String id) {
		String url = getTMForumHost + ":" + getTMForumPort + "/resourceCatalog/v4/resourceSpecification/" + id;
		log.debug("Call getTMFServiceSpecificationById to URL {}", url);
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

		String result = response.getBody();
		log.debug("Response for getTMFResourceSpecificationById with status code {}", response.getStatusCode().name());
		return result;
	}

}