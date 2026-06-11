package com.example.onlinestore.service;

import com.example.onlinestore.security.JwtTokenUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenBlacklistServiceTest {

    @Mock
    private StringRedisTemplate stringRedisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @InjectMocks
    private TokenBlacklistService tokenBlacklistService;

    private static final String TEST_TOKEN = "test-jwt-token";
    private static final String TEST_JTI = "test-jti-123";

    @Test
    void addToBlacklist_validToken_storesInRedis() {
        when(jwtTokenUtil.extractJti(TEST_TOKEN)).thenReturn(TEST_JTI);
        when(jwtTokenUtil.extractExpiration(TEST_TOKEN))
                .thenReturn(new Date(System.currentTimeMillis() + 3600_000));
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);

        tokenBlacklistService.addToBlacklist(TEST_TOKEN);

        verify(valueOperations).set(eq("blacklist:" + TEST_JTI), eq("1"), anyLong(), eq(TimeUnit.SECONDS));
    }

    @Test
    void addToBlacklist_expiredToken_doesNotStore() {
        when(jwtTokenUtil.extractJti(TEST_TOKEN)).thenReturn(TEST_JTI);
        when(jwtTokenUtil.extractExpiration(TEST_TOKEN))
                .thenReturn(new Date(System.currentTimeMillis() - 1000));

        tokenBlacklistService.addToBlacklist(TEST_TOKEN);

        verify(stringRedisTemplate, never()).opsForValue();
    }

    @Test
    void addToBlacklist_redisFailure_swallowsException() {
        when(jwtTokenUtil.extractJti(TEST_TOKEN)).thenReturn(TEST_JTI);
        when(jwtTokenUtil.extractExpiration(TEST_TOKEN))
                .thenReturn(new Date(System.currentTimeMillis() + 3600_000));
        when(stringRedisTemplate.opsForValue()).thenReturn(valueOperations);
        doThrow(new RuntimeException("Redis down")).when(valueOperations)
                .set(anyString(), anyString(), anyLong(), any(TimeUnit.class));

        assertDoesNotThrow(() -> tokenBlacklistService.addToBlacklist(TEST_TOKEN));
    }

    @Test
    void isBlacklisted_tokenInRedis_returnsTrue() {
        when(jwtTokenUtil.extractJti(TEST_TOKEN)).thenReturn(TEST_JTI);
        when(stringRedisTemplate.hasKey("blacklist:" + TEST_JTI)).thenReturn(true);

        assertTrue(tokenBlacklistService.isBlacklisted(TEST_TOKEN));
    }

    @Test
    void isBlacklisted_tokenNotInRedis_returnsFalse() {
        when(jwtTokenUtil.extractJti(TEST_TOKEN)).thenReturn(TEST_JTI);
        when(stringRedisTemplate.hasKey("blacklist:" + TEST_JTI)).thenReturn(false);

        assertFalse(tokenBlacklistService.isBlacklisted(TEST_TOKEN));
    }

    @Test
    void isBlacklisted_nullJti_returnsFalse() {
        when(jwtTokenUtil.extractJti(TEST_TOKEN)).thenReturn(null);

        assertFalse(tokenBlacklistService.isBlacklisted(TEST_TOKEN));
    }

    @Test
    void isBlacklisted_redisFailure_failsClosed() {
        when(jwtTokenUtil.extractJti(TEST_TOKEN)).thenReturn(TEST_JTI);
        when(stringRedisTemplate.hasKey(anyString())).thenThrow(new RuntimeException("Redis down"));

        assertTrue(tokenBlacklistService.isBlacklisted(TEST_TOKEN),
                "Should return true (fail-closed) when Redis is unavailable");
    }
}
