package com.example.onlinestore.config;

import com.example.onlinestore.bean.Brand;
import com.example.onlinestore.bean.Category;
import com.example.onlinestore.entity.MemberEntity;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Configuration
public class CaffeineCacheConfig {

    private static final long DEFAULT_TTL_MINUTES = 30;
    private static final long SHORT_TTL_SECONDS = 60;
    private static final long MEDIUM_TTL_MINUTES = 2;

    @Bean
    public Cache<String, MemberEntity> memberByNameCache() {
        return Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(DEFAULT_TTL_MINUTES, TimeUnit.MINUTES)
                .recordStats()
                .build();
    }

    @Bean
    public Cache<Long, MemberEntity> memberByIdCache() {
        return Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(DEFAULT_TTL_MINUTES, TimeUnit.MINUTES)
                .recordStats()
                .build();
    }

    @Bean
    public Cache<Long, Brand> brandCache() {
        return Caffeine.newBuilder()
                .maximumSize(5000)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .recordStats()
                .build();
    }

    @Bean
    public Cache<Long, Category> categoryCache() {
        return Caffeine.newBuilder()
                .maximumSize(5000)
                .recordStats()
                .build();
    }

    @Bean
    public Cache<String, Integer> accessCountQueryCache() {
        return Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(SHORT_TTL_SECONDS, TimeUnit.SECONDS)
                .recordStats()
                .build();
    }

    @Bean
    public Cache<String, List<Map<String, Object>>> hotItemsCache() {
        return Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(MEDIUM_TTL_MINUTES, TimeUnit.MINUTES)
                .recordStats()
                .build();
    }
}
