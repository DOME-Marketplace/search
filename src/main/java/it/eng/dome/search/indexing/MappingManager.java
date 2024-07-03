package it.eng.dome.search.indexing;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.eng.dome.search.domain.IndexingObject;
import it.eng.dome.search.domain.ProductOffering;
import it.eng.dome.search.domain.ProductSpecification;
import it.eng.dome.search.domain.RelatedParty;
import it.eng.dome.search.domain.ResourceSpecification;
import it.eng.dome.search.domain.ServiceSpecification;
import it.eng.dome.search.rest.web.util.RestUtil;

@Component
public class MappingManager {

	@Autowired
	private RestUtil restTemplate;

	private static final ObjectMapper objectMapper = new ObjectMapper();
	private static final Logger log = LoggerFactory.getLogger(MappingManager.class);



	public IndexingObject prepareOfferingMetadata(ProductOffering product, IndexingObject objToIndex) {

		//prepare metadata of offerings
		objToIndex.setProductOffering(product);
		objToIndex.setProductOfferingDescription(product.getDescription());
		objToIndex.setProductOfferingId(product.getId());
		objToIndex.setProductOfferingIsBundle(product.getIsBundle());
		objToIndex.setProductOfferingLastUpdate(product.getLastUpdate());
		objToIndex.setProductOfferingLifecycleStatus(product.getLifecycleStatus());
		objToIndex.setProductOfferingName(product.getName());
		objToIndex.setProductOfferingNameText(product.getName());
		objToIndex.setCategories(product.getCategory());

		return objToIndex;

	}

	
	public IndexingObject prepareProdSpecMetadata(ProductSpecification productSpecDetails, IndexingObject objToIndex) {
		//prepare metadata of Products specification
		objToIndex.setProductSpecification(productSpecDetails);
		objToIndex.setProductSpecificationBrand(productSpecDetails.getBrand());
		objToIndex.setProductSpecificationId(productSpecDetails.getId());
		objToIndex.setProductSpecificationName(productSpecDetails.getName());

		if(productSpecDetails.getDescription() != null )
			objToIndex.setProductSpecificationDescription(productSpecDetails.getDescription());

		if(productSpecDetails.getRelatedParty() != null) {
			for(RelatedParty party : productSpecDetails.getRelatedParty()) {
				objToIndex.setRelatedPartyId(party.getId());
			}
		}

		return objToIndex;
	}



	public IndexingObject prepareServiceSpecMetadata(ServiceSpecification[] serviceList, IndexingObject objToIndex) {

		ArrayList<ServiceSpecification> listServiceDetails = new ArrayList<ServiceSpecification>();

		try {

			for(ServiceSpecification s : serviceList) {

				String requestForServiceSpecificationId = restTemplate.getServiceSpecificationById(s.getId());
				ServiceSpecification serviceSpecDetails = objectMapper.readValue(requestForServiceSpecificationId, ServiceSpecification.class);

				listServiceDetails.add(serviceSpecDetails);
			}	

		} catch (JsonProcessingException e) {
			log.warn("JsonProcessingException - Error during prepareServiceSpecMetadata(). Skipped: {}", e.getMessage());
			e.printStackTrace();
		}

		//ServiceSpecification[] listServiceToIndex = new ServiceSpecification[listServiceDetails.size()];

		ServiceSpecification[] listServiceToIndex = listServiceDetails.toArray(new ServiceSpecification[listServiceDetails.size()]);

		objToIndex.setServices(listServiceToIndex);	

		return objToIndex;
	}


	public IndexingObject prepareResourceSpecMetadata(ResourceSpecification[] resourceList, IndexingObject objToIndex) {

		List<ResourceSpecification> listResourceDetails = new ArrayList<ResourceSpecification>();

		try {

			for(ResourceSpecification r : resourceList) {

				String requestForResourceSpecificationId = restTemplate.getResourceSpecificationById(r.getId());
				ResourceSpecification resourceSpecDetails = objectMapper.readValue(requestForResourceSpecificationId, ResourceSpecification.class);

				listResourceDetails.add(resourceSpecDetails);
			}	

		} catch (JsonProcessingException e) {
			log.warn("JsonProcessingException - Error during prepareResourceSpecMetadata(). Skipped: {}", e.getMessage());
			e.printStackTrace();
		}

		//ResourceSpecification[] listResourceToIndex = new ResourceSpecification[listResourceDetails.size()];
		ResourceSpecification[] listResourceToIndex = listResourceDetails.toArray(new ResourceSpecification[listResourceDetails.size()]);

		objToIndex.setResources(listResourceToIndex);	

		return objToIndex;
	}
	
	
	public IndexingObject prepareTMFServiceSpecMetadata(ServiceSpecification[] serviceList, IndexingObject objToIndex) {

		ArrayList<ServiceSpecification> listServiceDetails = new ArrayList<ServiceSpecification>();

		try {

			for(ServiceSpecification s : serviceList) {

				String requestForServiceSpecificationId = restTemplate.getTMFServiceSpecificationById(s.getId());
				ServiceSpecification serviceSpecDetails = objectMapper.readValue(requestForServiceSpecificationId, ServiceSpecification.class);

				listServiceDetails.add(serviceSpecDetails);
			}	

		} catch (JsonProcessingException e) {
			log.warn("JsonProcessingException - Error during prepareServiceSpecMetadata(). Skipped: {}", e.getMessage());
			e.printStackTrace();
		}

		//ServiceSpecification[] listServiceToIndex = new ServiceSpecification[listServiceDetails.size()];

		ServiceSpecification[] listServiceToIndex = listServiceDetails.toArray(new ServiceSpecification[listServiceDetails.size()]);

		objToIndex.setServices(listServiceToIndex);	

		return objToIndex;
	}
	
	
	public IndexingObject prepareTMFResourceSpecMetadata(ResourceSpecification[] resourceList, IndexingObject objToIndex) {

		List<ResourceSpecification> listResourceDetails = new ArrayList<ResourceSpecification>();

		try {

			for(ResourceSpecification r : resourceList) {

				String requestForResourceSpecificationId = restTemplate.getTMFResourceSpecificationById(r.getId());
				ResourceSpecification resourceSpecDetails = objectMapper.readValue(requestForResourceSpecificationId, ResourceSpecification.class);

				listResourceDetails.add(resourceSpecDetails);
			}	

		} catch (JsonProcessingException e) {
			log.warn("JsonProcessingException - Error during prepareResourceSpecMetadata(). Skipped: {}", e.getMessage());
			e.printStackTrace();
		}

		//ResourceSpecification[] listResourceToIndex = new ResourceSpecification[listResourceDetails.size()];
		ResourceSpecification[] listResourceToIndex = listResourceDetails.toArray(new ResourceSpecification[listResourceDetails.size()]);

		objToIndex.setResources(listResourceToIndex);	

		return objToIndex;
	}



}
