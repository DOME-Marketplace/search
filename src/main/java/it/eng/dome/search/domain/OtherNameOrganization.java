package it.eng.dome.search.domain;

public class OtherNameOrganization {

    private String name;
    private String nameType;
    private String tradingName;
    private ValidFor validFor;

    private String lastUpdate;
    private String version;

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
    public String getTradingName() {
        return tradingName;
    }
    public void setTradingName(String tradingName) {
        this.tradingName = tradingName;
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
