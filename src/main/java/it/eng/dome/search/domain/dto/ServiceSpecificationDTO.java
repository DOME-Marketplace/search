package it.eng.dome.search.domain.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceSpecificationDTO {

    protected String id;
    private String href;
    private String description;
    private Boolean isBundle;
    private String lastUpdate;
    private String lifecycleStatus;
    private String name;
    private String version;
    //private Attachment[] attachment;
    //private ConstraintRef[] constraint;
    //private EntitySpecRelationship[] entitySpecRelationship;
    //private FeatureSpecification featureSpecification;
//    private List<RelatedParty> relatedParty;
    //private ResourceSpecificationRef[] resourceSpecification;
    //private ServiceLevelSpecificationRef[] serviceLevelSpecification;
    //private ServiceSpecRelationship serviceSpecRelationship;
//    private List<CharacteristicSpecification> specCharacteristic; //CharacteristicSpecification
    //private TargetEntitySchema targetEntitySchema;
    //private ValidFor validFor;
}
