package it.eng.dome.search.domain;

public class OrganizationChildRelationship {

    private String relationshipType;
    private OrganizationRef organization;

    private String lastUpdate;
    private String version;

    public String getRelationshipType() {
        return relationshipType;
    }
    public void setRelationshipType(String relationshipType) {
        this.relationshipType = relationshipType;
    }
    public OrganizationRef getOrganization() {
        return organization;
    }
    public void setOrganization(OrganizationRef organization) {
        this.organization = organization;
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
