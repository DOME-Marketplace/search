package it.eng.dome.search.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.Nested;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import it.eng.dome.search.domain.ProviderIndex;
import it.eng.dome.search.service.dto.OrganizationSearchRequest;

@Service
public class ProviderProcessor {

	@Autowired
	private ElasticsearchOperations elasticsearchOperations;

	@Autowired
	private DomeCatalogService domeCatalogService;

	public ProviderProcessor(ElasticsearchOperations elasticsearchOperations, DomeCatalogService domeCatalogService) {
		this.elasticsearchOperations = elasticsearchOperations;
		this.domeCatalogService = domeCatalogService;
	}

	// --- search provider con considerAllOrgs (Gerarchico: AND tra root, OR tra foglie)---
	public Page<ProviderIndex> searchProvider(OrganizationSearchRequest request, boolean considerAllOrgs, Pageable pageable) {
		try {
			BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

			// Filter by categories (nested)
			// 1. FILTRO CATEGORIE (Gerarchico: AND tra root, OR tra foglie)
			if (request.getCategories() != null && !request.getCategories().isEmpty()) {
				Map<String, List<String>> groupedRequest = domeCatalogService.groupCategoriesByRoot(request.getCategories());
				
				if (groupedRequest == null || groupedRequest.isEmpty()) {
					// Categorie inesistenti: forziamo il fallimento della query
					boolQuery.filter(QueryBuilders.termQuery("categories.name", "FORCE_EMPTY_RESULT_NO_MATCH_STR_TO_ZERO_RESULTS"));
				} else {
					for (Map.Entry<String, List<String>> entry : groupedRequest.entrySet()) {
						List<String> catValues = entry.getValue();
						BoolQueryBuilder groupOrQuery = QueryBuilders.boolQuery();
						for (String value : catValues) {
							groupOrQuery.should(QueryBuilders.termQuery("categories.name", value));
						}
						boolQuery.filter(
							QueryBuilders.nestedQuery("categories", groupOrQuery, ScoreMode.None)
						);
					}
				}
			}

			// Filter by countries
			if (request.getCountries() != null && !request.getCountries().isEmpty()) {
				boolQuery.filter(QueryBuilders.termsQuery("country", request.getCountries()));
			}

			// Filter by complianceLevels
			if (request.getComplianceLevels() != null && !request.getComplianceLevels().isEmpty()) {
				List<String> mappedLevels = request.getComplianceLevels().stream()
						.map(level -> {
							switch (level.trim()) {
								case "Baseline": return "BL";
								case "Professional": return "P";
								case "Professional+": return "PP";
								default: return level;
							}
						})
						.collect(Collectors.toList());
				boolQuery.filter(QueryBuilders.termsQuery("complianceLevels", mappedLevels));
			}

			// --- filtro per organizzazioni con offering solo se considerAllOrgs = false ---
			if (!considerAllOrgs) {
				boolQuery.filter(QueryBuilders.rangeQuery("publishedOfferingsCount").gte(1));
			}

			//execution
			NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder()
					.withQuery(boolQuery)
					.withPageable(pageable);

			SearchHits<ProviderIndex> searchHits = elasticsearchOperations.search(queryBuilder.build(), ProviderIndex.class);

			List<ProviderIndex> results = searchHits.stream()
					.map(hit -> hit.getContent())
					.collect(Collectors.toList());

			return new PageImpl<>(results, pageable, searchHits.getTotalHits());

		} catch (Exception e) {
			e.printStackTrace();
			return new PageImpl<>(new ArrayList<>());
		}
	}

	// --- Aggregazioni con considerAllOrgs ---
	public List<String> getAllCountries(boolean considerAllOrgs) {
		return getAggregatedField("country", considerAllOrgs);
	}

	public List<String> getAllComplianceLevels(boolean considerAllOrgs) {
		return getAggregatedField("complianceLevels", considerAllOrgs);
	}

	public List<String> getAllCategories(boolean considerAllOrgs) {
		// Nested aggregation
		NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder().withMaxResults(0);

		if (!considerAllOrgs) {
			builder.withQuery(QueryBuilders.rangeQuery("publishedOfferingsCount").gte(1));
		}

		builder.withAggregations(
				AggregationBuilders.nested("nested_categories", "categories")
						.subAggregation(
								AggregationBuilders.terms("names")
										.field("categories.name")
										.size(1000)
						)
		);

		SearchHits<ProviderIndex> searchHits = elasticsearchOperations.search(builder.build(), ProviderIndex.class);
		if (searchHits.getAggregations() == null) return new ArrayList<>();

		org.elasticsearch.search.aggregations.Aggregations aggregations =
				(org.elasticsearch.search.aggregations.Aggregations) searchHits.getAggregations().aggregations();

		Nested nested = aggregations.get("nested_categories");
		Terms terms = nested.getAggregations().get("names");

		return terms.getBuckets().stream().map(Terms.Bucket::getKeyAsString).collect(Collectors.toList());
	}

	// --- helper per aggregazioni semplici ---
	private List<String> getAggregatedField(String fieldName, boolean considerAllOrgs) {
		NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder().withMaxResults(0);

		if (!considerAllOrgs) {
			builder.withQuery(QueryBuilders.rangeQuery("publishedOfferingsCount").gte(1));
		}

		builder.withAggregations(
				AggregationBuilders.terms("agg_" + fieldName)
						.field(fieldName)
						.size(1000)
		);

		SearchHits<ProviderIndex> searchHits = elasticsearchOperations.search(builder.build(), ProviderIndex.class);
		if (searchHits.getAggregations() == null) return new ArrayList<>();

		org.elasticsearch.search.aggregations.Aggregations aggregations =
				(org.elasticsearch.search.aggregations.Aggregations) searchHits.getAggregations().aggregations();

		Terms terms = aggregations.get("agg_" + fieldName);

		return terms.getBuckets().stream().map(Terms.Bucket::getKeyAsString).collect(Collectors.toList());
	}
}