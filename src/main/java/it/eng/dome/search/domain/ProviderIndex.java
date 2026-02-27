package it.eng.dome.search.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.eng.dome.search.domain.dto.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(indexName = "provider-index")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProviderIndex {

    @Id
    protected String id;

    @Field(type = FieldType.Text)
    private String tradingName;

    @Field(type = FieldType.Object)
    private OrganizationDTO organization;

    @Field(type = FieldType.Nested)
    private List<CategoryDTO> categories;

    @Field(type = FieldType.Keyword)
    private String country;

    @Field(type = FieldType.Keyword)
    private List<String> complianceLevels;

    @Field(type = FieldType.Integer)
    private Integer publishedOfferingsCount;
}
