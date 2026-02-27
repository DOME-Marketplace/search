package it.eng.dome.search.service;

import it.eng.dome.search.domain.IndexingObject;
import it.eng.dome.search.domain.ProviderIndex;
import it.eng.dome.tmforum.tmf620.v4.model.ProductOffering;
import it.eng.dome.tmforum.tmf632.v4.model.Organization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ResultProcessor {

	private static final Logger log = LoggerFactory.getLogger(ResultProcessor.class);

	@Autowired
	TmfDataRetriever tmfDataRetriever;

	public Page<ProductOffering> processResults(Page<IndexingObject> page, Pageable pageable) {

		HashMap<String, ProductOffering> mapProductOffering = new HashMap<String, ProductOffering>();
		List<ProductOffering> listProductOffering = new ArrayList<ProductOffering>();

		try {
			log.info("Total number of Elements " + page.getTotalElements());

			List<IndexingObject> listIdexingObject = page.getContent();

			for (IndexingObject indexingObj : listIdexingObject) {
				if (indexingObj.getProductOfferingId() != null) {
					if (!mapProductOffering.containsKey(indexingObj.getProductOfferingId())) {
						ProductOffering productOffering = tmfDataRetriever.getProductOfferingById(indexingObj.getProductOfferingId(), null);
						if (productOffering == null) {
							log.warn("getProductOfferingById {} - Product Offering cannot found", indexingObj.getProductOfferingId());
						} else {
							// store the fetched ProductOffering
							mapProductOffering.put(indexingObj.getProductOfferingId(), productOffering);
						}
					}
				}
			}

			if (!mapProductOffering.isEmpty()) {
				for (Entry<String, ProductOffering> entry : mapProductOffering.entrySet()) {
					listProductOffering.add(entry.getValue());
					log.info(entry.getKey() + " " + entry.getValue().getName());
				}
			}

			return new PageImpl<>(listProductOffering, pageable, page.getTotalElements());
		} catch (HttpServerErrorException e) {
			log.warn("HttpServerErrorException - Error during processResults(). Skipped: {}", e.getMessage(), e);
			return new PageImpl<>(new ArrayList<>());
		}
	}

	public Page<ProductOffering> processResultsWithScore(Map<Page<IndexingObject>, Map<IndexingObject, Float>> resultPage, Pageable pageable) {

		HashMap<String, ProductOffering> mapProductOffering = new HashMap<>();
        Page<IndexingObject> page = null;
		Map<IndexingObject, Float> scoreMap = new ConcurrentHashMap<>();

		// Extract the first entry from resultPage to get the indexed objects and their scores
		if (!resultPage.isEmpty()) {
			for (Entry<Page<IndexingObject>, Map<IndexingObject, Float>> entry : resultPage.entrySet()) {
				page = entry.getKey();
				scoreMap = entry.getValue(); // Retrieve the score map
				break; // Exit after retrieving the first entry (only the first result is needed)
			}
		} else {
			log.warn("ResultProcessor.java - processResultsScore: no results to be processed");
		}

		try {
			log.info("Total number of elements: {}", page.getTotalElements());

			List<IndexingObject> listIndexingObject = page.getContent();
			Map<ProductOffering, Float> productScoreMap = new ConcurrentHashMap<>();

			for (IndexingObject indexingObj : listIndexingObject) {
				if (indexingObj.getProductOfferingId() != null) {
					// Retrieve product details only if not already fetched
					if (!mapProductOffering.containsKey(indexingObj.getProductOfferingId())) {
						ProductOffering productOffering = tmfDataRetriever.getProductOfferingById(indexingObj.getProductOfferingId(), null);
						if (productOffering == null) {
							log.warn("getProductOfferingById {} - PO cannot be found", indexingObj.getProductOfferingId());
						} else {
							// Store the fetched ProductOffering
							mapProductOffering.put(indexingObj.getProductOfferingId(), productOffering);
						}
					}

					// Associate ProductOffering with IndexingObject and its score
					ProductOffering productOffering = mapProductOffering.get(indexingObj.getProductOfferingId());
					if (productOffering != null) {
						Float score = scoreMap.get(indexingObj);
//						log.debug("Processing IndexingObject: {} with score: {}", indexingObj.getProductOfferingId(), score);
						productScoreMap.put(productOffering, score != null ? score : 0.0f);
					}
				}
			}

			// Add all retrieved ProductOfferings to the list
            List<ProductOffering> listProductOffering = new ArrayList<>(mapProductOffering.values());

			// Sort the list based on scores in descending order
			listProductOffering.sort((p1, p2) -> Float.compare(productScoreMap.get(p2), productScoreMap.get(p1)));

			if(!listProductOffering.isEmpty()) {
				log.info("Sorted ProductOfferings:");
				for (ProductOffering productOffering : listProductOffering) {
					log.info("ProductOffering: {} - {} with score: {}", productOffering.getId(), productOffering.getName(), productScoreMap.get(productOffering));
				}
			}

			return new PageImpl<>(listProductOffering, pageable, page.getTotalElements());

		} catch (HttpServerErrorException e) {
			log.warn("HttpServerErrorException - Error during processResultsScore(). Skipped: {}", e.getMessage());
			return new PageImpl<>(new ArrayList<>());
		}
	}

	public Page<Organization> processProviderResults(Page<ProviderIndex> page, Pageable pageable) {

		Map<String, Organization> mapOrganizations = new HashMap<>();
		List<Organization> resultList = new ArrayList<>();

		log.info("Total number of Providers: {}", page.getTotalElements());

		for (ProviderIndex providerIndex : page.getContent()) {
			String orgId = providerIndex.getId();
			if (orgId != null && !mapOrganizations.containsKey(orgId)) {
				Organization org = tmfDataRetriever.getOrganizationById(orgId, null);
				if (org != null) {
					mapOrganizations.put(orgId, org);
					resultList.add(org);
					log.info("Fetched Organization {}: {}", orgId, org.getTradingName());
				} else {
					log.warn("Organization {} not found", orgId);
				}
			}
		}

		return new PageImpl<>(resultList, pageable, page.getTotalElements());
	}

}