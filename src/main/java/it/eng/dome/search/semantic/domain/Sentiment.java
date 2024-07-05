package it.eng.dome.search.semantic.domain;

public class Sentiment {
	
	public double overall;
	public double negativity;
	public double positivity;
	public Items[] items;
	
	public double getOverall() {
		return overall;
	}
	public void setOverall(double overall) {
		this.overall = overall;
	}
	public double getNegativity() {
		return negativity;
	}
	public void setNegativity(double negativity) {
		this.negativity = negativity;
	}
	public double getPositivity() {
		return positivity;
	}
	public void setPositivity(double positivity) {
		this.positivity = positivity;
	}
	public Items[] getItems() {
		return items;
	}
	public void setItems(Items[] items) {
		this.items = items;
	}

}
