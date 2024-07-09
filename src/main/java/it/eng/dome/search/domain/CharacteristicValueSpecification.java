package it.eng.dome.search.domain;

public class CharacteristicValueSpecification {

	private Boolean isDefault;
	private String value;
	private String unitOfMeasure;
	private String valueFrom;
	
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
	public String getValueFrom() {
		return valueFrom;
	}
	public void setValueFrom(String valueFrom) {
		this.valueFrom = valueFrom;
	}
}
