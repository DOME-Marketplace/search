package it.eng.dome.search.service;

import it.eng.dome.search.domain.IndexingObject;
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

    private static final Logger logger = LoggerFactory.getLogger(BrowsingProcessor.class);

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

}