package it.eng.dome.search.service.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
public class SearchRequest {
	
	private ArrayList<String> categories = new ArrayList<>();
	private List<String> complianceLevels = new ArrayList<>();
	private List<String> procurementType = new ArrayList<>();
}
