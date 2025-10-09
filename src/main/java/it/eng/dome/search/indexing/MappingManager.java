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
		dto.setName(product.getName() != null ? product.getName() : null);
		dto.setHref(product.getHref() != null ? product.getHref().toString() : null);
		dto.setDescription(product.getDescription() != null ? product.getDescription() : null);
		dto.setIsBundle(product.getIsBundle() != null ? product.getIsBundle() : null);
		dto.setLifecycleStatus(product.getLifecycleStatus() != null ? product.getLifecycleStatus() : null);
		dto.setLastUpdate(product.getLastUpdate() != null
				? product.getLastUpdate().format(DATE_FORMATTER)
				: null);
		dto.setCategory(product.getCategory() );
		dto.setVersion(product.getVersion() != null ? product.getVersion() : null);
		dto.setProductSpecification(product.getProductSpecification());
		dto.setProductOfferingPrice(product.getProductOfferingPrice());
		return dto;
	}

	private ProductSpecificationDTO toProductSpecificationDTO(ProductSpecification productSpec) {
		ProductSpecificationDTO dto = new ProductSpecificationDTO();
		dto.setId(productSpec.getId());
		dto.setHref(productSpec.getHref() != null ? productSpec.getHref().toString() : null);
		dto.setName(productSpec.getName() != null ? productSpec.getName() : null);
		dto.setDescription(productSpec.getDescription() != null ? productSpec.getDescription() : null);
		dto.setBrand(productSpec.getBrand() != null ? productSpec.getBrand() : null);
		dto.setVersion(productSpec.getVersion() != null ? productSpec.getVersion() : null);
		dto.setLifecycleStatus(productSpec.getLifecycleStatus() != null ? productSpec.getLifecycleStatus() : null);
		dto.setIsBundle(productSpec.getIsBundle() != null ? productSpec.getIsBundle() : null);
		dto.setLastUpdate(productSpec.getLastUpdate() != null
				? productSpec.getLastUpdate().format(DATE_FORMATTER)
				: null);
		dto.setRelatedParty(productSpec.getRelatedParty() != null ? productSpec.getRelatedParty() : null);
		dto.setProductNumber(productSpec.getProductNumber() != null ? productSpec.getProductNumber() : null);
		return dto;
	}

	private ServiceSpecificationDTO toServiceSpecificationDTO(ServiceSpecification serviceSpec) {
		ServiceSpecificationDTO dto = new ServiceSpecificationDTO();
		dto.setId(serviceSpec.getId());
		dto.setName(serviceSpec.getName() != null ? serviceSpec.getName() : null);
		dto.setDescription(serviceSpec.getDescription() != null ? serviceSpec.getDescription() : null);
		dto.setVersion(serviceSpec.getVersion() != null ? serviceSpec.getVersion() : null);
		dto.setLifecycleStatus(serviceSpec.getLifecycleStatus() != null ? serviceSpec.getLifecycleStatus() : null);
		dto.setLastUpdate(serviceSpec.getLastUpdate() != null
				? serviceSpec.getLastUpdate().format(DATE_FORMATTER)
				: null);		return dto;
	}

	private ResourceSpecificationDTO toResourceSpecificationDTO(ResourceSpecification resourceSpec) {
		ResourceSpecificationDTO dto = new ResourceSpecificationDTO();
		dto.setId(resourceSpec.getId());
		dto.setName(resourceSpec.getName() != null ? resourceSpec.getName() : null);
		dto.setDescription(resourceSpec.getDescription() != null ? resourceSpec.getDescription() : null);
		dto.setVersion(resourceSpec.getVersion() != null ? resourceSpec.getVersion() : null);
		dto.setLifecycleStatus(resourceSpec.getLifecycleStatus() != null ? resourceSpec.getLifecycleStatus() : null);
		//DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
		dto.setLastUpdate(resourceSpec.getLastUpdate() != null
				? resourceSpec.getLastUpdate().format(DATE_FORMATTER)
				: null);
		dto.setRelatedParty(resourceSpec.getRelatedParty() != null ? resourceSpec.getRelatedParty() : null);
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
		dto.setHref(categoryRef.getHref() != null ? categoryRef.getHref().toString() : null);
		dto.setName(categoryRef.getName() != null ? categoryRef.getName() : null);
		//TODO: add other fields if needed
		return dto;
	}

}
