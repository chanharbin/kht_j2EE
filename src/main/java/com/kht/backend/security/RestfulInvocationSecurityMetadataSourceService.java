package com.kht.backend.security;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
@Service
public class RestfulInvocationSecurityMetadataSourceService implements
        FilterInvocationSecurityMetadataSource {
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        Collection<ConfigAttribute> configAttributeCollection=new ArrayList<>();
        configAttributeCollection.add(new SecurityConfig("null"));
        return configAttributeCollection;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}

