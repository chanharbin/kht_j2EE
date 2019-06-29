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
import java.util.stream.Collectors;

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
                .parseClaimsJws("eyJhbGciOiJIUzUxMiJ9.eyJjb2RlIjoiMTAwMHp4YzAwMDAzIiwidGVsZXBob25lIjoxODYwMDAwMDAwMiwidXNlclR5cGUiOiIxIiwiZXhwIjo1MTYxNzMxMzU1LCJpYXQiOjE1NjE3MzEzNTUsInVzZXJDb2RlIjo3LCJhdXRob3JpdGllcyI6W3sidXJsIjoiL2xvZ2luL2VtcGxveWVlIiwib3BlcmFUeXBlIjoiUE9TVCIsImF1dGhvcml0eSI6Ii9sb2dpbi9lbXBsb3llZTtQT1NUIn0seyJ1cmwiOiIvZW1wbG95ZWUiLCJvcGVyYVR5cGUiOiJHRVQiLCJhdXRob3JpdHkiOiIvZW1wbG95ZWU7R0VUIn0seyJ1cmwiOiIvZW1wbG95ZWUvbmFtZSIsIm9wZXJhVHlwZSI6IkdFVCIsImF1dGhvcml0eSI6Ii9lbXBsb3llZS9uYW1lO0dFVCJ9LHsidXJsIjoiL2VtcGxveWVlIiwib3BlcmFUeXBlIjoiUE9TVCIsImF1dGhvcml0eSI6Ii9lbXBsb3llZTtQT1NUIn0seyJ1cmwiOiIvZW1wbG95ZWUiLCJvcGVyYVR5cGUiOiJQVVQiLCJhdXRob3JpdHkiOiIvZW1wbG95ZWU7UFVUIn0seyJ1cmwiOiIvZW1wbG95ZWUiLCJvcGVyYVR5cGUiOiJERUxFVEUiLCJhdXRob3JpdHkiOiIvZW1wbG95ZWU7REVMRVRFIn0seyJ1cmwiOiIvdXNlci9saXN0Iiwib3BlcmFUeXBlIjoiR0VUIiwiYXV0aG9yaXR5IjoiL3VzZXIvbGlzdDtHRVQifSx7InVybCI6Ii91c2VyL3tPUkdfQ09ERX0iLCJvcGVyYVR5cGUiOiJHRVQiLCJhdXRob3JpdHkiOiIvdXNlci97T1JHX0NPREV9O0dFVCJ9LHsidXJsIjoiL3VzZXItdmFsaWRhdGUiLCJvcGVyYVR5cGUiOiJQVVQiLCJhdXRob3JpdHkiOiIvdXNlci12YWxpZGF0ZTtQVVQifSx7InVybCI6Ii9zeXN0ZW0tcGFyYW1ldGVyIiwib3BlcmFUeXBlIjoiR0VUIiwiYXV0aG9yaXR5IjoiL3N5c3RlbS1wYXJhbWV0ZXI7R0VUIn0seyJ1cmwiOiIvc3lzdGVtLXBhcmFtZXRlciIsIm9wZXJhVHlwZSI6IlBVVCIsImF1dGhvcml0eSI6Ii9zeXN0ZW0tcGFyYW1ldGVyO1BVVCJ9LHsidXJsIjoiL2RhdGEtZGljdGlvbmFyeSIsIm9wZXJhVHlwZSI6IkdFVCIsImF1dGhvcml0eSI6Ii9kYXRhLWRpY3Rpb25hcnk7R0VUIn0seyJ1cmwiOiIvZGF0YS1kaWN0aW9uYXJ5L3NlYXJjaCIsIm9wZXJhVHlwZSI6IkdFVCIsImF1dGhvcml0eSI6Ii9kYXRhLWRpY3Rpb25hcnkvc2VhcmNoO0dFVCJ9LHsidXJsIjoiL2RhdGEtZGljdGlvbmFyeSIsIm9wZXJhVHlwZSI6IlBPU1QiLCJhdXRob3JpdHkiOiIvZGF0YS1kaWN0aW9uYXJ5O1BPU1QifSx7InVybCI6Ii9kYXRhLWRpY3Rpb25hcnkiLCJvcGVyYVR5cGUiOiJQVVQiLCJhdXRob3JpdHkiOiIvZGF0YS1kaWN0aW9uYXJ5O1BVVCJ9LHsidXJsIjoiL2RhdGEtZGljdGlvbmFyeSIsIm9wZXJhVHlwZSI6IkRFTEVURSIsImF1dGhvcml0eSI6Ii9kYXRhLWRpY3Rpb25hcnk7REVMRVRFIn0seyJ1cmwiOiIvb3BlcmF0aW9uLWxvZyIsIm9wZXJhVHlwZSI6IkdFVCIsImF1dGhvcml0eSI6Ii9vcGVyYXRpb24tbG9nO0dFVCJ9LHsidXJsIjoiL29wZXJhdGlvbi1sb2cve2VtcGxveWVlQ29kZX0iLCJvcGVyYVR5cGUiOiJHRVQiLCJhdXRob3JpdHkiOiIvb3BlcmF0aW9uLWxvZy97ZW1wbG95ZWVDb2RlfTtHRVQifSx7InVybCI6Ii9vcGVyYXRpb24tbG9nL3tzdGFydFRpbWV9L3tlbmRUaW1lfSIsIm9wZXJhVHlwZSI6IkdFVCIsImF1dGhvcml0eSI6Ii9vcGVyYXRpb24tbG9nL3tzdGFydFRpbWV9L3tlbmRUaW1lfTtHRVQifSx7InVybCI6Ii9vcmdhbml6YXRpb24iLCJvcGVyYVR5cGUiOiJHRVQiLCJhdXRob3JpdHkiOiIvb3JnYW5pemF0aW9uO0dFVCJ9LHsidXJsIjoiL29yZ2FuaXphdGlvbiIsIm9wZXJhVHlwZSI6IlBPU1QiLCJhdXRob3JpdHkiOiIvb3JnYW5pemF0aW9uO1BPU1QifSx7InVybCI6Ii9vcmdhbml6YXRpb24iLCJvcGVyYVR5cGUiOiJQVVQiLCJhdXRob3JpdHkiOiIvb3JnYW5pemF0aW9uO1BVVCJ9LHsidXJsIjoiL29yZ2FuaXphdGlvbiIsIm9wZXJhVHlwZSI6IkRFTEVURSIsImF1dGhvcml0eSI6Ii9vcmdhbml6YXRpb247REVMRVRFIn0seyJ1cmwiOiIvb3JnYW5pemF0aW9uQnlOYW1lIiwib3BlcmFUeXBlIjoiR0VUIiwiYXV0aG9yaXR5IjoiL29yZ2FuaXphdGlvbkJ5TmFtZTtHRVQifV19.Ud_Po7j0OQF3nHGWKsQKbIpYmBm_ibwPlEJJ86JxXUia1z1c3ez0Q6tY7uFWyBrZ6k2AnTDVBl-NktxJrAruug")                .getBody();
        System.out.println(claims);
        UserPrincipal userPrincipal=UserPrincipal.create(claims);
        System.out.println(userPrincipal.getUsername());
        System.out.println(userPrincipal.getPassword());
        System.out.println(userPrincipal.getAuthorities());
        System.out.println(userPrincipal.getAuthorities().getClass());

    }

    @Test
    public void validateToken() {
        System.out.println();
    }
}
