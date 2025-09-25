package it.eng.dome.search.indexing;

import java.util.ArrayList;
import java.util.List;

import it.eng.dome.brokerage.api.ResourceSpecificationApis;
import it.eng.dome.brokerage.api.ServiceSpecificationApis;
import it.eng.dome.search.tmf.TmfApiFactory;
import it.eng.dome.tmforum.tmf633.v4.model.ServiceSpecification;
import it.eng.dome.tmforum.tmf634.v4.model.ResourceSpecification;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.eng.dome.brokerage.api.OrganizationApis;
import it.eng.dome.brokerage.api.ProductSpecificationApis;
import it.eng.dome.search.domain.IndexingObject;
import it.eng.dome.search.rest.web.util.RestSemanticUtil;
import it.eng.dome.search.rest.web.util.RestUtil;
import it.eng.dome.search.semantic.domain.Analysis;
import it.eng.dome.search.semantic.domain.AnalyzeResultObject;
import it.eng.dome.search.semantic.domain.CategorizationResultObject;
import it.eng.dome.tmforum.tmf620.v4.model.ProductOffering;
import it.eng.dome.tmforum.tmf620.v4.model.ProductSpecification;
import it.eng.dome.tmforum.tmf620.v4.model.RelatedParty;
import it.eng.dome.tmforum.tmf620.v4.model.ResourceSpecificationRef;
import it.eng.dome.tmforum.tmf620.v4.model.ServiceSpecificationRef;

@Component
public class MappingManager implements InitializingBean {

	private static final Logger log = LoggerFactory.getLogger(MappingManager.class);

	@Autowired
	private RestUtil restTemplate;

	@Autowired
	private RestSemanticUtil restSemanticUtil;

	private static final ObjectMapper objectMapper = new ObjectMapper();

	// Factory for TMF APIss
	@Autowired
	private TmfApiFactory tmfApiFactory;

	private ServiceSpecificationApis serviceSpecificationApis;

	private ResourceSpecificationApis resourceSpecificationApis;

