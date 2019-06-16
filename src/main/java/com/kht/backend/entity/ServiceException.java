package com.kht.backend.entity;

public class ServiceException extends RuntimeException{
    private ErrorCode errorCode;
    private String desc;

    public ServiceException(ErrorCode errorCode, String desc){
        this.errorCode = errorCode;
        this.desc = desc;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getDesc() {
        return desc;
    }
}
