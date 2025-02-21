package it.eng.dome.search.domain;

public class Characteristic {

    private String name;
    private String valueType;
    private Any value;

    private String lastUpdate;
    private String version;

    public String getName () {
        return name;
    }
    public void setName (String name) {
        this.name = name;
    }
    public String getValueType () {
        return valueType;
    }
    public void setValueType (String valueType) {
        this.valueType = valueType;
    }
    public Any getValue () {
        return value;
    }
    public void setValue (Any value) {
        this.value = value;
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
