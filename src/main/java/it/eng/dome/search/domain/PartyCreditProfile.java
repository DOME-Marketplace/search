package it.eng.dome.search.domain;

public class PartyCreditProfile {

    private String creditAgencyName;
    private String creditAgencyType;
    private String ratingReference;
    private String lastUpdate;
    private String version;
    private Integer ratingScore;
    private ValidFor validFor;


    public String getCreditAgencyName() {
        return creditAgencyName;
    }
    public void setCreditAgencyName(String creditAgencyName) {
        this.creditAgencyName = creditAgencyName;
    }
    public String getCreditAgencyType() {
        return creditAgencyType;
    }
    public void setCreditAgencyType(String creditAgencyType) {
        this.creditAgencyType = creditAgencyType;
    }
    public String getRatingReference() {
        return ratingReference;
    }
    public void setRatingReference(String ratingReference) {
        this.ratingReference = ratingReference;
    }
    public Integer getRatingScore() {
        return ratingScore;
    }
    public void setRatingScore(Integer ratingScore) {
        this.ratingScore = ratingScore;
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
