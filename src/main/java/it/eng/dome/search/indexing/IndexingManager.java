package it.eng.dome.search.indexing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.eng.dome.search.domain.IndexingObject;
import it.eng.dome.search.domain.ProductOffering;
import it.eng.dome.search.domain.ProductSpecification;
import it.eng.dome.search.domain.ResourceSpecification;
import it.eng.dome.search.domain.ServiceSpecification;
import it.eng.dome.search.rest.web.util.RestUtil;

@Service
public class IndexingManager {

	@Autowired
	private RestUtil restUtil;

	@Autowired
	private MappingManager mappingManager;

	private static final Logger log = LoggerFactory.getLogger(IndexingManager.class);

	private static final ObjectMapper objectMapper = new ObjectMapper();

	public IndexingObject processOffering(ProductOffering product, IndexingObject objToIndex) {

		try {

			objToIndex = mappingManager.prepareOfferingMetadata(product, objToIndex);

			ProductSpecification productSpec = product.getProductSpecification();			
			if(productSpec.getId() == null) {
				log.info("null value in ProductSpecification ID");
			}else {
				String requestForProductSpecById = restUtil.getProductSpecificationById(productSpec.getId());

				ProductSpecification productSpecDetails = objectMapper.readValue(requestForProductSpecById, ProductSpecification.class);

				objToIndex = mappingManager.prepareProdSpecMetadata(productSpecDetails, objToIndex);

				ServiceSpecification[] serviceList = productSpecDetails.getServiceSpecification();

				if(serviceList != null) {

					log.info("---Mapping Services associated---");
					objToIndex = mappingManager.prepareServiceSpecMetadata(serviceList,objToIndex);
				}

				ResourceSpecification[] resourceList = productSpecDetails.getResourceSpecification();
				if(resourceList != null) {
					log.info("---Mapping Resources associated---");
					objToIndex = mappingManager.prepareResourceSpecMetadata(resourceList,objToIndex);
				}
			}

		} catch (JsonMappingException e) {
			log.warn("JsonMappingException - Error during processProductOffering(). Skipped: {}", e.getMessage());
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			log.warn("JsonProcessingException - Error during processProductOffering(). Skipped: {}", e.getMessage());
			e.printStackTrace();
		} catch (NullPointerException e) {
			log.warn("JsonProcessingException - Error during processProductOffering(). Skipped: {}", e.getMessage());
			e.printStackTrace();
		}

		return objToIndex;

	}

	public IndexingObject processOfferingFromTMForum(ProductOffering product, IndexingObject objToIndex) {
		try {

			//			if(product.getName() == null) {
			//				log.info("null value in name");
			//			}
			//			
			//			if(product.getDescription() == null) {
			//				log.info("null value in description");
			//			
			//			}else{
			objToIndex = mappingManager.prepareOfferingMetadata(product, objToIndex);

			ProductSpecification productSpec = product.getProductSpecification();			
			if(productSpec.getId() == null) {
				log.info("null value in ProductSpecification ID");
			}else {
				String requestForProductSpecById = restUtil.getTMFProductSpecificationById(productSpec.getId());

				ProductSpecification productSpecDetails = objectMapper.readValue(requestForProductSpecById, ProductSpecification.class);

				objToIndex = mappingManager.prepareProdSpecMetadata(productSpecDetails, objToIndex);

				ServiceSpecification[] serviceList = productSpecDetails.getServiceSpecification();

				if(serviceList != null) {

					log.info("---Mapping Services associated---");
					objToIndex = mappingManager.prepareTMFServiceSpecMetadata(serviceList,objToIndex);
				}

				ResourceSpecification[] resourceList = productSpecDetails.getResourceSpecification();
				if(resourceList != null) {
					log.info("---Mapping Resources associated---");
					objToIndex = mappingManager.prepareTMFResourceSpecMetadata(resourceList,objToIndex);
				}
			}
			//}
		} catch (JsonMappingException e) {
			log.warn("JsonMappingException - Error during processProductOffering(). Skipped: {}", e.getMessage());
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			log.warn("JsonProcessingException - Error during processProductOffering(). Skipped: {}", e.getMessage());
			e.printStackTrace();
		} catch (NullPointerException e) {
			log.warn("JsonProcessingException - Error during processProductOffering(). Skipped: {}", e.getMessage());
			e.printStackTrace();
		}

		return objToIndex;
	}

}