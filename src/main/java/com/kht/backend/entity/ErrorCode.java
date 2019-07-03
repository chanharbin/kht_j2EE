package com.kht.backend.entity;

public enum ErrorCode {
    /**
     * 暂时只有一个错误代码400，后续实践中补上
     * 客户端错误
     */
    PARAM_ERR_COMMON(400, "Bad Request"),

    /**
     * 身份认证错失败
     */
    AUTHENTICATION_EXCEPTION(401, "身份认证错失败"),

    /**
     * 鉴权失败，没有权限
     */
    FORBIDDEN__EXCEPTION(403,"没有权限"),
    /**
     * jwt失效
     */
    NOT_ACCEPTABLE(405,"token失效"),

    /**
     * 文件格式错误
     */
    FILE_EXT_ERROR(415, "文件格式错误"),

    /**
     * 服务器端错误
     */
    SERVER_EXCEPTION(500, "服务器发生异常");

    private int statusCode;
    private String msg;

    ErrorCode(int statusCode,String msg){
        this.statusCode = statusCode;
        this.msg = msg;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMsg() {
        return msg;
    }
}
