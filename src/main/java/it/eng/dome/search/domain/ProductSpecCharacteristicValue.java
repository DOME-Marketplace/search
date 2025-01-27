package it.eng.dome.search.domain;

public class ProductSpecCharacteristicValue {

	private Boolean isDefault;
	private Boolean configurable;
	private String value;
	private String unitOfMeasure;
	private int valueFrom;
	private int valueTo;

	private ValidFor validFor;
	//	private String rangeInterval;
	//	private String regex;
	//	private String valueType;

	public int getValueTo() {
		return valueTo;
	}

	public void setValueTo(int valueTo) {
		this.valueTo = valueTo;
	}

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

	public ValidFor getValidFor() {
		return validFor;
	}
	public void setValidFor(ValidFor validFor) {
		this.validFor = validFor;
	}
}