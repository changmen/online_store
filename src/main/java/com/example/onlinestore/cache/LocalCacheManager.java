package com.example.onlinestore.cache;

import com.example.onlinestore.exception.CacheOperateException;
import com.example.onlinestore.utils.JacksonJsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 本地缓存管理器实现
 */
@Component
public class LocalCacheManager implements CacheManager, DisposableBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalCacheManager.class);

    // 缓存数据
    private final Map<String, CacheItem<?>> cache = new ConcurrentHashMap<>();

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public LocalCacheManager() {
        // 每分钟执行一次清理过期缓存的任务
        scheduler.scheduleAtFixedRate(this::cleanExpiredCache, 1, 1, TimeUnit.MINUTES);
    }

    @Override
    public <T> T get(String key, Class<T> clazz) throws CacheOperateException {
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
            return JacksonJsonUtils.toObject(jsonValue, clazz);
        } catch (IOException e) {
            LOGGER.error("Failed to deserialize cache value for key: {}", key, e);
            throw new CacheOperateException("Failed to deserialize cache value for key: " + key);
        }
    }

    @Override
    public <T> void set(String key, T value) throws CacheOperateException {
        // 支持不过期缓存
        set(key, value, -1);
    }

    @Override
    public <T> void set(String key, T value, long expireSeconds) throws CacheOperateException {
        try {
            // 将对象序列化为JSON字符串
            String jsonValue = JacksonJsonUtils.toString(value);

            long expireTime = expireSeconds > 0 ?
                    System.currentTimeMillis() + expireSeconds * 1000 : -1;

            cache.put(key, new CacheItem<>(jsonValue, expireTime));
        } catch (JsonProcessingException e) {
            LOGGER.error("Failed to serialize cache value for key: {}", key, e);
            throw new CacheOperateException("Failed to serialize cache value for key: " + key);
        }
    }

    @Override
    public void delete(String key) {
        cache.remove(key);
        LOGGER.debug("Cache deleted: key={}", key);
    }

    @Override
    public boolean exists(String key) throws CacheOperateException {
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
    public void clear() throws CacheOperateException {
        cache.clear();
        LOGGER.debug("Cache cleared");
    }

    @Override
    public void destroy() throws Exception {
        scheduler.shutdownNow();
        LOGGER.debug("Cache destroyed");
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