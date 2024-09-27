package it.eng.dome.search.semantic.domain;

public class Topics {
	
	public String label;
	public long id;
	public long score;
	public boolean winner;
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getScore() {
		return score;
	}
	public void setScore(long score) {
		this.score = score;
	}
	public boolean isWinner() {
		return winner;
	}
	public void setWinner(boolean winner) {
		this.winner = winner;
	}

}
