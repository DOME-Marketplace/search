package it.eng.dome.search.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class CatalogResponse {

    private List<Category> category;

    @Setter
    @Getter
    public static class Category {
        private String id;
        private String name;

    }
}
