package com.kht.backend.filter;

import com.kht.backend.util.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Arrays;
import java.util.List;

@ControllerAdvice
public class JwtResponseBodyAdvice implements ResponseBodyAdvice {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    private List<String> noSupportMethod = Arrays.asList("authenticateUser", "registerUser", "setUserAccountInfo");

    /**
     * 根据方法筛选是否要加token
     *
     * @param methodParameter
     * @param aClass
     * @return
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        //methodParameter.
        //System.out.println(methodParameter.getMethod().getName());
        //if(noSupportMethod.stream().anyMatch(str->str.trim().matches(methodParameter.getMethod().getName())))
        //     return false;
        return true;
    }

    /**
     * 给每个响应的头部加上token
     *
     * @param o
     * @param methodParameter
     * @param mediaType
     * @param aClass
     * @param serverHttpRequest
     * @param serverHttpResponse
     * @return
     */
    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        ServletServerHttpResponse servletServerResponse = (ServletServerHttpResponse) serverHttpResponse;
        List<String> bearerToken = serverHttpRequest.getHeaders().get("jwtauthorization");
        List<String> responseToken = serverHttpResponse.getHeaders().get("jwtauthorization");
        //如果响应头已有token不再添加
        if (responseToken != null && !responseToken.isEmpty()) {
            return o;
        }
        String token = null;
        String newToken = null;
        if (bearerToken != null && bearerToken.size() == 1 && StringUtils.hasText(bearerToken.get(0)) && bearerToken.get(0).startsWith("Bearer ")) {
            token = bearerToken.get(0).substring(7, bearerToken.get(0).length());
            newToken = jwtTokenProvider.refreshToken(token);
        }
        if (newToken != null) {
            servletServerResponse.getHeaders().add("jwtauthorization", "Bearer " + newToken);
        }
        return o;
    }
}
