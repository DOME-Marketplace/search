package it.eng.dome.search.domain;

public class CharacteristicValueSpecification {

	private Boolean isDefault;
	private String value;
	private String unitOfMeasure;
	private int valueFrom;
	private int valueTo;
	
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
}
