package it.eng.dome.search.service.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO to filtered research on Organizations.
 */
@Data
public class OrganizationSearchRequest {

    private List<String> categories = new ArrayList<>();
    private List<String> countries = new ArrayList<>();
    private List<String> complianceLevels = new ArrayList<>();
}