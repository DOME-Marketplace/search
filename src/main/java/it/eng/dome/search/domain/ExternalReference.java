package it.eng.dome.search.domain;

public class ExternalReference {

    private String externalReferenceType;
    private String name;

    private String lastUpdate;
    private String version;

    public String getExternalReferenceType() {
        return externalReferenceType;
    }
    public void setExternalReferenceType(String externalReferenceType) {
        this.externalReferenceType = externalReferenceType;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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
