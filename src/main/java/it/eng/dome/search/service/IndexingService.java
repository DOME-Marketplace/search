package it.eng.dome.search.service;

import it.eng.dome.search.domain.IndexingObject;
import it.eng.dome.search.indexing.IndexingManager;
import it.eng.dome.search.repository.OfferingRepository;
import it.eng.dome.tmforum.tmf620.v4.model.ProductOffering;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class IndexingService {
	private static final Logger log = LoggerFactory.getLogger(IndexingService.class);

	@Autowired
	IndexingManager indexingManager;

	@Autowired
	OfferingRepository offeringRepo;

	@Autowired
	TmfDataRetriever tmfDataRetriever;

	private final AtomicBoolean running = new AtomicBoolean(false);

	@Scheduled(fixedDelay = 300000) // every 5 min
	public void indexing() {
		if (!running.compareAndSet(false, true)) {
			log.warn("Indexing already running, skipping execution.");
			return;
		}

		try {
			log.info("Starting indexing process...");

			List<ProductOffering> offerings = tmfDataRetriever.getAllPaginatedProductOfferings(null, null);
			if (offerings == null || offerings.isEmpty()) {
				log.warn("No ProductOfferings found from TMF");
				return;
			}
			log.info("Fetched {} ProductOfferings from TMF", offerings.size());

			// Step 1: prefetch existing
			List<String> offeringIds = offerings.stream()
					.map(ProductOffering::getId)
					.collect(Collectors.toList());

			List<IndexingObject> existing = offeringRepo.findByProductOfferingIdIn(offeringIds);
			Map<String, IndexingObject> existingMap = existing.stream()
					.collect(Collectors.toMap(IndexingObject::getProductOfferingId, io -> io));

			// Step 2: process and collect updates
			int created = 0;
			int updated = 0;
			List<IndexingObject> toSave = new ArrayList<>();

			for (ProductOffering po : offerings) {
				IndexingObject existingObj = existingMap.get(po.getId());
				if (existingObj != null) {
					updated++;
				} else {
					created++;
					existingObj = new IndexingObject();
				}
				
				log.debug("Processing productOffering: {}", po.getId());
				IndexingObject processed = indexingManager.processOfferingFromTMForum(po, existingObj);
				toSave.add(processed);
			}

			log.debug("Saving {} index objects", toSave.size());
			offeringRepo.saveAll(toSave);
			log.info("Indexing process terminated: {} processed ({} updated, {} created)", toSave.size(), updated, created);

		} catch (Exception e) {
			log.error("Error during indexing: {}", e.getMessage(), e);
		} finally {
			running.set(false);
		}
	}

	/** General usage*/
	public void clearRepository() {
		offeringRepo.deleteAll();
	}

	// @Scheduled(fixedDelay = 3000)
	// private void test() {
	//
	// System.out.println("test -> " + new Date());
	//
	// try {
	// Thread.sleep(2000);
	// }
	// catch (Exception e) {
	// System.out.println("error");
	// }
	// }

//	@Scheduled(fixedDelay = 300000)
//	public void indexing() {
//		log.info("Indexing is executing ..... ");
//		// invoke TMF API to get all ProductOfferings
//		List<ProductOffering> listProductOfferings = productOfferingApis.getAllProductOfferings(null, null); // -------> from TMF directly (change when needed)
//
//		if (listProductOfferings == null || listProductOfferings.isEmpty()) {
//			log.warn("listProductOfferings cannot be null");
//		} else {
//			log.info("Total of ProductOfferings found: {}", listProductOfferings.size());
//
//			for (ProductOffering product : listProductOfferings) {
//				String idProduct = product.getId();
//				log.debug("Id product: {} ", idProduct);
//
//				List<IndexingObject> listFromRepo = offeringRepo.findByProductOfferingId(idProduct);
//
//				if (listFromRepo.isEmpty()) {
//					log.debug("ProductOffering listFromRepo empty");
//					IndexingObject indexingObjEmpty = new IndexingObject();
//					indexingObjEmpty = indexingManager.processOfferingFromTMForum(product, indexingObjEmpty);
//					offeringRepo.save(indexingObjEmpty);
//				} else {
//					log.debug("ProductOffering listFromRepo size: {}", listFromRepo.size());
//					for (IndexingObject obj : listFromRepo) {
//						IndexingObject indexingObjEmpty = new IndexingObject();
//						indexingObjEmpty.setId(obj.getId());
//						indexingObjEmpty = indexingManager.processOfferingFromTMForum(product, indexingObjEmpty);
//						offeringRepo.save(indexingObjEmpty);
//					}
//				}
//			}
//		}
//
//		log.info("Indexing process terminated ");
//	}
}
