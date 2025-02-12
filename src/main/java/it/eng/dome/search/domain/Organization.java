package it.eng.dome.search.domain;

public class Organization {

    protected String id;
    private String href;
    private Boolean isHeadOffice;
    private Boolean isLegalEntity;
    private String name;
    private String nameType;
    private String organizationType;
    private String tradingName;
    private ContactMedium[] contactMedium;
    private PartyCreditProfile[] creditRating;
    private ValidFor existsDuring;
    private ExternalReference[] externalReference;
    private OrganizationChildRelationship[] organizationChildRelationship;
    private OrganizationIdentification[] organizationIdentification;
    private OrganizationParentRelationship organizationParentRelationship;
    private OtherNameOrganization[] otherName;
    private Characteristic[] partyCharacteristic;
    private RelatedParty[] relatedParty;
    private OrganizationStateType status;
    private TaxExemptionCertificate[] taxExemptionCertificate;

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

    public Boolean getHeadOffice() {
        return isHeadOffice;
    }

    public void setHeadOffice(Boolean headOffice) {
        isHeadOffice = headOffice;
    }

    public Boolean getLegalEntity() {
        return isLegalEntity;
    }

    public void setLegalEntity(Boolean legalEntity) {
        isLegalEntity = legalEntity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameType() {
        return nameType;
    }

    public void setNameType(String nameType) {
        this.nameType = nameType;
    }

    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    public String getTradingName() {
        return tradingName;
    }

    public void setTradingName(String tradingName) {
        this.tradingName = tradingName;
    }

    public ContactMedium[] getContactMedium() {
        return contactMedium;
    }

    public void setContactMedium(ContactMedium[] contactMedium) {
        this.contactMedium = contactMedium;
    }

    public PartyCreditProfile[] getCreditRating() {
        return creditRating;
    }

    public void setCreditRating(PartyCreditProfile[] creditRating) {
        this.creditRating = creditRating;
    }

    public ValidFor getExistsDuring() {
        return existsDuring;
    }

    public void setExistsDuring(ValidFor existsDuring) {
        this.existsDuring = existsDuring;
    }

    public ExternalReference[] getExternalReference() {
        return externalReference;
    }

    public void setExternalReference(ExternalReference[] externalReference) {
        this.externalReference = externalReference;
    }

    public OrganizationChildRelationship[] getOrganizationChildRelationship() {
        return organizationChildRelationship;
    }

    public void setOrganizationChildRelationship(OrganizationChildRelationship[] organizationChildRelationship) {
        this.organizationChildRelationship = organizationChildRelationship;
    }

    public OrganizationIdentification[] getOrganizationIdentification() {
        return organizationIdentification;
    }

    public void setOrganizationIdentification(OrganizationIdentification[] organizationIdentification) {
        this.organizationIdentification = organizationIdentification;
    }

    public OrganizationParentRelationship getOrganizationParentRelationship() {
        return organizationParentRelationship;
    }

    public void setOrganizationParentRelationship(OrganizationParentRelationship organizationParentRelationship) {
        this.organizationParentRelationship = organizationParentRelationship;
    }

    public OtherNameOrganization[] getOtherName() {
        return otherName;
    }

    public void setOtherName(OtherNameOrganization[] otherName) {
        this.otherName = otherName;
    }

    public Characteristic[] getPartyCharacteristic() {
        return partyCharacteristic;
    }

    public void setPartyCharacteristic(Characteristic[] partyCharacteristic) {
        this.partyCharacteristic = partyCharacteristic;
    }

    public RelatedParty[] getRelatedParty() {
        return relatedParty;
    }

    public void setRelatedParty(RelatedParty[] relatedParty) {
        this.relatedParty = relatedParty;
    }

    public OrganizationStateType getStatus() {
        return status;
    }

    public void setStatus(OrganizationStateType status) {
        this.status = status;
    }

    public TaxExemptionCertificate[] getTaxExemptionCertificate() {
        return taxExemptionCertificate;
    }

    public void setTaxExemptionCertificate(TaxExemptionCertificate[] taxExemptionCertificate) {
        this.taxExemptionCertificate = taxExemptionCertificate;
    }

}
