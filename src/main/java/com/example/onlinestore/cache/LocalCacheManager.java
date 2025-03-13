package com.example.onlinestore.cache;

import com.example.onlinestore.utils.JacksonJsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 本地缓存管理器实现
 */
@Component
public class LocalCacheManager implements CacheManager {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(LocalCacheManager.class);
    
    // 缓存数据
    private final Map<String, CacheItem<?>> cache = new ConcurrentHashMap<>();
    
    public LocalCacheManager() {
        // 每分钟执行一次清理过期缓存的任务
        // 定时清理过期缓存的线程池
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::cleanExpiredCache, 1, 1, TimeUnit.MINUTES);
    }
    
    @Override
    public <T> T get(String key,Class<T> clazz) {
        CacheItem<?> item = cache.get(key);
        if (item == null) {
            return null;
        }
        
        // 检查是否过期
        if (item.isExpired()) {
            cache.remove(key);
            return null;
        }
        String jsonValue = (String) item.getValue();
        
        try {
            // 将JSON字符串转换回原始类型
            return JacksonJsonUtils.toObject(jsonValue, clazz);
        } catch (Exception e) {
            LOGGER.error("Failed to deserialize cache value for key: {}", key, e);
            return null;
        }
    }
    
    @Override
    public <T> void set(String key, T value) {
        set(key, value, -1); // 不过期
    }
    
    @Override
    public <T> void set(String key, T value, long expireSeconds) {
        try {
            // 将对象序列化为JSON字符串
            String jsonValue = JacksonJsonUtils.toString(value);
            
            long expireTime = expireSeconds > 0 ? 
                    System.currentTimeMillis() + expireSeconds * 1000 : -1;
            
            cache.put(key, new CacheItem<>(jsonValue, expireTime));
            LOGGER.debug("Cache set: key={}, expireSeconds={}", key, expireSeconds);
        } catch (JsonProcessingException e) {
            LOGGER.error("Failed to serialize cache value for key: {}", key, e);
        }
    }
    
    @Override
    public void delete(String key) {
        cache.remove(key);
        LOGGER.debug("Cache deleted: key={}", key);
    }
    
    @Override
    public boolean exists(String key) {
        CacheItem<?> item = cache.get(key);
        if (item == null) {
            return false;
        }
        
        // 检查是否过期
        if (item.isExpired()) {
            cache.remove(key);
            return false;
        }
        
        return true;
    }
    
    @Override
    public void clear() {
        cache.clear();
        LOGGER.debug("Cache cleared");
    }
    
    /**
     * 清理过期的缓存项
     */
    private void cleanExpiredCache() {
        int count = 0;
        for (Map.Entry<String, CacheItem<?>> entry : cache.entrySet()) {
            if (entry.getValue().isExpired()) {
                cache.remove(entry.getKey());
                count++;
            }
        }
        if (count > 0) {
            LOGGER.debug("Cleaned {} expired cache items", count);
        }
    }

} 