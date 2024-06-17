package it.eng.dome.search.service.dto;

import java.util.ArrayList;


public class SearchRequest {
	
	ArrayList<String> categories = new ArrayList<>();
	
	public ArrayList<String> getCategories(){
		return categories;
	}
	
	public void setCategories(ArrayList<String> categories) {
		this.categories = categories;
	}

}
