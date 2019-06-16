package com.kht.backend.service.model;

public class OrganizationModel {
    private String  organizationId;

    private String organizationName;

    private String organizationAddress;

    private long telphone;

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getOrganizationAddress() {
        return organizationAddress;
    }

    public void setOrganizationAddress(String organizationAddress) {
        this.organizationAddress = organizationAddress;
    }

    public long getTelphone() {
        return telphone;
    }

    public void setTelphone(long telphone) {
        this.telphone = telphone;
    }
}
