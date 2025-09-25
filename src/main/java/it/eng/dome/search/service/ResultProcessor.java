package it.eng.dome.search.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import it.eng.dome.brokerage.api.ProductOfferingApis;
import it.eng.dome.search.tmf.TmfApiFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.eng.dome.search.domain.IndexingObject;
import it.eng.dome.tmforum.tmf620.v4.model.ProductOffering;

@Service
public class ResultProcessor implements InitializingBean {

	private static final Logger log = LoggerFactory.getLogger(ResultProcessor.class);
	private static final ObjectMapper objectMapper = new ObjectMapper();

	// Factory for TMF APIss
	@Autowired
	private TmfApiFactory tmfApiFactory;

	private ProductOfferingApis productOfferingApis;

	@Override
	public void afterPropertiesSet () throws Exception {
		this.productOfferingApis = new ProductOfferingApis(tmfApiFactory.getTMF620ProductCatalogApiClient());

		log.info("ResultProcessor initialized with productOfferingApis");
	}

	public Page<ProductOffering> processResults(Page<IndexingObject> page, Pageable pageable) {

		HashMap<String, ProductOffering> mapProductOffering = new HashMap<String, ProductOffering>();
		List<ProductOffering> listProductOffering = new ArrayList<ProductOffering>();

		try {
			log.info("Total number of Elements " + page.getTotalElements());

			List<IndexingObject> listIdexingObject = page.getContent();
			for (IndexingObject indexingObj : listIdexingObject) {

				if (indexingObj.getProductOfferingId() != null) {

					if (!mapProductOffering.containsKey(indexingObj.getProductOfferingId())) {
						//String requestForProductOfferingId = restTemplate.getProductOfferingById(indexingObj.getProductOfferingId());
						ProductOffering productOffering = this.productOfferingApis.getProductOffering(indexingObj.getProductOfferingId(), null);

						if (productOffering == null) {
							log.warn("product offering with ID {} cannot found", indexingObj.getProductOfferingId());
						} else {
							//ProductOffering productOfferingDetails = objectMapper.readValue(requestForProductOfferingId,ProductOffering.class);
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
		List<ProductOffering> listProductOffering = new ArrayList<>();
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
						//String requestForProductOfferingId = restTemplate.getProductOfferingById(indexingObj.getProductOfferingId());
						ProductOffering productOffering = this.productOfferingApis.getProductOffering(indexingObj.getProductOfferingId(), null);

						if (productOffering == null) {
							log.warn("product offering with ID {} cannot be found", indexingObj.getProductOfferingId());
						} else {
							// Convert the retrieved JSON response into a ProductOffering object
							//ProductOffering productOfferingDetails = objectMapper.readValue(requestForProductOfferingId, ProductOffering.class);
							mapProductOffering.put(indexingObj.getProductOfferingId(), productOffering);
						}
					}

					// Associate ProductOffering with IndexingObject and its score
					ProductOffering productOffering = mapProductOffering.get(indexingObj.getProductOfferingId());
					if (productOffering != null) {
						Float score = scoreMap.get(indexingObj);
						//TODO: CHECK OUTPUT IN CONSOLE
						log.debug("Processing IndexingObject: {} with score: {}", indexingObj.getProductOfferingId(), score);
						productScoreMap.put(productOffering, score != null ? score : 0.0f);
					}
				}
			}

			// Add all retrieved ProductOfferings to the list
			listProductOffering.addAll(mapProductOffering.values());

			// Sort the list based on scores in descending order
			listProductOffering.sort((p1, p2) -> Float.compare(productScoreMap.get(p2), productScoreMap.get(p1)));

//			log.info("Sorted ProductOfferings:");
//			for (ProductOffering productOffering : listProductOffering) {
//				log.info("ProductOffering: {} with score: {}", productOffering.getId(), productScoreMap.get(productOffering));
//			}

			return new PageImpl<>(listProductOffering, pageable, page.getTotalElements());

		} catch (HttpServerErrorException e) {
			log.warn("HttpServerErrorException - Error during processResultsScore(). Skipped: {}", e.getMessage());
			return new PageImpl<>(new ArrayList<>());
		}
	}

    public Page<ProductOffering> processBrowsingResults(Page<IndexingObject> page, Pageable pageable) {
        List<ProductOffering> listProductOffering = new ArrayList<>();

        try {
            log.info("Total number of Elements " + page.getTotalElements());

            // gets IndexingObject list
            List<IndexingObject> indexingObjects = page.getContent();

            for (IndexingObject indexingObject : indexingObjects) {
                if (indexingObject.getProductOfferingId() != null) {
                    String productOfferingId = indexingObject.getProductOfferingId();

                    // Recuperiamo il ProductOffering tramite l'ID
                    //String response = restTemplate.getProductOfferingById(productOfferingId);
					ProductOffering response = this.productOfferingApis.getProductOffering(productOfferingId, null);

                    if (response == null) {
                        log.warn("`product offering with ID {} not found", productOfferingId);
                    } else {
                        //ProductOffering productOffering = objectMapper.readValue(response, ProductOffering.class);
                        listProductOffering.add(response);
                        log.info("Added ProductOffering with ID: " + productOfferingId + " and name: " + response.getName());
                    }
                }
            }

            return new PageImpl<>(listProductOffering, pageable, page.getTotalElements());

        } catch (HttpServerErrorException e) {
            log.warn("HttpServerErrorException - Error during processBrowsingResults: {}", e.getMessage(), e);
            return new PageImpl<>(new ArrayList<>());
        }
    }
}