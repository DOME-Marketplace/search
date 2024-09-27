package it.eng.dome.search.semantic.domain;

import java.util.List;

public class Related {
	
	public String relation;
	
	public String text;
	public String lemma;
	public long syncon;
	public String type;
	public int phrase;
	public int relevance;
	public List<Related> related;

	public List<Related> getRelated() { return related; }
	public void setRelated(List<Related> related) {	this.related = related;	}
	public String getRelation() {
		return relation;
	}
	public void setRelation(String relation) {
		this.relation = relation;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getLemma() {
		return lemma;
	}
	public void setLemma(String lemma) {
		this.lemma = lemma;
	}
	public long getSyncon() {
		return syncon;
	}
	public void setSyncon(long syncon) {
		this.syncon = syncon;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getPhrase() {
		return phrase;
	}
	public void setPhrase(int phrase) {
		this.phrase = phrase;
	}
	public int getRelevance() {
		return relevance;
	}
	public void setRelevance(int relevance) {
		this.relevance = relevance;
	}
}
