package com.kht.backend.service.model;

import java.io.Serializable;

public class UserListResponse implements Serializable {
    private int userCode;
    private int infoCode;
    private String name;
    private String idType;
    private String idCode;
    private String orgName;
    private String email;

    public UserListResponse(int userCode,int infoCode, String name, String idType, String idCode, String orgName, String email) {
        this.userCode = userCode;
        this.infoCode = infoCode;
        this.name = name;
        this.idType = idType;
        this.idCode = idCode;
        this.orgName = orgName;
        this.email = email;
    }

    public int getInfoCode() {
        return infoCode;
    }

    public void setInfoCode(int infoCode) {
        this.infoCode = infoCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdType() {
        return idType;
    }

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdCode() {
        return idCode;
    }

    public void setIdCode(String idCode) {
        this.idCode = idCode;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getUserCode() {
        return userCode;
    }

    public void setUserCode(int userCode) {
        this.userCode = userCode;
    }
}
