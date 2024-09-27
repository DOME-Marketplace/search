package it.eng.dome.search.semantic.domain;

public class Verb {
	
	public String text;
	public String host;
	public long syncon;
	public int phrase;
	public String type;
	public int relevance;
	public String lemma;

	public String getLemma() { return lemma; }
	public void setLemma(String lemma) { this.lemma = lemma; }
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public long getSyncon() {
		return syncon;
	}
	public void setSyncon(long syncon) {
		this.syncon = syncon;
	}
	public int getPhrase() {
		return phrase;
	}
	public void setPhrase(int phrase) {
		this.phrase = phrase;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getRelevance() {
		return relevance;
	}
	public void setRelevance(int relevance) {
		this.relevance = relevance;
	}

}
