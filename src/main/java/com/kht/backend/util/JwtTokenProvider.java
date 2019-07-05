package com.kht.backend.util;


import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kht.backend.service.impl.RedisServiceImpl;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import com.kht.backend.service.model.UserPrincipal;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    @Autowired
    private RedisServiceImpl redisService;

    @Value("${app.jwtSecret}")
    private String jwtSecret;


    @Value("${app.jwtExpirationInMs}")
    private long jwtExpirationInMs;

    private final long softTimeInMs=5000;
    public String generateToken(Authentication authentication) {
        UserPrincipal userPrincipal=(UserPrincipal)authentication.getPrincipal();
        Date now=new Date();
        redisService.setJwtBlackList(userPrincipal.getUserCode(),now);
        Date expiryDate=new Date(now.getTime()+jwtExpirationInMs);
        return Jwts.builder()
                .setClaims(userPrincipal.convertToMap())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512,jwtSecret)
                .compact();
    }
    public UserPrincipal getUserPrincipalFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("jwtauthorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return getUserPrincipalFromJWT(bearerToken.substring(7, bearerToken.length()));
        }
        return null;
    }
    public UserPrincipal getUserPrincipalFromJWT(String token) {
        Claims claims=Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return UserPrincipal.create(claims);
    }
    public  boolean validateToken(String authToken){
        try {
            Claims claims=Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(authToken).getBody();
            Date curTime=redisService.getJwtTime((int)claims.get("userCode"));
            System.out.println("redis time"+ curTime.getTime());
            System.out.println("token time"+claims.getIssuedAt().getTime());
            //claims > curTime
            if(claims.getIssuedAt().getTime()+softTimeInMs<=curTime.getTime()){
                logger.error("JWT time out");
                throw new  UnsupportedJwtException("JWT time out");
            }
            return true;
        }catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }
    public Map<String,Object> getUserCodeAndTimeFromJwt(String token){
        Map<String,Object>stringObjectMap=new HashMap<>();
        Claims claims=Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        stringObjectMap.put("userCode",claims.get("userCode"));
        stringObjectMap.put("time",claims.getIssuedAt());
        return stringObjectMap;
    }
    public String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("jwtauthorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
    public String refreshToken(String token) {
        if(validateToken(token)){
            final Claims claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token)
                    .getBody();
            //判断是否应该刷新token
            if(redisService.getJwtRefreshStatus((int)claims.get("userCode"))){
                //System.out.println("dont need to refresh");
                return token;
            }
            Date now=new Date();
            Date expiryDate=new Date(now.getTime()+jwtExpirationInMs);
            redisService.setJwtBlackList((int)claims.get("userCode"),now);
            return Jwts.builder()
                    .setClaims(claims)
                    .setIssuedAt(now)
                    .setExpiration(expiryDate)
                    .signWith(SignatureAlgorithm.HS512,jwtSecret)
                    .compact();
        }
        return null;
    }
}
