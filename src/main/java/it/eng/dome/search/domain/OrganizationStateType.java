package it.eng.dome.search.domain;

public class OrganizationStateType {

    private String type;

    private String lastUpdate;
    private String version;

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
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
