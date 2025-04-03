package com.example.onlinestore.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 基于Redis的分布式锁实现
 */
@Component
public class RedisDistributedLock {

    private static final Logger logger = LoggerFactory.getLogger(RedisDistributedLock.class);
    
    private static final String LOCK_SUCCESS = "OK";
    private static final Long RELEASE_SUCCESS = 1L;
    
    // 解锁脚本
    private static final String RELEASE_LOCK_LUA_SCRIPT = 
            "if redis.call('get', KEYS[1]) == ARGV[1] then " +
            "    return redis.call('del', KEYS[1]) " +
            "else " +
            "    return 0 " +
            "end";
    
    @Autowired
    private StringRedisTemplate redisTemplate;
    
    private final ThreadLocal<String> lockValue = new ThreadLocal<>();
    
    /**
     * 尝试获取分布式锁
     * 
     * @param lockKey 锁的键
     * @param requestId 请求标识
     * @param expireTime 超期时间
     * @param timeUnit 时间单位
     * @return 是否获取成功
     */
    public boolean tryLock(String lockKey, String requestId, long expireTime, TimeUnit timeUnit) {
        String value = requestId;
        if (value == null) {
            value = UUID.randomUUID().toString();
        }
        
        Boolean success = redisTemplate.opsForValue().setIfAbsent(lockKey, value, expireTime, timeUnit);
        
        if (Boolean.TRUE.equals(success)) {
            lockValue.set(value);
        }
        
        return Boolean.TRUE.equals(success);
    }
    
    /**
     * 尝试获取分布式锁（使用默认的UUID作为请求标识）
     * 
     * @param lockKey 锁的键
     * @param expireTime 超期时间
     * @param timeUnit 时间单位
     * @return 是否获取成功
     */
    public boolean tryLock(String lockKey, long expireTime, TimeUnit timeUnit) {
        return tryLock(lockKey, null, expireTime, timeUnit);
    }
    
    /**
     * 释放分布式锁
     *
     * @param lockKey 锁的键
     */
    public void releaseLock(String lockKey) {
        String value = lockValue.get();
        if (value == null) {
            logger.warn("No lock value found for key: {}", lockKey);
            return;
        }
        
        RedisScript<Long> script = new DefaultRedisScript<>(RELEASE_LOCK_LUA_SCRIPT, Long.class);
        Long result = redisTemplate.execute(script, Collections.singletonList(lockKey), value);
        
        boolean success = RELEASE_SUCCESS.equals(result);
        if (success) {
            lockValue.remove();
        }

    }
    
    /**
     * 尝试获取分布式锁（带重试机制）
     * 
     * @param lockKey 锁的键
     * @param requestId 请求标识
     * @param expireTime 超期时间
     * @param timeUnit 时间单位
     * @param retryTimes 重试次数
     * @param retryInterval 重试间隔（毫秒）
     * @return 是否获取成功
     */
    public boolean tryLockWithRetry(String lockKey, String requestId, long expireTime, 
                                   TimeUnit timeUnit, int retryTimes, long retryInterval) {
        boolean locked = tryLock(lockKey, requestId, expireTime, timeUnit);
        
        int retryCount = 0;
        while (!locked && retryCount < retryTimes) {
            try {
                Thread.sleep(retryInterval);
                locked = tryLock(lockKey, requestId, expireTime, timeUnit);
                retryCount++;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        
        return locked;
    }
    
    /**
     * 尝试获取分布式锁（带重试机制，使用默认的UUID作为请求标识）
     * 
     * @param lockKey 锁的键
     * @param expireTime 超期时间
     * @param timeUnit 时间单位
     * @param retryTimes 重试次数
     * @param retryInterval 重试间隔（毫秒）
     * @return 是否获取成功
     */
    public boolean tryLockWithRetry(String lockKey, long expireTime, 
                                   TimeUnit timeUnit, int retryTimes, long retryInterval) {
        return tryLockWithRetry(lockKey, null, expireTime, timeUnit, retryTimes, retryInterval);
    }
} 