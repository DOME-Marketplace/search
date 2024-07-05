package it.eng.dome.search.semantic.domain;

public class Paragraphs {

	public int[] sentences;
	public int start;
	public int end;
	
	public int[] getSentences() {
		return sentences;
	}
	public void setSentences(int[] sentences) {
		this.sentences = sentences;
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
