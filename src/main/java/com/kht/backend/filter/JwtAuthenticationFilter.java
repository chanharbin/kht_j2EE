package com.kht.backend.filter;

import com.kht.backend.entity.ErrorCode;
import com.kht.backend.entity.ServiceException;
import com.kht.backend.util.JwtTokenProvider;
import com.kht.backend.service.impl.UserPrincipalServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private List<String> whiteList= Arrays.asList("/user/check-code","/user/login","/user/register","/employee/login","/");
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private UserPrincipalServiceImpl userPrincipalService;
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        AntPathRequestMatcher matcher;
        for(String whiteUrl:whiteList) {
            matcher=new AntPathRequestMatcher(whiteUrl);
            if(matcher.matches(httpServletRequest)){
                filterChain.doFilter(httpServletRequest, httpServletResponse);
                return;
            }
        }
            System.out.println("jwtFilter");
            try {
                String jwt = tokenProvider.getJwtFromRequest(httpServletRequest);
                if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                    UserDetails userDetails = tokenProvider.getUserPrincipalFromJWT(jwt);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
                else{
                    throw new ServiceException(ErrorCode.NOT_ACCEPTABLE, "token失效");
                }
            } catch (Exception ex) {

                logger.error("Could not set user authentication in security context", ex);
            }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
