package it.eng.dome.search.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.eng.dome.search.domain.IndexingObject;
import it.eng.dome.search.domain.ProductOffering;
import it.eng.dome.search.rest.web.util.RestUtil;
import it.eng.dome.search.service.dto.SearchRequest;
import org.elasticsearch.index.query.*;
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

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BrowsingProcessor {

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Autowired
    private RestUtil restUtil;

    private static final Logger logger = LoggerFactory.getLogger(BrowsingProcessor.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    public BrowsingProcessor (ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    public Page<IndexingObject> getAllRandomizedProducts (Pageable pageable) {

        // build query with all indexed product
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();

        // Add a filter to include only products with status "launched"
        TermQueryBuilder termQueryBuilderStatus = QueryBuilders.termQuery("productOfferingLifecycleStatus", "launched");
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().filter(termQueryBuilderStatus);

        // Build the Elasticsearch query
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withPageable(pageable);

        Query elasticQuery = nativeSearchQueryBuilder.build();

        try {
            // Execute the search query
            SearchHits<IndexingObject> searchHits = elasticsearchOperations.search(elasticQuery, IndexingObject.class);

            // Convert search results into a list of IndexingObjects
            List<IndexingObject> resultPage = searchHits.stream()
                    .map(SearchHit::getContent)
                    .collect(Collectors.toList());

            // randomize order
            Collections.shuffle(resultPage);

            // pagination
            Page<IndexingObject> pagedResult = new PageImpl<>(resultPage, pageable, searchHits.getTotalHits());

            return pagedResult;

        } catch (Exception e) {
            logger.warn("Errore durante il recupero dei prodotti indicizzati: {}", e.getMessage());
            return Page.empty(pageable); // Restituire una pagina vuota in caso di errore
        }
    }

    public Page<ProductOffering> getAllRandomizedProductOfferings(Pageable pageable) {

        String responseJson = restUtil.getAllProductOfferings();

        if (responseJson == null) {
            return Page.empty(pageable); // return empty page
        }

        try {
            // JSON to ProductOffering list
            List<ProductOffering> productOfferings = objectMapper.readValue(
                    responseJson,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, ProductOffering.class)
            );

            // filter ProductOffering with lifecycleStatus = "launched"**
            List<ProductOffering> launchedProducts = productOfferings.stream()
                    .filter(product -> "launched".equalsIgnoreCase(product.getLifecycleStatus()))
                    .collect(Collectors.toList());

            // random order
            Collections.shuffle(launchedProducts);

            // pagination
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), launchedProducts.size());

            List<ProductOffering> paginatedList = launchedProducts.subList(start, end);

            return new PageImpl<>(paginatedList, pageable, launchedProducts.size());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Page.empty(pageable); // if there is an errore, return empty page
        }
    }

    public Page<ProductOffering> getAllRandomizedProductOfferings(SearchRequest filter, Pageable pageable) {
        String responseJson = restUtil.getAllProductOfferingsPaginated();

        if (responseJson == null) {
            return Page.empty(pageable); // return empty page
        }

        try {
            // JSON to ProductOffering list
            List<ProductOffering> productOfferings = objectMapper.readValue(
                    responseJson,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, ProductOffering.class)
            );

            // filter product offerings with lifecycleStatus "launched"
            List<ProductOffering> filteredOfferings = productOfferings.stream()
                    .filter(po -> "launched".equalsIgnoreCase(po.getLifecycleStatus()))
                    .collect(Collectors.toList());

            // if request body contains categories, apply the filter
            if (filter != null && filter.getCategories() != null && !filter.getCategories().isEmpty()) {
                logger.info("Adding category filter for categories: {}", filter.getCategories());
                filteredOfferings = filteredOfferings.stream()
                        .filter(po -> filter.getCategories().contains(po.getCategory())) // Supponiamo che esista getCategory()
                        .collect(Collectors.toList());
            }

            // random order
            Collections.shuffle(filteredOfferings);

            // pagination
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), filteredOfferings.size());
            List<ProductOffering> paginatedList = filteredOfferings.subList(start, end);

            return new PageImpl<>(paginatedList, pageable, filteredOfferings.size());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Page.empty(pageable); // if there is an errore, return empty page
        }
    }



}