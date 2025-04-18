package it.eng.dome.search.domain;

public class ProductSpecCharacteristic {

	protected String id;
	private Boolean configurable;
	private String name;
	private String valueType;
	private String description;
	private String lastUpdate;
	private String version;
	private ProductSpecCharacteristicValue[] productSpecCharacteristicValue;
	private ValidFor validFor;
	//	private Boolean extensible;
	//	private Boolean isUnique;
	//	private int maxCardinality;
	//	private int minCardinality;
	//	private String regex;
	//	private ProductSpecCharacteristicRelationship[] productSpecCharRelationship; (eventualmente, creare classe ProductSpecCharacteristicRelationship)

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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public ValidFor getValidFor() {
		return validFor;
	}
	public void setValidFor(ValidFor validFor) {
		this.validFor = validFor;
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
