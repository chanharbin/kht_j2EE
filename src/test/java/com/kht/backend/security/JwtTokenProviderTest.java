/*
package com.kht.backend.security;

import com.kht.backend.util.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Test;
import com.kht.backend.service.model.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JwtTokenProviderTest {
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    String token;
    @Test
    public void generateToken() {

        Date now=new Date();
        Date expiryDate=new Date(now.getTime()+360000000000000L);
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_Admin"));
        authorities.add(new SimpleGrantedAuthority("ROLE_Admin2"));
        System.out.println(authorities);
        UserPrincipal userPrincipal=new UserPrincipal(1,2L,"1","1","1",authorities);
        System.out.println(userPrincipal.convertToMap());
        token=Jwts.builder()
                .setClaims(userPrincipal.convertToMap())
                .setSubject("1")
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512,"KHT_Backend")
                .compact();
        System.out.println(token);
        return ;
    }
    //eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwicGFzc3dvcmQiOiIxIiwiY29kZSI6IjEiLCJ0ZWxlcGhvbmUiOjIsInVzZXJUeXBlIjoxLCJleHAiOjM2MTU2MDk5ODM3OSwiaWF0IjoxNTYwOTk4Mzc5LCJ1c2VyQ29kZSI6MSwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfQWRtaW4ifSx7ImF1dGhvcml0eSI6IlJPTEVfQWRtaW4yIn1dfQ.6UccRkialAUZRwSyuGNfAElIT2SNgmIH-73DUfFRyyb-siixpPRdd1gDWdLVfcQ12LOUOIP-CS9vxkHrV5uCAQ
    @Test
    public void getUserPrincipalFromJWT() {
        Claims claims=Jwts.parser()
                .setSigningKey("KHT_Backend")
                .parseClaimsJws("eyJhbGciOiJIUzUxMiJ9.eyJwYXNzd29yZCI6ImUxMGFkYzM5NDliYTU5YWJiZTU2ZTA1N2YyMGY4ODNlIiwiY29kZSI6bnVsbCwidGVsZXBob25lIjoxMjM0NTY3ODkxMiwidXNlclR5cGUiOiIwIiwiZXhwIjoxNTYxMzczNzg2LCJpYXQiOjE1NjEzNzAxODYsInVzZXJDb2RlIjoxLCJhdXRob3JpdGllcyI6W3siYXV0aG9yaXR5IjoiUk9MRV9VU0VSIn1dfQ.0-Ebgl9Jjco9WI3ntHEsbT72KvghNaftRw6pq9kSyYEChDj7vOqeYU0Psi5XzXCAjsOwH-Hu4EjwefQsahePEw")                .getBody();
        System.out.println(claims);
        UserPrincipal userPrincipal=UserPrincipal.create(claims);
        System.out.println(userPrincipal.getUsername());
        System.out.println(userPrincipal.getPassword());

    }

    @Test
    public void validateToken() {
        System.out.println();
    }
}*/
