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

import java.util.List;

@ControllerAdvice
public class JwtResponseBodyAdvice implements ResponseBodyAdvice {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Override
    public boolean supports(MethodParameter methodParameter, Class aClass) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object o, MethodParameter methodParameter, MediaType mediaType, Class aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        ServletServerHttpResponse servletServerResponse = (ServletServerHttpResponse) serverHttpResponse;
        ServletServerHttpRequest servletServerRequest = (ServletServerHttpRequest) serverHttpRequest;
        List<String> bearerToken=serverHttpRequest.getHeaders().get("Authorization");
        String newToken=null;
        String token=null;
        //System.out.println(bearerToken);
        if(bearerToken!=null&&bearerToken.size()==1&& StringUtils.hasText(bearerToken.get(0))&&bearerToken.get(0).startsWith("Bearer ")){
            token=bearerToken.get(0).substring(7, bearerToken.get(0).length());
        }
        newToken=jwtTokenProvider.refreshToken(token);

        servletServerResponse.getHeaders().add("Authorization","Bearer "+newToken);
        return o;
    }
}
