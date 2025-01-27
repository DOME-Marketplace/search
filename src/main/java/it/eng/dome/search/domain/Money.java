package it.eng.dome.search.domain;

public class Money {

    private String unit;
    private float value;

    public float getValue() {
        return value;
    }
    public void setValue(float value) {
        this.value = value;
    }
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }

}