package it.eng.dome.search.service;


import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import it.eng.dome.brokerage.api.APIPartyApis;
import it.eng.dome.brokerage.api.ProductCatalogManagementApis;
import it.eng.dome.brokerage.api.ResourceCatalogManagementApis;
import it.eng.dome.brokerage.api.ServiceCatalogManagementApis;
import it.eng.dome.brokerage.api.fetch.FetchUtils;
import it.eng.dome.tmforum.tmf620.v4.model.ProductOffering;
import it.eng.dome.tmforum.tmf620.v4.model.ProductSpecification;
import it.eng.dome.tmforum.tmf632.v4.ApiException;
import it.eng.dome.tmforum.tmf632.v4.model.Organization;
import it.eng.dome.tmforum.tmf633.v4.model.ServiceSpecification;
import it.eng.dome.tmforum.tmf634.v4.model.ResourceSpecification;

@Service
public class TmfDataRetriever {

    private final Logger logger = LoggerFactory.getLogger(TmfDataRetriever.class);
   
    private ProductCatalogManagementApis productCatalogManagementApis;
    private ServiceCatalogManagementApis serviceCatalogManagementApis;
    private ResourceCatalogManagementApis resourceCatalogManagementApis;
    private APIPartyApis apiPartyApis;

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
        	
        	logger.debug("Retrieving Organization from TMF API with id: {}", orgId);
			return apiPartyApis.getOrganization(orgId, fields);
		} catch (ApiException e) {
			logger.error("Error: {}", e.getMessage());
			return null;
		}
    }

    public List<Organization> getAllPaginatedOrganizations(String fields, Map<String, String> filter) {
//        logger.debug("Retrieving all Product Offerings from TMF API");
        //TODO: implement pagination
//    	List<Organization> allOfferings = orgApi.getOrganizations(fields, filter);
        
        List<Organization> allOrganizations = FetchUtils.streamAll(
	        apiPartyApis::listOrganizations,    // method reference
	        null,                       		// fields
	        null,            					// filter
	        100                         		// pageSize
		).toList();        

        logger.info("Retrieved {} organization from TMF API", allOrganizations.size());
        return allOrganizations;
    }

    // =============== PRODUCT OFFERING ===============

    public ProductOffering getProductOfferingById(String poId, String fields) {
    	
        if (poId == null) {
            logger.warn("Product Offering ID is null, cannot retrieve product offering.");
            return null;
        }
        
        logger.debug("Retrieving Product Offering from TMF API with id: {}", poId);
        
        try {
			return productCatalogManagementApis.getProductOffering(poId, fields);
		} catch (it.eng.dome.tmforum.tmf620.v4.ApiException e) {
			logger.error("Error: {}", e.getMessage());
			return null;
		}
    }

    public List<ProductOffering> getAllPaginatedProductOfferings(String fields, Map<String, String> filter) {
//        logger.debug("Retrieving all Product Offerings from TMF API");
        //TODO: implement pagination 
//    	List<ProductOffering> allOfferings = productOfferingApi.getAllProductOfferings(fields, filter);
    	
        List<ProductOffering> allProductOfferings = FetchUtils.streamAll(
        	productCatalogManagementApis::listProductOfferings,    // method reference
	        null,                       		// fields
	        null,            					// filter
	        100                         		// pageSize
		).toList();
        
        logger.info("Retrieved {} offerings from TMF API", allProductOfferings.size());
        return allProductOfferings;
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