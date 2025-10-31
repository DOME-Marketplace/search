package it.eng.dome.search.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
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

}
