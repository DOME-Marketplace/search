package it.eng.dome.search.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {

    @Field(type = FieldType.Keyword)
    private String id;

//    private String href;

//    @Field(type = FieldType.Boolean)
//    private Boolean isRoot;

    private String lastUpdate;

    @Field(type = FieldType.Keyword)
    private String lifecycleStatus;

    @Field(type = FieldType.Keyword)
    private String name;

    private String validFor;
}

