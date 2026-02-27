package it.eng.dome.search.web.rest;

import it.eng.dome.search.domain.ProviderIndex;
import it.eng.dome.search.rest.web.util.PaginationUtil;
import it.eng.dome.search.service.ProviderIndexingService;
import it.eng.dome.search.service.ProviderProcessor;
import it.eng.dome.search.service.ResultProcessor;
import it.eng.dome.search.service.dto.OrganizationSearchRequest;
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
    private ProviderProcessor providerProcessor;

    @Autowired
    private ResultProcessor resultProcessor;

    @Autowired
    private ProviderIndexingService providerIndexingService;

    // --- GET /categories ---
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getCategories(@RequestParam(name = "considerAllOrgs", required = false, defaultValue = "false") boolean considerAllOrgs) {
        List<String> categories = providerProcessor.getAllCategories(considerAllOrgs);
        return ResponseEntity.ok(categories);
    }

    // --- GET /countries ---
    @GetMapping("/countries")
    public ResponseEntity<List<String>> getCountries(@RequestParam(name = "considerAllOrgs", required = false, defaultValue = "false") boolean considerAllOrgs) {
        List<String> countries = providerProcessor.getAllCountries(considerAllOrgs);
        return ResponseEntity.ok(countries);
    }

    // --- GET /countries ---
    @GetMapping("/complianceLevels")
    public ResponseEntity<List<String>> getComplianceLevels(@RequestParam(name = "considerAllOrgs", required = false, defaultValue = "false") boolean considerAllOrgs) {
        List<String> complianceLevels = providerProcessor.getAllComplianceLevels(considerAllOrgs);
        return ResponseEntity.ok(complianceLevels);
    }

    @PostMapping(value = "/SearchOrganizations")
    public ResponseEntity<List<Organization>> searchOrganizations(@RequestBody OrganizationSearchRequest request,
                                                                  @RequestParam(name="considerAllOrgs", required = false, defaultValue = "false") boolean considerAllOrgs,
                                                                  Pageable pageable){
        Page<ProviderIndex> page = providerProcessor.searchProvider(request, considerAllOrgs, pageable);
        Page<Organization> pageProduct = resultProcessor.processProviderResults(page, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(pageProduct, "/api/searchOrganizations");
        return new ResponseEntity<>(pageProduct.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/organizations/clearRepository")
    public ResponseEntity<?> clearRepository() {
        providerIndexingService.clearRepository();
        return  ResponseEntity.noContent().build();
    }

}