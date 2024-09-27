package it.eng.dome.search.semantic.domain;

public class Items {
	
	public String lemma;
	public long syncon;
	public double sentiment;
	public Items[] items;
	
	
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
	public double getSentiment() {
		return sentiment;
	}
	public void setSentiment(double sentiment) {
		this.sentiment = sentiment;
	}
	public Items[] getItems() {
		return items;
	}
	public void setItems(Items[] items) {
		this.items = items;
	}

}
