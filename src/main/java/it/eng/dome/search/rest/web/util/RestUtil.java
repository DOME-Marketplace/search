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
	
	@Value("${dome.getproductofferingbyid}")
	private String getProductOfferingById;
	
	@Value("${dome.getproductspecificationbyid}")
	private String getProductSpecificationById;
	
	@Value("${dome.getservicespecificationbyid}")
	private String getServiceSpecificationById;
	
	@Value("${dome.getresourcespecificationbyid}")
	private String getResourceSpecificationById;
	
	@Value("${dome.getallproductofferings}")
	private String getAllProductOfferings;
	
	@Value("${dome.gettmfallproductofferings}")
	private String getTMFAllProductOfferings;
	
	@Value("${dome.gettmfproductofferingbyid}")
	private String getTMFProductOfferingById;
	
	@Value("${dome.gettmfproductspecificationbyid}")
	private String getTMFProductSpecificationById;
	
	@Value("${dome.gettmfservicespecificationbyid}")
	private String getTMFServiceSpecificationById;
	
	@Value("${dome.gettmfresourcespecificationbyid}")
	private String getTMFResourceSpecificationById;
	
	
	private static final Logger log = LoggerFactory.getLogger(RestUtil.class);
	private static RestTemplate restTemplate = new RestTemplate();
	
	/*
	 * ---------- BAE Endpoints
	*/
	
	public String getProductOfferingById(String id) {

		ResponseEntity<String> response= restTemplate.getForEntity(getProductOfferingById + "/"+id, String.class);
		String result= response.getBody();	

		return result;
	}
	
	
	public String getProductSpecificationById(String id) {

		ResponseEntity<String> response= restTemplate.getForEntity(getProductSpecificationById + "/"+id, String.class);
		String result= response.getBody();	

		return result;
	}
	
	
	public String getServiceSpecificationById(String id) {

		ResponseEntity<String> response= restTemplate.getForEntity(getServiceSpecificationById + "/"+id, String.class);
		String result= response.getBody();	

		return result;
	}
	
	
	public String getResourceSpecificationById(String id) {

		ResponseEntity<String> response= restTemplate.getForEntity(getResourceSpecificationById + "/"+id, String.class);
		String result= response.getBody();	

		return result;
	}
	
	
	public String getAllProductOfferings() {

		ResponseEntity<String> response= restTemplate.getForEntity(getAllProductOfferings, String.class);
		String result= response.getBody();	

		return result;
	}
	
	
	
	/*
	 * ---------- TM Forum Endpoints
	*/
	
	public String getAllProductOfferingsFromTMForum() {

		ResponseEntity<String> response= restTemplate.getForEntity(getTMFAllProductOfferings, String.class);
		String result= response.getBody();	

		return result;
	}
	
	
	public String getTMFProductOfferingById(String id) {

		ResponseEntity<String> response= restTemplate.getForEntity(getTMFProductOfferingById + "/"+id, String.class);
		String result= response.getBody();	

		return result;
	}
	
	
	public String getTMFProductSpecificationById(String id) {

		ResponseEntity<String> response= restTemplate.getForEntity(getTMFProductSpecificationById + "/"+id, String.class);
		String result= response.getBody();	

		return result;
	}
	
	
	public String getTMFServiceSpecificationById(String id) {

		ResponseEntity<String> response= restTemplate.getForEntity(getTMFServiceSpecificationById + "/"+id, String.class);
		String result= response.getBody();	

		return result;
	}
	
	
	public String getTMFResourceSpecificationById(String id) {

		ResponseEntity<String> response= restTemplate.getForEntity(getTMFResourceSpecificationById + "/"+id, String.class);
		String result= response.getBody();	

		return result;
	}

	
	/*
	 * ---------- NLAPI Services
	*/
	

}
