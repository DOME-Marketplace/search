package it.eng.dome.search.service;

import it.eng.dome.brokerage.api.*;
import it.eng.dome.search.tmf.TmfApiFactory;
import it.eng.dome.tmforum.tmf620.v4.model.ProductOffering;
import it.eng.dome.tmforum.tmf620.v4.model.ProductSpecification;
import it.eng.dome.tmforum.tmf632.v4.model.Organization;
import it.eng.dome.tmforum.tmf633.v4.model.ServiceSpecification;
import it.eng.dome.tmforum.tmf634.v4.model.ResourceSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TmfDataRetriever implements InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(TmfDataRetriever.class);

    // Factory for TMF APIss
    @Autowired
    private TmfApiFactory tmfApiFactory;

    // TMForum API to retrieve bills, organizations and products
    private OrganizationApis orgApi;
    private ProductOfferingApis productOfferingApi;
    private ProductSpecificationApis productSpecificationApi;
    private ServiceSpecificationApis serviceSpecificationApi;
    private ResourceSpecificationApis resourceSpecificationApi;

    public TmfDataRetriever() {
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.orgApi = new OrganizationApis(tmfApiFactory.getTMF632PartyManagementApiClient());
        this.productOfferingApi = new ProductOfferingApis(tmfApiFactory.getTMF620ProductCatalogApiClient());
        this.productSpecificationApi = new ProductSpecificationApis(tmfApiFactory.getTMF620ProductCatalogApiClient());
        this.serviceSpecificationApi = new ServiceSpecificationApis(tmfApiFactory.getTMF633ServiceCatalogApiClient());
        this.resourceSpecificationApi = new ResourceSpecificationApis(tmfApiFactory.getTMF634ResourceCatalogApiClient());
        logger.info("TmfDataRetriever initialized with the following api: {}, {}, {}, {}, {}", orgApi, productOfferingApi, productSpecificationApi, serviceSpecificationApi, resourceSpecificationApi);
    }

    // =============== TMF ORGANIZATIONS ===============

    public Organization getOrganizationById(String orgId, String fields) {
//        logger.debug("Retrieving Organization from TMF API with id: {}", orgId);
        if (orgId == null) {
            logger.warn("Organization ID is null, cannot retrieve organization.");
            return null;
        }
        return this.orgApi.getOrganization(orgId, fields);
    }

    // =============== PRODUCT OFFERING ===============

    public ProductOffering getProductOfferingById(String poId, String fields) {
//        logger.debug("Retrieving Product Offering from TMF API with id: {}", poId);
        if (poId == null) {
            logger.warn("Product Offering ID is null, cannot retrieve product offering.");
            return null;
        }
        return this.productOfferingApi.getProductOffering(poId, fields);
    }

    public List<ProductOffering> getAllPaginatedProductOfferings(String fields, Map<String, String> filter) {
//        logger.debug("Retrieving all Product Offerings from TMF API");
        //TODO: implement pagination
        List<ProductOffering> allOfferings = productOfferingApi.getAllProductOfferings(fields, filter);

        logger.info("Retrieved {} offerings from TMF API", allOfferings.size());
        return allOfferings;
    }

    // =============== PRODUCT SPECIFICATION ===============

    public ProductSpecification getProductSpecificationById(String psId, String fields) {
        if (psId == null) {
            logger.warn("Product Specification ID is null, cannot retrieve product specification.");
            return null;
        }
        return this.productSpecificationApi.getProductSpecification(psId, fields);

    }

    // =============== SERVICE SPECIFICATION ===============

    public ServiceSpecification getServiceSpecificationById(String ssId, String fields) {
        if (ssId == null) {
            logger.warn("Service Specification ID is null, cannot retrieve service specification.");
            return null;
        }
        return this.serviceSpecificationApi.getServiceSpecification(ssId, fields);
    }

    // =============== RESOURCE SPECIFICATION ===============

    public ResourceSpecification getResourceSpecificationById(String rsId, String fields) {
        if (rsId == null) {
            logger.warn("Resource Specification ID is null, cannot retrieve resource specification.");
            return null;
        }
        return this.resourceSpecificationApi.getResourceSpecification(rsId, fields);
    }
}