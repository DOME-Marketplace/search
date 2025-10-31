package it.eng.dome.search.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
    private List<ProductOfferingPriceDTO> productOfferingPrice;
//    private ValidFor validFor;
    private List<CategoryDTO> category;
    private ProductSpecificationDTO productSpecification;
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

}
