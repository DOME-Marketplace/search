package it.eng.dome.search.domain.dto;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

public class CategoryDTO {

    @Field(type = FieldType.Keyword)
    private String id;

    private String href;

//    @Field(type = FieldType.Boolean)
//    private Boolean isRoot;

    private String lastUpdate;

    @Field(type = FieldType.Keyword)
    private String lifecycleStatus;

    @Field(type = FieldType.Keyword)
    private String name;

    private String validFor;

    // --- GETTERS & SETTERS ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getHref() { return href; }
    public void setHref(String href) { this.href = href; }

//    public Boolean getIsRoot() { return isRoot; }
//    public void setIsRoot(Boolean isRoot) { this.isRoot = isRoot; }

    public String getLastUpdate() { return lastUpdate; }
    public void setLastUpdate(String lastUpdate) { this.lastUpdate = lastUpdate; }

    public String getLifecycleStatus() { return lifecycleStatus; }
    public void setLifecycleStatus(String lifecycleStatus) { this.lifecycleStatus = lifecycleStatus; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getValidFor() { return validFor; }
    public void setValidFor(String validFor) { this.validFor = validFor; }
}

