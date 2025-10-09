package it.eng.dome.search.domain.dto;

import it.eng.dome.tmforum.tmf620.v4.model.RelatedParty;

import java.util.List;

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
//    private ProductSpecCharacteristic[] productSpecCharacteristic;
//    private ProductSpecificationRelationship[] productSpecificationRelationship;
    private List<RelatedParty> relatedParty;
//    private ResourceSpecification[] resourceSpecification;
//    private ServiceSpecification[] serviceSpecification;
    //private TargetProductSchema[] targetProductSchema;
//    private ValidFor validFor;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
//    public String getHref() {
//        return href;
//    }
//    public void setHref(String href) {
//        this.href = href;
//    }
    public String getBrand() {
        return brand;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }
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
//    public void setAttachment(Attachment[] attachment) {
//        this.attachment = attachment;
//    }
//    public Attachment[] getAttachment() {
//        return attachment;
//    }
//    public ProductSpecCharacteristic[] getProductSpecCharacteristic() {
//        return productSpecCharacteristic;
//    }
//    public void setProductSpecCharacteristic(ProductSpecCharacteristic[] productSpecCharacteristic) {
//        this.productSpecCharacteristic = productSpecCharacteristic;
//    }
    public List<RelatedParty> getRelatedParty() {
        return relatedParty;
    }
    public void setRelatedParty(List<RelatedParty> relatedParty) {
        this.relatedParty = relatedParty;
    }
//    public ResourceSpecification[] getResourceSpecification() {
//        return resourceSpecification;
//    }
//    public void setResourceSpecification(ResourceSpecification[] resourceSpecification) {
//        this.resourceSpecification = resourceSpecification;
//    }
//    public ServiceSpecification[] getServiceSpecification() {
//        return serviceSpecification;
//    }
//    public void setServiceSpecification(ServiceSpecification[] serviceSpecification) {
//        this.serviceSpecification = serviceSpecification;
//    }
    public String getProductNumber() {
        return productNumber;
    }
    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }
//    public ValidFor getValidFor() {
//        return validFor;
//    }
//    public void setValidFor(ValidFor validFor) {
//        this.validFor = validFor;
//    }
//    public ProductSpecificationRelationship[] getProductSpecificationRelationship() {
//        return productSpecificationRelationship;
//    }
//    public void setProductSpecificationRelationship(ProductSpecificationRelationship[] productSpecificationRelationship) {
//        this.productSpecificationRelationship = productSpecificationRelationship;
//    }
}
