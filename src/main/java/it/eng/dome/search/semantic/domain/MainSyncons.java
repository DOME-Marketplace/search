package it.eng.dome.search.semantic.domain;

public class MainSyncons {
	
	public long syncon;
	public String lemma;
	public long score;
	public Positions[] positions;
	
	public long getSyncon() {
		return syncon;
	}
	public void setSyncon(long syncon) {
		this.syncon = syncon;
	}
	public String getLemma() {
		return lemma;
	}
	public void setLemma(String lemma) {
		this.lemma = lemma;
	}
	public long getScore() {
		return score;
	}
	public void setScore(long score) {
		this.score = score;
	}
	public Positions[] getPositions() {
		return positions;
	}
	public void setPositions(Positions[] positions) {
		this.positions = positions;
	}

}
