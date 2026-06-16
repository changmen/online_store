package com.example.onlinestore.service;

import com.example.onlinestore.security.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenBlacklistService {

    private static final Logger logger = LoggerFactory.getLogger(TokenBlacklistService.class);
    private static final String BLACKLIST_PREFIX = "blacklist:";

    private final StringRedisTemplate stringRedisTemplate;
    private final JwtTokenUtil jwtTokenUtil;

    public void addToBlacklist(String token) {
        try {
            String jti = jwtTokenUtil.extractJti(token);
            Date expiration = jwtTokenUtil.extractExpiration(token);
            long ttlSeconds = (expiration.getTime() - System.currentTimeMillis()) / 1000;
            if (ttlSeconds > 0 && jti != null) {
                stringRedisTemplate.opsForValue().set(
                        BLACKLIST_PREFIX + jti,
                        "1",
                        ttlSeconds,
                        TimeUnit.SECONDS
                );
                logger.debug("Added token jti={} to blacklist with TTL={}s", jti, ttlSeconds);
            }
        } catch (Exception e) {
            logger.warn("Failed to add token to blacklist: {}", e.getMessage());
        }
    }

    public boolean isBlacklisted(String token) {
        try {
            String jti = jwtTokenUtil.extractJti(token);
            return isBlacklistedByJti(jti);
        } catch (Exception e) {
            logger.error("Failed to extract jti from token, failing closed: {}", e.getMessage());
            return true;
        }
    }

    public boolean isBlacklistedByJti(String jti) {
        if (jti == null) {
            return false;
        }
        try {
            Boolean exists = stringRedisTemplate.hasKey(BLACKLIST_PREFIX + jti);
            return exists != null && exists;
        } catch (Exception e) {
            logger.error("Failed to check token blacklist, failing closed: {}", e.getMessage());
            return true;
        }
    }
}
