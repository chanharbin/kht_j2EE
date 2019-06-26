package com.kht.backend.filter;

import com.kht.backend.util.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.handler.Handler;

public class ResponseInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        /*String url=request.getServletPath();
        if(url.matches("/user/accountOpeningInfo$"))*/
        /*String token=jwtTokenProvider.getJwtFromRequest(request);
        if(token==null) {
            response.setHeader("Authorization", null);
        }
        else {
            String newToken = jwtTokenProvider.refreshToken(token);
            response.setHeader("Authorization", "Bearer " + newToken);
        }*/
        //TODO 更改response失败
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
