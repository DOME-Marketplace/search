package it.eng.dome.search.domain;

public class ProductOfferingTerm {
	
	private String name;
	private String description;
	private ValidFor validFor;

	private Duration duration;
	
	public void setValidFor(ValidFor validFor) {
		this.validFor = validFor;
	}

	public ValidFor getValidFor() {
		return validFor;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Duration getDuration() {
		return duration;
	}
	public void setDuration(Duration duration) {
		this.duration = duration;
	}
}
