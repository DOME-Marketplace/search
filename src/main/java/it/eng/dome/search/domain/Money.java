package it.eng.dome.search.domain;

public class Money {

    private String unit;
    private float value;

    private String lastUpdate;
    private String version;

    public float getValue() {
        return value;
    }
    public void setValue(float value) {
        this.value = value;
    }
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
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