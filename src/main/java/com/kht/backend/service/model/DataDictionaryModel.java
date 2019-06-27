package com.kht.backend.service.model;

import lombok.Data;

@Data
public class DataDictionaryModel {

    private Integer mainCode;

    private Integer subCode;

    private String colName;

    private String colCode;

    private String tabCode;

    private String valueCode;

    private String value;

    public Integer getMainCode() {
        return mainCode;
    }

    public void setMainCode(Integer mainCode) {
        this.mainCode = mainCode;
    }

    public Integer getSubCode() {
        return subCode;
    }

    public void setSubCode(Integer subCode) {
        this.subCode = subCode;
    }

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

    public String getColCode() {
        return colCode;
    }

    public void setColCode(String colCode) {
        this.colCode = colCode;
    }

    public String getTabCode() {
        return tabCode;
    }

    public void setTabCode(String tabCode) {
        this.tabCode = tabCode;
    }

    public String getValueCode() {
        return valueCode;
    }

    public void setValueCode(String valueCode) {
        this.valueCode = valueCode;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
