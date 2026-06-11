package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.Item;
import com.example.onlinestore.bean.ItemDetail;
import com.example.onlinestore.bean.Sku;
import com.example.onlinestore.errors.ErrorCode;
import com.example.onlinestore.exceptions.BizException;
import com.example.onlinestore.service.ItemDetailService;
import com.example.onlinestore.service.ItemService;
import com.example.onlinestore.service.SkuService;
import com.example.onlinestore.utils.JacksonJsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ItemDetailServiceImpl implements ItemDetailService {
    private static final Logger logger = LoggerFactory.getLogger(ItemDetailServiceImpl.class);
    private static final String CACHE_KEY_PREFIX = "ITEM_DETAIL:";
    private static final long CACHE_EXPIRE_TIME = 30;
    private static final long NULL_CACHE_EXPIRE_TIME = 5;
    private static final String NULL_CACHE_VALUE = "NULL";

    private final Map<Long, Object> loadLocks = new ConcurrentHashMap<>();

    private final ItemService itemService;
    private final SkuService skuService;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    @Transactional(readOnly = true)
    public ItemDetail getItemDetail(@NotNull Long itemId) {
        if (itemId == null) {
            throw new IllegalArgumentException("itemId is null");
        }

        ItemDetail result = loadFromCache(itemId);
        if (result != null) {
            return result;
        }

        Object lock = loadLocks.computeIfAbsent(itemId, k -> new Object());
        synchronized (lock) {
            result = loadFromCache(itemId);
            if (result != null) {
                return result;
            }
            return loadFromDbAndCache(itemId);
        }
    }

    private ItemDetail loadFromCache(Long itemId) {
        String cacheKey = CACHE_KEY_PREFIX + itemId;
        String cachedValue = redisTemplate.opsForValue().get(cacheKey);

        if (cachedValue == null) {
            return null;
        }
        if (NULL_CACHE_VALUE.equals(cachedValue)) {
            throw new BizException(ErrorCode.ITEM_NOT_FOUND);
        }
        try {
            return JacksonJsonUtils.toObject(cachedValue, ItemDetail.class);
        } catch (IOException e) {
            logger.error("Failed to deserialize cached item detail, itemId: {}", itemId, e);
            redisTemplate.delete(cacheKey);
            return null;
        }
    }

    private ItemDetail loadFromDbAndCache(Long itemId) {
        String cacheKey = CACHE_KEY_PREFIX + itemId;

        Item item;
        try {
            item = itemService.getItemById(itemId);
        } catch (BizException e) {
            redisTemplate.opsForValue().set(cacheKey, NULL_CACHE_VALUE, NULL_CACHE_EXPIRE_TIME, TimeUnit.MINUTES);
            throw e;
        }

        List<Sku> skus = skuService.getSkusByItemId(itemId);

        ItemDetail result = new ItemDetail();
        result.setItem(item);
        result.setSkus(skus);

        try {
            String jsonValue = JacksonJsonUtils.toString(result);
            redisTemplate.opsForValue().set(cacheKey, jsonValue, CACHE_EXPIRE_TIME, TimeUnit.MINUTES);
        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize item detail for caching, itemId: {}", itemId, e);
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        return result;
    }
}