package it.eng.dome.search.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.*;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
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

import it.eng.dome.search.domain.IndexingObject;
import it.eng.dome.search.repository.OfferingRepository;
import it.eng.dome.search.service.dto.SearchRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class SearchProcessor {

	@Autowired
	private ElasticsearchOperations elasticsearchOperations;

	private static final Logger logger = LoggerFactory.getLogger(SearchProcessor.class);

	@Autowired
	OfferingRepository offeringRepo;

	public SearchProcessor(ElasticsearchOperations elasticsearchOperations) {
		this.elasticsearchOperations = elasticsearchOperations;
	}

	// search 0.1 - get
	public Page<IndexingObject> search(String q, Pageable pageable){

		// QueryBuilder queryBuilder = QueryBuilders.simpleQueryStringQuery(q);
		QueryBuilder queryBuilder = QueryBuilders.queryStringQuery(q); //.defaultOperator(null);

		NativeSearchQueryBuilder nativeSearchQueryBuilder = new
				NativeSearchQueryBuilder() .withQuery(queryBuilder ) .withPageable(pageable);

		Query elasticQuery = nativeSearchQueryBuilder.build();

		try { SearchHits<IndexingObject> searchHits =
				elasticsearchOperations.search(elasticQuery, IndexingObject.class);
		List<IndexingObject> resultPage =
				searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());
		return new PageImpl<>(resultPage,pageable,searchHits.getTotalHits()); } catch
		(Exception e) { logger.warn("Error during search. Skipped: {}",
				e.getMessage()); return new PageImpl<>(new ArrayList<>()); }
	}


	//search request con filtraggio su nome categorie
//	public Page<IndexingObject> search(String q, SearchRequest request, Pageable pageable){
//
//		// Main query
//		QueryBuilder queryBuilder = QueryBuilders.queryStringQuery(q);
//
//		// Create a BoolQueryBuilder to combine the main query and the category filter
//		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(queryBuilder);
//
//		// Add category filter if categories are specified in the searchRequest
//		if (request.getCategories() != null && ! request.getCategories().isEmpty()) {
//			logger.error("Adding category filter for categories: {}", request.getCategories());
//			//boolQueryBuilder.filter(QueryBuilders.termsQuery("categories.name", request.getCategories()));
//			TermsQueryBuilder termsQueryBuilder = QueryBuilders.termsQuery("categories.name", request.getCategories());
//			boolQueryBuilder.filter(termsQueryBuilder);
//		} else {
//			logger.debug("No categories specified for filtering.");
//		}
//
//		//		NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder().withQuery(boolQueryBuilder).withPageable(pageable);
//		//
//		//		Query elasticQuery = nativeSearchQueryBuilder.build();
//
//		// Build the search query using SearchSourceBuilder
//		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//		searchSourceBuilder.query(boolQueryBuilder);
//		searchSourceBuilder.from((int) pageable.getOffset());
//		searchSourceBuilder.size(pageable.getPageSize());
//
//		// Create the NativeSearchQuery
//		Query elasticQuery = new NativeSearchQuery(queryBuilder);
//
//		try { 
//			SearchHits<IndexingObject> searchHits = elasticsearchOperations.search(elasticQuery, IndexingObject.class);
//			List<IndexingObject> resultPage = searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());
//			return new PageImpl<>(resultPage,pageable,searchHits.getTotalHits()); 
//
//		} catch (Exception e) { 
//			logger.warn("Error during search. Skipped: {}", e.getMessage()); 
//			return new PageImpl<>(new ArrayList<>()); 
//		}
//
//	}

	//search 1.0
