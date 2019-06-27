package com.kht.backend.service.model;

public class DictionaryModel {
    private String value;
    private String valueCode;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValueCode() {
        return valueCode;
    }

    public void setValueCode(String valueCode) {
        this.valueCode = valueCode;
    }

    public DictionaryModel(String value, String valueCode) {
        this.value = value;
        this.valueCode = valueCode;
    }
}
