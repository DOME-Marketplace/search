package it.eng.dome.search.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductSpecCharacteristicDTO {

    protected String id;
//    private Boolean configurable;
    private String name;
//    private String valueType;
//    private String description;
    @Field(type = FieldType.Nested)
    private ProductSpecCharacteristicValueDTO[] productSpecCharacteristicValue;
//    private ValidForDTO validFor;
    //	private Boolean extensible;
    //	private Boolean isUnique;
    //	private int maxCardinality;
    //	private int minCardinality;
    //	private String regex;
    //	private ProductSpecCharacteristicRelationship[] productSpecCharRelationship; (eventualmente, creare classe ProductSpecCharacteristicRelationship)
}