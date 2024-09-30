package it.eng.dome.search.indexing;

import java.util.ArrayList;
import java.util.List;

import it.eng.dome.search.semantic.domain.Analysis;
import it.eng.dome.search.semantic.domain.AnalyzeResultObject;
import org.jsoup.Jsoup;
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
import it.eng.dome.search.rest.web.util.RestSemanticUtil;
import it.eng.dome.search.rest.web.util.RestUtil;
import it.eng.dome.search.semantic.domain.CategorizationResultObject;
import org.springframework.web.client.HttpStatusCodeException;

@Component
public class MappingManager {

	@Autowired
	private RestUtil restTemplate;
	
	@Autowired
	private RestSemanticUtil restSemanticUtil;

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
		objToIndex.setProductOfferingLifecycleStatus(product.getLifecycleStatus());
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

			for (ServiceSpecification s : serviceList) {
				try {
					String requestForServiceSpecificationId = restTemplate.getServiceSpecificationById(s.getId());

					if (requestForServiceSpecificationId == null) {
						log.warn("getServiceSpecificationById {} cannot found", s.getId());
					} else {

						ServiceSpecification serviceSpecDetails = objectMapper.readValue(requestForServiceSpecificationId, ServiceSpecification.class);

						listServiceDetails.add(serviceSpecDetails);
					}
				} catch (HttpStatusCodeException exception) {
					log.error("Error for getServiceSpecificationById with status: {} - {}", exception.getStatusCode().value(), exception.getStatusCode().name());
				}
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

			for (ResourceSpecification r : resourceList) {
				try {
					String requestForResourceSpecificationId = restTemplate.getResourceSpecificationById(r.getId());

					if (requestForResourceSpecificationId == null) {
						log.warn("getResourceSpecificationById {} cannot found", r.getId());
					} else {
						ResourceSpecification resourceSpecDetails = objectMapper.readValue(requestForResourceSpecificationId, ResourceSpecification.class);

						listResourceDetails.add(resourceSpecDetails);
					}
				} catch (HttpStatusCodeException exception) {
					log.error("Error for getResourceSpecificationById with status: {} - {}", exception.getStatusCode().value(), exception.getStatusCode().name());
				}
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

			for (ServiceSpecification s : serviceList) {

				try {
					String requestForServiceSpecificationId = restTemplate.getTMFServiceSpecificationById(s.getId());

					if (requestForServiceSpecificationId == null) {
						log.warn("getTMFServiceSpecificationById {} cannot found", s.getId());
					} else {

						ServiceSpecification serviceSpecDetails = objectMapper.readValue(requestForServiceSpecificationId, ServiceSpecification.class);

						listServiceDetails.add(serviceSpecDetails);
					}
				} catch (HttpStatusCodeException exception) {
					log.error("Error for getTMFServiceSpecificationById with status: {} - {}", exception.getStatusCode().value(), exception.getStatusCode().name());
				}
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

			for (ResourceSpecification r : resourceList) {

				try {
					String requestForResourceSpecificationId = restTemplate.getTMFResourceSpecificationById(r.getId());
					if (requestForResourceSpecificationId == null) {
						log.warn("getTMFResourceSpecificationById {} cannot found", r.getId());
					} else {
						ResourceSpecification resourceSpecDetails = objectMapper.readValue(requestForResourceSpecificationId, ResourceSpecification.class);

						listResourceDetails.add(resourceSpecDetails);
					}
				} catch (HttpStatusCodeException exception) {
					log.error("Error for getTMFResourceSpecificationById with status: {} - {}", exception.getStatusCode().value(), exception.getStatusCode().name());
				}
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


    public IndexingObject prepareClassify(IndexingObject objToIndex) {

        try {
            String contentToClassify = null;
            if (objToIndex.getProductOfferingDescription() != null){
				contentToClassify = objToIndex.getProductOfferingDescription();
			}

			//log.debug("Content to Classify {}", contentToClassify);

			if (contentToClassify != null) {
				contentToClassify = Jsoup.parse(contentToClassify).text();
				log.info("Product Offering ID {}", objToIndex.getProductOfferingId());
				try {
					String requestForClassifyObject = restSemanticUtil.classifyText(contentToClassify);
					if (requestForClassifyObject == null) {
						log.warn("ClassifyText: product offering ID {} cannot found", objToIndex.getProductOfferingId());
					} else {
						CategorizationResultObject categorizationResultObj = objectMapper.readValue(requestForClassifyObject, CategorizationResultObject.class);
						String[] cat = categorizationResultObj.getIpct_categories();
//                log.info("CategorizationResultObject: {}", cat);
						if (cat.length != 0) {
							objToIndex.setClassifyResult(cat);
						}
						/*
						 * if(cat.length!=0) { for(String s :cat) objToIndex.setClassifyResult(" , "+s);
						 * }
						 */
					}

				} catch (HttpStatusCodeException exception) {
					log.error("Error for classifyText with status: {} - {}", exception.getStatusCode().value(), exception.getStatusCode().name());
				}
			}
		} catch (JsonProcessingException e) {
			log.warn("JsonProcessingException - Error during prepareClassify(). Skipped: {}", e.getMessage());
		}

        return objToIndex;
    }

    public IndexingObject prepareAnalyze(IndexingObject objToIndex) {

        try {
            if (objToIndex.getProductOfferingDescription() != null){
                String contentToAnalyze = objToIndex.getProductOfferingDescription();
                contentToAnalyze = contentToAnalyze.replace("\\", " ");
                contentToAnalyze = Jsoup.parse(contentToAnalyze).text();
                if (!contentToAnalyze.isEmpty()) {
					try {
						String requestForAnalyzeObject = restSemanticUtil.analyzeText(contentToAnalyze);
						if (requestForAnalyzeObject == null) {
							log.warn("analyzeText {} cannot found", contentToAnalyze);
						} else{
							AnalyzeResultObject analyzeResultObject = objectMapper.readValue(requestForAnalyzeObject, AnalyzeResultObject.class);

							Analysis cat = analyzeResultObject.getAnalysis();
							if (!cat.content.isEmpty()) {
								objToIndex.setAnalyzeResult(cat.getContent());
							}

							/*
							 * if(cat.length!=0) { for(String s :cat) objToIndex.setAnalyzeResult(" , "+s);
							 * }
							 */
						}
					} catch (HttpStatusCodeException exception) {
						log.error("Error for analyzeText with status: {} - {}", exception.getStatusCode().value(), exception.getStatusCode().name());
					}
                }
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return objToIndex;
    }
}
