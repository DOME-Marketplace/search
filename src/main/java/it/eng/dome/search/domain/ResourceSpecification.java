package it.eng.dome.search.domain;

public class ResourceSpecification {
	
	protected String id;
	private String href;
	private String description;
	private Boolean isBundle;
	private String lastUpdate;
	private String lifecycleStatus;
	private String name;
	private RelatedParty[] relatedParty;
	private ResourceSpecCharacteristic[] resourceSpecCharacteristic;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Boolean getIsBundle() {
		return isBundle;
	}
	public void setIsBundle(Boolean isBundle) {
		this.isBundle = isBundle;
	}
	public String getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public String getLifecycleStatus() {
		return lifecycleStatus;
	}
	public void setLifecycleStatus(String lifecycleStatus) {
		this.lifecycleStatus = lifecycleStatus;
	}
	public RelatedParty[] getRelatedParty() {
		return relatedParty;
	}
	public void setRelatedParty(RelatedParty[] relatedParty) {
		this.relatedParty = relatedParty;
	}
	public ResourceSpecCharacteristic[] getResourceSpecCharacteristic() {
		return resourceSpecCharacteristic;
	}
	public void setResourceSpecCharacteristic(ResourceSpecCharacteristic[] resourceSpecCharacteristic) {
		this.resourceSpecCharacteristic = resourceSpecCharacteristic;
	}
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

}
