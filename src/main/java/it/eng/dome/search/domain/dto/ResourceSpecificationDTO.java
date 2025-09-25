package it.eng.dome.search.domain.dto;

import it.eng.dome.tmforum.tmf634.v4.model.RelatedParty;
import it.eng.dome.tmforum.tmf634.v4.model.ResourceSpecificationCharacteristic;

import java.util.List;

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
    //private Attachment[] attachment;
    //private FeatureSpecification[] featureSpecification;
    private List<RelatedParty> relatedParty;
//    private List<ResourceSpecificationCharacteristic> resourceSpecCharacteristic;
    //private ResourceSpecRelationship[] resourceSpecRelationship;
    //private TargetResourceSchema[] targetResourceSchema;
    //private ValidFor validFor;

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Boolean getIsBundle() {
        return isBundle;
    }
    public void setIsBundle(Boolean isBundle) {
        this.isBundle = isBundle;
    }
    public String getLastUpdate() {
        return lastUpdate;
    }
    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    public String getLifecycleStatus() {
        return lifecycleStatus;
    }
    public void setLifecycleStatus(String lifecycleStatus) {
        this.lifecycleStatus = lifecycleStatus;
    }
    public List<RelatedParty> getRelatedParty() {
        return relatedParty;
    }
    public void setRelatedParty(List<RelatedParty> relatedParty) {
        this.relatedParty = relatedParty;
    }
//    public List<ResourceSpecificationCharacteristic> getResourceSpecCharacteristic() {
//        return resourceSpecCharacteristic;
//    }
//    public void setResourceSpecCharacteristic(List<ResourceSpecificationCharacteristic> resourceSpecCharacteristic) {
//        this.resourceSpecCharacteristic = resourceSpecCharacteristic;
//    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getHref() {
        return href;
    }
    public void setHref(String href) {
        this.href = href;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }
}
