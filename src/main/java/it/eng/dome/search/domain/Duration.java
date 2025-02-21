package it.eng.dome.search.domain;

public class Duration {

    private int amount;
    private String units;

    private String lastUpdate;
    private String version;

    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
    public String getUnits() {
        return units;
    }
    public void setUnits(String units) {
        this.units = units;
    }

    public String getLastUpdate () {
        return lastUpdate;
    }
    public void setLastUpdate (String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    public String getVersion () {
        return version;
    }
    public void setVersion (String version) {
        this.version = version;
    }
}