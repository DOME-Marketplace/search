package it.eng.dome.search.domain;

public class ProductSpecificationRelationship {

    protected String id;
    private String href;
    private String name;
    private String relationshipType;
    private String lastUpdate;
    private String version;
    private ValidFor validFor;

    public String getHref() {
        return href;
    }
    public void setHref(String href) {
        this.href = href;
    }
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
    public String getRelationshipType() {
        return relationshipType;
    }
    public void setRelationshipType(String relationshipType) {
        this.relationshipType = relationshipType;
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
