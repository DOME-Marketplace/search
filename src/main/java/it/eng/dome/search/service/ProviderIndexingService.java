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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

            Set<String> currentTmfOrgIds = new HashSet<>();
            for (Organization org : allOrganizations) {
                if (org != null && org.getId() != null) {
                    currentTmfOrgIds.add(org.getId());
                }
            }

            /*
             * STEP 4
             * Retrieve DOME catalog categories
             */
            // List<String> domeCatalogCategories = domeCatalogService.getCatalogCategories();
            // log.info("Found {} categories in DOME Catalog",
            //         domeCatalogCategories.size());

            // List<String> subCategories = domeCatalogService.getDomeMainSubCategories();
            // log.info("Found {} sub-categories in DOME Main Categories:", subCategories.size());
            // subCategories.forEach(cat -> log.info(" - Category: {}", cat));

            List<String> subCategories = domeCatalogService.getOnlyDomeLeafCategories();
            log.info("Found {} sub-categories in DOME Main Categories:", subCategories.size());
            subCategories.forEach(cat -> log.info(" - Category: {}", cat));

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
                                subCategories
                        );

                providersToSave.add(processed);
            }

            /*
             * STEP 6
             * Bulk save active/updated providers
             */
            if (!providersToSave.isEmpty()) {
                providerIndexRepository.saveAll(providersToSave);
            }

            /*
             * STEP 7: CLEANUP PHASE FOR ORPHANED PROVIDERS
             */
            log.info("Running cleanup phase for providers no longer present in TMF...");
            int markedAsDeletedCount = 0;
            int providerPageNumber = 0;
            int providerPageSize = 500;
            Page<ProviderIndex> providerPage;

            do {
                Pageable providerPageable = PageRequest.of(providerPageNumber, providerPageSize);
                providerPage = providerIndexRepository.findAll(providerPageable);

                List<ProviderIndex> providersToDelete = new ArrayList<>();

                for (ProviderIndex indexedProvider : providerPage.getContent()) {
                    String providerId = indexedProvider.getId(); // o il campo corrispondente all'ID org nel documento ProviderIndex

                    // Se l'organizzazione indicizzata non esiste più nelle TMF API
                    if (providerId != null && !currentTmfOrgIds.contains(providerId)) {
                        log.info("Provider/Organization {} no longer in TMF source. Removing from index.", providerId);
                        providersToDelete.add(indexedProvider);
                        markedAsDeletedCount++;
                    }
                }

                // Eseguiamo la cancellazione (o potresti fare un update di stato se il ProviderIndex gestisce uno stato "Deleted")
                if (!providersToDelete.isEmpty()) {
                    providerIndexRepository.deleteAll(providersToDelete);
                }

                providerPageNumber++;

            } while (providerPage.hasNext());

            log.info("Provider indexing completed successfully. {} providers indexed, {} orphaned providers removed.",
                    providersToSave.size(), markedAsDeletedCount);

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