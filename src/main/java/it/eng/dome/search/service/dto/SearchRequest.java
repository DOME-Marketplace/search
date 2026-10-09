package it.eng.dome.search.service.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SearchRequest {
	
	private ArrayList<String> categories = new ArrayList<>();
	private List<String> complianceLevels = new ArrayList<>();
	private List<String> procurementType = new ArrayList<>();
	// Optional: restricts results to offerings whose Seller is this organization
	private String organizationId;
}
