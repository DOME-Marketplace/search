package it.eng.dome.search.domain;

public class Characteristic {

    private String name;
    private String valueType;
    private Any value;  // da controllare classe Any perchè vuota

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }

    public Any getValue() {
        return value;
    }

    public void setValue(Any value) {
        this.value = value;
    }
}
