package it.eng.dome.search.domain.dto;

import it.eng.dome.tmforum.tmf634.v4.model.RelatedParty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceSpecificationDTO {

    protected String id;
    private String href;
    //private String category;
    private String description;
    private Boolean isBundle;
    private String lastUpdate;
    private String lifecycleStatus;
    private String name;
    private String version;
    private List<RelatedParty> relatedParty;
//    private List<ResourceSpecificationCharacteristic> resourceSpecCharacteristic;
    //private Attachment[] attachment;
    //private FeatureSpecification[] featureSpecification;
    //private ResourceSpecRelationship[] resourceSpecRelationship;
    //private TargetResourceSchema[] targetResourceSchema;
    //private ValidFor validFor;
}
