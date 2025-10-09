package it.eng.dome.search.domain.dto;

public class ProductOfferingPriceDTO {

    protected String id;

//    private String href;
    private String name;

    private String description;
    private String lastUpdate;
    private String lifecycleStatus;
    private String priceType;
    private String version;
    private MoneyDTO price;
    private ValidForDTO validFor;
    private boolean isBundle;
    //	private float percentage;
    //	private int recurringChargePeriodLength;
    //	private String recurringChargePeriodType;
    //	private BundledProductOfferingPriceRelationship[] bundledPopRelationship; (eventualmente, creare classe BundledProductOfferingPriceRelationship)
    //	private Constraint[] constraint; (eventualmente, creare classe Constraint)
    //	private Place[] place; (eventualmente, creare classe Place)
    //	private ProductOfferingPriceRelationship[] popRelationship; (eventualmente, creare classe ProductOfferingPriceRelationship)
    //	private PricingLogicAlgorithm[] pricingLogicAlgorithm; (eventualmente, creare classe PricingLogicAlgorithm)
    //	private ProductSpecCharacteristicValue[] prodSpecCharValueUse;
    //	private ProductOfferingTerm[] productOfferingTerm;
    //	private TaxItem[] tax;
    //	private Quantity unitOfMeasure;

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
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
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
    public String getPriceType() {
        return priceType;
    }
    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }
    public MoneyDTO getPrice() {
        return price;
    }
    public void setPrice(MoneyDTO price) {
        this.price = price;
    }
    public ValidForDTO getValidFor() {
        return validFor;
    }
    public void setValidFor(ValidForDTO validFor) {
        this.validFor = validFor;
    }
    public String getVersion () {
        return version;
    }
    public void setVersion (String version) {
        this.version = version;
    }
    public boolean isBundle () {
        return isBundle;
    }
    public void setBundle (boolean isBundle) {
        this.isBundle = isBundle;
    }
}
