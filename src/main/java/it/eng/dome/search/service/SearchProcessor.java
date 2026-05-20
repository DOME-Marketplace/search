package it.eng.dome.search.service;

import it.eng.dome.search.domain.IndexingObject;
import it.eng.dome.search.repository.OfferingRepository;
import it.eng.dome.search.service.dto.SearchRequest;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class SearchProcessor {

	@Autowired
	private ElasticsearchOperations elasticsearchOperations;

	@Autowired
	private DomeCatalogService domeCatalogService;

	private static final Logger logger = LoggerFactory.getLogger(SearchProcessor.class);

	@Autowired
	OfferingRepository offeringRepo;

	public SearchProcessor(ElasticsearchOperations elasticsearchOperations, DomeCatalogService domeCatalogService) {
		this.elasticsearchOperations = elasticsearchOperations;
		this.domeCatalogService = domeCatalogService;
	}

	// search in all fields with boosting, fuzzy, wildcard, category filtering and
	// launched status
	public Map<Page<IndexingObject>, Map<IndexingObject, Float>> searchAllFields(String q, SearchRequest request,
			Pageable pageable) {

		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		String[] words = null;

		if (q != null && !q.trim().isEmpty()) {
			q = q.toLowerCase();
			// Split the query into individual words
			words = q.split("\\s+");

			// Create a bool query to collect all conditions
			BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
					// Exact Match
					.should(QueryBuilders.matchQuery("productOfferingName", q).boost(290))
					.should(QueryBuilders.matchQuery("productOfferingNameText", q).boost(270))
					.should(QueryBuilders.matchQuery("productSpecificationName", q).boost(70))
					.should(QueryBuilders.matchQuery("productSpecificationBrand", q).boost(65))
					.should(QueryBuilders.matchQuery("productSpecificationOwner", q).boost(65))

					// Phrase Match
					.should(QueryBuilders.matchPhraseQuery("productOfferingNameText", q).boost(240))
					.should(QueryBuilders.matchPhraseQuery("productSpecificationName", q).boost(45))
					.should(QueryBuilders.matchPhraseQuery("productSpecificationBrand", q).boost(25))
					.should(QueryBuilders.matchPhraseQuery("productSpecificationOwner", q).boost(25))

					// Multi-Match Exact (for individual words)
					.should(QueryBuilders.multiMatchQuery(q)
							.field("productOfferingNameText", 220)
							.field("productSpecificationName", 35)
							.field("productSpecificationBrand", 20)
							.field("productSpecificationOwner", 20)
							.field("productOfferingDescription", 8)
							.field("productSpecificationDescription", 4)
							.operator(Operator.OR));

			// Wildcard and Fuzzy for each word
			for (String word : words) {
				if (word.length() < 2)
					continue; // skip tiny tokens

				// Add wildcard for each word
				boolQuery
						.should(QueryBuilders.wildcardQuery("productOfferingNameText", word + "*").boost(200))
						.should(QueryBuilders.wildcardQuery("productSpecificationName", word + "*").boost(25))
						.should(QueryBuilders.wildcardQuery("productSpecificationBrand", word + "*").boost(15))
						.should(QueryBuilders.wildcardQuery("productSpecificationOwner", word + "*").boost(15))
						.should(QueryBuilders.wildcardQuery("productOfferingDescription", word + "*").boost(4))
						.should(QueryBuilders.wildcardQuery("productSpecificationDescription", word + "*").boost(3));

				// Add fuzzy search for each word
				boolQuery
						.should(QueryBuilders.fuzzyQuery("productOfferingNameText", word).fuzziness(Fuzziness.AUTO)
								.boost(60))
						.should(QueryBuilders.fuzzyQuery("productSpecificationName", word).fuzziness(Fuzziness.AUTO)
								.boost(15))
						.should(QueryBuilders.fuzzyQuery("productSpecificationBrand", word).fuzziness(Fuzziness.AUTO)
								.boost(10))
						.should(QueryBuilders.fuzzyQuery("productSpecificationOwner", word).fuzziness(Fuzziness.AUTO)
								.boost(10))
						.should(QueryBuilders.fuzzyQuery("productOfferingDescription", word).fuzziness(Fuzziness.AUTO)
								.boost(2))
						.should(QueryBuilders.fuzzyQuery("productSpecificationDescription", word)
								.fuzziness(Fuzziness.AUTO)
								.boost(1));
			}

			// Maintain the original score
			QueryBuilder queryBuilder = QueryBuilders.functionScoreQuery(boolQuery,
					ScoreFunctionBuilders.weightFactorFunction(1));

			// Create a BoolQueryBuilder to combine the main query and the category filter
			// BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

			// Log della query full-text
			logger.info("Building full-text query for: {}", q);
			boolQueryBuilder.must(queryBuilder);
		} else {
			logger.info("No search query provided, skipping full-text search.");
			// If there's no textual query, we use match_all to allow filters to work
			// function
			boolQueryBuilder.must(QueryBuilders.matchAllQuery());
		}

		// if (request.getCategories() != null && !request.getCategories().isEmpty()) {
		// logger.info("Adding category filter for categories: {}",
		// request.getCategories());
		// BoolQueryBuilder nestedBoolQuery = QueryBuilders.boolQuery();
		// nestedBoolQuery.must(QueryBuilders.termsQuery("categories.name",
		// request.getCategories()));
		// // Create a nested query to filter by categories
		// boolQueryBuilder = boolQueryBuilder.filter(
		// QueryBuilders.nestedQuery("categories", nestedBoolQuery,
		// org.apache.lucene.search.join.ScoreMode.None));
		// } else {
		// logger.info("No categories specified for filtering.");
		// }

		// HIERARCHICAL CATEGORIES LOGIC (AND between groups, OR between leaves)
		if (request.getCategories() != null && !request.getCategories().isEmpty()) {
			Map<String, Object> hierarchy = domeCatalogService.getCategoryHierarchy();
			Map<String, List<String>> groupedRequest = domeCatalogService
					.groupCategoriesByRoot(request.getCategories());
			// Se l'utente ha richiesto categorie, ma il servizio di raggruppamento restituisce una mappa vuota,
            // significa che nessuna delle categorie inserite esiste nella gerarchia ufficiale.
            if (groupedRequest == null || groupedRequest.isEmpty()) {
                logger.warn("Categories were requested but none exist in the hierarchy. Forcing 0 results.");
                // Iniettiamo un filtro impossibile da soddisfare per azzerare i risultati (AND con il resto)
                boolQueryBuilder.filter(QueryBuilders.termQuery("categories.name", "FORCE_EMPTY_RESULT_NO_MATCH_STR_TO_FORCE_ZERO_RESULTS"));
			} else {
				for (Map.Entry<String, List<String>> entry : groupedRequest.entrySet()) {
					List<String> catValues = entry.getValue();

					// Create an OR block for categories of the same group
					BoolQueryBuilder groupOrQuery = QueryBuilders.boolQuery();
					for (String value : catValues) {
						groupOrQuery.should(QueryBuilders.termQuery("categories.name", value));
					}

					// Add the group in FILTER (AND) via a Nested Query
					boolQueryBuilder.filter(
							QueryBuilders.nestedQuery("categories", groupOrQuery,
									org.apache.lucene.search.join.ScoreMode.None));
				}
			}
		}

		// COMPLIANCE LEVELS LOGIC
		if (request.getComplianceLevels() != null && !request.getComplianceLevels().isEmpty()) {
			logger.info("Adding compliance levels filter: {}", request.getComplianceLevels());

			// Mapping of values from frontend to short format for Elasticsearch
			List<String> mappedLevels = request.getComplianceLevels().stream()
					.map(level -> {
						switch (level.trim()) {
							case "Baseline":
								return "BL";
							case "Professional":
								return "P";
							case "Professional+":
								return "PP";
							default:
								return level; // Keeps the original value if not recognized (e.g. if it's already BL)
						}
					})
					.collect(Collectors.toList());

			// Since it's a simple Keyword field, we use termsQuery.
			// This will filter documents that have AT LEAST ONE of the values passed in the list.
			// We execute the filter with the mapped list (BL, P, P+)
    		boolQueryBuilder.filter(QueryBuilders.termsQuery("complianceLevels", mappedLevels));
		}

		// PROCUREMENT TYPE LOGIC
        if (request.getProcurementType() != null && !request.getProcurementType().isEmpty()) {
            logger.info("Adding procurement type filter for: {}", request.getProcurementType());

            // Verifichiamo cosa è stato richiesto nel payload (case-insensitive per sicurezza)
            boolean wantsReadyToBuy = request.getProcurementType().stream()
                    .anyMatch(val -> val.trim().equalsIgnoreCase("Ready to Buy"));
            
            boolean wantsRequestQuote = request.getProcurementType().stream()
                    .anyMatch(val -> val.trim().equalsIgnoreCase("Request Quote") || val.trim().equalsIgnoreCase("Request a Quote"));

            // Caso 1: Ha scelto SOLO "Ready to Buy" -> Escludiamo i "custom"
            if (wantsReadyToBuy && !wantsRequestQuote) {
                logger.info("Filtering for 'Ready to Buy' (productOfferingPriceType != custom)");
                boolQueryBuilder.filter(
                    QueryBuilders.boolQuery().mustNot(QueryBuilders.termQuery("productOfferingPriceType", "custom"))
                );
            } 
            // Caso 2: Ha scelto SOLO "Request Quote" -> Includiamo SOLO i "custom"
            else if (!wantsReadyToBuy && wantsRequestQuote) {
                logger.info("Filtering for 'Request Quote' (productOfferingPriceType == custom)");
                boolQueryBuilder.filter(QueryBuilders.termQuery("productOfferingPriceType", "custom"));
            }
            // Caso 3: Se sono presenti entrambi (o nessuno dei due match), non aggiungiamo filtri 
            // perché l'utente vuole vedere tutto l'insieme.
        }

		// Add a filter to include only products with status "launched"
		// Add a filter to include only products with status "Launched" or "launched"
		boolQueryBuilder.filter(
				QueryBuilders.termsQuery(
						"productOfferingLifecycleStatus",
						"Launched",
						"launched"));

		// Build the Elasticsearch query
		NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder()
				.withQuery(boolQueryBuilder)
				.withPageable(pageable)
				.withTrackScores(true); // Enable score tracking

		Query elasticQuery = nativeSearchQueryBuilder.build();

		try {
			// Execute the search query
			SearchHits<IndexingObject> searchHits = elasticsearchOperations.search(elasticQuery, IndexingObject.class);
			// logger.info("Found {} results", searchHits.getTotalHits());

			// Create a map to associate each IndexingObject with its score
			Map<IndexingObject, Float> resultScoreMap = new ConcurrentHashMap<>();

			// Convert search results into a list of IndexingObjects
			List<IndexingObject> resultPage = searchHits.stream()
					// .peek(hit -> logger.info("BEFORE BOOST -> Product: {} | Score: {}",
					// hit.getContent().getProductOfferingName(), // Nome prodotto
					// hit.getScore())) // Punteggio originale
					.map(SearchHit::getContent)
					.collect(Collectors.toList());
			// logger.info("Generated score map with {} entries", resultScoreMap.size());

			// Apply the manual boost (only if is present)
			String finalQ = (words != null && words.length > 0) ? words[0] : null;

			resultPage.forEach(obj -> {
				float newScore = searchHits.getSearchHit(resultPage.indexOf(obj)).getScore(); // original score
				if (finalQ != null && !finalQ.isEmpty()) {
					String name = obj.getProductOfferingNameText();
					if (name != null && !name.isEmpty()) {
					String firstWord = name.split("\\s+")[0]; // First word
						if (firstWord.toLowerCase().startsWith(finalQ.toLowerCase())) {
							newScore += 500;
						}
					}
				}
				resultScoreMap.put(obj, newScore);
			});

			/*
			 * // SECOND PRINT: Scores after the boost, ordered
			 * resultPage.stream()
			 * .peek(obj -> logger.info("AFTER BOOST -> Product: {} | Score: {}",
			 * obj.getProductOfferingName(), // Nome prodotto
			 * resultScoreMap.get(obj))) // Punteggio aggiornato
			 * .collect(Collectors.toList());
			 */

			// Create a paginated result set
			Page<IndexingObject> p = new PageImpl<>(resultPage, pageable, searchHits.getTotalHits());
			Map<Page<IndexingObject>, Map<IndexingObject, Float>> resultPageMap = new ConcurrentHashMap<>();
			resultPageMap.put(p, resultScoreMap);

			return resultPageMap;

		} catch (Exception e) {
			logger.warn("Error executing search query: {}", e.getMessage());
			return new HashMap<>();
		}
	}


	// Filter by category (only) - not used (before v1.4.0)
	public Page<IndexingObject> search(SearchRequest request, Pageable pageable) {

		// Create a BoolQueryBuilder to combine the category filters
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

		// Add category filter if categories are specified in the searchRequest
		if (request.getCategories() != null && !request.getCategories().isEmpty()) {
			logger.info("Adding category filter for categories: {}", request.getCategories());

			BoolQueryBuilder nestedBoolQuery = QueryBuilders.boolQuery();
			nestedBoolQuery.must(QueryBuilders.termsQuery("categories.name", request.getCategories()));

			// Create nested query
			boolQueryBuilder.filter(QueryBuilders.nestedQuery("categories", nestedBoolQuery,
					org.apache.lucene.search.join.ScoreMode.None));
		} else {
			logger.info("No categories specified for filtering.");
		}

		// Add a filter to include only products with status "launched"
		// Add a filter to include only products with status "Launched" or "launched"
		boolQueryBuilder.filter(
				QueryBuilders.termsQuery(
						"productOfferingLifecycleStatus",
						"Launched",
						"launched"));

		NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder()
				.withQuery(boolQueryBuilder)
				.withPageable(pageable);

		Query elasticQuery = nativeSearchQueryBuilder.build();

		// Log the final query for debugging
		// logger.info("Final Elasticsearch Query: {}", elasticQuery.toString());

		try {
			SearchHits<IndexingObject> searchHits = elasticsearchOperations.search(elasticQuery, IndexingObject.class);
			List<IndexingObject> resultPage = searchHits.stream()
					.map(SearchHit::getContent)
					.collect(Collectors.toList());
			return new PageImpl<>(resultPage, pageable, searchHits.getTotalHits());
		} catch (Exception e) {
			logger.warn("Error during search. Skipped: {}", e.getMessage());
			return new PageImpl<>(new ArrayList<>());
		}
	}
}