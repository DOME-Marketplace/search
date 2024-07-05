package it.eng.dome.search.semantic.domain;

public class Phrases {

	public int[] tokens;
	public String type;
	public int start;
	public int end;
	
	public int[] getTokens() {
		return tokens;
	}
	public void setTokens(int[] tokens) {
		this.tokens = tokens;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
