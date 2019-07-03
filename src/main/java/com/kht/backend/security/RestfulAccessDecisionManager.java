package com.kht.backend.security;
import com.kht.backend.entity.ErrorCode;
import com.kht.backend.entity.ServiceException;
import com.kht.backend.service.model.UserGrantedAuthority;
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
public class RestfulAccessDecisionManager implements AccessDecisionManager{
    //TODO 白名单待完善
    private List<String> whiteList= Arrays.asList("/user/check-code","/user/login","/user/register","/employee/login");
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
        HttpServletResponse response=((FilterInvocation) object).getHttpResponse();
        String url,method;
        AntPathRequestMatcher matcher;
        System.out.println("current url "+request.getRequestURI());
        System.out.println("request.getMethod "+request.getMethod());
        if(request.getRequestURI().equals("/error")){
            return;
        }
        /*
        if(authentication.getAuthorities()==null){
            for(String whiteUrl:whiteList){
                matcher = new AntPathRequestMatcher(whiteUrl);
                if(matcher.matches(request)){
                    return;
                }
            }
        }*/
        for(GrantedAuthority grantedAuthority:authentication.getAuthorities()){
            if(grantedAuthority instanceof UserGrantedAuthority){
                UserGrantedAuthority userGrantedAuthority=(UserGrantedAuthority) grantedAuthority;
                url=userGrantedAuthority.getUrl();
                method=userGrantedAuthority.getOperaType();
                //System.out.println("url "+url);
                //System.out.println("method "+method);
                matcher=new AntPathRequestMatcher(url);
                if(matcher.matches(request)&&
                        method.equals(request.getMethod())){
                        return;
                }
            }else if(grantedAuthority.getAuthority().equals("ROLE_ANONYMOUS")){
                for(String whiteUrl:whiteList){
                    matcher = new AntPathRequestMatcher(whiteUrl);
                    if(matcher.matches(request)){
                        return;
                    }
                }
            }
        }

        System.out.println("bad decision");
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
