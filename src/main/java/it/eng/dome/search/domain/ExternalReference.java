package it.eng.dome.search.domain;

public class ExternalReference {

    private String externalReferenceType;
    private String name;

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
}
