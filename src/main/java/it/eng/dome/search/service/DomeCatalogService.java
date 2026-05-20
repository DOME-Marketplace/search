package it.eng.dome.search.service;

import it.eng.dome.search.service.dto.CatalogCategoryDTO;
import it.eng.dome.search.service.dto.CatalogResponse;
import it.eng.dome.search.service.dto.DomeConfigResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Value("${dome.catalog.categories-path}")
    private String categoriesPath;

    public DomeCatalogService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * retrieve categories from catalog endpoint DOME using defaultId from config
     * endpointm - old (before 1.4.0)
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
                    .map(CatalogCategoryDTO::getName)
                    .collect(Collectors.toList());

        } catch (RestClientException e) {
            throw new IllegalStateException("Error calling catalog DOME: " + e.getMessage(), e);
        }
    }

    public Map<String, Object> getCategoryHierarchy() {
        Map<String, Object> finalHierarchy = new HashMap<>();

        // 1. Recupera il catalogo principale (Main Categories)
        CatalogResponse mainCatalog = getFullCatalog();

        for (CatalogCategoryDTO mainCat : mainCatalog.getCategory()) {
            String name = mainCat.getName();

            // 2. Recupera i figli di 1° livello (es. SaaS per Delivery Model o Security per
            // DOME Categories)
            List<CatalogCategoryDTO> children = getChildrenByParentId(mainCat.getId());

            if ("DOME Categories".equalsIgnoreCase(name)) {
                // Struttura Nidificata: Map<String, List<String>>
                Map<String, List<String>> domeSubMap = new HashMap<>();

                for (CatalogCategoryDTO subCat : children) {
                    // 3. Recupera i figli di 2° livello (es. figli di Security)
                    List<String> level2Names = getChildrenByParentId(subCat.getId()).stream()
                            .map(CatalogCategoryDTO::getName)
                            .collect(Collectors.toList());

                    domeSubMap.put(subCat.getName(), level2Names);
                }
                finalHierarchy.put(name, domeSubMap);

            } else {
                // Struttura Piatta: List<String>
                List<String> level1Names = children.stream()
                        .map(CatalogCategoryDTO::getName)
                        .collect(Collectors.toList());

                finalHierarchy.put(name, level1Names);
            }
        }
        return finalHierarchy;
    }

    /**
     * Recupera le categorie figlie filtrando solo quelle in stato "Launched".
     * Gestisce il JSON di tipo ARRAY [...]
     */
    private List<CatalogCategoryDTO> getChildrenByParentId(String parentId) {
        String url = buildUrl(categoriesPath) + "?parentId=" + parentId;
        try {
            // Mappatura diretta da array JSON a Array Java
            CatalogCategoryDTO[] response = restTemplate.getForObject(url, CatalogCategoryDTO[].class);

            if (response == null)
                return Collections.emptyList();

            return Arrays.stream(response)
                    .filter(cat -> "Launched".equalsIgnoreCase(cat.getLifecycleStatus()))
                    .collect(Collectors.toList());
        } catch (RestClientException e) {
            // Loggare l'errore se necessario
            return Collections.emptyList();
        }
    }

    /**
     * Restituisce i nomi delle sottocategorie dirette di "DOME Categories"
     * Esempio: ["Security", "Infrastructure", "DevOps", ...]
     */
    public List<String> getDomeMainSubCategories() {
        Map<String, Object> hierarchy = getCategoryHierarchy();
        Object domeData = hierarchy.get("DOME Categories");

        if (domeData instanceof Map) {
            return new ArrayList<>(((Map<String, List<String>>) domeData).keySet());
        }

        return Collections.emptyList();
    }

    public List<String> getOnlyDomeLeafCategories() {
        Map<String, Object> hierarchy = getCategoryHierarchy();
        Object domeContent = hierarchy.get("DOME Categories");

        if (domeContent instanceof Map) {
            Map<String, List<String>> subMap = (Map<String, List<String>>) domeContent;
            return subMap.values().stream()
                    .flatMap(List::stream)
                    .distinct()
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    /**
     * Returns the internal hierarchy of a specific main category (e.g.
     * "Sector" or "DOME Categories")
     * The return is Object because it can be List<String> or Map<String,
     * List<String>>
     * List<String>>
     */
    public Object getHierarchyByMainCategory(String mainCategoryName) {
        Map<String, Object> fullHierarchy = getCategoryHierarchy();

        // Search for the key ignoring case for flexibility
        return fullHierarchy.entrySet().stream()
                .filter(entry -> entry.getKey().equalsIgnoreCase(mainCategoryName))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElseThrow(
                        () -> new IllegalArgumentException("Categoria non trovata nel catalogo: " + mainCategoryName));
    }

    /**
     * retrieve full catalog (DTO object) to access ID and names
     */
    public CatalogResponse getFullCatalog() {
        String defaultId = getCatalogSource();
        String url = buildUrl(catalogPath, defaultId);

        try {
            CatalogResponse response = restTemplate.getForObject(url, CatalogResponse.class);
            if (response == null || response.getCategory() == null) {
                throw new IllegalStateException("Catalog or categories not found for catalogId: " + defaultId);
            }
            return response;
        } catch (RestClientException e) {
            throw new IllegalStateException("Error calling catalog DOME: " + e.getMessage(), e);
        }
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
     * Build full URL combining baseUrl + path segments
     */
    private String buildUrl(String... paths) {
        StringBuilder sb = new StringBuilder(baseUrl.replaceAll("/$", ""));
        for (String path : paths) {
            sb.append(path.startsWith("/") ? path : "/" + path);
        }
        return sb.toString();
    }

    /**
     * Prende una lista piatta di categorie e le raggruppa per Radice (Sector, DOME
     * Categories, etc.)
     * basandosi sulla gerarchia attuale del catalogo.
     */
    public Map<String, List<String>> groupCategoriesByRoot(List<String> requestedCats) {
        // Recuperiamo la gerarchia internamente
        Map<String, Object> hierarchy = this.getCategoryHierarchy();
        Map<String, List<String>> grouped = new HashMap<>();

        if (requestedCats == null || requestedCats.isEmpty()) {
            return grouped;
        }

        for (String cat : requestedCats) {
            for (Map.Entry<String, Object> entry : hierarchy.entrySet()) {
                String root = entry.getKey();
                Object content = entry.getValue();

                boolean found = false;
                if (content instanceof List) {
                    if (((List<String>) content).contains(cat))
                        found = true;
                } else if (content instanceof Map) {
                    Map<String, List<String>> subMap = (Map<String, List<String>>) content;
                    found = subMap.values().stream().anyMatch(list -> list.contains(cat));
                }

                if (found) {
                    grouped.computeIfAbsent(root, k -> new ArrayList<>()).add(cat);
                    break;
                }
            }
        }
        return grouped;
    }
}