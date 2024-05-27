package it.eng.dome.search.domain;

public class ProductSpecCharacteristic {

	protected String id;
	private Boolean configurable;
	private String name;
	private String valueType;
	private ProductSpecCharacteristicValue[] productSpecCharacteristicValue;

	public void setProductSpecCharacteristicValue(ProductSpecCharacteristicValue[] productSpecCharacteristicValue) {
		this.productSpecCharacteristicValue = productSpecCharacteristicValue;
	}

	public ProductSpecCharacteristicValue[] getProductSpecCharacteristicValue() {
		return productSpecCharacteristicValue;
	}

	public Boolean getConfigurable() {
		return configurable;
	}

	public void setConfigurable(Boolean configurable) {
		this.configurable = configurable;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

}
