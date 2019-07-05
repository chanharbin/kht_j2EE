package com.kht.backend.security;

import com.kht.backend.entity.ErrorCode;
import com.kht.backend.entity.ServiceException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Service
public class RestfulAccessDeniedHandler implements AccessDeniedHandler {
    /**
     * 鉴权失败处理
     * @param httpServletRequest
     * @param httpServletResponse
     * @param e
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        httpServletResponse.sendError(ErrorCode.FORBIDDEN__EXCEPTION.getStatusCode(),ErrorCode.FORBIDDEN__EXCEPTION.getMsg());
    }
}
