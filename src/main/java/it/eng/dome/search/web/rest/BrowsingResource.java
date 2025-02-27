package it.eng.dome.search.web.rest;

import it.eng.dome.search.domain.IndexingObject;
import it.eng.dome.search.domain.ProductOffering;
import it.eng.dome.search.rest.web.util.PaginationUtil;
import it.eng.dome.search.service.BrowsingProcessor;
import it.eng.dome.search.service.ResultProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BrowsingResource {

    @Autowired
    private BrowsingProcessor browsingProcessor;

    @Autowired
    private ResultProcessor resultProcessor;

    // browse 1.0
    @GetMapping(value = "/RandomizedProducts")
    public ResponseEntity<List<ProductOffering>> getRandomizedProducts(Pageable pageable) {
        // gets all indexed products with random order
        Page<IndexingObject> page = browsingProcessor.getAllRandomizedProducts(pageable);
        // IndexingObject to ProductOffering
        Page<ProductOffering> pageProductOffering = resultProcessor.processBrowsingResults(page, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageProductOffering, "/api/RandomizedProducts");
        return new ResponseEntity<>(pageProductOffering.getContent(), headers, HttpStatus.OK);
    }
}