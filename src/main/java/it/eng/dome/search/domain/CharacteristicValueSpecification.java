package it.eng.dome.search.domain;

public class CharacteristicValueSpecification {

	private Boolean isDefault;
	private String value;
	private String unitOfMeasure;
	private int valueFrom;
	private int valueTo;

	private String rangeInterval;
	private String regex;
	private ValidFor validFor;
	private String valueType;

	private String lastUpdate;
	private String version;

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

	public int getValueTo() {
		return valueTo;
	}
	public void setValueTo(int valueTo) {
		this.valueTo = valueTo;
	}
	public int getValueFrom() {
		return valueFrom;
	}
	public void setValueFrom(int valueFrom) {
		this.valueFrom = valueFrom;
	}

	public String getRangeInterval() {
		return rangeInterval;
	}
	public void setRangeInterval(String rangeInterval) {
		this.rangeInterval = rangeInterval;
	}
	public String getRegex() {
		return regex;
	}
	public void setRegex(String regex) {
		this.regex = regex;
	}
	public ValidFor getValidFor() {
		return validFor;
	}
	public void setValidFor(ValidFor validFor) {
		this.validFor = validFor;
	}
	public String getValueType() {
		return valueType;
	}
	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

	public String getLastUpdate () {
		return lastUpdate;
	}
	public void setLastUpdate (String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public String getVersion () {
		return version;
	}
	public void setVersion (String version) {
		this.version = version;
	}
}
