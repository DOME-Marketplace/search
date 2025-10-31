package it.eng.dome.search.domain.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoneyDTO {

    private String unit;
    private float value;
    private String lastUpdate;
    private String version;

}