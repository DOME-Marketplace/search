package it.eng.dome.search.service;

import it.eng.dome.search.service.dto.CatalogResponse;
import it.eng.dome.search.service.dto.DomeConfigResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DomeCatalogService {

    private final RestTemplate restTemplate;

    @Value("${dome.catalog.base-url}")
    private String baseUrl;

    @Value("${dome.catalog.catalog-path}")
    private String catalogPath;

    @Value("${dome.catalog.config-path}")
    private String configPath;

    public DomeCatalogService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * retrieve defaultId from config endpoint DOME
     */
    public String getCatalogSource() {
        String url = buildUrl(configPath);
        try {
            DomeConfigResponse response = restTemplate.getForObject(url, DomeConfigResponse.class);

            if (response == null || response.getDefaultId() == null) {
                throw new IllegalStateException("defaultId not found in config DOME");
            }

            return response.getDefaultId();
        } catch (RestClientException e) {
            throw new IllegalStateException("Error calling config DOME: " + e.getMessage(), e);
        }
    }

    /**
     * retrieve categories from catalog endpoint DOME using defaultId from config endpoint
     */
    public List<String> getCatalogCategories() {
        String defaultId = getCatalogSource();
        String url = buildUrl(catalogPath, defaultId);

        try {
            CatalogResponse response = restTemplate.getForObject(url, CatalogResponse.class);

            if (response == null || response.getCategory() == null) {
                throw new IllegalStateException("Categories not found for catalogId: " + defaultId);
            }

            return response.getCategory()
                    .stream()
                    .map(CatalogResponse.Category::getName)
                    .collect(Collectors.toList());

        } catch (RestClientException e) {
            throw new IllegalStateException("Error calling catalog DOME: " + e.getMessage(), e);
        }
    }

    /**
     * Build full URL combining baseUrl + path segments
     */
    private String buildUrl(String... paths) {
        StringBuilder sb = new StringBuilder(baseUrl.replaceAll("/$", ""));
        for (String path : paths) {
            sb.append(path.startsWith("/") ? path : "/" + path);
        }
        return sb.toString();
    }
}