package com.kht.backend.service.model;


import lombok.Data;

@Data
public class OperationLogModel {

    private int logCode;

    private long logTime;

    private String employeeCode;

    private String operaName;

    private String url;

    private String operaType;

}
