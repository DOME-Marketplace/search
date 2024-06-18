package it.eng.dome.search.rest.web.util;

import org.apache.http.client.utils.URIBuilder;
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
	/*
	 * @Value("${dome.tmforum.port}")
	 * private String getTMForumPort;
	 * 
	 * @Value("${dome.tmforum.path}")
	 * private String getTMForumPath;
	 * 
	 * @Value("${dome.bae.schema}")
	 * private String getBAESchema;
	 * 
	 * @Value("${dome.bae.host}")
	 * private String getBAEHost;
	 * 
	 * @Value("${dome.bae.port}")
	 * private String getBAEPort;
	 * 
	 * @Value("${dome.bae.path}")
	 * private String getBAEPath;
	 */
	@Value("${dome.bae.url}")
	private String getBAEUrl;

	private static final Logger log = LoggerFactory.getLogger(RestUtil.class);
	private static RestTemplate restTemplate = new RestTemplate();

	private String tmForumURL = null;
	private String baeURL = null;

	private String getTMForumURL() {
		if (tmForumURL == null) {
			tmForumURL = getTMForumUrl;
			log.info("Set TMForum URL {}", tmForumURL);
		}
		return tmForumURL;
	}

	private String getBaeURL() {
		if (baeURL == null) {
			baeURL = getBAEUrl;
			log.info("Set BAE URL {}", baeURL);
		}
		return getBAEUrl;
	}

	/*****************
	 * BAE Endpoints *
	 *****************/

	public String getProductOfferingById(String id) {
		log.debug("Call getProductOfferingById with id {}", id);
		ResponseEntity<String> response = restTemplate.getForEntity(getBaeURL() + "/catalog/productOffering/" + id,
				String.class);

		String result = response.getBody();
		log.debug("Response with status code {}", response.getStatusCode().name());
		return result;
	}

	public String getProductSpecificationById(String id) {
		log.debug("Call getProductSpecificationById with id {}", id);

		ResponseEntity<String> response = restTemplate
				.getForEntity(getBaeURL() + "/catalog/productSpecification/" + id, String.class);

		String result = response.getBody();
		log.debug("Response with status code {}", response.getStatusCode().name());
		return result;
	}

	public String getServiceSpecificationById(String id) {
		log.debug("Call getServiceSpecificationById with id {}", id);
		ResponseEntity<String> response = restTemplate.getForEntity(getBaeURL() + "/service/serviceSpecification/" + id,
				String.class);

		String result = response.getBody();
		log.debug("Response with status code {}", response.getStatusCode().name());
		return result;
	}

	public String getResourceSpecificationById(String id) {
		log.debug("Call getResourceSpecificationById with id {}", id);
		ResponseEntity<String> response = restTemplate
				.getForEntity(getBaeURL() + "/resource/resourceSpecification/" + id, String.class);

		String result = response.getBody();
		log.debug("Response with status code {}", response.getStatusCode().name());
		return result;
	}

	public String getAllProductOfferings() {
		log.debug("Call getAllProductOfferings");
		ResponseEntity<String> response = restTemplate.getForEntity(getBaeURL() + "/catalog/productOffering",
				String.class);

		String result = response.getBody();
		log.debug("Response with status code {}", response.getStatusCode().name());
		return result;
	}

	/*********************
	 * TMForum Endpoints *
	 *********************/

	public String getTMFProductSpecificationById(String id) {
		log.debug("Call getTMFProductSpecificationById with id {}", id);
		ResponseEntity<String> response = restTemplate.getForEntity(
				getTMForumURL() + "/productCatalogManagement/v4/productSpecification/" + id, String.class);

		String result = response.getBody();
		log.debug("Response with status code {}", response.getStatusCode().name());
		return result;
	}

	public String getAllProductOfferingsFromTMForum() {
		log.debug("Call getAllProductOfferingsFromTMForum");
		ResponseEntity<String> response = restTemplate
				.getForEntity(getTMForumURL() + "/serviceCatalogManagement/v4/serviceSpecification", String.class);

		String result = response.getBody();
		log.debug("Response with status code {}", response.getStatusCode().name());
		return result;
	}

	public String getTMFProductOfferingById(String id) {
		log.debug("Call getTMFProductOfferingById with id {}", id);
		ResponseEntity<String> response = restTemplate
				.getForEntity(getTMForumURL() + "/productCatalogManagement/v4/productOffering/" + id, String.class);

		String result = response.getBody();
		log.debug("Response with status code {}", response.getStatusCode().name());
		return result;
	}

	public String getTMFServiceSpecificationById(String id) {
		log.debug("Call getTMFServiceSpecificationById with id {}", id);
		ResponseEntity<String> response = restTemplate
				.getForEntity(getTMForumURL() + "/productCatalogManagement/v4/productOffering/" + id, String.class);

		String result = response.getBody();
		log.debug("Response with status code {}", response.getStatusCode().name());
		return result;
	}

	public String getTMFResourceSpecificationById(String id) {
		log.debug("Call getTMFServiceSpecificationById with id {}", id);
		ResponseEntity<String> response = restTemplate
				.getForEntity(getTMForumURL() + "/resourceCatalog/v4/resourceSpecification/" + id, String.class);

		String result = response.getBody();
		log.debug("Response with status code {}", response.getStatusCode().name());
		return result;
	}

}