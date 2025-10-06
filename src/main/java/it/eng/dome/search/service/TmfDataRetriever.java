package it.eng.dome.search.service;

import it.eng.dome.search.tmf.TMFApiUtils;
import it.eng.dome.search.tmf.TmfApiFactory;
import it.eng.dome.tmforum.tmf620.v4.ApiException;
import it.eng.dome.tmforum.tmf620.v4.api.ProductOfferingApi;
import it.eng.dome.tmforum.tmf620.v4.model.ProductOffering;
import it.eng.dome.tmforum.tmf632.v4.api.OrganizationApi;
import it.eng.dome.tmforum.tmf632.v4.model.Organization;
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
    private OrganizationApi orgApi;
    private ProductOfferingApi productOfferingApi;

    public TmfDataRetriever() {
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.orgApi = new OrganizationApi(tmfApiFactory.getTMF632PartyManagementApiClient());
        this.productOfferingApi = new ProductOfferingApi(tmfApiFactory.getTMF620ProductCatalogApiClient());
        logger.info("TmfDataRetriever initialized with the following api: {}, {}", orgApi, productOfferingApi);
    }

    // ======== TMF ORGANIZATIONS ========
    public List<Organization> getAllPaginatedOrganization(String fields, Map<String, String> filter) {
//        logger.info("Retrieving all organizations from TMF API");
        List<Organization> allOrgs = null;
        try {
            allOrgs = TMFApiUtils.fetchAllOrganizations(orgApi, fields, 15, filter);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        logger.info("Retrieved {} organizations from TMF API", allOrgs.size());
        return allOrgs;
    }

    public Organization getOrganizationById(String orgId, String fields) {
//        logger.debug("Retrieving Organization from TMF API with id: {}", orgId);
        if (orgId == null) {
            logger.warn("Organization ID is null, cannot retrieve organization.");
            return null;
        }
        try {
            return this.orgApi.retrieveOrganization(orgId, fields);
        } catch (it.eng.dome.tmforum.tmf632.v4.ApiException e) {
            throw new RuntimeException(e);
        }
    }

    // ======== PRODUCT OFFERING ========

    public ProductOffering getProductOfferingById(String poId, String fields) {
//        logger.debug("Retrieving Product Offering from TMF API with id: {}", poId);
        if (poId == null) {
            logger.warn("Product Offering ID is null, cannot retrieve product offering.");
            return null;
        }
        try {
            return this.productOfferingApi.retrieveProductOffering(poId, fields);
        } catch (ApiException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ProductOffering> getAllPaginatedProductOfferings(String fields, Map<String, String> filter) {
//        logger.debug("Retrieving all Product Offerings from TMF API");
        List<ProductOffering> allOfferings = null;
        try {
            allOfferings = TMFApiUtils.fetchAllProductOfferings(productOfferingApi, fields, 50, filter);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        logger.info("Retrieved {} offerings from TMF API", allOfferings.size());
        return allOfferings;
    }
}