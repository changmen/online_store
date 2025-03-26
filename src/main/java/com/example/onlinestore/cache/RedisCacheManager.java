package com.example.onlinestore.cache;

import com.example.onlinestore.exception.CacheOperateException;
import com.example.onlinestore.utils.JacksonJsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Redis缓存管理器实现
 */
@Component
public class RedisCacheManager implements CacheManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisCacheManager.class);

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${spring.redis.cache-prefix:Redis.Cache.}")
    private String redisCachePrefix;


    @Override
    public <T> T get(String key, Class<T> clazz) throws CacheOperateException {
        String fullKey = redisCachePrefix + key;
        String jsonValue = redisTemplate.opsForValue().get(fullKey);

        if (StringUtils.isBlank(jsonValue)) {
            LOGGER.debug("Cache not found: key={}", key);
            return null;
        }
        return deserialize(key, jsonValue, clazz);
    }

    @Override
    public <T> void set(String key, T value) throws CacheOperateException {
        // 支持不过期缓存
        set(key, value, -1);
    }

    @Override
    public <T> void set(String key, T value, long expireSeconds) throws CacheOperateException {
        String fullKey = redisCachePrefix + key;

        try {
            // 将对象序列化为JSON字符串
            String jsonValue = JacksonJsonUtils.toString(value);

            if (expireSeconds > 0) {
                redisTemplate.opsForValue().set(fullKey, jsonValue, expireSeconds, TimeUnit.SECONDS);
            } else {
                redisTemplate.opsForValue().set(fullKey, jsonValue);
            }

            LOGGER.debug("Cache set: key={}, expireSeconds={}", key, expireSeconds);
        } catch (JsonProcessingException e) {
            LOGGER.error("Failed to serialize cache value for key: {}", key, e);
            throw new CacheOperateException("Failed to serialize cache value for key: " + key);
        }
    }

    @Override
    public void delete(String key) throws CacheOperateException {
        String fullKey = redisCachePrefix + key;
        redisTemplate.delete(fullKey);
        LOGGER.debug("Cache deleted: key={}", key);
    }

    @Override
    public boolean exists(String key) throws CacheOperateException {
        String fullKey = redisCachePrefix + key;
        Boolean exists = redisTemplate.hasKey(fullKey);
        return Boolean.TRUE.equals(exists);
    }

    private <T>  T deserialize(String key, String json, Class<T> clazz) throws CacheOperateException {
        try {
            return JacksonJsonUtils.toObject(json, clazz);
        } catch (IOException e) {
            LOGGER.error("Failed to deserialize cache value for key: {}", key, e);
            throw new CacheOperateException("Failed to deserialize cache value for key: " + key);
        }
    }

} 