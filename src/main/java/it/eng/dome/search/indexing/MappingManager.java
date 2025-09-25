package it.eng.dome.search.indexing;

import it.eng.dome.brokerage.api.ResourceSpecificationApis;
import it.eng.dome.brokerage.api.ServiceSpecificationApis;
import it.eng.dome.search.domain.IndexingObject;
import it.eng.dome.search.domain.dto.ResourceSpecificationDTO;
import it.eng.dome.search.domain.dto.ServiceSpecificationDTO;
import it.eng.dome.search.tmf.TmfApiFactory;
import it.eng.dome.tmforum.tmf620.v4.model.*;
import it.eng.dome.tmforum.tmf633.v4.model.ServiceSpecification;
import it.eng.dome.tmforum.tmf634.v4.model.ResourceSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class MappingManager implements InitializingBean{

	private static final Logger log = LoggerFactory.getLogger(MappingManager.class);

	@Autowired
	TmfApiFactory tmfApiFactory;

	ServiceSpecificationApis serviceSpecificationApis;

	ResourceSpecificationApis resourceSpecificationApis;

	@Override
	public void afterPropertiesSet () throws Exception {
		this.serviceSpecificationApis = new ServiceSpecificationApis(tmfApiFactory.getTMF633ServiceCatalogApiClient());
		this.resourceSpecificationApis = new ResourceSpecificationApis(tmfApiFactory.getTMF634ResourceCatalogApiClient());

		log.info("MappingManager initialized with ServiceSpecificationApis and ResourceSpecificationApis");
	}

	public IndexingObject prepareOfferingMetadata(ProductOffering product, IndexingObject objToIndex) {

		//prepare metadata of offerings
//		objToIndex.setProductOffering(product);
		objToIndex.setProductOfferingDescription(product.getDescription());
		objToIndex.setProductOfferingId(product.getId());
		objToIndex.setProductOfferingIsBundle(product.getIsBundle());
		objToIndex.setProductOfferingLastUpdate(product.getLastUpdate().toString());
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

	/*public IndexingObject prepareServiceSpecMetadata(ServiceSpecification[] serviceList, IndexingObject objToIndex) {

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
				} catch (ResourceAccessException e) {
					log.error("Error for getServiceSpecificationById. Caught ResourceAccessException: {}", e.getMessage());
					e.printStackTrace();
				}
			}

		} catch (JsonProcessingException e) {
			log.warn("JsonProcessingException - Error during prepareServiceSpecMetadata(). Skipped: {}", e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			log.warn("Exception - Error during prepareServiceSpecMetadata(). Skipped: {}", e.getMessage());
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
				} catch (ResourceAccessException e) {
					log.error("Error for getResourceSpecificationById. Caught ResourceAccessException: {}", e.getMessage());
					e.printStackTrace();
				}
			}

		} catch (JsonProcessingException e) {
			log.warn("JsonProcessingException - Error during prepareResourceSpecMetadata(). Skipped: {}", e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			log.warn("Exception - Error during prepareResourceSpecMetadata(). Skipped: {}", e.getMessage());
			e.printStackTrace();
		}

		//ResourceSpecification[] listResourceToIndex = new ResourceSpecification[listResourceDetails.size()];
		ResourceSpecification[] listResourceToIndex = listResourceDetails.toArray(new ResourceSpecification[listResourceDetails.size()]);

		objToIndex.setResources(listResourceToIndex);

		return objToIndex;
	}*/

	public IndexingObject prepareTMFServiceSpecMetadata(List<ServiceSpecificationRef> serviceList, IndexingObject objToIndex) {

		ArrayList<ServiceSpecification> listServiceDetails = new ArrayList<ServiceSpecification>();
		try {
			for (ServiceSpecificationRef s : serviceList) {
				try {
//					String requestForServiceSpecificationId = restTemplate.getTMFServiceSpecificationById(s.getId());
					ServiceSpecification serviceSpecification = serviceSpecificationApis.getServiceSpecification(s.getId(), null);

					if (serviceSpecification == null) {
						log.warn("getTMFServiceSpecificationById {} - Service Specification cannot found", s.getId());
					} else {
//						ServiceSpecification serviceSpecDetails = objectMapper.readValue(requestForServiceSpecificationId, ServiceSpecification.class);

						listServiceDetails.add(serviceSpecification);
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
//		ServiceSpecification[] listServiceToIndex = listServiceDetails.toArray(new ServiceSpecification[listServiceDetails.size()]);
		List<ServiceSpecificationDTO> listServiceDTO = new ArrayList<>();
		for(ServiceSpecification serv : listServiceDetails) {
			ServiceSpecificationDTO servDTO = new ServiceSpecificationDTO();
			servDTO.setId(serv.getId());
			servDTO.setName(serv.getName());
			servDTO.setDescription(serv.getDescription());
			servDTO.setVersion(serv.getVersion());
			servDTO.setLifecycleStatus(serv.getLifecycleStatus());
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
			servDTO.setLastUpdate(serv.getLastUpdate().format(formatter));

			listServiceDTO.add(servDTO);

		}
		ServiceSpecificationDTO[] arrayServiceDTO = new ServiceSpecificationDTO[listServiceDTO.size()];
		listServiceDTO.toArray(arrayServiceDTO);
		objToIndex.setServices(arrayServiceDTO);

		return objToIndex;
	}


	public IndexingObject prepareTMFResourceSpecMetadata(List<ResourceSpecificationRef> resourceList, IndexingObject objToIndex) {

		List<ResourceSpecification> listResourceDetails = new ArrayList<ResourceSpecification>();
		try {
			for (ResourceSpecificationRef r : resourceList) {
				try {
//					String requestForResourceSpecificationId = restTemplate.getTMFResourceSpecificationById(r.getId());
					ResourceSpecification resourceSpecification = resourceSpecificationApis.getResourceSpecification(r.getId(), null);
					if (resourceSpecification == null) {
						log.warn("getTMFResourceSpecificationById {} - Resource Specification cannot found", r.getId());
					} else {
//						ResourceSpecification resourceSpecDetails = objectMapper.readValue(requestForResourceSpecificationId, ResourceSpecification.class);
						listResourceDetails.add(resourceSpecification);
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
//		ResourceSpecification[] listResourceToIndex = listResourceDetails.toArray(new ResourceSpecification[listResourceDetails.size()]);

		List<ResourceSpecificationDTO> listResourceDTO = new ArrayList<>();
		for(ResourceSpecification res : listResourceDetails) {
			ResourceSpecificationDTO resDTO = new ResourceSpecificationDTO();
			resDTO.setId(res.getId());
			resDTO.setName(res.getName());
			resDTO.setDescription(res.getDescription());
			resDTO.setVersion(res.getVersion());
			resDTO.setLifecycleStatus(res.getLifecycleStatus());
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
			resDTO.setLastUpdate(res.getLastUpdate().format(formatter));

//			resDTO.setCategory(res.getCategory());
//			resDTO.setCharacteristic(res.getCharacteristic());
//			resDTO.setResourceSpecCharacteristic(res.getResourceSpecCharacteristic());
//			resDTO.setResourceType(res.getResourceType());
			resDTO.setRelatedParty(res.getRelatedParty());
			listResourceDTO.add(resDTO);
		}
		ResourceSpecificationDTO[] arrayResourceDTO = new ResourceSpecificationDTO[listResourceDTO.size()];
		listResourceDTO.toArray(arrayResourceDTO);
		objToIndex.setResources(arrayResourceDTO);

		return objToIndex;
	}

/*    public IndexingObject prepareClassify(IndexingObject objToIndex) {

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
						*//*
						 * if(cat.length!=0) { for(String s :cat) objToIndex.setClassifyResult(" , "+s);
						 * }
						 *//*
					}

				} catch (HttpStatusCodeException exception) {
					log.error("Error for classifyText with status: {} - {}", exception.getStatusCode().value(), exception.getStatusCode().name());
				} catch (ResourceAccessException e) {
					log.error("Error for classifyText. Caught ResourceAccessException: {}", e.getMessage());
					e.printStackTrace();
				}
			}
		} catch (JsonProcessingException e) {
			log.warn("JsonProcessingException - Error during prepareClassify(). Skipped: {}", e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			log.warn("Exception - Error during prepareClassify(). Skipped: {}", e.getMessage());
			e.printStackTrace();
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

							*//*
							 * if(cat.length!=0) { for(String s :cat) objToIndex.setAnalyzeResult(" , "+s);
							 * }
							 *//*
						}
					} catch (HttpStatusCodeException exception) {
						log.error("Error for analyzeText with status: {} - {}", exception.getStatusCode().value(), exception.getStatusCode().name());
					} catch (ResourceAccessException e) {
						log.error("Error for analyzeText. Caught ResourceAccessException: {}", e.getMessage());
						e.printStackTrace();
					}
                }
            }

        } catch (JsonProcessingException e) {
			log.warn("JsonProcessingException - Error during prepareAnalyze(). Skipped: {}", e.getMessage());
			e.printStackTrace();
        } catch (Exception e) {
			log.warn("Exception - Error during prepareAnalyze(). Skipped: {}", e.getMessage());
			e.printStackTrace();
		}
        return objToIndex;
    }*/
}
