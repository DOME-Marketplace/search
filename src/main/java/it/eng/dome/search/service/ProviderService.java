package it.eng.dome.search.service;

import it.eng.dome.search.domain.IndexingObject;
import it.eng.dome.search.repository.OfferingRepository;
import it.eng.dome.search.service.dto.SearchRequest;
import it.eng.dome.tmforum.tmf632.v4.model.Organization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProviderService {

    private static final Logger log = LoggerFactory.getLogger(ProviderService.class);

    @Autowired
    private OfferingRepository offeringRepo;

    @Autowired
    TmfDataRetriever tmfDataRetriever;

    public Page<Organization> getProvidersByCategories(SearchRequest filter, Pageable pageable) {
        if (filter == null || filter.getCategories() == null || filter.getCategories().isEmpty()) {
            log.warn("No categories provided in filter.");
            return Page.empty(pageable);
        }
        log.info("Searching providers for categories: {}", filter.getCategories());

        // Collect all RelatedParty IDs for the given categories
        List<IndexingObject> allObjects = offeringRepo.findByCategoryIdsOrNames(filter.getCategories());
        if (allObjects.isEmpty()) {
            return Page.empty(pageable);
        }

        //Unique RelatedParty IDs
        Set<String> relatedPartyIds = allObjects.stream()
                .map(IndexingObject::getRelatedPartyId)
                .filter(id -> id != null && !id.isBlank())
                .collect(Collectors.toSet());

        //log.info("Found {} unique RelatedParty IDs", relatedPartyIds.size());
        if(relatedPartyIds.isEmpty()) {
            return Page.empty(pageable); // return empty page
        }
//        log.debug("RelatedParty IDs: {}", relatedPartyIds);

        // Retrieve RelatedParty (Organization) details from TMF API in parallel
        List<Organization> providers = relatedPartyIds.parallelStream()
                .map(id -> {
                    try {
                        return tmfDataRetriever.getOrganizationById(id, null);
                    } catch (Exception e) {
                        log.error("Error retrieving Organization {}: {}", id, e.getMessage());
                        return null;
                    }
                })
                .filter(org -> org != null)
                .collect(Collectors.toList());

        log.debug("Found {} Providers: {}", providers.size(),
                providers.stream()
                        .map(Organization::getId)
                        .collect(Collectors.toList()));

        // pagination
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), providers.size());
        if (start >= end) {
            return Page.empty(pageable);
        }
        List<Organization> paginatedList = providers.subList(start, end);

        return new PageImpl<>(paginatedList, pageable, providers.size());
    }

}