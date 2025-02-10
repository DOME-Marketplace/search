package it.eng.dome.search.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.eng.dome.search.domain.IndexingObject;
import it.eng.dome.search.domain.ProductOffering;
import it.eng.dome.search.rest.web.util.RestUtil;

@Service
public class ResultProcessor {

	private static final Logger log = LoggerFactory.getLogger(ResultProcessor.class);
	private static final ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	private RestUtil restTemplate;

	public Page<ProductOffering> processResults(Page<IndexingObject> page, Pageable pageable) {

		HashMap<String, ProductOffering> mapProductOffering = new HashMap<String, ProductOffering>();
		List<ProductOffering> listProductOffering = new ArrayList<ProductOffering>();

		try {
			log.info("Total number of Elements " + page.getTotalElements());

			List<IndexingObject> listIdexingObject = page.getContent();
			for (IndexingObject indexingObj : listIdexingObject) {

				if (indexingObj.getProductOfferingId() != null) {

					if (!mapProductOffering.containsKey(indexingObj.getProductOfferingId())) {
						String requestForProductOfferingId = restTemplate.getProductOfferingById(indexingObj.getProductOfferingId());

						if (requestForProductOfferingId == null) {
							log.warn("getProductOfferingById {} cannot found", indexingObj.getProductOfferingId());
						} else {

							ProductOffering productOfferingDetails = objectMapper.readValue(requestForProductOfferingId,
									ProductOffering.class);

							mapProductOffering.put(indexingObj.getProductOfferingId(), productOfferingDetails);
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
		} catch (JsonProcessingException e) {
			log.warn("JsonProcessingException - Error during processResults(). Skipped: {}", e.getMessage());
			e.printStackTrace();
			return new PageImpl<>(new ArrayList<>());
		} catch (HttpServerErrorException e) {
			log.warn("HttpServerErrorException - Error during processResults(). Skipped: {}", e.getMessage());
			e.printStackTrace();
			return new PageImpl<>(new ArrayList<>());
		}
	}

	public Page<ProductOffering> processResultsWithScore(Map<Page<IndexingObject>, Map<IndexingObject, Float>> resultPage, Pageable pageable) {

		HashMap<String, ProductOffering> mapProductOffering = new HashMap<>();
		List<ProductOffering> listProductOffering = new ArrayList<>();
		Page<IndexingObject> page = null;
		Map<IndexingObject, Float> scoreMap = new HashMap<>();

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
			Map<ProductOffering, Float> productScoreMap = new HashMap<>();

			for (IndexingObject indexingObj : listIndexingObject) {
				if (indexingObj.getProductOfferingId() != null) {
					// Retrieve product details only if not already fetched
					if (!mapProductOffering.containsKey(indexingObj.getProductOfferingId())) {
						String requestForProductOfferingId = restTemplate.getProductOfferingById(indexingObj.getProductOfferingId());

						if (requestForProductOfferingId == null) {
							log.warn("getProductOfferingById {} cannot be found", indexingObj.getProductOfferingId());
						} else {
							// Convert the retrieved JSON response into a ProductOffering object
							ProductOffering productOfferingDetails = objectMapper.readValue(requestForProductOfferingId, ProductOffering.class);
							mapProductOffering.put(indexingObj.getProductOfferingId(), productOfferingDetails);
						}
					}

					// Associate ProductOffering with IndexingObject and its score
					ProductOffering productOffering = mapProductOffering.get(indexingObj.getProductOfferingId());
					if (productOffering != null) {
						Float score = scoreMap.get(indexingObj);
						log.debug("Processing IndexingObject: {} with score: {}", indexingObj.getProductOfferingId(), score);
						productScoreMap.put(productOffering, score != null ? score : 0.0f);
					}
				}
			}

			// Add all retrieved ProductOfferings to the list
			listProductOffering.addAll(mapProductOffering.values());

			// Sort the list based on scores in descending order
			listProductOffering.sort((p1, p2) -> Float.compare(productScoreMap.get(p2), productScoreMap.get(p1)));

			return new PageImpl<>(listProductOffering, pageable, page.getTotalElements());

		} catch (JsonProcessingException e) {
			log.warn("JsonProcessingException - Error during processResultsScore(). Skipped: {}", e.getMessage());
			return new PageImpl<>(new ArrayList<>());
		} catch (HttpServerErrorException e) {
			log.warn("HttpServerErrorException - Error during processResultsScore(). Skipped: {}", e.getMessage());
			return new PageImpl<>(new ArrayList<>());
		}
	}

}