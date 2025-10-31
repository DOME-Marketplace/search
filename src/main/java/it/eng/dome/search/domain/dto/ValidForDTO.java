package it.eng.dome.search.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidForDTO {

    private String startDateTime;
    private String endDateTime;
    private String lastUpdate;
    private String version;
}