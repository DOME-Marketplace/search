package it.eng.dome.search.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.eng.dome.search.domain.dto.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(indexName = "indexing-object")
@JsonIgnoreProperties(ignoreUnknown = true)
public class IndexingObject {

	@Id
	protected String id;

	@Field(type = FieldType.Nested)
	private ProductOfferingDTO productOffering;

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

	@Field(type = FieldType.Date, format = DateFormat.date_time)
	private String productOfferingLastUpdate;

	@Field(type = FieldType.Boolean)
	private Boolean productOfferingIsBundle;
	
	@Field(type = FieldType.Nested)
	private List<CategoryDTO> categories;

	// from ProductSpecification

	@Field(type = FieldType.Nested)
	private ProductSpecificationDTO productSpecification;

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

	//from product OfferingPrice
	@Field(type = FieldType.Nested)
	private List<ProductOfferingPriceDTO> productOfferingPrice;

	@Field(type = FieldType.Keyword)
	private String productOfferingPriceId;

	@Field(type = FieldType.Text)
	private String productOfferingPriceName;

	@Field(type = FieldType.Text)
	private String productOfferingPriceType;

	// compliance levels
	@Field(type = FieldType.Keyword)
	private List<String> complianceLevels;

	// from RelatedParty
	@Field(type = FieldType.Nested)
	private List<RelatedPartyDTO> relatedParties;

	// from ServiceSpecification
	@Field(type = FieldType.Nested)
	private List<ServiceSpecificationDTO> services;

	// from Resource Specification
	@Field(type = FieldType.Nested)
	private List<ResourceSpecificationDTO> resources;

	// from Categorization and Entities Extraction
	// put here fields for semantic
	// services ----
	@Field(type = FieldType.Text)
	private String[] classifyResult;

	@Field(type = FieldType.Text)
	private String analyzeResult;
}