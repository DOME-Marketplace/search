package it.eng.dome.search.semantic.domain;

public class AnalyzeResultObject {
	
	public String input_text;
	public String language;
	public Analysis analysis;
	
	
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
	public Analysis getAnalysis() {
		return analysis;
	}
	public void setAnalysis(Analysis analysis) {
		this.analysis = analysis;
	}

}
