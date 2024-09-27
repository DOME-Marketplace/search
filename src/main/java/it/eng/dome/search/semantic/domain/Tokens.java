package it.eng.dome.search.semantic.domain;

public class Tokens {

	public int syncon;
	public Vsyn vsyn;
	public int start;
	public int end;
	public String type;
	public String lemma;
	public String pos;
	public int paragraph;
	public int sentence;
	public int phrase;
	public Atoms[] atoms;
	
	
	public int getSyncon() {
		return syncon;
	}
	public void setSyncon(int syncon) {
		this.syncon = syncon;
	}
	public Vsyn getVsyn() {
		return vsyn;
	}
	public void setVsyn(Vsyn vsyn) {
		this.vsyn = vsyn;
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
	public String getPos() {
		return pos;
	}
	public void setPos(String pos) {
		this.pos = pos;
	}
	public int getParagraph() {
		return paragraph;
	}
	public void setParagraph(int paragraph) {
		this.paragraph = paragraph;
	}
	public int getSentence() {
		return sentence;
	}
	public void setSentence(int sentence) {
		this.sentence = sentence;
	}
	public int getPhrase() {
		return phrase;
	}
	public void setPhrase(int phrase) {
		this.phrase = phrase;
	}
	public Atoms[] getAtoms() {
		return atoms;
	}
	public void setAtoms(Atoms[] atoms) {
		this.atoms = atoms;
	}
	
}
