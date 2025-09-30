package it.eng.dome.search.service;

import it.eng.dome.brokerage.api.OrganizationApis;
import it.eng.dome.search.domain.IndexingObject;
import it.eng.dome.search.repository.OfferingRepository;
import it.eng.dome.search.tmf.TmfApiFactory;
import it.eng.dome.tmforum.tmf632.v4.model.Organization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProviderService implements InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(ProviderService.class);

    @Autowired
    private OfferingRepository offeringRepo;

    @Autowired
    private TmfApiFactory tmfClient; // client per chiamare le API TMF

    private OrganizationApis organizationApis;

    @Override
    public void afterPropertiesSet () throws Exception {
        this.organizationApis = new OrganizationApis(tmfClient.getTMF632PartyManagementApiClient());

        log.info("ProviderService initialized with OrganizationApis");
    }

    public List<Organization> getProvidersByCategories(List<String> categoryIds) {

        // Collect all RelatedParty IDs for the given categories
        Set<String> relatedPartyIds = new HashSet<>();
        for (String categoryId : categoryIds) {
            List<IndexingObject> objects = offeringRepo.findByCategoryId(categoryId);
            objects.forEach(obj -> {
                if (obj.getRelatedPartyId() != null) {
                    relatedPartyIds.add(obj.getRelatedPartyId());
                }
            });
        }

        log.info("Found {} RelatedParty IDs", relatedPartyIds.size());
        log.debug("RelatedParty IDs: {}", relatedPartyIds);

        // Retrieve RelatedParty (Organization) details from TMF API
        List<Organization> providers = new ArrayList<>();
        for (String relatedPartyId : relatedPartyIds) {
            //log.info("Fetching RelatedParty for ID: {}", relatedPartyId);
            try {
                Organization party = organizationApis.getOrganization(relatedPartyId, null);
                if (party != null) {
                    providers.add(party);
                } else {
                    log.warn("RelatedParty not found for ID: {}", relatedPartyId);
                }
            } catch (Exception e) {
                log.error("Error retrieving RelatedParty with ID {}: {}", relatedPartyId, e.getMessage());
            }
        }

        log.debug("Found {} Providers: {}", providers.size(),
                providers.stream()
                        .map(Organization::getId)
                        .collect(Collectors.toList()));

        return providers;
    }

}