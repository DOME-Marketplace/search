package it.eng.dome.search.service;

import it.eng.dome.brokerage.api.ProductOfferingApis;
import it.eng.dome.search.service.dto.SearchRequest;
import it.eng.dome.search.tmf.TmfApiFactory;
import it.eng.dome.tmforum.tmf620.v4.model.ProductOffering;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BrowsingProcessor implements InitializingBean {

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    private static final Logger logger = LoggerFactory.getLogger(BrowsingProcessor.class);

//    private final ObjectMapper objectMapper = new ObjectMapper();

    // Factory for TMF APIss
    @Autowired
    private TmfApiFactory tmfApiFactory;

    private ProductOfferingApis productOfferingApis;

    @Override
    public void afterPropertiesSet () throws Exception {
        this.productOfferingApis = new ProductOfferingApis(tmfApiFactory.getTMF620ProductCatalogApiClient());

        logger.info("BrowsingProcessor initialized with productOfferingApis");
    }

    public BrowsingProcessor (ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    public Page<ProductOffering> getAllRandomizedProductOfferings(SearchRequest filter, Pageable pageable) {
        //String responseJson = restUtil.getAllProductOfferingsPaginated();

        //TODO: check if filter on payload is the same of tmf
        HashMap <String, String> queryParams = new HashMap<>();
        queryParams.put("lifecycleStatus", "launched");
        //TODO: filter on categories - undestand
        List<ProductOffering> productOfferings = productOfferingApis.getAllProductOfferings(null, queryParams);

        if (productOfferings == null) {
            return Page.empty(pageable); // return empty page
        }

        //try {
            // JSON to ProductOffering list
//            List<ProductOffering> productOfferings = objectMapper.readValue(responseJson, objectMapper.getTypeFactory().constructCollectionType(List.class, ProductOffering.class)
//            );

//            // filter product offerings with lifecycleStatus "launched"
//            List<ProductOffering> filteredOfferings = productOfferings.stream()
//                    .filter(po -> "launched".equalsIgnoreCase(po.getLifecycleStatus()))
//                    .collect(Collectors.toList());

            // if request body contains categories, apply the filter
            // TODO: check if it work properly
            if (filter != null && filter.getCategories() != null && !filter.getCategories().isEmpty()) {
                logger.info("Adding category filter for categories: {}", filter.getCategories());
                productOfferings = productOfferings.stream()
                        .filter(po -> po.getCategory() != null &&
                                    po.getCategory().stream()
                                            .anyMatch(cat -> (cat.getId() != null && filter.getCategories().contains(cat.getId()))
                                                        || (cat.getName() != null && filter.getCategories().contains(cat.getName()))))
                        .collect(Collectors.toList());
            }

            // random order
            Collections.shuffle(productOfferings);

            // pagination
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), productOfferings.size());
            List<ProductOffering> paginatedList = productOfferings.subList(start, end);

            return new PageImpl<>(paginatedList, pageable, productOfferings.size());
        //}
    }

    //    public Page<IndexingObject> getAllRandomizedProducts (Pageable pageable) {
//
//        // build query with all indexed product
//        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
//
//        // Add a filter to include only products with status "launched"
//        TermQueryBuilder termQueryBuilderStatus = QueryBuilders.termQuery("productOfferingLifecycleStatus", "launched");
//        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery().filter(termQueryBuilderStatus);
//
//        // Build the Elasticsearch query
//        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder()
//                .withQuery(boolQueryBuilder)
//                .withPageable(pageable);
//
//        Query elasticQuery = nativeSearchQueryBuilder.build();
//
//        try {
//            // Execute the search query
//            SearchHits<IndexingObject> searchHits = elasticsearchOperations.search(elasticQuery, IndexingObject.class);
//
//            // Convert search results into a list of IndexingObjects
//            List<IndexingObject> resultPage = searchHits.stream()
//                    .map(SearchHit::getContent)
//                    .collect(Collectors.toList());
//
//            // randomize order
//            Collections.shuffle(resultPage);
//
//            // pagination
//            Page<IndexingObject> pagedResult = new PageImpl<>(resultPage, pageable, searchHits.getTotalHits());
//
//            return pagedResult;
//
//        } catch (Exception e) {
//            logger.warn("Errore durante il recupero dei prodotti indicizzati: {}", e.getMessage());
//            return Page.empty(pageable); // Restituire una pagina vuota in caso di errore
//        }
//    }
//
//    public Page<ProductOffering> getAllRandomizedProductOfferings(Pageable pageable) {
//
//        String responseJson = restUtil.getAllProductOfferingsPaginated();
//
//        if (responseJson == null) {
//            return Page.empty(pageable); // return empty page
//        }
//
//        try {
//            // JSON to ProductOffering list
//            List<ProductOffering> productOfferings = objectMapper.readValue(
//                    responseJson,
//                    objectMapper.getTypeFactory().constructCollectionType(List.class, ProductOffering.class)
//            );
//
//            // filter ProductOffering with lifecycleStatus = "launched"**
//            List<ProductOffering> launchedProducts = productOfferings.stream()
//                    .filter(product -> "launched".equalsIgnoreCase(product.getLifecycleStatus()))
//                    .collect(Collectors.toList());
//
//            // random order
//            Collections.shuffle(launchedProducts);
//
//            // pagination
//            int start = (int) pageable.getOffset();
//            int end = Math.min((start + pageable.getPageSize()), launchedProducts.size());
//
//            List<ProductOffering> paginatedList = launchedProducts.subList(start, end);
//
//            return new PageImpl<>(paginatedList, pageable, launchedProducts.size());
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            return Page.empty(pageable); // if there is an errore, return empty page
//        }
//    }
}