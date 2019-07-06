package com.kht.backend.security;

import com.kht.backend.service.impl.RedisServiceImpl;
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
import java.util.stream.Collectors;

public class JwtTokenProviderTest {
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    String token;
    @Autowired
    private RedisServiceImpl redisService;

    @Test
    public void generateToken() {

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 360000000000000L);
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_Admin"));
        authorities.add(new SimpleGrantedAuthority("ROLE_Admin2"));
        System.out.println(authorities);
        UserPrincipal userPrincipal = new UserPrincipal(1, 2L, "1", "1", "1", authorities);
        System.out.println(userPrincipal.convertToMap());
        token = Jwts.builder()
                .setClaims(userPrincipal.convertToMap())
                .setSubject("1")
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, "KHT_Backend")
                .compact();
        System.out.println(token);
        return;
    }

    //eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwicGFzc3dvcmQiOiIxIiwiY29kZSI6IjEiLCJ0ZWxlcGhvbmUiOjIsInVzZXJUeXBlIjoxLCJleHAiOjM2MTU2MDk5ODM3OSwiaWF0IjoxNTYwOTk4Mzc5LCJ1c2VyQ29kZSI6MSwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IlJPTEVfQWRtaW4ifSx7ImF1dGhvcml0eSI6IlJPTEVfQWRtaW4yIn1dfQ.6UccRkialAUZRwSyuGNfAElIT2SNgmIH-73DUfFRyyb-siixpPRdd1gDWdLVfcQ12LOUOIP-CS9vxkHrV5uCAQ
    @Test
    public void getUserPrincipalFromJWT() {
        Claims claims = Jwts.parser()
                .setSigningKey("KHT_Backend")
                .parseClaimsJws("eyJhbGciOiJIUzUxMiJ9.eyJwYXNzd29yZCI6ImUxMGFkYzM5NDliYTU5YWJiZTU2ZTA1N2YyMGY4ODNlIiwiY29kZSI6IjEwMDExNzE4MTc4MTUyIiwidGVsZXBob25lIjoxMzUxMjM0NTY3OCwidXNlclR5cGUiOiIwIiwiZXhwIjoxNTYyMzkyMjc2LCJpYXQiOjE1NjIzODg2NzYsInVzZXJDb2RlIjo2OCwiYXV0aG9yaXRpZXMiOlt7InVybCI6Ii91c2VyL3JlZ2lzdGVyIiwib3BlcmFUeXBlIjoiUE9TVCIsImF1dGhvcml0eSI6Ii91c2VyL3JlZ2lzdGVyO1BPU1QifSx7InVybCI6Ii91c2VyL2xvZ2luIiwib3BlcmFUeXBlIjoiUE9TVCIsImF1dGhvcml0eSI6Ii91c2VyL2xvZ2luO1BPU1QifSx7InVybCI6Ii91c2VyL3Bhc3N3b3JkIiwib3BlcmFUeXBlIjoiUFVUIiwiYXV0aG9yaXR5IjoiL3VzZXIvcGFzc3dvcmQ7UFVUIn0seyJ1cmwiOiIvdXNlci9jaGVjay1jb2RlIiwib3BlcmFUeXBlIjoiR0VUIiwiYXV0aG9yaXR5IjoiL3VzZXIvY2hlY2stY29kZTtHRVQifSx7InVybCI6Ii91c2VyL2FjY291bnQtb3BlbmluZy1pbmZvIiwib3BlcmFUeXBlIjoiR0VUIiwiYXV0aG9yaXR5IjoiL3VzZXIvYWNjb3VudC1vcGVuaW5nLWluZm87R0VUIn0seyJ1cmwiOiIvdXNlci9hY2NvdW50LW9wZW5pbmctaW5mby9zdGF0dXMiLCJvcGVyYVR5cGUiOiJHRVQiLCJhdXRob3JpdHkiOiIvdXNlci9hY2NvdW50LW9wZW5pbmctaW5mby9zdGF0dXM7R0VUIn0seyJ1cmwiOiIvb3JnYW5pemF0aW9uL2xpc3QiLCJvcGVyYVR5cGUiOiJHRVQiLCJhdXRob3JpdHkiOiIvb3JnYW5pemF0aW9uL2xpc3Q7R0VUIn0seyJ1cmwiOiIvZWR1Y2F0aW9uIiwib3BlcmFUeXBlIjoiR0VUIiwiYXV0aG9yaXR5IjoiL2VkdWNhdGlvbjtHRVQifSx7InVybCI6Ii9iYW5rIiwib3BlcmFUeXBlIjoiR0VUIiwiYXV0aG9yaXR5IjoiL2Jhbms7R0VUIn0seyJ1cmwiOiIvdXNlci9jdXN0b21lci1hY2NvdW50Iiwib3BlcmFUeXBlIjoiR0VUIiwiYXV0aG9yaXR5IjoiL3VzZXIvY3VzdG9tZXItYWNjb3VudDtHRVQifSx7InVybCI6Ii91c2VyL3RyYWRlLWFjY291bnQiLCJvcGVyYVR5cGUiOiJHRVQiLCJhdXRob3JpdHkiOiIvdXNlci90cmFkZS1hY2NvdW50O0dFVCJ9LHsidXJsIjoiL3VzZXIvZGVwb3NpdG9yeS1hY2NvdW50Iiwib3BlcmFUeXBlIjoiR0VUIiwiYXV0aG9yaXR5IjoiL3VzZXIvZGVwb3NpdG9yeS1hY2NvdW50O0dFVCJ9LHsidXJsIjoiL3VzZXIvY2FwaXRhbC1hY2NvdW50Iiwib3BlcmFUeXBlIjoiR0VUIiwiYXV0aG9yaXR5IjoiL3VzZXIvY2FwaXRhbC1hY2NvdW50O0dFVCJ9LHsidXJsIjoiL3VzZXIvY2FwaXRhbC1hY2NvdW50Iiwib3BlcmFUeXBlIjoiUE9TVCIsImF1dGhvcml0eSI6Ii91c2VyL2NhcGl0YWwtYWNjb3VudDtQT1NUIn0seyJ1cmwiOiIvdXNlci9jYXBpdGFsLWFjY291bnQiLCJvcGVyYVR5cGUiOiJQVVQiLCJhdXRob3JpdHkiOiIvdXNlci9jYXBpdGFsLWFjY291bnQ7UFVUIn0seyJ1cmwiOiIvaW1hZ2UiLCJvcGVyYVR5cGUiOiJQT1NUIiwiYXV0aG9yaXR5IjoiL2ltYWdlO1BPU1QifV19.XTH9a5aU5pOuJW5e_jqx8UYL2R7hI6-gkO2nn-j8PSRPV8RK8Y8lx2ir6JTc0VcK1LETl-CfxHniehJehfkj3g")
                .getBody();
        System.out.println(claims);
        UserPrincipal userPrincipal = UserPrincipal.create(claims);
        System.out.println(userPrincipal.getUsername());
        System.out.println(userPrincipal.getPassword());
        System.out.println(userPrincipal.getAuthorities());
        System.out.println(userPrincipal.getAuthorities().getClass());
        System.out.println("create time "+claims.getIssuedAt().getTime());
        System.out.println("delete time "+claims.getExpiration().getTime());

    }

    @Test
    public void validateToken() {
        Date now = new Date();
        System.out.println(now.getTime());
        System.out.println(now.getTime());
    }
}
