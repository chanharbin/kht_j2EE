package com.kht.backend.exception;

import com.kht.backend.entity.ErrorCode;
import com.kht.backend.entity.ServiceException;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
        //throw new ServiceException(ErrorCode.PARAM_ERR_COMMON, "鉴权失败");
    }
}