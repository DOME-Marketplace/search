package it.eng.dome.search.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductSpecificationDTO {

    protected String id;
//    private String href;
    private String brand;
    private String description;
    private Boolean isBundle;
    private String lastUpdate;
    private String lifecycleStatus;
    private String name;
    private String productNumber;
    private String version;
//    private Attachment[] attachment;
    //private BundledProductSpecification[] bundledProductSpecification;
    @Field(type = FieldType.Nested)
    private List<ProductSpecCharacteristicDTO> productSpecCharacteristic;
//    private ProductSpecificationRelationship[] productSpecificationRelationship;
    private List<RelatedPartyDTO> relatedParty;
//    private ResourceSpecification[] resourceSpecification;
//    private ServiceSpecification[] serviceSpecification;
    //private TargetProductSchema[] targetProductSchema;
//    private ValidFor validFor;

}
