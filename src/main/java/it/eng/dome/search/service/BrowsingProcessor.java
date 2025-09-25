package it.eng.dome.search.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.eng.dome.brokerage.api.ProductOfferingApis;
import it.eng.dome.search.rest.web.util.RestUtil;
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

    private static final Logger logger = LoggerFactory.getLogger(BrowsingProcessor.class);

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    //    @Autowired
//    private RestUtil restUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    TmfApiFactory tmfApiFactory;

    ProductOfferingApis productOfferingApis;

    @Override
    public void afterPropertiesSet () throws Exception {
        this.productOfferingApis = new ProductOfferingApis(tmfApiFactory.getTMF620ProductCatalogApiClient());

        logger.info("BrowsingProcessor initialized with ProductOfferingApis");
    }

    public BrowsingProcessor (ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    public Page<ProductOffering> getAllRandomizedProductOfferings (SearchRequest filter, Pageable pageable) {
//        String responseJson = restUtil.getAllProductOfferingsPaginated();

//        HashMap<String, String> queryParams = new HashMap<>();
//        queryParams.put("lifecycleStatus", "Launched");
        //TODO implements query params for launched status directly in the API call
        List<ProductOffering> listProductOfferings = productOfferingApis.getAllProductOfferings(null, null);

        if (listProductOfferings == null) {
            return Page.empty(pageable); // return empty page
        }

        // JSON to ProductOffering list
/*            List<ProductOffering> productOfferings = objectMapper.readValue(
                responseJson,
                objectMapper.getTypeFactory().constructCollectionType(List.class, ProductOffering.class)
        );*/

        // filter product offerings with lifecycleStatus "launched"
        List<ProductOffering> filteredOfferings = listProductOfferings.stream()
                .filter(po -> "launched".equalsIgnoreCase(po.getLifecycleStatus()))
                .collect(Collectors.toList());

        //TODO: fix beacause categories are not filtered correctly
        // if request body contains categories, apply the filter
        if (filter != null && filter.getCategories() != null && !filter.getCategories().isEmpty()) {
            logger.info("Adding category filter for categories: {}", filter.getCategories());
            filteredOfferings = filteredOfferings.stream()
                    .filter(po -> po.getCategory() != null &&
                            po.getCategory().stream()
                                    .anyMatch(cat -> (cat.getId() != null && filter.getCategories().contains(cat.getId()))
                                            || (cat.getName() != null && filter.getCategories().contains(cat.getName()))))
                    .collect(Collectors.toList());
        }

        // random order
        Collections.shuffle(filteredOfferings);

        // pagination
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredOfferings.size());
        List<ProductOffering> paginatedList = filteredOfferings.subList(start, end);

        return new PageImpl<>(paginatedList, pageable, filteredOfferings.size());
    }
}