package it.eng.dome.search.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RelatedPartyDTO {

    protected String id;
//    private String href;
    private String name;
    private String role;
    private String referredType;
//    private String lastUpdate;
//    private String version;

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
//    public String getHref() {
//        return href;
//    }
//    public void setHref(String href) {
//        this.href = href;
//    }

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public String getReferredType() {
        return referredType;
    }
    public void setReferredType(String referredType) {
        this.referredType = referredType;
    }

//    public String getLastUpdate() {
//        return lastUpdate;
//    }
//    public void setLastUpdate(String lastUpdate) {
//        this.lastUpdate = lastUpdate;
//    }
//    public String getVersion() {
//        return version;
//    }
//    public void setVersion(String version) {
//        this.version = version;
//    }

}
