package it.eng.dome.search.semantic.domain;

public class Attributes {
	
	public String attribute;
	public String lemma;
	public int syncon;
	public String type;
	
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public String getLemma() {
		return lemma;
	}
	public void setLemma(String lemma) {
		this.lemma = lemma;
	}
	public int getSyncon() {
		return syncon;
	}
	public void setSyncon(int syncon) {
		this.syncon = syncon;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

}
