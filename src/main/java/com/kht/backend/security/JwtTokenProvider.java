package com.kht.backend.security;


import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
public class JwtTokenProvider {
    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs}")
    private long jwtExpirationInMs;
    private ObjectMapper objectMapper=new ObjectMapper();
    public String generateToken(Authentication authentication) {
        UserPrincipal userPrincipal=(UserPrincipal)authentication.getPrincipal();
        Date now=new Date();
        Date expiryDate=new Date(now.getTime()+jwtExpirationInMs);

        return Jwts.builder()
                .setClaims((Map<String, Object>)objectMapper.convertValue(userPrincipal,Map.class))
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.ES512,jwtSecret)
                .compact();
    }
    public UserPrincipal getUserPrincipalFromJWT(String token) {
        Claims claims=Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return objectMapper.convertValue(claims.getSubject(),UserPrincipal.class);
    }
    public  boolean validateToken(String authToken){
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        }
        catch (SignatureException ex) {
            //TODO
        }
        return false;
    }
}
