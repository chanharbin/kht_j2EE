package com.kht.backend.service.model;

public class OperationLogModel {

    private  int id;

    private long time;

    private String employeeId;

    private String operatedPersonnel;

    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getOperatedPersonnel() {
        return operatedPersonnel;
    }

    public void setOperatedPersonnel(String operatedPersonnel) {
        this.operatedPersonnel = operatedPersonnel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
