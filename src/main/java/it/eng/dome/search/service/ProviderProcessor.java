package it.eng.dome.search.service;

import it.eng.dome.search.domain.ProviderIndex;
import it.eng.dome.search.service.dto.OrganizationSearchRequest;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProviderProcessor {

	@Autowired
	private ElasticsearchOperations elasticsearchOperations;

	public ProviderProcessor(ElasticsearchOperations elasticsearchOperations) {
		this.elasticsearchOperations = elasticsearchOperations;
	}

	// --- search provider con considerAllOrgs ---
	public Page<ProviderIndex> searchProvider(OrganizationSearchRequest request, boolean considerAllOrgs, Pageable pageable) {
		try {
			BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

			// Filter by categories (nested)
			if (request.getCategories() != null && !request.getCategories().isEmpty()) {
				for (String cat : request.getCategories()) {
					boolQuery.should(QueryBuilders.nestedQuery(
							"categories",
							QueryBuilders.termQuery("categories.name", cat),
							ScoreMode.None
					));
				}
				boolQuery.minimumShouldMatch(1);
			}

			// Filter by countries
			if (request.getCountries() != null && !request.getCountries().isEmpty()) {
				boolQuery.filter(QueryBuilders.termsQuery("country", request.getCountries()));
			}

			// Filter by complianceLevels
			if (request.getComplianceLevels() != null && !request.getComplianceLevels().isEmpty()) {
				boolQuery.filter(QueryBuilders.termsQuery("complianceLevels", request.getComplianceLevels()));
			}

			// --- filtro per organizzazioni con offering solo se considerAllOrgs = false ---
			if (!considerAllOrgs) {
				boolQuery.filter(QueryBuilders.rangeQuery("publishedOfferingsCount").gte(1));
			}

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
		return getAggregatedField("country", considerAllOrgs, "publishedOfferingsCount");
	}

	public List<String> getAllComplianceLevels(boolean considerAllOrgs) {
		return getAggregatedField("complianceLevels", considerAllOrgs, "publishedOfferingsCount");
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
	private List<String> getAggregatedField(String fieldName, boolean considerAllOrgs, String countField) {
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