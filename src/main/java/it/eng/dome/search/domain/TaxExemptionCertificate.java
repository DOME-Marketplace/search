package it.eng.dome.search.domain;

public class TaxExemptionCertificate {

    protected String id;
    private String lastUpdate;
    private String version;
    private Attachment attachment;
    private TaxDefinition[] taxDefinition;
    private ValidFor validFor;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Attachment getAttachment() {
        return attachment;
    }
    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }
    public TaxDefinition[] getTaxDefinition() {
        return taxDefinition;
    }
    public void setTaxDefinition(TaxDefinition[] taxDefinition) {
        this.taxDefinition = taxDefinition;
    }
    public ValidFor getValidFor() {
        return validFor;
    }
    public void setValidFor(ValidFor validFor) {
        this.validFor = validFor;
    }
    public String getLastUpdate() {
        return lastUpdate;
    }
    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }
}
