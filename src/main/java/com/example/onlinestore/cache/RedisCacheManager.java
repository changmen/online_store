package com.example.onlinestore.cache;

import com.example.onlinestore.utils.JacksonJsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis缓存管理器实现
 */
@Component
public class RedisCacheManager implements CacheManager {

    private static final Logger logger = LoggerFactory.getLogger(RedisCacheManager.class);

    private static final String CACHE_PREFIX = "item:cache:";

    @Autowired
    private StringRedisTemplate redisTemplate;


    @Override
    public <T> T get(String key, Class<T> clazz) {
        String fullKey = CACHE_PREFIX + key;
        String jsonValue = redisTemplate.opsForValue().get(fullKey);

        if (jsonValue == null) {
            return null;
        }

        try {
            // 将JSON字符串转换回原始类型
            return JacksonJsonUtils.toObject(jsonValue, clazz);
        } catch (Exception e) {
            logger.error("Failed to deserialize cache value for key: {}", key, e);
            return null;
        }
    }

    @Override
    public <T> void set(String key, T value) {
        set(key, value, -1); // 不过期
    }

    @Override
    public <T> void set(String key, T value, long expireSeconds) {
        String fullKey = CACHE_PREFIX + key;

        try {
            // 将对象序列化为JSON字符串
            String jsonValue = JacksonJsonUtils.toString(value);

            if (expireSeconds > 0) {
                redisTemplate.opsForValue().set(fullKey, jsonValue, expireSeconds, TimeUnit.SECONDS);
            } else {
                redisTemplate.opsForValue().set(fullKey, jsonValue);
            }

            logger.debug("Cache set: key={}, expireSeconds={}", key, expireSeconds);
        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize cache value for key: {}", key, e);
        }
    }

    @Override
    public void delete(String key) {
        String fullKey = CACHE_PREFIX + key;
        redisTemplate.delete(fullKey);
        logger.debug("Cache deleted: key={}", key);
    }

    @Override
    public boolean exists(String key) {
        String fullKey = CACHE_PREFIX + key;
        Boolean exists = redisTemplate.hasKey(fullKey);
        return Boolean.TRUE.equals(exists);
    }

    @Override
    public void clear() {
        Set<String> keys = redisTemplate.keys(CACHE_PREFIX + "*");
        if (!keys.isEmpty()) {
            redisTemplate.delete(keys);
            logger.debug("Cache cleared: {} keys removed", keys.size());
        }
    }
} 