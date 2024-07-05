package it.eng.dome.search.semantic.domain;

public class CategorizationResultObject {

	public String input_text;
	public String language;
	public String[] iptc_categories;
	public String[] geo_categories;
	
	
	
	public String getInput_text() {
		return input_text;
	}
	public void setInput_text(String input_text) {
		this.input_text = input_text;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String[] getIpct_categories() {
		return iptc_categories;
	}
	public void setIpct_categories(String[] iptc_categories) {
		this.iptc_categories = iptc_categories;
	}
	public String[] getGeo_categories() {
		return geo_categories;
	}
	public void setGeo_categories(String[] geo_categories) {
		this.geo_categories = geo_categories;
	}
	
}
