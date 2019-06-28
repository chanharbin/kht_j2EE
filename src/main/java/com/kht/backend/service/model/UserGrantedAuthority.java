package com.kht.backend.service.model;

import org.springframework.security.core.GrantedAuthority;

public class UserGrantedAuthority implements GrantedAuthority {
    private String url;
    private String operaType;
    @Override
    public String getAuthority() {
        return url+operaType;
    }

    public UserGrantedAuthority(String url, String operaType) {
        this.url = url;
        this.operaType = operaType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOperaType() {
        return operaType;
    }

    public void setOperaType(String operaType) {
        this.operaType = operaType;
    }
}
