package com.example.onlinestore.cache;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 缓存管理器接口，定义缓存的基本操作
 */
public interface CacheManager {

    class CacheItem<T> {
        private final T value;
        private final long expireTime; // 过期时间戳，-1表示永不过期

        public CacheItem(T value, long expireTime) {
            this.value = value;
            this.expireTime = expireTime;
        }

        public T getValue() {
            return value;
        }

        public boolean isExpired() {
            return expireTime > 0 && System.currentTimeMillis() > expireTime;
        }
    }

    /**
     * 获取缓存
     * @param key 缓存键
     * @param <T> 返回类型
     * @return 缓存值，如果不存在则返回null
     */
    <T> T get(String key,Class<T> clazz);
    
    /**
     * 设置缓存
     * @param key 缓存键
     * @param value 缓存值
     * @param <T> 值类型
     */
    <T> void set(String key, T value);
    
    /**
     * 设置缓存并指定过期时间
     * @param key 缓存键
     * @param value 缓存值
     * @param expireSeconds 过期时间（秒）
     * @param <T> 值类型
     */
    <T> void set(String key, T value, long expireSeconds);
    
    /**
     * 删除缓存
     * @param key 缓存键
     */
    void delete(String key);
    
    /**
     * 判断缓存是否存在
     * @param key 缓存键
     * @return 是否存在
     */
    boolean exists(String key);
    
    /**
     * 清空所有缓存
     */
    void clear();
} 