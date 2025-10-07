package it.eng.dome.search.tmf;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component(value = "tmfApiFactory")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public final class TmfApiFactory implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(TmfApiFactory.class);
    private static final String TMF_ENDPOINT_CONCAT_PATH = "-";

    @Value("${tmforumapi.tmf_endpoint}")
    public String tmfEndpoint;

    @Value("${tmforumapi.tmf_envoy}")
    public boolean tmfEnvoy;

    @Value("${tmforumapi.tmf_namespace}")
    public String tmfNamespace;

    @Value("${tmforumapi.tmf_postfix}")
    public String tmfPostfix;

    @Value("${tmforumapi.tmf_port}")
    public String tmfPort;

    @Value( "${tmforumapi.tmf620_catalog_path}" )
    private String tmf620ProductCatalogPath;

    @Value( "${tmforumapi.tmf632_party_management_path}" )
    private String tmf632PartyManagementPath;

    @Value( "${tmforumapi.tmf633_service_catalog_path}" )
    private String tmf633ServiceCatalogPath;

    @Value( "${tmforumapi.tmf634_resource_catalog_path}" )
    private String tmf634ResourceCatalogPath;

    private it.eng.dome.tmforum.tmf620.v4.ApiClient apiClientTmf620;
    private it.eng.dome.tmforum.tmf632.v4.ApiClient apiClientTmf632;
    private it.eng.dome.tmforum.tmf633.v4.ApiClient apiClientTmf633;
    private it.eng.dome.tmforum.tmf634.v4.ApiClient apiClientTmf634;

    public it.eng.dome.tmforum.tmf620.v4.ApiClient getTMF620ProductCatalogApiClient() {
        if (apiClientTmf620 == null) {
            apiClientTmf620 = it.eng.dome.tmforum.tmf620.v4.Configuration.getDefaultApiClient();
            if (tmfEnvoy) {
                // usage of envoyProxy to access on TMForum APIs
                apiClientTmf620.setBasePath(tmfEndpoint + "/" + tmf620ProductCatalogPath);
            }else {
                // use direct access on specific TMForum APIs software
                apiClientTmf620.setBasePath(tmfEndpoint + TMF_ENDPOINT_CONCAT_PATH + "product-catalog" + "." + tmfNamespace + "." + tmfPostfix + ":" + tmfPort + "/" + tmf620ProductCatalogPath);
            }
            log.debug("Invoke Product Catalog API at endpoint: " + apiClientTmf620.getBasePath());
        }
        return apiClientTmf620;
    }

    public it.eng.dome.tmforum.tmf632.v4.ApiClient getTMF632PartyManagementApiClient() {
        if (apiClientTmf632 == null) {
            apiClientTmf632 = it.eng.dome.tmforum.tmf632.v4.Configuration.getDefaultApiClient();
            if (tmfEnvoy) {
                // usage of envoyProxy to access on TMForum APIs
                apiClientTmf632.setBasePath(tmfEndpoint + "/" + tmf632PartyManagementPath);
            }else {
                // use direct access on specific TMForum APIs software
                apiClientTmf632.setBasePath(tmfEndpoint + TMF_ENDPOINT_CONCAT_PATH + "party-catalog-management" + "." + tmfNamespace + "." + tmfPostfix + ":" + tmfPort + "/" + tmf632PartyManagementPath);
            }
            log.debug("Invoke Product Ordering API at endpoint: " + apiClientTmf632.getBasePath());
        }
        return apiClientTmf632;
    }

    public it.eng.dome.tmforum.tmf633.v4.ApiClient getTMF633ServiceCatalogApiClient() {
        if (apiClientTmf633 == null) {
            apiClientTmf633 = it.eng.dome.tmforum.tmf633.v4.Configuration.getDefaultApiClient();
            if (tmfEnvoy) {
                // usage of envoyProxy to access on TMForum APIs
                apiClientTmf633.setBasePath(tmfEndpoint + "/" + tmf633ServiceCatalogPath);
            }else {
                // use direct access on specific TMForum APIs software
                apiClientTmf633.setBasePath(tmfEndpoint + TMF_ENDPOINT_CONCAT_PATH + "service-catalog" + "." + tmfNamespace + "." + tmfPostfix + ":" + tmfPort + "/" + tmf633ServiceCatalogPath);
            }
            log.debug("Invoke Customer Billing API at endpoint: " + apiClientTmf633.getBasePath());
        }
        return apiClientTmf633;
    }

    public it.eng.dome.tmforum.tmf634.v4.ApiClient getTMF634ResourceCatalogApiClient() {
        if (apiClientTmf634 == null) {
            apiClientTmf634 = it.eng.dome.tmforum.tmf634.v4.Configuration.getDefaultApiClient();
            if (tmfEnvoy) {
                // usage of envoyProxy to access on TMForum APIs
                apiClientTmf634.setBasePath(tmfEndpoint + "/" + tmf634ResourceCatalogPath);
            }else {
                // use direct access on specific TMForum APIs software
                apiClientTmf634.setBasePath(tmfEndpoint + TMF_ENDPOINT_CONCAT_PATH + "resource-catalog" + "." + tmfNamespace + "." + tmfPostfix + ":" + tmfPort + "/" + tmf634ResourceCatalogPath);
            }
            log.debug("Invoke Product Catalog API at endpoint: " + apiClientTmf634.getBasePath());
        }
        return apiClientTmf634;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        log.info("Billing Engine is using the following TMForum endpoint prefix: " + tmfEndpoint);
        if (tmfEnvoy) {
            log.info("You set the apiProxy for TMForum endpoint. No tmf_port {} can be applied", tmfPort);
        } else {
            log.info("No apiProxy set for TMForum APIs. You have to access on specific software via paths at tmf_port {}", tmfPort);
        }

        Assert.state(!StringUtils.isBlank(tmfEndpoint), "Search Engine not properly configured. tmfEndPoint property has no value.");
        Assert.state(!StringUtils.isBlank(tmf620ProductCatalogPath), "Search Engine not properly configured. tmf620_catalog_path property has no value.");
        Assert.state(!StringUtils.isBlank(tmf632PartyManagementPath), "Search Engine not properly configured. tmf632_party_management_path property has no value.");
        Assert.state(!StringUtils.isBlank(tmf633ServiceCatalogPath), "Search Engine not properly configured. tmf633_service_catalog_path property has no value.");
        Assert.state(!StringUtils.isBlank(tmf634ResourceCatalogPath), "Search Engine not properly configured. tmf634_resource_catalog_path property has no value.");

        if (tmfEndpoint.endsWith("/")) {
            tmfEndpoint = removeFinalSlash(tmfEndpoint);
        }

        if (tmf620ProductCatalogPath.startsWith("/")) {
            tmf620ProductCatalogPath = removeInitialSlash(tmf620ProductCatalogPath);
        }

        if (tmf632PartyManagementPath.startsWith("/")) {
            tmf632PartyManagementPath = removeInitialSlash(tmf632PartyManagementPath);
        }

        if (tmf633ServiceCatalogPath.startsWith("/")) {
            tmf633ServiceCatalogPath = removeInitialSlash(tmf633ServiceCatalogPath);
        }

        if (tmf634ResourceCatalogPath.startsWith("/")) {
            tmf634ResourceCatalogPath = removeInitialSlash(tmf634ResourceCatalogPath);
        }
    }

    private String removeFinalSlash(String s) {
        String path = s;
        while (path.endsWith("/"))
            path = path.substring(0, path.length() - 1);

        return path;
    }

    private String removeInitialSlash(String s) {
        String path = s;
        while (path.startsWith("/")) {
            path = path.substring(1);
        }
        return path;
    }
}