	@Override
	public void afterPropertiesSet() throws Exception {
		this.serviceSpecificationApis = new ServiceSpecificationApis(tmfApiFactory.getTMF633ServiceCatalogApiClient());
		this.resourceSpecificationApis = new ResourceSpecificationApis(tmfApiFactory.getTMF634ResourceCatalogApiClient());

		log.info("MappingManager initialized with ServiceSpecificationApis and ResourceSpecificationApis");
	}

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

//	public IndexingObject prepareServiceSpecMetadata(List<ServiceSpecificationRef> serviceList, IndexingObject objToIndex) {
//		ArrayList<ServiceSpecificationRef> listServiceDetails = new ArrayList<>();
//
//		try {
//			for (ServiceSpecificationRef s : serviceList) {
//				try {
//					String requestForServiceSpecificationId = restTemplate.getServiceSpecificationById(s.getId());
//
//					if (requestForServiceSpecificationId == null) {
//						log.warn("getServiceSpecificationById {} cannot found", s.getId());
//					} else {
//
//						ServiceSpecificationRef serviceSpecDetails = objectMapper.readValue(requestForServiceSpecificationId, ServiceSpecificationRef.class);
//
//						listServiceDetails.add(serviceSpecDetails);
//					}
//				} catch (HttpStatusCodeException exception) {
//					log.error("Error for getServiceSpecificationById with status: {} - {}", exception.getStatusCode().value(), exception.getStatusCode().name());
//				} catch (ResourceAccessException e) {
//					log.error("Error for getServiceSpecificationById. Caught ResourceAccessException: {}", e.getMessage(), e);
//				}
//			}
//		} catch (JsonProcessingException e) {
//			log.warn("JsonProcessingException - Error during prepareServiceSpecMetadata(). Skipped: {}", e.getMessage(), e);
//		} catch (Exception e) {
//			log.warn("Exception - Error during prepareServiceSpecMetadata(). Skipped: {}", e.getMessage(), e);
//		}
//
//		//ServiceSpecification[] listServiceToIndex = new ServiceSpecification[listServiceDetails.size()];
//		// List<ServiceSpecification> listServiceToIndex = listServiceDetails.toArray(new ServiceSpecification[listServiceDetails.size()]);
//		objToIndex.setServices(listServiceDetails);
//		return objToIndex;
//	}

/*
	public IndexingObject prepareResourceSpecMetadata(List<ResourceSpecificationRef> resourceList, IndexingObject objToIndex) {
		List<ResourceSpecificationRef> listResourceDetails = new ArrayList<ResourceSpecificationRef>();

		try {
			for (ResourceSpecificationRef r : resourceList) {
				try {
					String requestForResourceSpecificationId = restTemplate.getResourceSpecificationById(r.getId());

					if (requestForResourceSpecificationId == null) {
						log.warn("getResourceSpecificationById {} cannot found", r.getId());
					} else {
						ResourceSpecificationRef resourceSpecDetails = objectMapper.readValue(requestForResourceSpecificationId, ResourceSpecificationRef.class);

						listResourceDetails.add(resourceSpecDetails);
					}
				} catch (HttpStatusCodeException exception) {
					log.error("Error for getResourceSpecificationById with status: {} - {}", exception.getStatusCode().value(), exception.getStatusCode().name());
				} catch (ResourceAccessException e) {
					log.error("Error for getResourceSpecificationById. Caught ResourceAccessException: {}", e.getMessage(), e);
				}
			}
		} catch (JsonProcessingException e) {
			log.warn("JsonProcessingException - Error during prepareResourceSpecMetadata(). Skipped: {}", e.getMessage(), e);
		} catch (Exception e) {
			log.warn("Exception - Error during prepareResourceSpecMetadata(). Skipped: {}", e.getMessage(), e);
		}

		//ResourceSpecification[] listResourceToIndex = new ResourceSpecification[listResourceDetails.size()];
		// ResourceSpecification[] listResourceToIndex = listResourceDetails.toArray(new ResourceSpecification[listResourceDetails.size()]);
		objToIndex.setResources(listResourceDetails);
		return objToIndex;
	}
*/

	public IndexingObject prepareTMFServiceSpecMetadata(List<ServiceSpecificationRef> serviceList, IndexingObject objToIndex) {

		ArrayList<ServiceSpecificationRef> listServiceDetails = new ArrayList<ServiceSpecificationRef>();
		try {
			for (ServiceSpecificationRef s : serviceList) {
				try {
//					String requestForServiceSpecificationId = restTemplate.getTMFServiceSpecificationById(s.getId());
					ServiceSpecification serviceSpec = serviceSpecificationApis.getServiceSpecification(s.getId(), null);

					if (serviceSpec == null) {
						log.warn("cannot found Service Specification with ID {}", s.getId());
					} else {

//						ServiceSpecificationRef serviceSpecDetails = objectMapper.readValue(requestForServiceSpecificationId, ServiceSpecificationRef.class);
						ServiceSpecificationRef serviceSpecRef = new ServiceSpecificationRef();
						serviceSpecRef.setId(serviceSpec.getId());
						serviceSpecRef.setHref(serviceSpec.getHref());
						serviceSpecRef.setName(serviceSpec.getName());

						listServiceDetails.add(serviceSpecRef);
					}
				} catch (HttpStatusCodeException exception) {
					log.error("Error for getTMFServiceSpecificationById with status: {} - {}", exception.getStatusCode().value(), exception.getStatusCode().name());
				} catch (ResourceAccessException e) {
					log.error("Error for prepareTMFServiceSpecMetadata. Caught ResourceAccessException: {}", e.getMessage(), e);
				}
			}

		} catch (Exception e) {
			log.warn("Exception - Error during prepareServiceSpecMetadata(). Skipped: {}", e.getMessage(), e);
		}

		//ServiceSpecification[] listServiceToIndex = new ServiceSpecification[listServiceDetails.size()];
		// ServiceSpecification[] listServiceToIndex = listServiceDetails.toArray(new ServiceSpecification[listServiceDetails.size()]);
		objToIndex.setServices(listServiceDetails);
		return objToIndex;
	}

