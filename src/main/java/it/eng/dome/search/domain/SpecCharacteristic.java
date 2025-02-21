package it.eng.dome.search.domain;

public class SpecCharacteristic /*CharacteristicSpecification*/{
	
	protected String id;
    private Boolean configurable;
    private String description;
	//private Boolean extensible;
	//private Boolean isUnique;
	//private int maxCardinality;
	//private int minCardinality;
	//private String regex;
    private String name;
    private String valueType;
	private String lastUpdate;
	private String version;
	//private CharacteristicSpecificationRelationship[] charSpecRelationship;
    private CharacteristicValueSpecification[] characteristicValueSpecification;
	//private ValidFor validFor;
    
    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Boolean getConfigurable() {
		return configurable;
	}
	public void setConfigurable(Boolean configurable) {
		this.configurable = configurable;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValueType() {
		return valueType;
	}
	public void setValueType(String valueType) {
		this.valueType = valueType;
	}
	public CharacteristicValueSpecification[] getCharacteristicValueSpecification() {
		return characteristicValueSpecification;
	}
	public void setCharacteristicValueSpecification(CharacteristicValueSpecification[] characteristicValueSpecification) {
		this.characteristicValueSpecification = characteristicValueSpecification;
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
