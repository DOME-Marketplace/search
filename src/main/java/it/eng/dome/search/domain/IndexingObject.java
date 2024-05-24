package it.eng.dome.search.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Document(indexName = "indexing-object")
@JsonIgnoreProperties(ignoreUnknown = true)
public class IndexingObject {
	
	public IndexingObject indexingObject() {
		return this.indexingObject();
		}

	@Id
	protected String id;
	
	@Field (type = FieldType.Nested)
	private ProductOffering productOffering;
	
	//from ProductOffering
	
	@Field(type = FieldType.Keyword)
	private String productOfferingId;
	
	@Field(type = FieldType.Text)
	private String productOfferingDescription;

	@Field(type = FieldType.Keyword)
	private String productOfferingName;
	
	@Field(type = FieldType.Text)
	private String productOfferingNameText;
	
	@Field (type = FieldType.Date)
	private String productOfferingLastUpdate;
	
	@Field (type = FieldType.Boolean)
	private Boolean productOfferingIsBundle;	
	
	//from ProductSpecification
	
	@Field(type = FieldType.Nested)
	private ProductSpecification productSpecification;

	@Field(type = FieldType.Keyword)
	private String productSpecificationId;

	@Field(type = FieldType.Text)
	private String productSpecificationBrand;

	@Field(type = FieldType.Text)
	private String productSpecificationName;
	
	@Field(type = FieldType.Text)
	private String productSpecificationDescription;	
	
	//from RelatedParty
	@Field(type = FieldType.Keyword)
	private String relatedPartyId;

	//from ServiceSpecification
	@Field(type = FieldType.Nested)
	private ServiceSpecification[] services;
	
	//from Resource Specification
	@Field(type = FieldType.Nested)
	private ResourceSpecification[] resources;
	
	//from Categorization and Entities Extraction  //put here fields for semantic services ----
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProductOfferingId() {
		return productOfferingId;
	}

	public void setProductOfferingId(String productOfferingId) {
		this.productOfferingId = productOfferingId;
	}
	
	public String getProductOfferingDescription() {
		return productOfferingDescription;
	}

	public void setProductOfferingDescription(String productOfferingDescription) {
		this.productOfferingDescription = productOfferingDescription;
	}
	
	public String getProductOfferingName() {
		return productOfferingName;
	}

	public void setProductOfferingName(String productOfferingName) {
		this.productOfferingName = productOfferingName;
	}
	

	public String getProductOfferingNameText() {
		return productOfferingNameText;
	}

	public void setProductOfferingNameText(String productOfferingNameText) {
		this.productOfferingNameText = productOfferingNameText;
	}

	public String getProductOfferingLastUpdate() {
		return productOfferingLastUpdate;
	}

	public void setProductOfferingLastUpdate(String productOfferingLastUpdate) {
		this.productOfferingLastUpdate = productOfferingLastUpdate;
	}
	

	public Boolean getProductOfferingIsBundle() {
		return productOfferingIsBundle;
	}

	public void setProductOfferingIsBundle(Boolean productOfferingIsBundle) {
		this.productOfferingIsBundle = productOfferingIsBundle;
	}
	
	
	public String getProductSpecificationId() {
		return productSpecificationId;
	}

	public void setProductSpecificationId(String productSpecificationId) {
		this.productSpecificationId = productSpecificationId;
	}
	
	public String getProductSpecificationBrand() {
		return productSpecificationBrand;
	}

	public void setProductSpecificationBrand(String productSpecificationBrand) {
		this.productSpecificationBrand = productSpecificationBrand;
	}
	
	public String getProductSpecificationName() {
		return productSpecificationName;
	}

	public void setProductSpecificationName(String productSpecificationName) {
		this.productSpecificationName = productSpecificationName;
	}
	
	public String getProductSpecificationDescription() {
		return productSpecificationDescription;
	}

	public void setProductSpecificationDescription(String productSpecificationDescription) {
		this.productSpecificationDescription = productSpecificationDescription;
	}

	public String getRelatedPartyId() {
		return relatedPartyId;
	}

	public void setRelatedPartyId(String relatedPartyId) {
		this.relatedPartyId = relatedPartyId;
	}

	public ServiceSpecification[] getServices() {		
		return services;
	}

	public void setServices(ServiceSpecification[] services) {
		this.services = services;
	}

	public ResourceSpecification[] getResources() {
		return resources;
	}

	public void setResources(ResourceSpecification[] resources) {
		this.resources = resources;
	}
	
	public ProductOffering getProductOffering() {
		return productOffering;
	}

	public void setProductOffering(ProductOffering productOffering) {
		this.productOffering = productOffering;
	}

	public ProductSpecification getProductSpecification() {
		return productSpecification;
	}

	public void setProductSpecification(ProductSpecification productSpecification) {
		this.productSpecification = productSpecification;
	}

}