	public IndexingObject prepareTMFResourceSpecMetadata(List<ResourceSpecificationRef> resourceList, IndexingObject objToIndex) {
		List<ResourceSpecificationRef> listResourceDetails = new ArrayList<ResourceSpecificationRef>();

		try {
			for (ResourceSpecificationRef r : resourceList) {

				try {
//					String requestForResourceSpecificationId = restTemplate.getTMFResourceSpecificationById(r.getId());
					ResourceSpecification resourceSpec = resourceSpecificationApis.getResourceSpecification(r.getId(), null);
					if (resourceSpec == null) {
						log.warn("cannot found Resource Specification with ID {}", r.getId());
					} else {
//						ResourceSpecificationRef resourceSpecDetails = objectMapper.readValue(requestForResourceSpecificationId, ResourceSpecificationRef.class);
						ResourceSpecificationRef resourceSpecRef = new ResourceSpecificationRef();
						resourceSpecRef.setId(resourceSpec.getId());
						resourceSpecRef.setHref(resourceSpec.getHref());
						resourceSpecRef.setName(resourceSpec.getName());

						listResourceDetails.add(resourceSpecRef);
					}
				} catch (HttpStatusCodeException exception) {
					log.error("Error for getTMFResourceSpecificationById with status: {} - {}", exception.getStatusCode().value(), exception.getStatusCode().name());
				} catch (ResourceAccessException e) {
					log.error("Error for prepareTMFResourceSpecMetadata. Caught ResourceAccessException: {}", e.getMessage(), e);
				}
			}
		} catch (Exception e) {
			log.warn("Exception - Error during prepareResourceSpecMetadata(). Skipped: {}", e.getMessage(), e);
		}

		//ResourceSpecification[] listResourceToIndex = new ResourceSpecification[listResourceDetails.size()];
		//ResourceSpecification[] listResourceToIndex = listResourceDetails.toArray(new ResourceSpecification[listResourceDetails.size()]);
		objToIndex.setResources(listResourceDetails);
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
				log.info("Classify for Product Offering ID {}", objToIndex.getProductOfferingId());
				try {
					String requestForClassifyObject = restSemanticUtil.classifyText(contentToClassify);

					if (requestForClassifyObject == null) {
						log.warn("ClassifyText: product offering ID {} cannot found", objToIndex.getProductOfferingId());
					} else {
						CategorizationResultObject categorizationResultObj = objectMapper.readValue(requestForClassifyObject, CategorizationResultObject.class);
						String[] cat = categorizationResultObj.getIpct_categories();

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
				} catch (ResourceAccessException e) {
					log.error("Error for classifyText. Caught ResourceAccessException: {}", e.getMessage(), e);
				}
			}
		} catch (JsonProcessingException e) {
			log.warn("JsonProcessingException - Error during prepareClassify(). Skipped: {}", e.getMessage(), e);
		} catch (Exception e) {
			log.warn("Exception - Error during prepareClassify(). Skipped: {}", e.getMessage(), e);
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
							log.warn("Request for analyzeText cannot found");
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
					} catch (ResourceAccessException e) {
						log.error("Error for analyzeText. Caught ResourceAccessException: {}", e.getMessage(), e);
					}
                }
            }
        } catch (JsonProcessingException e) {
			log.warn("JsonProcessingException - Error during prepareAnalyze(). Skipped: {}", e.getMessage(), e);
        } catch (Exception e) {
			log.warn("Exception - Error during prepareAnalyze(). Skipped: {}", e.getMessage(), e);
		}
        return objToIndex;
    }
}