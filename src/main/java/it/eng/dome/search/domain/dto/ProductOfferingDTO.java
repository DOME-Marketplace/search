package it.eng.dome.search.domain.dto;

import it.eng.dome.tmforum.tmf620.v4.model.*;

import java.util.List;

public class ProductOfferingDTO {
    protected String id;
    private String href;
    private String description;
    private Boolean isBundle;
    private String lastUpdate;
    private String lifecycleStatus;
    private String name;
    private String version;
//    private List<ProductOfferingTerm> productOfferingTerm;
    private ProductSpecificationDTO productSpecification;
    private List<ProductOfferingPriceDTO> productOfferingPrice;
//    private ValidFor validFor;
    private List<CategoryDTO> category;
    // private boolean isSellable;
    // private String statusReason;
    // private Agreement[] agreement; (bisogna creare anche classe Agreement)
    // private Attachment[] attachment; (da controllare tipo se AttachmentRefOrValue o Attachment)
    // private BundledProductOffering[] bundledProductOffering; (bisogna creare anche classe BundledProductOffering)
    // private Channel[] channel; (bisogna creare anche classe Channel)
    // private MarketSegment[] marketSegment; (bisogna creare anche classe MarketSegment)
    // private Place[] place; (bisogna creare anche classe Place)
    // private ProdSpecCharValue[] prodSpecCharValueUse;
    // private ProductOfferingRelationship[] productOfferingRelationship; (aggiungere anche classe ProductOfferingRelationship)
    // private ResourceCandidate resourceCandidate; (aggiungere anche la classe ResourceCandidate)
    // private ServiceCandidate serviceCandidate; (aggiungere anche la classe ServiceCandidate)
    // private SLA serviceLevelAgreement; (aggiungere anche la classe SLA)

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
    public ProductSpecificationDTO getProductSpecificationDTO() {
        return productSpecification;
    }
    public void setProductSpecification(ProductSpecificationDTO productSpecificationDTO) {
        this.productSpecification = productSpecificationDTO;
    }
    public List<ProductOfferingPriceDTO> getProductOfferingPrice() {
        return productOfferingPrice;
    }
    public void setProductOfferingPrice(List<ProductOfferingPriceDTO> productOfferingPrice) {
        this.productOfferingPrice = productOfferingPrice;
    }
//    public List<ProductOfferingTerm> getProductOfferingTerm() {
//        return productOfferingTerm;
//    }
//    public void setProductOfferingTerm(List<ProductOfferingTerm> productOfferingTerm) {
//        this.productOfferingTerm = productOfferingTerm;
//    }
//    public ValidFor getValidFor() {
//        return validFor;
//    }
//    public void setValidFor(ValidFor validFor) {
//        this.validFor = validFor;
//    }
    public List<CategoryDTO> getCategory() {
        return category;
    }
    public void setCategory(List<CategoryDTO> category) {
        this.category = category;
    }
}
