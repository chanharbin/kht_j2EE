package com.kht.backend.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {
    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs}")
    private long jwtExpirationInMs;

    public String generateToken(Authentication authentication) {
        UserPrincipal userPrincipal=(UserPrincipal)authentication.getPrincipal();
        Date now=new Date();
        Date expiryDate=new Date(now.getTime()+jwtExpirationInMs);

        return Jwts.builder()
                //TODO
                //https://www.callicoder.com/spring-boot-spring-security-jwt-mysql-react-app-part-2/
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.ES512,jwtSecret)
                .compact();
    }
    public String getUserIdFromJWT(String token)
    {
        Claims claims=Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
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
