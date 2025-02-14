package it.eng.dome.search.domain;

public class OrganizationIdentification {

    protected String identificationId;
    private String identificationType;
    private String issuingAuthority;
    private String issuingDate;
    private Attachment attachment;
    private ValidFor validFor;

    private String lastUpdate;
    private String version;

    public String getIdentificationId() {
        return identificationId;
    }
    public void setIdentificationId(String identificationId) {
        this.identificationId = identificationId;
    }
    public String getIdentificationType() {
        return identificationType;
    }
    public void setIdentificationType(String identificationType) {
        this.identificationType = identificationType;
    }
    public String getIssuingAuthority() {
        return issuingAuthority;
    }
    public void setIssuingAuthority(String issuingAuthority) {
        this.issuingAuthority = issuingAuthority;
    }
    public String getIssuingDate() {
        return issuingDate;
    }
    public void setIssuingDate(String issuingDate) {
        this.issuingDate = issuingDate;
    }
    public Attachment getAttachment() {
        return attachment;
    }
    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }
    public ValidFor getValidFor() {
        return validFor;
    }
    public void setValidFor(ValidFor validFor) {
        this.validFor = validFor;
    }

    public String getLasUpdate () {
        return lastUpdate;
    }
    public void setLastUpdate (String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    public String getVersion () {
        return version;
    }
    public void setVersion (String version) {
        this.version = version;
    }
}
