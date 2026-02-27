package it.eng.dome.search.service;

import it.eng.dome.search.domain.IndexingObject;
import it.eng.dome.search.domain.ProviderIndex;
import it.eng.dome.search.domain.dto.RelatedPartyDTO;
import it.eng.dome.search.indexing.IndexingManager;
import it.eng.dome.search.repository.OfferingRepository;
import it.eng.dome.search.repository.ProviderIndexRepository;
import it.eng.dome.tmforum.tmf632.v4.model.Organization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class ProviderIndexingService {

    private static final Logger log = LoggerFactory.getLogger(ProviderIndexingService.class);

    @Autowired
    private TmfDataRetriever tmfDataRetriever;

    @Autowired
    private ProviderIndexRepository providerIndexRepository;

    @Autowired
    private IndexingManager indexingManager;

    private final DomeCatalogService domeCatalogService;

    private final OfferingRepository offeringRepository;

    private final AtomicBoolean running = new AtomicBoolean(false);

    public ProviderIndexingService (DomeCatalogService domeCatalogService, OfferingRepository offeringRepository) {
        this.domeCatalogService = domeCatalogService;
        this.offeringRepository = offeringRepository;
    }

//    @Scheduled(fixedDelay = 100000) // each 100 seconds
    public void indexing() {

        if (!running.compareAndSet(false, true)) {
            log.warn("ProviderIndexing already running, skipping execution.");
            return;
        }
        try {
            log.info("Starting Provider indexing process... (using ElasticSearch)");

            /*
             * STEP 1 & 2
             * Retrieve offerings paginated and build map: organizationId -> List<IndexingObject> (Launched only)
             */
            Map<String, List<IndexingObject>> organizationToOfferings = new HashMap<>();

            Pageable pageable = PageRequest.of(0, 500);
            Page<IndexingObject> page;

            do {
                page = offeringRepository.findAll(pageable);

                for (IndexingObject io : page.getContent()) {

                    if (!"Launched".equalsIgnoreCase(io.getProductOfferingLifecycleStatus())) {
                        continue;
                    }

                    if (io.getRelatedParties() == null) {
                        continue;
                    }

                    for (RelatedPartyDTO rp : io.getRelatedParties()) {

                        if ("Seller".equalsIgnoreCase(rp.getRole()) && rp.getId() != null) {
                            organizationToOfferings
                                    .computeIfAbsent(rp.getId(), k -> new ArrayList<>())
                                    .add(io);
                        }
                    }
                }

                pageable = page.nextPageable();

            } while (page.hasNext());

            log.info("Found {} organizations with launched offerings", organizationToOfferings.size());

            /*
             * STEP 3
             * Retrieve all Organizations from TMF
             */
            List<Organization> allOrganizations =
                    tmfDataRetriever.getAllPaginatedOrganizations(null, null, 50);

            log.info("Found {} total organizations",
                    allOrganizations.size());

            /*
             * STEP 4
             * Retrieve DOME catalog categories
             */
            List<String> domeCatalogCategories = domeCatalogService.getCatalogCategories();
            log.info("Found {} categories in DOME Catalog",
                    domeCatalogCategories.size());

            /*
             * STEP 5
             * Build ProviderIndex list
             */
            List<ProviderIndex> providersToSave = new ArrayList<>();

            for (Organization organization : allOrganizations) {

                if (organization == null || organization.getId() == null) {
                    continue;
                }

                String orgId = organization.getId();

                List<IndexingObject> offerings =
                        organizationToOfferings.getOrDefault(orgId, Collections.emptyList());

                ProviderIndex existing =
                        providerIndexRepository.findById(orgId)
                                .orElse(new ProviderIndex());

                ProviderIndex processed =
                        indexingManager.processProviderFromIndexingObject(
                                organization,
                                offerings,
                                existing,
                                domeCatalogCategories
                        );

                providersToSave.add(processed);
            }

            /*
             * STEP 6
             * Bulk save
             */
            providerIndexRepository.saveAll(providersToSave);

            log.info("Provider indexing completed successfully. {} providers indexed.",
                    providersToSave.size());

        } catch (Exception e) {
            log.error("Unexpected error during Provider indexing: {}",
                    e.getMessage(), e);
        } finally {
            running.set(false);
        }
    }

    public void clearRepository() {
        providerIndexRepository.deleteAll();
    }
}