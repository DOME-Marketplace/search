package it.eng.dome.search.domain;

public class ContactMedium {

    private String mediumType;
    private Boolean preferred;
    private MediumCharacteristic characteristic;
    private ValidFor validfor;

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
}
