package com.kht.backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import com.kht.backend.service.model.UserPrincipal;

import java.util.Map;

import static org.junit.Assert.*;

public class JwtTokenProviderTest {

    private ObjectMapper objectMapper=new ObjectMapper();
    @Test
    public void generateToken() {
        UserPrincipal userPrincipal=new UserPrincipal("fff123456adsd","123456",0,null,"fff123456adsd");
        Map<String, Object>map=objectMapper.convertValue(userPrincipal,Map.class);
        System.out.println(map.toString());
    }

    @Test
    public void getUserPrincipalFromJWT() {
    }

    @Test
    public void validateToken() {
    }
}