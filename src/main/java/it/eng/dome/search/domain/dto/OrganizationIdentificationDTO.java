package it.eng.dome.search.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationIdentificationDTO {
    private String identificationId;
    private String identificationType;
    private String issuingAuthority;
//    private OffsetDateTime issuingDate;
//    private AttachmentRefOrValue attachment;
//    private TimePeriod validFor;
//    private String atBaseType;
//    private URI atSchemaLocation;
    @JsonProperty("@type")
    private String atType;
}
