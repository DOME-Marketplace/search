package it.eng.dome.search.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CatalogCategoryDTO {
    private String id;
    private String name;
    private String parentId;
    private String lifecycleStatus;
}
