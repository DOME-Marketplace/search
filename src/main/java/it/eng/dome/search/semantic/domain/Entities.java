package it.eng.dome.search.semantic.domain;

public class Entities {

	public String type;
	public String lemma;
	public int syncon;
	public Positions[] positions;
	public int relevance;
	public Attributes[] attributes;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public Positions[] getPositions() {
		return positions;
	}
	public void setPositions(Positions[] positions) {
		this.positions = positions;
	}
	public int getRelevance() {
		return relevance;
	}
	public void setRelevance(int relevance) {
		this.relevance = relevance;
	}
	public Attributes[] getAttributes() {
		return attributes;
	}
	public void setAttributes(Attributes[] attributes) {
		this.attributes = attributes;
	}
}
