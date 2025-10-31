package it.eng.dome.search.web.rest;

import it.eng.dome.search.rest.web.util.PaginationUtil;
import it.eng.dome.search.service.ProviderService;
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
    private ProviderService providerService;

    @PostMapping("/searchOrganizations")
    public ResponseEntity<List<Organization>> searchOrganizations(@RequestBody OrganizationSearchRequest request, Pageable pageable) {
        Page<Organization> results = providerService.filterOrganizations(request, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(results, "/api/searchOrganizations");
        return new ResponseEntity<>(results.getContent(), headers, HttpStatus.OK);
    }

    // --- GET /categories ---
    @GetMapping("/categories")
    public ResponseEntity<List<String>> getCategories() {
        List<String> categories = providerService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    // --- GET /countries ---
    @GetMapping("/countries")
    public ResponseEntity<List<String>> getCountries() {
        List<String> countries = providerService.getAllCountries();
        return ResponseEntity.ok(countries);
    }

    // --- GET /countries ---
    @GetMapping("/complianceLevels")
    public ResponseEntity<List<String>> getComplianceLevels() {
        List<String> complianceLevels = providerService.getAllComplianceLevels();
        return ResponseEntity.ok(complianceLevels);
    }
}