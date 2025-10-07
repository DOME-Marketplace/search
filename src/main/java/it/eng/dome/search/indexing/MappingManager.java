package it.eng.dome.search.indexing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.eng.dome.search.domain.IndexingObject;
import it.eng.dome.search.domain.dto.*;
import it.eng.dome.search.rest.web.util.RestSemanticUtil;
import it.eng.dome.search.semantic.domain.Analysis;
import it.eng.dome.search.semantic.domain.AnalyzeResultObject;
import it.eng.dome.search.semantic.domain.CategorizationResultObject;
import it.eng.dome.search.service.TmfDataRetriever;
import it.eng.dome.tmforum.tmf620.v4.model.*;
import it.eng.dome.tmforum.tmf633.v4.model.ServiceSpecification;
import it.eng.dome.tmforum.tmf634.v4.model.ResourceSpecification;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class MappingManager {

	private static final Logger log = LoggerFactory.getLogger(MappingManager.class);

	private static final ObjectMapper objectMapper = new ObjectMapper();

	// --- static unique formatter ---
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");

	@Autowired
	private RestSemanticUtil restSemanticUtil;

	@Autowired
	private TmfDataRetriever tmfDataRetriever;

	public IndexingObject prepareOfferingMetadata(ProductOffering product, IndexingObject objToIndex) {
		// prepare metadata of offerings
		ProductOfferingDTO productDTO = toProductOfferingDTO(product);
		objToIndex.setProductOffering(productDTO);
		objToIndex.setProductOfferingDescription(product.getDescription());
		objToIndex.setProductOfferingId(product.getId());
		objToIndex.setProductOfferingIsBundle(product.getIsBundle());
		objToIndex.setProductOfferingLastUpdate(product.getLastUpdate().toString());
		objToIndex.setProductOfferingLifecycleStatus(product.getLifecycleStatus());
		objToIndex.setProductOfferingName(product.getName());
		objToIndex.setProductOfferingNameText(product.getName());
		objToIndex.setCategories(toCategoryDTOList(product.getCategory()));
		return objToIndex;
	}

	public IndexingObject prepareProdSpecMetadata(ProductSpecification productSpecDetails, IndexingObject objToIndex) {
		// prepare metadata of Product Specifications
		ProductSpecificationDTO prodSpecDTO = toProductSpecificationDTO(productSpecDetails);
		objToIndex.setProductSpecification(prodSpecDTO);
		objToIndex.setProductSpecificationBrand(productSpecDetails.getBrand());
		objToIndex.setProductSpecificationId(productSpecDetails.getId());
		objToIndex.setProductSpecificationName(productSpecDetails.getName());

		if (productSpecDetails.getDescription() != null)
			objToIndex.setProductSpecificationDescription(productSpecDetails.getDescription());

		if (productSpecDetails.getRelatedParty() != null) {
			for (RelatedParty party : productSpecDetails.getRelatedParty()) {
				objToIndex.setRelatedPartyId(party.getId());
			}
		}

		return objToIndex;
	}

	public IndexingObject prepareTMFServiceSpecMetadata(List<ServiceSpecificationRef> serviceList, IndexingObject objToIndex) {
		// Retrieve TMF objects
		List<ServiceSpecification> listServiceDetails = fetchServiceSpecifications(serviceList);
		// Mapping to DTO and set in IndexingObject
		objToIndex.setServices(toServiceSpecificationDTOs(listServiceDetails));
		return objToIndex;
	}

	public IndexingObject prepareTMFResourceSpecMetadata(List<ResourceSpecificationRef> resourceList, IndexingObject objToIndex) {
		// Retrieve TMF objects
		List<ResourceSpecification> listResourceDetails = fetchResourceSpecifications(resourceList);
		// Mapping to DTO and set in IndexingObject
		objToIndex.setResources(toResourceSpecificationDTOs(listResourceDetails));
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

						 /* if(cat.length!=0) { for(String s :cat) objToIndex.setClassifyResult(" , "+s);
						 }*/

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


							/* if(cat.length!=0) { for(String s :cat) objToIndex.setAnalyzeResult(" , "+s);
							 }*/

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

	// --- Helper methods for fetching TMF objects ---
	private List<ServiceSpecification> fetchServiceSpecifications(List<ServiceSpecificationRef> serviceRefs) {
		List<ServiceSpecification> list = new ArrayList<>();

		for (ServiceSpecificationRef s : serviceRefs) {
			try {
				ServiceSpecification spec = tmfDataRetriever.getServiceSpecificationById(s.getId(), null);
				if (spec != null) {
					list.add(spec);
				} else {
					log.warn("getTMFServiceSpecificationById {} - Service Specification cannot found", s.getId());
				}
			} catch (HttpStatusCodeException exception) {
				log.error("Error for getTMFServiceSpecificationById with status: {} - {}", exception.getStatusCode().value(), exception.getStatusCode().name());
			} catch (ResourceAccessException e) {
				log.error("Error for prepareTMFServiceSpecMetadata. Caught ResourceAccessException: {}", e.getMessage(), e);
			}
		}

		return list;
	}

	private List<ResourceSpecification> fetchResourceSpecifications(List<ResourceSpecificationRef> resourceRefs) {
		List<ResourceSpecification> list = new ArrayList<>();

		for (ResourceSpecificationRef r : resourceRefs) {
			try {
				ResourceSpecification spec = tmfDataRetriever.getResourceSpecificationById(r.getId(), null);
				if (spec != null) {
					list.add(spec);
				} else {
					log.warn("getTMFResourceSpecificationById {} - Resource Specification cannot found", r.getId());
				}
			} catch (HttpStatusCodeException exception) {
				log.error("Error for getTMFResourceSpecificationById with status: {} - {}", exception.getStatusCode().value(), exception.getStatusCode().name());
			} catch (ResourceAccessException e) {
				log.error("Error for prepareTMFResourceSpecMetadata. Caught ResourceAccessException: {}", e.getMessage(), e);
			}
		}

		return list;
	}

	// --- Helper methods for conversion to DTO ---
	private List<ServiceSpecificationDTO> toServiceSpecificationDTOs(List<ServiceSpecification> list) {
		List<ServiceSpecificationDTO> dtos = new ArrayList<>();

		for (ServiceSpecification s : list)
			dtos.add(toServiceSpecificationDTO(s));

		return dtos;
	}

	private List<ResourceSpecificationDTO> toResourceSpecificationDTOs(List<ResourceSpecification> list) {
		List<ResourceSpecificationDTO> dtos = new ArrayList<>();

		for (ResourceSpecification r : list)
			dtos.add(toResourceSpecificationDTO(r));

		return dtos;
	}

	// --- Helper methods for DTO conversion ---
	private ProductOfferingDTO toProductOfferingDTO(ProductOffering product) {
		ProductOfferingDTO dto = new ProductOfferingDTO();
		dto.setId(product.getId());
		dto.setName(product.getName());
		dto.setHref(product.getHref());
		dto.setDescription(product.getDescription());
		dto.setIsBundle(product.getIsBundle());
		dto.setLifecycleStatus(product.getLifecycleStatus());
		dto.setLastUpdate(product.getLastUpdate().toString());
		dto.setCategory(product.getCategory());
		dto.setVersion(product.getVersion());
		dto.setProductSpecification(product.getProductSpecification());
		dto.setProductOfferingPrice(product.getProductOfferingPrice());
		return dto;
	}

	private ProductSpecificationDTO toProductSpecificationDTO(ProductSpecification productSpec) {
		ProductSpecificationDTO dto = new ProductSpecificationDTO();
		dto.setId(productSpec.getId());
		dto.setHref(productSpec.getHref());
		dto.setName(productSpec.getName());
		dto.setDescription(productSpec.getDescription());
		dto.setBrand(productSpec.getBrand());
		dto.setVersion(productSpec.getVersion());
		dto.setLifecycleStatus(productSpec.getLifecycleStatus());
		dto.setIsBundle(productSpec.getIsBundle());
		dto.setLastUpdate(productSpec.getLastUpdate().toString());
		dto.setRelatedParty(productSpec.getRelatedParty());
		dto.setProductNumber(productSpec.getProductNumber());
		return dto;
	}

	private ServiceSpecificationDTO toServiceSpecificationDTO(ServiceSpecification serviceSpec) {
		ServiceSpecificationDTO dto = new ServiceSpecificationDTO();
		dto.setId(serviceSpec.getId());
		dto.setName(serviceSpec.getName());
		dto.setDescription(serviceSpec.getDescription());
		dto.setVersion(serviceSpec.getVersion());
		dto.setLifecycleStatus(serviceSpec.getLifecycleStatus());
		dto.setLastUpdate(serviceSpec.getLastUpdate().format(DATE_FORMATTER));
		return dto;
	}

	private ResourceSpecificationDTO toResourceSpecificationDTO(ResourceSpecification resourceSpec) {
		ResourceSpecificationDTO dto = new ResourceSpecificationDTO();
		dto.setId(resourceSpec.getId());
		dto.setName(resourceSpec.getName());
		dto.setDescription(resourceSpec.getDescription());
		dto.setVersion(resourceSpec.getVersion());
		dto.setLifecycleStatus(resourceSpec.getLifecycleStatus());
		//DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
		dto.setLastUpdate(resourceSpec.getLastUpdate().format(DATE_FORMATTER));
		dto.setRelatedParty(resourceSpec.getRelatedParty());
		return dto;
	}

	List<CategoryDTO> toCategoryDTOList(List<CategoryRef> categoryRefs) {
		List<CategoryDTO> dtos = new ArrayList<>();
		if(categoryRefs!=null) {
			for (CategoryRef c : categoryRefs)
				dtos.add(toCategoryDTO(c));
		}
		return dtos;
	}

	private CategoryDTO toCategoryDTO(CategoryRef categoryRef) {
		CategoryDTO dto = new CategoryDTO();
		dto.setId(categoryRef.getId());
		dto.setHref(categoryRef.getHref().toString());
		dto.setName(categoryRef.getName());
		//TODO: add other fields if needed
		return dto;
	}

}
