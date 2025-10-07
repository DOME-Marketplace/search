package it.eng.dome.search.indexing;

import it.eng.dome.search.domain.IndexingObject;
import it.eng.dome.search.service.TmfDataRetriever;
import it.eng.dome.tmforum.tmf620.v4.model.*;
import it.eng.dome.tmforum.tmf632.v4.model.Organization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IndexingManager {

	private static final Logger log = LoggerFactory.getLogger(IndexingManager.class);

	@Autowired
	private MappingManager mappingManager;

	@Autowired
	TmfDataRetriever tmfDataRetriever;

	public IndexingObject processOfferingFromTMForum(ProductOffering product, IndexingObject objToIndex) {
		try {
			// map ProductOffering
			objToIndex = mappingManager.prepareOfferingMetadata(product, objToIndex);

			ProductSpecificationRef productSpecRef = product.getProductSpecification();
			if (productSpecRef == null) {
				log.warn("null value in ProductSpecification");
			} else {
				ProductSpecification productSpec = tmfDataRetriever.getProductSpecificationById(productSpecRef.getId(), null);

				if (productSpec == null) {
					log.warn("getTMFProductSpecificationById {} - Product Specification cannot found", productSpecRef.getId());
				} else {
					// map ProductSpecification
					objToIndex = mappingManager.prepareProdSpecMetadata(productSpec, objToIndex);

					// map owner
					mapOwner(productSpec, objToIndex);

					// map ServiceSpecification
					objToIndex = mapServiceSpec(productSpec, objToIndex);

					// map ResourceSpecification
					objToIndex = mapResourceSpec(productSpec, objToIndex);

					//TODO: for semantic services contact ExAI team for more details.
					// process semantic services
					objToIndex = processSemantic(objToIndex, product);
				}
			}
		} catch (Exception e) {
			log.warn("Exception - Error during processProductOfferingTMForum(). Skipped: {}", e.getMessage(), e);
		}

		return objToIndex;
	}

	private void mapOwner(ProductSpecification productSpec, IndexingObject objToIndex) {
		// retrieve the owner from the RelatedParty list
		if (productSpec.getRelatedParty() != null) {
			for (RelatedParty party : productSpec.getRelatedParty()) {
				if ("Owner".equalsIgnoreCase(party.getRole())) {
					String ownerId = party.getId();
					if (ownerId != null) {
						Organization organizationDetails = tmfDataRetriever.getOrganizationById(ownerId, null);
						if (organizationDetails != null) {
							String ownerName = organizationDetails.getTradingName();
							if (ownerName != null && !ownerName.isEmpty()) {
								objToIndex.setProductSpecificationOwner(ownerName);
							}
						} else {
							log.warn("Organization {} cannot be found", ownerId);
						}
					}
					break; // Stop after finding the first owner
				}
			}
		}
//		return objToIndex;
	}

	private IndexingObject mapServiceSpec(ProductSpecification productSpec, IndexingObject objToIndex) {
		// map ServiceSpecification
		List<ServiceSpecificationRef> serviceList = productSpec.getServiceSpecification();
		if (serviceList != null) {
			log.info("ProcessOffering TMForum => Mapping Services associated: " + serviceList.size());
			objToIndex = mappingManager.prepareTMFServiceSpecMetadata(serviceList, objToIndex);
		}
		return objToIndex;
	}

	private IndexingObject mapResourceSpec(ProductSpecification productSpec, IndexingObject objToIndex) {
		// map ResourceSpecification
		List<ResourceSpecificationRef> resourceList = productSpec.getResourceSpecification();
		if (resourceList != null) {
			log.info("ProcessOffering TMForum => Mapping Resources associated: " + resourceList.size());
			objToIndex = mappingManager.prepareTMFResourceSpecMetadata(resourceList, objToIndex);
		}
		return objToIndex;
	}

	private IndexingObject processSemantic(IndexingObject objToIndex, ProductOffering product) {
		// Reactivate for Semantic services
		if ((objToIndex.getProductOfferingDescription() == null) || (objToIndex.getProductOfferingDescription().isEmpty())) {
			log.warn("null value for description in product: " + product.getId());
		} else {
			// TODO: maybe equalsIgnoreCase then contains for launched is better
			if (objToIndex.getProductOfferingLifecycleStatus().contains("Launched")) {
				objToIndex = mappingManager.prepareClassify(objToIndex);
				objToIndex = mappingManager.prepareAnalyze(objToIndex);
			}
		}
		return objToIndex;
	}

}