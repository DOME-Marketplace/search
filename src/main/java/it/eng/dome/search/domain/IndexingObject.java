package it.eng.dome.search.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import it.eng.dome.tmforum.tmf620.v4.model.CategoryRef;
import it.eng.dome.tmforum.tmf620.v4.model.ProductOffering;
import it.eng.dome.tmforum.tmf620.v4.model.ProductSpecification;
import it.eng.dome.tmforum.tmf620.v4.model.ResourceSpecificationRef;
import it.eng.dome.tmforum.tmf620.v4.model.ServiceSpecificationRef;

import java.util.List;
import java.time.OffsetDateTime;

@Document(indexName = "indexing-object")
@JsonIgnoreProperties(ignoreUnknown = true)
public class IndexingObject {

	public IndexingObject indexingObject() {
		return new IndexingObject();
	}

	@Id
	protected String id;

	@Field(type = FieldType.Nested)
	private ProductOffering productOffering;

	// from ProductOffering
	
	@Field(type = FieldType.Keyword)
	private String productOfferingId;

	@Field(type = FieldType.Text)
	private String productOfferingDescription;

	@Field(type = FieldType.Keyword)
	private String productOfferingName;

	@Field(type = FieldType.Text)
	private String productOfferingNameText;
	
	@Field(type = FieldType.Text)
	private String productOfferingLifecycleStatus;

	@Field(type = FieldType.Date)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
	private OffsetDateTime productOfferingLastUpdate;

	@Field(type = FieldType.Boolean)
	private Boolean productOfferingIsBundle;
	
	@Field(type = FieldType.Nested)
	private List<CategoryRef> categories;

	// from ProductSpecification

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

	@Field(type = FieldType.Text)
	private String productSpecificationOwner;

	// from RelatedParty
	@Field(type = FieldType.Keyword)
	private String relatedPartyId;

	// from ServiceSpecification
	@Field(type = FieldType.Nested)
	private List<ServiceSpecificationRef> services;

	// from Resource Specification
	@Field(type = FieldType.Nested)
	private List<ResourceSpecificationRef> resources;

	// from Categorization and Entities Extraction //put here fields for semantic
	// services ----
	@Field(type = FieldType.Text)
	private String[] classifyResult;

	@Field(type = FieldType.Text)
	private String analyzeResult;

	//---------------------
	
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
	
	public List<CategoryRef> getCategories() {
		return categories;
	}

	public void setCategories(List<CategoryRef> categories) {
		this.categories = categories;
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
	
	public String getProductOfferingLifecycleStatus() {
		return productOfferingLifecycleStatus;
	}

	public void setProductOfferingLifecycleStatus(String productOfferingLifecycleStatus) {
		this.productOfferingLifecycleStatus = productOfferingLifecycleStatus;
	}

	public OffsetDateTime getProductOfferingLastUpdate() {
		return productOfferingLastUpdate;
	}

	public void setProductOfferingLastUpdate(OffsetDateTime productOfferingLastUpdate) {
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

	public List<ServiceSpecificationRef> getServices() {
		return services;
	}

	public void setServices(List<ServiceSpecificationRef> services) {
		this.services = services;
	}

	public List<ResourceSpecificationRef> getResources() {
		return resources;
	}

	public void setResources(List<ResourceSpecificationRef> resources) {
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
	
	public String[] getClassifyResult() {
		return classifyResult;
	}

	public void setClassifyResult(String[] classifyResult) {
		this.classifyResult = classifyResult;
	}
	
	public String getAnalyzeResult() {
		return analyzeResult;
	}

	public void setAnalyzeResult(String analyzeResult) {
		this.analyzeResult = analyzeResult;
	}

	public String getProductSpecificationOwner () {
		return productSpecificationOwner;
	}

	public void setProductSpecificationOwner (String productSpecificationOwner) {
		this.productSpecificationOwner = productSpecificationOwner;
	}
}
