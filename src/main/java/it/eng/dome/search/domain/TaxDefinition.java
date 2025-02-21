package it.eng.dome.search.domain;

public class TaxDefinition {

    protected String id;
    private String name;
    private String taxType;
    private String lastUpdate;
    private String version;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getTaxType() {
        return taxType;
    }
    public void setTaxType(String taxType) {
        this.taxType = taxType;
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
