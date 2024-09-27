package it.eng.dome.search.semantic.domain;

public class Sentences {

	public int[] phrases;
	public int start;
	public int end;
	public int[] getPhrases() {
		return phrases;
	}
	public void setPhrases(int[] phrases) {
		this.phrases = phrases;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}
}
