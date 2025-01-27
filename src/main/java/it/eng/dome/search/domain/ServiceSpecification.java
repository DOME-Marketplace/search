package it.eng.dome.search.domain;


import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceSpecification {
	
	@Id
	protected String id;
	private String href;
	private String description;
	private Boolean isBundle;
	private String lastUpdate;
	private String lifecycleStatus;
	private String name;
	//private String Version;

	//private Attachment[] attachment;
	//private ConstraintRef[] constraint;
	//private EntitySpecRelationship[] entitySpecRelationship;
	//private FeatureSpecification featureSpecification;
	private RelatedParty[] relatedParty;
	//private ResourceSpecificationRef[] resourceSpecification;
	//private ServiceLevelSpecificationRef[] serviceLevelSpecification;
	//private ServiceSpecRelationship serviceSpecRelationship;
	private SpecCharacteristic[] specCharacteristic; //CharacteristicSpecification
	//private TargetEntitySchema targetEntitySchema;
	//private ValidFor validFor;
	
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public RelatedParty[] getRelatedParty() {
		return relatedParty;
	}
	public void setRelatedParty(RelatedParty[] relatedParty) {
		this.relatedParty = relatedParty;
	}
	public SpecCharacteristic[] getSpecCharacteristic() {
		return specCharacteristic;
	}
	public void setSpecCharacteristic(SpecCharacteristic[] specCharacteristic) {
		this.specCharacteristic = specCharacteristic;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
