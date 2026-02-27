package it.eng.dome.search.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationDTO {
    private String id;
    private String href;
    private String tradingName;
    private List<ExternalReferenceDTO> externalReference;
    private List<OrganizationIdentificationDTO> organizationIdentification;
    private List<PartyCharacteristicDTO> partyCharacteristic;
}
