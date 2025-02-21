package it.eng.dome.search.domain;

public class ResourceSpecCharacteristicValue /*CharacteristicValueSpecification*/ {
	
	private Boolean isDefault;
	//private String rangeInterval;
	//private String regex;
	private Boolean configurable;
	private String value;
	private String unitOfMeasure;
	private String lastUpdate;
	private String version;
	private int valueFrom;
	private int valueTo;
	//private String valueType;
	//private ValidFor validfor;
	//private Any value;

	public String getUnitOfMeasure() {
		return unitOfMeasure;
	}
	public void setUnitOfMeasure(String unitOfMeasure) {
		this.unitOfMeasure = unitOfMeasure;
	}
	public Boolean getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Boolean getConfigurable() {
		return configurable;
	}
	public void setConfigurable(Boolean configurable) {
		this.configurable = configurable;
	}
	public int getValueFrom() {
		return valueFrom;
	}
	public void setValueFrom(int valueFrom) {
		this.valueFrom = valueFrom;
	}
	public int getValueTo() {
		return valueTo;
	}
	public void setValueTo(int valueTo) {
		this.valueTo = valueTo;
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
