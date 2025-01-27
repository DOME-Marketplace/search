package it.eng.dome.search.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@JsonIgnoreProperties(ignoreUnknown = true)
public class RelatedParty {

	protected String id;
	private String href;
	//private String name;
	private String role;
	private String referredType;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getReferredType() {
		return referredType;
	}
	public void setReferredType(String referredType) {
		this.referredType = referredType;
	}
	
}
