package it.eng.dome.search.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductSpecCharacteristicValueDTO {

    private Boolean isDefault;
    private String value;

//    private Boolean configurable;
//    private String unitOfMeasure;
//    private int valueFrom;
//    private int valueTo;
//    private ValidFor validFor;
    //	private String rangeInterval;
    //	private String regex;
    //	private String valueType;
}