//	public Page<IndexingObject> search(String q, SearchRequest request, Pageable pageable){
//
//		// Main query
//		QueryBuilder queryBuilder = QueryBuilders.queryStringQuery(q);
//
//		// Create a BoolQueryBuilder to combine the main query and the category filter
//		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(queryBuilder);
//
//		// Add category filter if categories are specified in the searchRequest
//		if (request.getCategories() != null && ! request.getCategories().isEmpty()) {
//			logger.info("Adding category filter for categories: {}", request.getCategories());
//			BoolQueryBuilder nestedBoolQuery = QueryBuilders.boolQuery();
//            nestedBoolQuery.must(QueryBuilders.termsQuery("categories.name", request.getCategories()));
//
//            // Create nested query
//            boolQueryBuilder  = boolQueryBuilder.filter(QueryBuilders.nestedQuery("categories", nestedBoolQuery, org.apache.lucene.search.join.ScoreMode.None));
//		} else {
//			logger.info("No categories specified for filtering.");
//		}
//
//		TermQueryBuilder termQueryBuilderStatus = QueryBuilders.termQuery("productOfferingLifecycleStatus", "launched");
//		boolQueryBuilder= boolQueryBuilder.filter(termQueryBuilderStatus);
//
//
//
//		NativeSearchQueryBuilder nativeSearchQueryBuilder = new
//				NativeSearchQueryBuilder() .withQuery(boolQueryBuilder) .withPageable(pageable);
//
//		Query elasticQuery = nativeSearchQueryBuilder.build();
//
//
//		try {
//			SearchHits<IndexingObject> searchHits = elasticsearchOperations.search(elasticQuery, IndexingObject.class);
//			List<IndexingObject> resultPage = searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());
//			return new PageImpl<>(resultPage,pageable,searchHits.getTotalHits());
//
//		} catch (Exception e) {
//			logger.warn("Error during search. Skipped: {}", e.getMessage());
//			return new PageImpl<>(new ArrayList<>());
//		}
//
//	}


	//Filter by category
	public Page<IndexingObject> search(SearchRequest request, Pageable pageable) {

		// Create a BoolQueryBuilder to combine the category filters
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

		// Add category filter if categories are specified in the searchRequest
		if (request.getCategories() != null && !request.getCategories().isEmpty()) {
			logger.info("Adding category filter for categories: {}", request.getCategories());
			BoolQueryBuilder nestedBoolQuery = QueryBuilders.boolQuery();
            nestedBoolQuery.must(QueryBuilders.termsQuery("categories.name", request.getCategories()));

            // Create nested query
            boolQueryBuilder.filter(QueryBuilders.nestedQuery("categories", nestedBoolQuery, org.apache.lucene.search.join.ScoreMode.None));
        } else {
            logger.info("No categories specified for filtering.");
        }

        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withPageable(pageable);

        Query elasticQuery = nativeSearchQueryBuilder.build();

		// Log the final query for debugging
		logger.info("Final Elasticsearch Query: {}", elasticQuery.toString());

		 

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

	public Page<IndexingObject> fuzzySearch(String q, Pageable pageable){

		QueryBuilder queryBuilder = QueryBuilders.queryStringQuery(q).fuzziness(Fuzziness.AUTO);

		NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder()
				.withQuery(queryBuilder )
				.withPageable(pageable);

		Query elasticQuery = nativeSearchQueryBuilder.build();

		try {
			SearchHits<IndexingObject> searchHits = elasticsearchOperations.search(elasticQuery, IndexingObject.class); 
			List<IndexingObject> resultPage = searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());
			return  new PageImpl<>(resultPage,pageable,searchHits.getTotalHits());
		} catch (Exception e) {
			logger.warn("Error during Fuzzy search. Skipped: {}", e.getMessage());
			return new PageImpl<>(new ArrayList<>());
		}
	}



	public Page<IndexingObject> testSearch(String query, Pageable pageable) {

		QueryBuilder queryBuilder = QueryBuilders.boolQuery()
				.should(QueryBuilders.matchQuery("productOfferingDescription", query))
				.should(QueryBuilders.matchQuery("productOfferingNameText", query))
				.should(QueryBuilders.matchQuery("productOfferingName", query));

		NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder()
				.withQuery(queryBuilder)
				.withPageable(pageable);

		Query elasticQuery = nativeSearchQueryBuilder.build();

		try {
			SearchHits<IndexingObject> searchHits = elasticsearchOperations.search(elasticQuery, IndexingObject.class);

			List<IndexingObject> resultPage = searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());
			return  new PageImpl<>(resultPage,pageable,searchHits.getTotalHits());
		} catch (Exception e) {
			logger.warn("Error during test for oneDrive search. Skipped: {}", e.getMessage());
			return new PageImpl<>(new ArrayList<>());
		}
	}

	public Map<Page<IndexingObject>, Map<IndexingObject, Float>> searchAllFields (String q, SearchRequest request, Pageable pageable) {

		q = q.toLowerCase();
		// Split the query into individual words
		String[] words = q.split("\\s+");

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
			// Add wildcard for each word
			boolQuery
					.should(QueryBuilders.wildcardQuery("productOfferingNameText", word + "*" ).boost(200))
					.should(QueryBuilders.wildcardQuery("productSpecificationName", word + "*" ).boost(25))
					.should(QueryBuilders.wildcardQuery("productSpecificationBrand", word + "*" ).boost(15))
					.should(QueryBuilders.wildcardQuery("productSpecificationOwner", word + "*" ).boost(15))
					.should(QueryBuilders.wildcardQuery("productOfferingDescription", word + "*" ).boost(4))
					.should(QueryBuilders.wildcardQuery("productSpecificationDescription", word + "*" ).boost(3));

			// Add fuzzy search for each word
			boolQuery
					.should(QueryBuilders.fuzzyQuery("productOfferingNameText", word).fuzziness(Fuzziness.AUTO).boost(60))
					.should(QueryBuilders.fuzzyQuery("productSpecificationName", word).fuzziness(Fuzziness.AUTO).boost(15))
					.should(QueryBuilders.fuzzyQuery("productSpecificationBrand", word).fuzziness(Fuzziness.AUTO).boost(10))
					.should(QueryBuilders.fuzzyQuery("productSpecificationOwner", word).fuzziness(Fuzziness.AUTO).boost(10))
					.should(QueryBuilders.fuzzyQuery("productOfferingDescription", word).fuzziness(Fuzziness.AUTO).boost(2))
					.should(QueryBuilders.fuzzyQuery("productSpecificationDescription", word).fuzziness(Fuzziness.AUTO).boost(1));
		}

		// Maintain the original score
		QueryBuilder queryBuilder = QueryBuilders.functionScoreQuery(boolQuery, ScoreFunctionBuilders.weightFactorFunction(1));

		// Create a BoolQueryBuilder to combine the main query and the category filter
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(queryBuilder);

		// Add category filter if categories are specified in the searchRequest
		if (request.getCategories() != null && !request.getCategories().isEmpty()) {
			logger.info("Adding category filter for categories: {}", request.getCategories());
			BoolQueryBuilder nestedBoolQuery = QueryBuilders.boolQuery();
			nestedBoolQuery.must(QueryBuilders.termsQuery("categories.name", request.getCategories()));

			// Create a nested query to filter by categories
			boolQueryBuilder = boolQueryBuilder.filter(QueryBuilders.nestedQuery("categories", nestedBoolQuery, org.apache.lucene.search.join.ScoreMode.None));
		} else {
			logger.info("No categories specified for filtering.");
		}

		// Add a filter to include only products with status "launched"
		TermQueryBuilder termQueryBuilderStatus = QueryBuilders.termQuery("productOfferingLifecycleStatus", "launched");
		boolQueryBuilder = boolQueryBuilder.filter(termQueryBuilderStatus);

		// Build the Elasticsearch query
		NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder()
				.withQuery(boolQueryBuilder)
				.withPageable(pageable)
				.withTrackScores(true); // Enable score tracking

		Query elasticQuery = nativeSearchQueryBuilder.build();

		try {
			// Execute the search query
			SearchHits<IndexingObject> searchHits = elasticsearchOperations.search(elasticQuery, IndexingObject.class);
			//logger.info("Found {} results", searchHits.getTotalHits());

			// Create a map to associate each IndexingObject with its score
			Map<IndexingObject, Float> resultScoreMap = searchHits.stream()
					.collect(Collectors.toMap(SearchHit::getContent, SearchHit::getScore));

			// Convert search results into a list of IndexingObjects
			List<IndexingObject> resultPage = searchHits.stream()
//					.peek(hit -> logger.info("Product: {} | Score: {}",
//							hit.getContent().getProductOfferingName(), // Product name
//							hit.getScore())) // Result score
					.map(SearchHit::getContent)
					.collect(Collectors.toList());
			//logger.info("Generated score map with {} entries", resultScoreMap.size());

			// Create a paginated result set
			Map<Page<IndexingObject>, Map<IndexingObject, Float>> resultPageMap = new ConcurrentHashMap<>();
			Page<IndexingObject> p = new PageImpl<>(resultPage, pageable, searchHits.getTotalHits());

			// Store the paginated results along with their scores
			resultPageMap.put(p, resultScoreMap);

			return resultPageMap;

		} catch (Exception e) {
			logger.warn("Error during search. Skipped: {}", e.getMessage());
			return new HashMap<>();
		}
	}
}