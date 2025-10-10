package it.eng.dome.search.service;

import it.eng.dome.search.service.dto.SearchRequest;
import it.eng.dome.tmforum.tmf620.v4.model.ProductOffering;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class BrowsingProcessor {

    private static final Logger logger = LoggerFactory.getLogger(BrowsingProcessor.class);

//    @Autowired
//    private ElasticsearchOperations elasticsearchOperations;

    @Autowired
    TmfDataRetriever tmfDataRetriever;

    public BrowsingProcessor (ElasticsearchOperations elasticsearchOperations) {
//        this.elasticsearchOperations = elasticsearchOperations;
    }

    public Page<ProductOffering> getAllRandomizedProductOfferings (SearchRequest filter, Pageable pageable) {
        HashMap<String, String> queryParams = new HashMap<>();
        queryParams.put("lifecycleStatus", "Launched");

        List<ProductOffering> offerings = tmfDataRetriever.getAllPaginatedProductOfferings(null, queryParams);

        if (offerings == null || offerings.isEmpty()) {
            logger.info("No product offerings found with lifecycleStatus=Launched");
            return Page.empty(pageable); // return empty page
        }

        // filter product offerings with lifecycleStatus "launched"
//        List<ProductOffering> filteredOfferings = listProductOfferings.stream()
//                .filter(po -> "launched".equalsIgnoreCase(po.getLifecycleStatus()))
//                .collect(Collectors.toList());

        if (filter != null && filter.getCategories() != null && !filter.getCategories().isEmpty()) {
            logger.info("Applying category filter: {}", filter.getCategories());
            offerings = offerings.stream()
                    .filter(po -> po.getCategory() != null && po.getCategory().stream()
                            .anyMatch(cat -> {
                                String catId = cat.getId();
                                String catName = cat.getName();
                                return (catId != null && filter.getCategories().contains(catId))
                                        || (catName != null && filter.getCategories().contains(catName));
                            }))
                    .collect(Collectors.toList());
        }

        if (offerings.isEmpty()) {
            logger.info("No offerings matched the category filter");
            return Page.empty(pageable);
        }

        // random order
        Collections.shuffle(offerings);
        logger.info("Total ProductOfferings after filtering: {}", offerings.size());

        // pagination
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), offerings.size());
        if (start >= end) {
            return Page.empty(pageable);
        }
        List<ProductOffering> paginatedList = offerings.subList(start, end);

        return new PageImpl<>(paginatedList, pageable, offerings.size());
    }
}