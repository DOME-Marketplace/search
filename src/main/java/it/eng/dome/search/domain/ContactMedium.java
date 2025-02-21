package it.eng.dome.search.domain;

public class ContactMedium {

    private String mediumType;
    private Boolean preferred;
    private MediumCharacteristic characteristic;
    private ValidFor validfor;

    private String lastUpdate;
    private String version;

    public String getMediumType() {
        return mediumType;
    }
    public void setMediumType(String mediumType) {
        this.mediumType = mediumType;
    }
    public Boolean getPreferred() {
        return preferred;
    }
    public void setPreferred(Boolean preferred) {
        this.preferred = preferred;
    }
    public MediumCharacteristic getCharacteristic() {
        return characteristic;
    }
    public void setCharacteristic(MediumCharacteristic characteristic) {
        this.characteristic = characteristic;
    }
    public ValidFor getValidfor() {
        return validfor;
    }
    public void setValidfor(ValidFor validfor) {
        this.validfor = validfor;
    }

    public String getLastUpdate () {
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
