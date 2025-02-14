package it.eng.dome.search.domain;

public class Any {

    private String value;

    private String lastUpdate;
    private String version;

    public Any(String value) {
        this.value = value;
    }

    public String getValue () {
        return value;
    }
    public void setValue (String value) {
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
