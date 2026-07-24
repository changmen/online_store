package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.Item;
import com.example.onlinestore.bean.ItemDetail;
import com.example.onlinestore.bean.Sku;
import com.example.onlinestore.cache.CacheConstants;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
public class ItemDetailServiceImpl implements ItemDetailService {
    private static final Logger logger = LoggerFactory.getLogger(ItemDetailServiceImpl.class);
    private static final int MAX_LOAD_LOCKS = 10_000;

    private final ConcurrentHashMap<Long, ReentrantLock> loadLocks = new ConcurrentHashMap<>();

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

        ReentrantLock lock = loadLocks.computeIfAbsent(itemId, k -> new ReentrantLock());
        lock.lock();
        try {
            result = loadFromCache(itemId);
            if (result != null) {
                return result;
            }
            return loadFromDbAndCache(itemId);
        } finally {
            lock.unlock();
            if (loadLocks.size() > MAX_LOAD_LOCKS && !lock.hasQueuedThreads()) {
                loadLocks.remove(itemId, lock);
            }
        }
    }

    private ItemDetail loadFromCache(Long itemId) {
        String cacheKey = CacheConstants.itemDetailKey(itemId);
        String cachedValue = redisTemplate.opsForValue().get(cacheKey);

        if (cachedValue == null) {
            return null;
        }
        if (CacheConstants.ITEM_DETAIL_NULL_VALUE.equals(cachedValue)) {
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
        String cacheKey = CacheConstants.itemDetailKey(itemId);

        Item item;
        try {
            item = itemService.getItemById(itemId);
        } catch (BizException e) {
            redisTemplate.opsForValue().set(cacheKey, CacheConstants.ITEM_DETAIL_NULL_VALUE, CacheConstants.ITEM_DETAIL_NULL_TTL_MINUTES, TimeUnit.MINUTES);
            throw e;
        }

        List<Sku> skus = skuService.getSkusByItemId(itemId);

        ItemDetail result = new ItemDetail();
        result.setItem(item);
        result.setSkus(skus);

        try {
            String jsonValue = JacksonJsonUtils.toString(result);
            redisTemplate.opsForValue().set(cacheKey, jsonValue, CacheConstants.ITEM_DETAIL_TTL_MINUTES, TimeUnit.MINUTES);
        } catch (JsonProcessingException e) {
            logger.error("Failed to serialize item detail for caching, itemId: {}", itemId, e);
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        return result;
    }
}