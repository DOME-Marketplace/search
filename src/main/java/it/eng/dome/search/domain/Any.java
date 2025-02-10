package it.eng.dome.search.domain;

public class Any {
    private String value;

    public Any(String value) {
        this.value = value;
    }

    public String getValue () {
        return value;
    }

    public void setValue (String value) {
        this.value = value;
    }
}
