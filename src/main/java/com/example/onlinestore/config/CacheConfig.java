package com.example.onlinestore.config;

import com.example.onlinestore.cache.CacheManager;
import com.example.onlinestore.cache.LocalCacheManager;
import com.example.onlinestore.cache.RedisCacheManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * 缓存配置类
 */
@Configuration
public class CacheConfig {
    
    @Value("${cache.type:local}")
    private String cacheType;
    
    @Autowired
    private LocalCacheManager localCacheManager;
    
    @Autowired
    private RedisCacheManager redisCacheManager;
    
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
    
    /**
     * 根据配置选择缓存管理器
     */
    @Bean
    @Primary
    public CacheManager cacheManager() {
        if ("redis".equalsIgnoreCase(cacheType)) {
            return redisCacheManager;
        } else {
            return localCacheManager;
        }
    }
} 