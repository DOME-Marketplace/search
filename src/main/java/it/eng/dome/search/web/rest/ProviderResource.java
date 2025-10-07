package it.eng.dome.search.web.rest;

import it.eng.dome.search.rest.web.util.PaginationUtil;
import it.eng.dome.search.service.ProviderService;
import it.eng.dome.search.service.dto.SearchRequest;
import it.eng.dome.tmforum.tmf632.v4.model.Organization;
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
public class ProviderResource {

    @Autowired
    private ProviderService providerService;

    @PostMapping("/providersByCategories")
    public ResponseEntity<List<Organization>> getProvidersByCategories(@RequestBody(required = false) SearchRequest filterRequest, Pageable pageable) {
        Page<Organization> providersPage = providerService.getProvidersByCategories(filterRequest, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(providersPage, "/api/providersByCategories");
        return new ResponseEntity<>(providersPage.getContent(), headers, HttpStatus.OK);
    }
}

