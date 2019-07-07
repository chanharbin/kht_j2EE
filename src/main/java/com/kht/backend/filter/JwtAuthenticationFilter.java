package com.kht.backend.filter;

import com.kht.backend.entity.ErrorCode;
import com.kht.backend.entity.ServiceException;
import com.kht.backend.security.RestfulAccessDecisionManager;
import com.kht.backend.service.impl.RedisServiceImpl;
import com.kht.backend.service.model.UserPrincipal;
import com.kht.backend.util.JwtTokenProvider;
import com.kht.backend.service.impl.UserPrincipalServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private List<String> whiteList = Arrays.asList("/user/check-code", "/user/login", "/user/register", "/employee/login", "/");
    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private RedisServiceImpl redisService;

    @Autowired
    private  UserPrincipalServiceImpl userPrincipalService;
    /**
     * 线程保护判断token是否需要刷新权限
     * @param userCode
     * @return
     */
    private synchronized boolean judgeRefreshStatus(int userCode) {
        logger.error("checking     "+userCode);
        if (redisService.getRefreshStatus(userCode)) {
            logger.error("checking result True   ");
            redisService.setRefreshStatus(userCode, false);
            return true;
        }
        logger.error("checking result false   ");
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        AntPathRequestMatcher matcher;
        for (String whiteUrl : whiteList) {
            matcher = new AntPathRequestMatcher(whiteUrl);
            if (matcher.matches(httpServletRequest)) {
                filterChain.doFilter(httpServletRequest, httpServletResponse);
                return;
            }
        }
        logger.debug("go into jwtFilter");

        try {
            String jwt = tokenProvider.getJwtFromRequest(httpServletRequest);
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                UserPrincipal userDetails = tokenProvider.getUserPrincipalFromJWT(jwt);
                if(judgeRefreshStatus(userDetails.getUserCode())){
                    logger.debug("need to update authorities");
                    userDetails=(UserPrincipal) userPrincipalService.loadUserByUsername(userDetails.getUsername());
                }
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                //throw new ServiceException(ErrorCode.NOT_ACCEPTABLE, "token失效");
                httpServletResponse.sendError(ErrorCode.NOT_ACCEPTABLE.getStatusCode(), ErrorCode.NOT_ACCEPTABLE.getMsg());
                return;
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
