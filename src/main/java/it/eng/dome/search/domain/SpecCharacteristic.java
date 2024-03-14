package it.eng.dome.search.domain;

public class SpecCharacteristic {
	
	protected String id;
    private Boolean configurable;
    private String description;
    private String name;
    private String valueType;
    private CharacteristicValueSpecification[] characteristicValueSpecification;
	
    
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
        
}
