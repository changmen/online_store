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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ItemDetailServiceImpl implements ItemDetailService {
    private static final Logger logger = LoggerFactory.getLogger(ItemDetailServiceImpl.class);
    private static final String CACHE_KEY_PREFIX = "ITEM_DETAIL:";
    private static final long CACHE_EXPIRE_TIME = 30;
    @Autowired
    private ItemService itemService;

    @Autowired
    private SkuService skuService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public ItemDetail getItemDetail(@NotNull Long itemId) {
        if (itemId == null) {
            throw new IllegalArgumentException("itemId is null");
        }

        // 1. 尝试从缓存获取
        String cacheKey = CACHE_KEY_PREFIX + itemId;
        String cachedValue = redisTemplate.opsForValue().get(cacheKey);
        
        if (cachedValue != null) {
            try {
                return JacksonJsonUtils.toObject(cachedValue, ItemDetail.class);
            } catch (IOException e) {
                logger.error("Failed to deserialize cached item detail, itemId: {}", itemId, e);
                redisTemplate.delete(cacheKey);
            }
        }

        // 2. 从数据库获取数据
        Item item = itemService.getItemById(itemId);
        if (item == null) {
            throw new BizException(ErrorCode.ITEM_NOT_FOUND);
        }

        List<Sku> skus = skuService.getSkusByItemId(itemId);

        // 3. 构建响应
        ItemDetail result = new ItemDetail();
        result.setItem(item);
        result.setSkus(skus);

        // 4. 更新缓存
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