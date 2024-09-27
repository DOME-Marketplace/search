package it.eng.dome.search.semantic.domain;

public class MainLemmas {
	
	public String value;
	public long score;
	public Positions[] positions;
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
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
