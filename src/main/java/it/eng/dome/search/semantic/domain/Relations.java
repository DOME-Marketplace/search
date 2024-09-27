package it.eng.dome.search.semantic.domain;

public class Relations {
	
	public Verb verb;
	public Related[] related;
	
	public Verb getVerb() {
		return verb;
	}
	public void setVerb(Verb verb) {
		this.verb = verb;
	}
	public Related[] getRelated() {
		return related;
	}
	public void setRelated(Related[] related) {
		this.related = related;
	}

}
