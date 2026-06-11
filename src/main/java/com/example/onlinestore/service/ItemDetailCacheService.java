package com.example.onlinestore.service;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemDetailCacheService {
    private static final Logger logger = LoggerFactory.getLogger(ItemDetailCacheService.class);
    private static final String CACHE_KEY_PREFIX = "ITEM_DETAIL:";

    private final RedisTemplate<String, String> redisTemplate;

    public void evictItemDetailCache(@NotNull Long itemId) {
        try {
            redisTemplate.delete(CACHE_KEY_PREFIX + itemId);
        } catch (Exception e) {
            logger.warn("Failed to evict item detail cache, itemId: {}", itemId, e);
        }
    }
}
