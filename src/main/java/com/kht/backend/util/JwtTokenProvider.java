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

    //验证宽松时间
    private final long validateSoftTimeInMs = 500000L;
    //刷新宽松时间
    private final long refreshSoftTimeInMs=50000L;
    /**
     * 生成token
     * @param authentication
     * @return
     */
    public String generateToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Date now = new Date();
        redisService.setJwtBlackList(userPrincipal.getUserCode(), now.getTime());
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
        return Jwts.builder()
                .setClaims(userPrincipal.convertToMap())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    /**
     * 从请求头中直接生成userPrincipal
     * @param request
     * @return
     */
    public UserPrincipal getUserPrincipalFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("jwtauthorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return getUserPrincipalFromJWT(bearerToken.substring(7, bearerToken.length()));
        }
        return null;
    }

    /**
     * 由token生成userPrincipal 供spring security使用
     * @param token
     * @return
     */
    public UserPrincipal getUserPrincipalFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return UserPrincipal.create(claims);
    }

    /**
     * 验证token是否过期
     * @param authToken
     * @return
     */
    public boolean validateToken(String authToken) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(authToken).getBody();
            long curTime = redisService.getJwtTime((int) claims.get("userCode"));
            logger.debug("redis time" + curTime);
            logger.debug("token time" + claims.getIssuedAt().getTime());
            //claims > curTime
            if (claims.getIssuedAt().getTime() + validateSoftTimeInMs <= curTime) {
                logger.error("JWT time out");
                throw new UnsupportedJwtException("JWT time out");
            }
            return true;
        } catch (SignatureException ex) {
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

    /**
     * 从token中获取userCode
     * @param token
     * @return
     */
    public Map<String, Object> getUserCodeAndTimeFromJwt(String token) {
        Map<String, Object> stringObjectMap = new HashMap<>();
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        stringObjectMap.put("userCode", claims.get("userCode"));
        stringObjectMap.put("time", claims.getIssuedAt());
        return stringObjectMap;
    }

    /**
     * 从请求头中获取token
     * @param request
     * @return
     */
    public String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("jwtauthorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }

    /**
     * 判断是否应该刷新token，如果当前token产生时间小于redis中的时间或当前这一刻的时间在redis时间的宽松范围内都不刷新token
     * @param userCode
     * @param tokenTime
     * @param curTime
     * @return
     */
    private synchronized boolean refreshJudge(int userCode,long tokenTime,long curTime){

        long redisTime=redisService.getJwtTime(userCode);
        logger.debug("redis time "+redisTime);
        logger.error("curtime "+curTime);
        if((tokenTime<redisTime)||(redisTime+refreshSoftTimeInMs>curTime)){
            return false;
        }
        redisService.setJwtBlackList(userCode,curTime);
        return true;
    }
    /**
     * 刷新token
     * @param token
     * @return
     */
    public  String refreshToken(String token) {
        if (validateToken(token)) {
            final Claims claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token)
                    .getBody();
            Date curTime = new Date();
            //判断是否应该刷新token
            if (refreshJudge((int)claims.get("userCode"),claims.getIssuedAt().getTime(),curTime.getTime())) {
                logger.debug("dont need to refresh");
                return token;
            }
            Date expiryDate = new Date(curTime.getTime() + jwtExpirationInMs);
            return Jwts.builder()
                    .setClaims(claims)
                    .setIssuedAt(curTime)
                    .setExpiration(expiryDate)
                    .signWith(SignatureAlgorithm.HS512, jwtSecret)
                    .compact();
        }
        return null;
    }
}
