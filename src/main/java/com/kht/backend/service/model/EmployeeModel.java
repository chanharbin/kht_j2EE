package com.kht.backend.service.model;

import lombok.Data;

@Data
public class EmployeeModel {
    private String employeeCode;
    private Integer userCode;
    private String position;
    private String employeeName;
    private String idCode;
    private Long telephone;
    private String email;
    private String address;
    private String employeeStatus;
}
