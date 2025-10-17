package it.eng.dome.search.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import it.eng.dome.brokerage.api.APIPartyApis;
import it.eng.dome.brokerage.api.ProductCatalogManagementApis;
import it.eng.dome.brokerage.api.ResourceCatalogManagementApis;
import it.eng.dome.brokerage.api.ServiceCatalogManagementApis;
import it.eng.dome.brokerage.api.fetch.FetchUtils;
import it.eng.dome.brokerage.observability.AbstractHealthService;
import it.eng.dome.brokerage.observability.health.Check;
import it.eng.dome.brokerage.observability.health.Health;
import it.eng.dome.brokerage.observability.health.HealthStatus;
import it.eng.dome.brokerage.observability.info.Info;

@Service
public class HealthService extends AbstractHealthService {

	private final Logger logger = LoggerFactory.getLogger(HealthService.class);
	private final static String SERVICE_NAME = "Search Engine";
	
    private ProductCatalogManagementApis productCatalogManagementApis;
    private ServiceCatalogManagementApis serviceCatalogManagementApis;
    private ResourceCatalogManagementApis resourceCatalogManagementApis;
    private APIPartyApis apiPartyApis;

	public HealthService (ProductCatalogManagementApis productCatalogManagementApis, 
		ServiceCatalogManagementApis serviceCatalogManagementApis, 
		ResourceCatalogManagementApis resourceCatalogManagementApis, APIPartyApis apiPartyApis) {
	
		this.productCatalogManagementApis = productCatalogManagementApis;
		this.serviceCatalogManagementApis = serviceCatalogManagementApis;
		this.resourceCatalogManagementApis = resourceCatalogManagementApis;
		this.apiPartyApis = apiPartyApis;
	}


	@Override
	public Info getInfo() {

		Info info = super.getInfo();
		logger.debug("Response: {}", toJson(info));

		return info;
	}
	
	@Override
	public Health getHealth() {
		Health health = new Health();
	    health.setDescription("Health for the " + SERVICE_NAME);

	    health.elevateStatus(HealthStatus.PASS);

		// 1: check of the TMForum APIs dependencies
		for (Check c : getTMFChecks()) {
			health.addCheck(c);
	        health.elevateStatus(c.getStatus());
		}

		// 2: check dependencies: in case of FAIL or WARN set it to WARN
		boolean onlyDependenciesFailing = health.getChecks("self", null).stream()
				.allMatch(c -> c.getStatus() == HealthStatus.PASS);
		
	    if (onlyDependenciesFailing && health.getStatus() == HealthStatus.FAIL) {
	        health.setStatus(HealthStatus.WARN);
	    }
	    
	    // 3: check self info
	    for(Check c: getChecksOnSelf()) {
	    	health.addCheck(c);
	    	health.elevateStatus(c.getStatus());
        }
	    
	    // 4: build human-readable notes
	    health.setNotes(buildNotes(health));
		
		logger.debug("Health response: {}", toJson(health));
		
		return health;
	}

	
	private List<Check> getChecksOnSelf() {
	    List<Check> out = new ArrayList<>();

	    // Check getInfo API
	    Info info = getInfo();
	    HealthStatus infoStatus = (info != null) ? HealthStatus.PASS : HealthStatus.FAIL;
	    String infoOutput = (info != null)
	            ? SERVICE_NAME + " version: " + info.getVersion()
	            : SERVICE_NAME + " getInfo returned unexpected response";
	    Check infoCheck = createCheck("self", "get-info", "api", infoStatus, infoOutput);
	    out.add(infoCheck);

	    return out;
	}
	
	/**
	 * Check connectivity with TMForum API
	 */
	private List<Check> getTMFChecks() {

		List<Check> out = new ArrayList<>();

		// TMF620
		Check tmf620 = createCheck("tmf-api", "connectivity", "tmf620");

		try {
			FetchUtils.streamAll(
				productCatalogManagementApis::listProductOfferings,
			    null,
			    null,
			    1
			)
			.findAny();

			tmf620.setStatus(HealthStatus.PASS);
			
		} catch (Exception e) {
			tmf620.setStatus(HealthStatus.FAIL);
			tmf620.setOutput(e.toString());
		}

		out.add(tmf620);

		// TMF632
		Check tmf632 = createCheck("tmf-api", "connectivity", "tmf632");

		try {
			FetchUtils.streamAll(
				apiPartyApis::listOrganizations,
			    null,
			    null,
			    1
			)
			.findAny();
			
			tmf632.setStatus(HealthStatus.PASS);
		} catch (Exception e) {
			tmf632.setStatus(HealthStatus.FAIL);
			tmf632.setOutput(e.toString());
		}

		out.add(tmf632);

		// TMF633
		Check tmf633 = createCheck("tmf-api", "connectivity", "tmf633");

		try {
			FetchUtils.streamAll(
				serviceCatalogManagementApis::listServiceSpecifications,
			    null,
			    null,
			    1
			)
			.findAny();
			
			tmf633.setStatus(HealthStatus.PASS);
		} catch (Exception e) {
			tmf633.setStatus(HealthStatus.FAIL);
			tmf633.setOutput(e.toString());
		}

		out.add(tmf633);
		
		
		// TMF634
		Check tmf634 = createCheck("tmf-api", "connectivity", "tmf634");

		try {
			FetchUtils.streamAll(
				resourceCatalogManagementApis::listResourceSpecifications,
			    null,
			    null,
			    1
			)
			.findAny();
			
			tmf634.setStatus(HealthStatus.PASS);
		} catch (Exception e) {
			tmf634.setStatus(HealthStatus.FAIL);
			tmf634.setOutput(e.toString());
		}

		out.add(tmf634);
		
		return out;
	}
	
	/*
	@Override
	public Health getHealth() {

		Health h = new Health();
		h.setDescription("Health for the Invoicing Service");

		h.elevateStatus(HealthStatus.PASS);

		// check the TMF APIs
		for (Check c : this.getTMFChecks()) {
			h.addCheck(c);
		}

		// Status of its dependencies. In case of FAIL or WARN set it to WARN
		for (Check c : h.getAllChecks()) {
			h.elevateStatus(c.getStatus());
		}

		// ... but not to FAIL (which means a failure in the Invoicing itself)
		if (HealthStatus.FAIL.equals(h.getStatus())) {
			h.setStatus(HealthStatus.WARN);
		}

		// now look at the internal status (may lead to PASS, WARN or FAIL)
		for (Check c : this.getChecksOnSelf()) {
			h.addCheck(c);
			h.elevateStatus(c.getStatus());
		}

		// add some notes
		h.setNotes(this.buildNotes(h));

		logger.debug("Response: {}", toJson(h));

		return h;
	}
	
	private List<Check> getTMFChecks() {

        List<Check> out = new ArrayList<>();

        Check connectivity = new Check("tmf-api", "connectivity");
        connectivity.setComponentType("external");
        connectivity.setTime(OffsetDateTime.now());

        try {
            orgApi.listOrganization(null, null, 5, null);
            connectivity.setStatus(HealthStatus.PASS);
        } catch(Exception e) {
            connectivity.setStatus(HealthStatus.FAIL);
            connectivity.setOutput(e.toString());
        }

        out.add(connectivity);

        // TODO: return more/better checks

        return out;
    }

	private List<Check> getChecksOnSelf() {
		List<Check> out = new ArrayList<>();
		Check self = new Check("self", "");
		// FIXME... do a proper check, if applicable.
		self.setStatus(HealthStatus.PASS);
		self.setTime(OffsetDateTime.now());
		self.setComponentType("component");
		out.add(self);
		return out;
	}
*/
}
