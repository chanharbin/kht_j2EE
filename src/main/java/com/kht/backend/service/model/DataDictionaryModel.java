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

}
