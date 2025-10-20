package it.eng.dome.search.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import it.eng.dome.brokerage.api.APIPartyApis;
import it.eng.dome.brokerage.api.ProductCatalogManagementApis;
import it.eng.dome.brokerage.api.ResourceCatalogManagementApis;
import it.eng.dome.brokerage.api.ServiceCatalogManagementApis;
import it.eng.dome.search.tmf.TmfApiFactory;


@Configuration
public class TmfApiConfig {

	private final Logger logger = LoggerFactory.getLogger(TmfApiConfig.class);

	@Autowired
	private TmfApiFactory tmfApiFactory;

	@Bean
	public ProductCatalogManagementApis productCatalogManagementApis() {
		logger.info("Initializing of ProductCatalogManagementApis");

		return new ProductCatalogManagementApis(tmfApiFactory.getTMF620ProductCatalogApiClient());
	}

	@Bean
	public ServiceCatalogManagementApis serviceCatalogManagementApis() {
		logger.info("Initializing of ServiceCatalogManagementApis");

		return new ServiceCatalogManagementApis(tmfApiFactory.getTMF633ServiceCatalogApiClient());
	}

	@Bean
	public ResourceCatalogManagementApis resourceCatalogManagementApis() {
		logger.info("Initializing of ResourceCatalogManagementApis");

		return new ResourceCatalogManagementApis(tmfApiFactory.getTMF634ResourceCatalogApiClient());
	}

	@Bean
	public APIPartyApis apiPartyApis() {
		logger.info("Initializing of APIPartyApis");

		return new APIPartyApis(tmfApiFactory.getTMF632PartyManagementApiClient());
	}
}
