package com.kht.backend.exception;

import com.kht.backend.entity.ErrorCode;
import com.kht.backend.entity.ServiceException;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
        throw new ServiceException(ErrorCode.AUTHENTICATION_EXCEPTION, message);
    }
}