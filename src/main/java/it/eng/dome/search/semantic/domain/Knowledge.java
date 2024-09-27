package it.eng.dome.search.semantic.domain;

public class Knowledge {
	
	public long syncon;
	public String label;
	public long[] externalIds;
	public Properties[] properties;
	
	public long getSyncon() {
		return syncon;
	}
	public void setSyncon(long syncon) {
		this.syncon = syncon;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public long[] getExternalIds() {
		return externalIds;
	}
	public void setExternalIds(long[] externalIds) {
		this.externalIds = externalIds;
	}
	public Properties[] getProperties() {
		return properties;
	}
	public void setProperties(Properties[] properties) {
		this.properties = properties;
	}
}
