package it.eng.dome.search.service;

import it.eng.dome.search.domain.IndexingObject;
import it.eng.dome.search.domain.dto.RelatedPartyDTO;
import it.eng.dome.search.repository.OfferingRepository;
import it.eng.dome.search.service.dto.OrganizationSearchRequest;
import it.eng.dome.tmforum.tmf632.v4.model.Organization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.function.Function;
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Organization request is null.");
//            return Page.empty(pageable);
        }

        boolean hasCategories = hasValues(request.getCategories());
        boolean hasCountries = hasValues(request.getCountries());
        boolean hasComplianceLevels = hasValues(request.getComplianceLevels());

        if (!hasCategories && !hasCountries && !hasComplianceLevels) {
//            log.warn("No filter criteria provided");
            log.info("No filter criteria provided, return all providers");
            List<Organization> all = tmfDataRetriever.getAllPaginatedOrganizations(null, null, 100);
            printListLog("All Providers", all, Organization::getId);
            return this.paginate(all, pageable);
            //throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "At least one filter (categories or countries) must be provided");
            // change with line below if you want return all providers when the filters are empty.
            //return findAllProviders(pageable); // metodo da creare per recuperare tutto
        }

        // Lista temporanea per fare l'intersezione dei vari filtri
        List<List<Organization>> listsToIntersect = new ArrayList<>();

        if (hasCategories) {
            log.info("Searching providers for categories: {}", request.getCategories());
            List<Organization> byCategory = findOrganizationsByCategories(request.getCategories());
            printListLog("Category Providers", byCategory, Organization::getId);
            listsToIntersect.add(byCategory);
        }

        if (hasCountries) {
            log.info("Searching providers for countries: {}", request.getCountries());
            List<Organization> byCountry = findOrganizationsByCountry(request.getCountries());
            printListLog("Country Providers", byCountry, Organization::getId);
            listsToIntersect.add(byCountry);
        }

        if (hasComplianceLevels) {
            log.info("Searching providers for compliance levels: {}", request.getComplianceLevels());
            List<Organization> byCompliance = findOrganizationsByCompliance(request.getComplianceLevels());
            printListLog("Compliance Providers", byCompliance, Organization::getId);
            listsToIntersect.add(byCompliance);
        }

        // if not are valid filter return empty page
        if (listsToIntersect.isEmpty()) {
            return Page.empty(pageable);
        }

        // Se c'è un solo filtro, non serve fare l'intersezione
        if (listsToIntersect.size() == 1) {
            List<Organization> singleResult = listsToIntersect.get(0);
            log.info("Single filter applied, returning {} providers", singleResult.size());
            return paginate(singleResult, pageable);
        }

        // Intersezione di tutti i filtri selezionati
        List<Organization> intersected = intersectOrganizations(listsToIntersect.toArray(new List[0]));
        printListLog("After intersection, Providers", intersected, Organization::getId);
        return paginate(intersected, pageable);
    }

    private boolean hasValues(List<?> list) {
        return list != null && !list.isEmpty();
    }

    private List<Organization> findOrganizationsByCategories(List<String> categories) {
        // retrieve info from offering repo (elasticsearch)
        List<IndexingObject> allObjects = offeringRepo.findByCategoryIdsOrNames(categories);

        if (allObjects.isEmpty()) return Collections.emptyList();

        // extract unique RelatedParty IDs
        Set<String> relatedPartyIds = extractRelatedPartyIds(allObjects, null);

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
        List<IndexingObject> allObjects = offeringRepo.findAllWithRelatedParties();

        if (allObjects.isEmpty()) return Collections.emptyList();

        // extract unique RelatedParty IDs
        Set<String> relatedPartyIds = extractRelatedPartyIds(allObjects, null);

        log.debug("Found {} unique RelatedParty IDs from ProductOfferings {}", relatedPartyIds.size(), relatedPartyIds);

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

    private List<Organization> findOrganizationsByCompliance(List<String> complianceLevels) {
        if (complianceLevels == null || complianceLevels.isEmpty()) return Collections.emptyList();

        // Recupera tutti gli IndexingObject che contengono almeno un value tra i complianceLevels
        List<IndexingObject> matchedObjects = offeringRepo.findByComplianceLevels(complianceLevels);

        if (matchedObjects.isEmpty()) return Collections.emptyList();

        // // extract unique RelatedParty IDs with role Seller or Owner
        List<String> allowedRoles = List.of("Seller", "Owner");
        Set<String> relatedPartyIds = extractRelatedPartyIds(matchedObjects, allowedRoles);

        log.debug("Found {} unique RelatedParty IDs by compliance levels", relatedPartyIds.size());

        // Recupera Organizations da TMF API
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

    private Set<String> extractRelatedPartyIds(List<IndexingObject> objects, List<String> allowedRoles) {
        if (objects == null || objects.isEmpty()) return Collections.emptySet();

        return objects.stream()
                .filter(obj -> obj.getRelatedParties() != null)
                .flatMap(obj -> obj.getRelatedParties().stream())
                .filter(rp -> rp.getId() != null && !rp.getId().isBlank())
                .filter(rp -> {
                    // if allowedRoles is null or empty not filter by role
                    if (allowedRoles == null || allowedRoles.isEmpty())
                        return true;
                    return allowedRoles.stream().anyMatch(role -> role.equalsIgnoreCase(rp.getRole()));
                })
                .map(RelatedPartyDTO::getId)
                .collect(Collectors.toSet());
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

    private <T> void printListLog(String label, List<T> list, Function<T, ?> idExtractor) {
        log.debug("Found {} {}: {}",
                list.size(),
                label,
                list.stream()
                        .map(idExtractor)
                        .collect(Collectors.toList()));
    }


    // frontend endpoint populate
    public List<String> getAllCategories() {
        Set<String> allCategories = new HashSet<>();

        log.info("Retrieving all categories from TMF...");
        tmfDataRetriever.fetchCategoriesByBatch("name", null, 100, batch -> {
            if (batch != null) {
                batch.forEach(cat -> {
                    if (cat.getName() != null && !cat.getName().isBlank()) {
                        allCategories.add(cat.getName());
                    }
                });
            }
        });

        List<String> sortedCategories = allCategories.stream()
                .sorted()
                .collect(Collectors.toList());

        log.info("Collected {} unique categories: {}", sortedCategories.size(), sortedCategories);
        return sortedCategories;
    }

    public List<String> getAllCountries() {
        Set<String> allCountries = new HashSet<>();

        log.info("Retrieving all countries from TMF...");
        List<Organization> allOrg = tmfDataRetriever.getAllPaginatedOrganizations(null, null, 100);
        if (allOrg != null) {
            for (Organization org : allOrg) {
                if (org.getPartyCharacteristic() != null) {
                    for (var pc : org.getPartyCharacteristic()) {
                        if ("country".equalsIgnoreCase(pc.getName()) && pc.getValue() != null) {
                            allCountries.add(pc.getValue().toString());
                        }
                    }
                }
            }
        }

        List<String> sortedCountries = allCountries.stream()
                .sorted()
                .collect(Collectors.toList());

        log.info("Collected {} unique countries: {}", sortedCountries.size(), sortedCountries);
        return sortedCountries;
    }

    public List<String> getAllComplianceLevels() {
        return Arrays.asList("BL", "P", "P+");
    }

}