package com.example.onlinestore.config;

import com.example.onlinestore.bean.Brand;
import com.example.onlinestore.bean.Category;
import com.example.onlinestore.cache.CacheConstants;
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

    @Bean
    public Cache<String, MemberEntity> memberByNameCache() {
        return Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(CacheConstants.MEMBER_TTL_MINUTES, TimeUnit.MINUTES)
                .recordStats()
                .build();
    }

    @Bean
    public Cache<Long, MemberEntity> memberByIdCache() {
        return Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(CacheConstants.MEMBER_TTL_MINUTES, TimeUnit.MINUTES)
                .recordStats()
                .build();
    }

    @Bean
    public Cache<String, Boolean> memberNotFoundCache() {
        return Caffeine.newBuilder()
                .maximumSize(5000)
                .expireAfterWrite(CacheConstants.MEMBER_NOT_FOUND_TTL_MINUTES, TimeUnit.MINUTES)
                .recordStats()
                .build();
    }

    @Bean
    public Cache<Long, Brand> brandCache() {
        return Caffeine.newBuilder()
                .maximumSize(5000)
                .expireAfterWrite(CacheConstants.BRAND_TTL_MINUTES, TimeUnit.MINUTES)
                .recordStats()
                .build();
    }

    @Bean
    public Cache<Long, Category> categoryCache() {
        return Caffeine.newBuilder()
                .maximumSize(5000)
                .expireAfterWrite(CacheConstants.CATEGORY_TTL_MINUTES, TimeUnit.MINUTES)
                .recordStats()
                .build();
    }

    @Bean
    public Cache<String, Integer> accessCountQueryCache() {
        return Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(CacheConstants.ACCESS_COUNT_TTL_SECONDS, TimeUnit.SECONDS)
                .recordStats()
                .build();
    }

    @Bean
    public Cache<String, List<Map<String, Object>>> hotItemsCache() {
        return Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(CacheConstants.HOT_ITEMS_TTL_MINUTES, TimeUnit.MINUTES)
                .recordStats()
                .build();
    }
}
