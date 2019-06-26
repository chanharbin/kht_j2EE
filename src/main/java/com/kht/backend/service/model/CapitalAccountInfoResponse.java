package com.kht.backend.service.model;

import java.io.Serializable;

public class CapitalAccountInfoResponse implements Serializable {
    private String capCode;
    private String depCode;
    private String currency;
    private boolean mainFlag;
    private String attr;
    private String orgName;
    private Long openTime;
    private Long closeTime;
    private String capStatus;

    public String getCapCode() {
        return capCode;
    }

    public void setCapCode(String capCode) {
        this.capCode = capCode;
    }

    public String getDepCode() {
        return depCode;
    }

    public void setDepCode(String depCode) {
        this.depCode = depCode;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public boolean isMainFlag() {
        return mainFlag;
    }

    public void setMainFlag(boolean mainFlag) {
        this.mainFlag = mainFlag;
    }

    public String getAttr() {
        return attr;
    }

    public void setAttr(String attr) {
        this.attr = attr;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Long getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Long openTime) {
        this.openTime = openTime;
    }

    public Long getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(Long closeTime) {
        this.closeTime = closeTime;
    }

    public String getCapStatus() {
        return capStatus;
    }

    public void setCapStatus(String capStatus) {
        this.capStatus = capStatus;
    }

    public CapitalAccountInfoResponse(String capCode, String depCode, String currency, boolean mainFlag, String attr, String orgName, Long openTime, Long closeTime, String capStatus) {
        this.capCode = capCode;
        this.depCode = depCode;
        this.currency = currency;
        this.mainFlag = mainFlag;
        this.attr = attr;
        this.orgName = orgName;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.capStatus = capStatus;
    }
}
