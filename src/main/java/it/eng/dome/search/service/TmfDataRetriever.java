package it.eng.dome.search.service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import it.eng.dome.brokerage.api.APIPartyApis;
import it.eng.dome.brokerage.api.ProductCatalogManagementApis;
import it.eng.dome.brokerage.api.ResourceCatalogManagementApis;
import it.eng.dome.brokerage.api.ServiceCatalogManagementApis;
import it.eng.dome.brokerage.api.fetch.FetchUtils;
import it.eng.dome.tmforum.tmf620.v4.model.Category;
import it.eng.dome.tmforum.tmf620.v4.model.ProductOffering;
import it.eng.dome.tmforum.tmf620.v4.model.ProductSpecification;
import it.eng.dome.tmforum.tmf632.v4.ApiException;
import it.eng.dome.tmforum.tmf632.v4.model.Organization;
import it.eng.dome.tmforum.tmf633.v4.model.ServiceSpecification;
import it.eng.dome.tmforum.tmf634.v4.model.ResourceSpecification;

@Service
public class TmfDataRetriever {

    private final Logger logger = LoggerFactory.getLogger(TmfDataRetriever.class);
   
    private final ProductCatalogManagementApis productCatalogManagementApis;
    private final ServiceCatalogManagementApis serviceCatalogManagementApis;
    private final ResourceCatalogManagementApis resourceCatalogManagementApis;
    private final APIPartyApis apiPartyApis;

    public TmfDataRetriever(ProductCatalogManagementApis productCatalogManagementApis, 
    		ServiceCatalogManagementApis serviceCatalogManagementApis, 
    		ResourceCatalogManagementApis resourceCatalogManagementApis, APIPartyApis apiPartyApis) {
    	
    	this.productCatalogManagementApis = productCatalogManagementApis;
    	this.serviceCatalogManagementApis = serviceCatalogManagementApis;
    	this.resourceCatalogManagementApis = resourceCatalogManagementApis;
    	this.apiPartyApis = apiPartyApis;
    }


    // =============== TMF ORGANIZATIONS ===============

    public Organization getOrganizationById(String orgId, String fields) {

        if (orgId == null) {
            logger.warn("Organization ID is null, cannot retrieve organization.");
            return null;
        }
        try {
        	
//        	logger.debug("Retrieving Organization from TMF API with id: {}", orgId);
			return apiPartyApis.getOrganization(orgId, fields);
		} catch (ApiException e) {
			logger.error("Error: {}", e.getMessage());
			return null;
		}
    }

    public List<Organization> getAllPaginatedOrganizations(String fields, Map<String, String> filter, int pageSize) {
        List<Organization> allOrganizations = FetchUtils.streamAll(
	        apiPartyApis::listOrganizations,    // method reference
	        fields,                       		// fields
	        filter,            					// filter
	        pageSize                         	// pageSize
		).collect(Collectors.toList());


       // logger.info("Retrieved {} organization from TMF API", allOrganizations.size());
        return allOrganizations;
    }

    // =============== PRODUCT OFFERING ===============

    public ProductOffering getProductOfferingById(String poId, String fields) {
    	
        if (poId == null) {
            logger.warn("Product Offering ID is null, cannot retrieve product offering.");
            return null;
        }
        
        //logger.debug("Retrieving Product Offering from TMF API with id: {}", poId);
        
        try {
			return productCatalogManagementApis.getProductOffering(poId, fields);
		} catch (it.eng.dome.tmforum.tmf620.v4.ApiException e) {
			logger.error("Error: {}", e.getMessage());
			return null;
		}
    }

    /**
     * Fetches ProductOfferings from TMF API in batches and processes each batch using the provided processor.
     *
     * @param filter    optional filters to apply (pass null for no filters)
     * @param batchSize number of items per batch
     * @param processor callback to process each batch of ProductOfferings
     */
    public void fetchProductOfferingsByBatch(Map<String, String> filter, int batchSize, FetchUtils.BatchProcessor<ProductOffering> processor) {
        FetchUtils.fetchByBatch(
                productCatalogManagementApis::listProductOfferings,  // TMF API method
                null,                                                // fields to retrieve (null = all)
                (filter != null) ? filter : new HashMap<String, String>(),    // filters (empty map if null)
                batchSize,                                           // batch size
                processor                                             // callback to process each batch
        );
    }

    public List<ProductOffering> getAllPaginatedProductOfferings(String fields, Map<String, String> filter) {
        List<ProductOffering> allProductOfferings = FetchUtils.streamAll(
        	productCatalogManagementApis::listProductOfferings,    // method reference
	        fields,                       		// fields
	        filter,            					// filter
	        50                         		// pageSize
		).collect(Collectors.toList());

        logger.info("Retrieved {} offerings from TMF API", allProductOfferings.size());
        return allProductOfferings;
    }

    // =============== CATEGORY ===============
    public void fetchCategoriesByBatch(String fields, Map<String, String> filter, int batchSize, FetchUtils.BatchProcessor<Category> processor) {
        FetchUtils.fetchByBatch(
                productCatalogManagementApis::listCategories,                // TMF API method
                fields,                                                     // fields to retrieve (null = all)
                (filter != null) ? filter : new HashMap<String, String>(), // filters (empty map if null)
                batchSize,                                                // batch size
                processor                                                // callback to process each batch
        );
    }


    // =============== PRODUCT SPECIFICATION ===============

    public ProductSpecification getProductSpecificationById(String psId, String fields) {
    	
        if (psId == null) {
            logger.warn("Product Specification ID is null, cannot retrieve product specification.");
            return null;
        }
        
        try {
			return productCatalogManagementApis.getProductSpecification(psId, fields);
		} catch (it.eng.dome.tmforum.tmf620.v4.ApiException e) {
			logger.error("Error: {}", e.getMessage());
			return null;
		}
    }

    // =============== SERVICE SPECIFICATION ===============

    public ServiceSpecification getServiceSpecificationById(String ssId, String fields) {
        if (ssId == null) {
            logger.warn("Service Specification ID is null, cannot retrieve service specification.");
            return null;
        }
        
        try {
			return serviceCatalogManagementApis.getServiceSpecification(ssId, fields);
		} catch (it.eng.dome.tmforum.tmf633.v4.ApiException e) {
			logger.error("Error: {}", e.getMessage());
			return null;
		}
    }

    // =============== RESOURCE SPECIFICATION ===============

    public ResourceSpecification getResourceSpecificationById(String rsId, String fields) {
        if (rsId == null) {
            logger.warn("Resource Specification ID is null, cannot retrieve resource specification.");
            return null;
        }
        
        try {
			return resourceCatalogManagementApis.getResourceSpecification(rsId, fields);
		} catch (it.eng.dome.tmforum.tmf634.v4.ApiException e) {
			logger.error("Error: {}", e.getMessage());
			return null;
		}
    }
}