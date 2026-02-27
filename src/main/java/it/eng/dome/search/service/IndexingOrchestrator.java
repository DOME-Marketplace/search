package it.eng.dome.search.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class IndexingOrchestrator {

    private static final Logger log =
            LoggerFactory.getLogger(IndexingOrchestrator.class);

    private final IndexingService indexingService;
    private final ProviderIndexingService providerIndexingService;

    private final AtomicBoolean running = new AtomicBoolean(false);

    public IndexingOrchestrator(IndexingService indexingService,
                                    ProviderIndexingService providerIndexingService) {
        this.indexingService = indexingService;
        this.providerIndexingService = providerIndexingService;
    }

    // every 5 minutes
    @Scheduled(cron = "0 */5 * * * ?")
    public void runFullIndexingFlow() {

        if (!running.compareAndSet(false, true)) {
            log.warn("Full indexing flow already running, skipping.");
            return;
        }

        long start = System.currentTimeMillis();

        try {
            log.info("========== FULL INDEXING FLOW STARTED ==========");

            // STEP 1 → offerings
            log.info("Step 1: ProductOffering indexing started");
            indexingService.indexing();
            log.info("Step 1 completed");

            // STEP 2 → providers
            log.info("Step 2: Provider indexing started");
            providerIndexingService.indexing();
            log.info("Step 2 completed");

            long duration = System.currentTimeMillis() - start;
            log.info("========== FULL INDEXING FLOW COMPLETED in {} ms ==========", duration);

        } catch (Exception e) {
            log.error("Error during full indexing flow", e);
        } finally {
            running.set(false);
        }
    }
}