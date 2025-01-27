package it.eng.dome.search.domain;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

public class Category {
	
	@Field(type = FieldType.Keyword)
	protected String id;
	
	private String href;
	
	@Field(type = FieldType.Keyword)
	private String name;
	
	private String version;

	private boolean isRoot;
	private String lastUpdate;
	private String lifecycleStatus;
	private String parentId;
	private ValidFor validFor;
	//private String description;
	//ProductOffering[] productOffering;
	//In caso serva la subCategory bisogna controllare la tipologia se CategoryRef o Category
	//CategoryRef subCategory;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}

	public ValidFor getValidFor() {
		return validFor;
	}
	public void setValidFor(ValidFor validFor) {
		this.validFor = validFor;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getLifecycleStatus() {
		return lifecycleStatus;
	}
	public void setLifecycleStatus(String lifecycleStatus) {
		this.lifecycleStatus = lifecycleStatus;
	}
	public String getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public boolean isRoot() {
		return isRoot;
	}
	public void setRoot(boolean root) {
		isRoot = root;
	}
}
