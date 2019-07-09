package com.kht.backend.security;
import com.kht.backend.entity.ErrorCode;
import com.kht.backend.entity.ServiceException;
import com.kht.backend.service.model.UserGrantedAuthority;
import com.kht.backend.util.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Service
public class RestfulAccessDecisionManager implements AccessDecisionManager {
    private static final Logger logger = LoggerFactory.getLogger(RestfulAccessDecisionManager.class);
    private List<String> whiteList = Arrays.asList("/user/check-code", "/user/login", "/user/register", "/employee/login","/swagger-ui.html");

    /**
     * 自定义鉴权
     * @param authentication
     * @param object
     * @param configAttributes
     * @throws AccessDeniedException
     * @throws InsufficientAuthenticationException
     */
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
        HttpServletResponse response = ((FilterInvocation) object).getHttpResponse();
        String url, method;
        AntPathRequestMatcher matcher;
        logger.error("current url " + request.getRequestURI() + "  " + request.getMethod());
        if (request.getRequestURI().equals("/error")) {
            return;
        }
        //根据用户能执行的url列表判断是否允许用户执行发送的url
        for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
            if (grantedAuthority instanceof UserGrantedAuthority) {
                UserGrantedAuthority userGrantedAuthority = (UserGrantedAuthority) grantedAuthority;
                url = userGrantedAuthority.getUrl();
                method = userGrantedAuthority.getOperaType();
                //logger.debug("user url :" + url + " " + method);
                matcher = new AntPathRequestMatcher(url);
                if (matcher.matches(request) &&
                        method.equals(request.getMethod())) {
                    return;
                }
            } else if (grantedAuthority.getAuthority().equals("ROLE_ANONYMOUS")) {
                for (String whiteUrl : whiteList) {
                    matcher = new AntPathRequestMatcher(whiteUrl);
                    if (matcher.matches(request)) {
                        return;
                    }
                }
            }
        }

        logger.debug("bad decision");
        //throw new ServiceException(ErrorCode.FORBIDDEN__EXCEPTION,"没有权限");
        throw new AccessDeniedException("no right");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
