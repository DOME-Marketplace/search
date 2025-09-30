package it.eng.dome.search.web.rest;

import it.eng.dome.search.service.ProviderService;
import it.eng.dome.tmforum.tmf632.v4.model.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProviderController {

    @Autowired
    private ProviderService providerService;

    @GetMapping("/providersByCategories")
    public ResponseEntity<List<Organization>> getProvidersByCategories(@RequestParam List<String> categoryIds) {
        List<Organization> providers = providerService.getProvidersByCategories(categoryIds);
        return ResponseEntity.ok(providers);
    }
}

