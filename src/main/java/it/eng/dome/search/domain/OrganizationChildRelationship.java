package it.eng.dome.search.domain;

public class OrganizationChildRelationship {

    private String relationshipType;
    private OrganizationRef organization;

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
}
