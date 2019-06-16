package com.kht.backend.service.model;

public class EmployeeModel {

    private String employeeId;

    private String emplyeePwd;

    private String name;

    private String identityNumber;

    private long telephone;

    private String email;

    private String address;

    private Integer position;

    private Integer authority;

    private Integer employeeState;

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmplyeePwd() {
        return emplyeePwd;
    }

    public void setEmplyeePwd(String emplyeePwd) {
        this.emplyeePwd = emplyeePwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public long getTelephone() {
        return telephone;
    }

    public void setTelephone(long telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Integer getAuthority() {
        return authority;
    }

    public void setAuthority(Integer authority) {
        this.authority = authority;
    }

    public Integer getEmployeeState() {
        return employeeState;
    }

    public void setEmployeeState(Integer employeeState) {
        this.employeeState = employeeState;
    }
}
