package it.eng.dome.search.indexing;

import it.eng.dome.search.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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

			log.info("ProcessOffering objToIndex {}", objToIndex.getId());
			objToIndex = mappingManager.prepareOfferingMetadata(product, objToIndex);

			ProductSpecification productSpec = product.getProductSpecification();
			if (productSpec.getId() == null) {
				log.warn("null value in ProductSpecification ID");
			} else {

				String requestForProductSpecById = restUtil.getProductSpecificationById(productSpec.getId());
				// log.debug("ProductSpecification object: {}", requestForProductSpecById);

				if (requestForProductSpecById == null) {
					log.warn("getProductSpecificationById {} cannot found", productSpec.getId());
				} else {

					ProductSpecification productSpecDetails = objectMapper.readValue(requestForProductSpecById,
							ProductSpecification.class);
					log.debug("ProductSpecDetails Id: {}", productSpecDetails.getId());
					objToIndex = mappingManager.prepareProdSpecMetadata(productSpecDetails, objToIndex);

					// Retrieve the owner from the RelatedParty list
					if (productSpecDetails.getRelatedParty() != null) {
						for (RelatedParty party : productSpecDetails.getRelatedParty()) {
							if ("Owner".equalsIgnoreCase(party.getRole())) { // Check if the role is "Owner"
								String ownerId = party.getId();
								if (ownerId != null) {
									String requestForOrganizationById = restUtil.getOrganizationById(ownerId);
									if (requestForOrganizationById == null) {
										log.warn("getOrganizationById {} cannot found", productSpecDetails.getId());
									} else {
										Organization organizationDetails = objectMapper.readValue(requestForOrganizationById, Organization.class);
										String ownerName = organizationDetails.getTradingName();
										if (ownerName != null && !ownerName.isEmpty()) {
											objToIndex.setProductSpecificationOwner(ownerName); // Set the owner's name
										}
									}
								}
								break; // Stop after finding the first owner
							}
						}
					}

					ServiceSpecification[] serviceList = productSpecDetails.getServiceSpecification();

					if (serviceList != null) {
						log.info("ProcessOffering BAE => Mapping Services associated: " + serviceList.length);
						objToIndex = mappingManager.prepareServiceSpecMetadata(serviceList, objToIndex);
					}

					ResourceSpecification[] resourceList = productSpecDetails.getResourceSpecification();
					if (resourceList != null) {
						log.info("ProcessOffering BAE => Mapping Resources associated: " + resourceList.length);
						objToIndex = mappingManager.prepareResourceSpecMetadata(resourceList, objToIndex);
					}

					// Reactivate for Semantic services
					if ((objToIndex.getProductOfferingDescription() == null) || (objToIndex.getProductOfferingDescription() == "")) {
						log.warn("null value for description in product: " + product.getId());
					} else {

						if (objToIndex.getProductOfferingLifecycleStatus().contains("Launched") == true) {
							objToIndex = mappingManager.prepareClassify(objToIndex);
							objToIndex = mappingManager.prepareAnalyze(objToIndex);
						}
					}
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

			objToIndex = mappingManager.prepareOfferingMetadata(product, objToIndex);

			ProductSpecification productSpec = product.getProductSpecification();
			if (productSpec.getId() == null) {
				log.warn("null value in ProductSpecification ID");
			} else {
				String requestForProductSpecById = restUtil.getTMFProductSpecificationById(productSpec.getId());

				if (requestForProductSpecById == null) {
					log.warn("getTMFProductSpecificationById {} cannot found", productSpec.getId());
				} else {
					ProductSpecification productSpecDetails = objectMapper.readValue(requestForProductSpecById,
							ProductSpecification.class);

					objToIndex = mappingManager.prepareProdSpecMetadata(productSpecDetails, objToIndex);

					ServiceSpecification[] serviceList = productSpecDetails.getServiceSpecification();

					if (serviceList != null) {
						log.info("ProcessOffering TMForum => Mapping Services associated: " + serviceList.length);
						objToIndex = mappingManager.prepareTMFServiceSpecMetadata(serviceList, objToIndex);
					}

					ResourceSpecification[] resourceList = productSpecDetails.getResourceSpecification();
					if (resourceList != null) {
						log.info("ProcessOffering TMForum => Mapping Resources associated: " + resourceList.length);
						objToIndex = mappingManager.prepareTMFResourceSpecMetadata(resourceList, objToIndex);
					}
				}
			}
			// }
		} catch (Exception e) {
			log.warn("Exception - Error during processProductOfferingTMForum(). Skipped: {}", e.getMessage());
			e.printStackTrace();
		}
		return objToIndex;
	}

}