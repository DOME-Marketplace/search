package it.eng.dome.search.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
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
				e.getMessage()); return new PageImpl<>(new ArrayList<>()); } }



	//search request con filtraggio su nome categorie
	public Page<IndexingObject> search(String q, SearchRequest request, Pageable pageable){

		// Main query
		QueryBuilder queryBuilder = QueryBuilders.queryStringQuery(q);

		// Create a BoolQueryBuilder to combine the main query and the category filter
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().must(queryBuilder);

		// Add category filter if categories are specified in the searchRequest
		if (request.getCategories() != null && ! request.getCategories().isEmpty()) {
			logger.error("Adding category filter for categories: {}", request.getCategories());
			//boolQueryBuilder.filter(QueryBuilders.termsQuery("categories.name", request.getCategories()));
			TermsQueryBuilder termsQueryBuilder = QueryBuilders.termsQuery("categories.name", request.getCategories());
			boolQueryBuilder.filter(termsQueryBuilder);
		} else {
			logger.debug("No categories specified for filtering.");
		}

		//		NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder().withQuery(boolQueryBuilder).withPageable(pageable);
		//
		//		Query elasticQuery = nativeSearchQueryBuilder.build();

		// Build the search query using SearchSourceBuilder
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(boolQueryBuilder);
		searchSourceBuilder.from((int) pageable.getOffset());
		searchSourceBuilder.size(pageable.getPageSize());

		// Create the NativeSearchQuery
		Query elasticQuery = new NativeSearchQuery(queryBuilder);

		try { 
			SearchHits<IndexingObject> searchHits = elasticsearchOperations.search(elasticQuery, IndexingObject.class);
			List<IndexingObject> resultPage = searchHits.stream().map(SearchHit::getContent).collect(Collectors.toList());
			return new PageImpl<>(resultPage,pageable,searchHits.getTotalHits()); 

		} catch (Exception e) { 
			logger.warn("Error during search. Skipped: {}", e.getMessage()); 
			return new PageImpl<>(new ArrayList<>()); 
		}

	}


	//filtra per categorie
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

}
