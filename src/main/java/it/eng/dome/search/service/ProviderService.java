package it.eng.dome.search.service;

import it.eng.dome.search.domain.IndexingObject;
import it.eng.dome.search.repository.OfferingRepository;
import it.eng.dome.search.service.dto.OrganizationSearchRequest;
import it.eng.dome.search.service.dto.SearchRequest;
import it.eng.dome.tmforum.tmf632.v4.model.Organization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProviderService {

    private static final Logger log = LoggerFactory.getLogger(ProviderService.class);

    @Autowired
    private OfferingRepository offeringRepo;

    @Autowired
    TmfDataRetriever tmfDataRetriever;

    public Page<Organization> filterOrganizations(OrganizationSearchRequest request, Pageable pageable) {
        if (request == null) {
            log.warn("Organization request is null.");
            return Page.empty(pageable);
        }

        boolean hasCategories = hasValues(request.getCategories());
        boolean hasCountries = hasValues(request.getCountries());

        if (!hasCategories && !hasCountries) {
            log.warn("No filter criteria provided");
            return Page.empty(pageable);
        }

        List<Organization> resultOrganizations;

        if (hasCategories && hasCountries) {
            // both categories and countries
            log.info("Searching providers for categories: {} and countries: {}", request.getCategories(), request.getCountries());
            List<Organization> byCategory = findOrganizationsByCategories(request.getCategories());
            log.debug("Found {} Category Providers: {}", byCategory.size(),
                    byCategory.stream()
                            .map(Organization::getId)
                            .collect(Collectors.toList()));
            List<Organization> byCountry = findOrganizationsByCountry(request.getCountries());
            log.debug("Found {} Countries Providers: {}", byCountry.size(),
                    byCategory.stream()
                            .map(Organization::getId)
                            .collect(Collectors.toList()));
            resultOrganizations = intersectOrganizations(byCategory, byCountry);
            log.debug("After intersection, found {} Providers: {}", resultOrganizations.size(),
                    resultOrganizations.stream()
                            .map(Organization::getId)
                            .collect(Collectors.toList()));
        } else if (hasCategories) {
            // only categories
            log.info("Searching providers for categories: {}", request.getCategories());
            resultOrganizations = findOrganizationsByCategories(request.getCategories());
            log.debug("Found {} Category Providers: {}", resultOrganizations.size(),
                    resultOrganizations.stream()
                            .map(Organization::getId)
                            .collect(Collectors.toList()));
        } else {
            // only countries
            log.info("Searching providers for countries: {}", request.getCountries());
            resultOrganizations = findOrganizationsByCountry(request.getCountries());
            log.debug("Found {} Country Providers: {}", resultOrganizations.size(),
                    resultOrganizations.stream()
                            .map(Organization::getId)
                            .collect(Collectors.toList()));
        }

        return paginate(resultOrganizations, pageable);
    }


    private boolean hasValues(List<?> list) {
        return list != null && !list.isEmpty();
    }

    private List<Organization> findOrganizationsByCategories(List<String> categories) {
        // retrieve info from offering repo (elasticsearch)
        List<IndexingObject> allObjects = offeringRepo.findByCategoryIdsOrNames(categories);

        if (allObjects.isEmpty()) return Collections.emptyList();

        // extract unique RelatedParty IDs
        Set<String> relatedPartyIds = allObjects.stream()
                .map(IndexingObject::getRelatedPartyId)
                .filter(id -> id != null && !id.isBlank())
                .collect(Collectors.toSet());

        log.debug("Found {} unique RelatedParty IDs from ProductOfferings", relatedPartyIds.size());

        return relatedPartyIds.parallelStream()
                .map(id -> {
                    try {
                        return tmfDataRetriever.getOrganizationById(id, null);
                    } catch (Exception e) {
                        log.error("Error retrieving Organization {}: {}", id, e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private List<Organization> findOrganizationsByCountry(List<String> countries) {
        // retrieve all objects with a relatedPartyId from offering repo (elasticsearch)
        List<IndexingObject> allObjects = offeringRepo.findAllWithRelatedPartyId();

        if (allObjects.isEmpty()) return Collections.emptyList();

        // extract unique RelatedParty IDs
        Set<String> relatedPartyIds = allObjects.stream()
                .map(IndexingObject::getRelatedPartyId)
                .filter(id -> id != null && !id.isBlank())
                .collect(Collectors.toSet());

        log.debug("Found {} unique RelatedParty IDs from ProductOfferings", relatedPartyIds.size());

        List<Organization> allOrg = relatedPartyIds.parallelStream()
                .map(id -> {
                    try {
                        return tmfDataRetriever.getOrganizationById(id, null);
                    } catch (Exception e) {
                        log.error("Error retrieving Organization {}: {}", id, e.getMessage());
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return allOrg.stream()
                .filter(org -> org.getPartyCharacteristic() != null &&
                        org.getPartyCharacteristic().stream().anyMatch(ch ->
                                "country".equalsIgnoreCase(ch.getName()) &&
                                        ch.getValue() != null &&
                                        countries.stream()
                                                .anyMatch(c -> c.equalsIgnoreCase(ch.getValue().toString()))
                        ))
                .collect(Collectors.toList());
    }

    @SafeVarargs
    private final List<Organization> intersectOrganizations(List<Organization>... lists) {
        if (lists == null || lists.length == 0) {
            return Collections.emptyList();
        }

        // begin with IDs from the first list
        Set<String> commonIds = lists[0].stream()
                .map(Organization::getId)
                .collect(Collectors.toSet());

        // intersect with IDs from subsequent lists
        for (int i = 1; i < lists.length; i++) {
            Set<String> ids = lists[i].stream()
                    .map(Organization::getId)
                    .collect(Collectors.toSet());
            commonIds.retainAll(ids); // mantiene solo gli ID comuni
        }

        // filter the first list to include only organizations with IDs in commonIds
        return lists[0].stream()
                .filter(o -> commonIds.contains(o.getId()))
                .collect(Collectors.toList());
    }


    private Page<Organization> paginate(List<Organization> organizations, Pageable pageable) {
        if (organizations == null || organizations.isEmpty()) {
            return Page.empty(pageable);
        }

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), organizations.size());

        if (start >= end) return Page.empty(pageable);

        return new PageImpl<>(organizations.subList(start, end), pageable, organizations.size());
    }


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