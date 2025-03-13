package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.Item;
import com.example.onlinestore.bean.Sku;
import com.example.onlinestore.bean.VirtualItem;
import com.example.onlinestore.cache.CacheManager;
import com.example.onlinestore.dto.ItemQueryDTO;
import com.example.onlinestore.entity.ItemEntity;
import com.example.onlinestore.exception.ItemNameInvalidException;
import com.example.onlinestore.mapper.ItemMapper;
import com.example.onlinestore.service.ItemService;
import com.example.onlinestore.service.SkuService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    private static final Logger logger = LoggerFactory.getLogger(ItemServiceImpl.class);
    private static final int MAX_NAME_LENGTH = 64;
    private static final Pattern VALID_NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9\\u4e00-\\u9fa5\\s]+$");
    
    // 缓存相关常量
    private static final String CACHE_KEY_ITEM = "item:%d";
    private static final String CACHE_KEY_ITEMS_BY_CATEGORY = "items:category:%d:page:%d:size:%d";
    private static final String CACHE_KEY_ITEMS_BY_NAME = "items:name:%s:page:%d:size:%d";
    private static final String CACHE_KEY_ITEMS_ALL = "items:all:page:%d:size:%d";
    
    @Value("${cache.item.expire-seconds:3600}")
    private long itemCacheExpireSeconds;
    
    @Value("${cache.enabled:true}")
    private boolean cacheEnabled;

    @Autowired
    private ItemMapper itemMapper;
    
    @Autowired
    private SkuService skuService;
    
    @Autowired
    private CacheManager cacheManager;

    @Override
    public void addItem(String userId, Item item) {
        // 验证商品名称
        if (item.getName() == null || item.getName().trim().isEmpty()) {
            throw new ItemNameInvalidException("商品名称不能为空");
        }
        
        // 验证名称长度
        if (item.getName().length() > MAX_NAME_LENGTH) {
            throw new ItemNameInvalidException("商品名称不能超过64个字符");
        }
        
        // 验证名称是否包含特殊字符
        if (!VALID_NAME_PATTERN.matcher(item.getName()).matches()) {
            throw new ItemNameInvalidException("商品名称不能包含特殊字符");
        }
        
        // 验证名称是否重复
        ItemEntity existingItem = itemMapper.findByName(item.getName());
        if (existingItem != null) {
            throw new ItemNameInvalidException("商品名称已存在");
        }
        
        ItemEntity itemEntity = convertToItemEntity(item);
        itemMapper.insertItem(itemEntity);
        // 设置回ID
        item.setId(itemEntity.getId());
        
        // 清除相关缓存
        clearItemListCache();
    }

    @Override
    public Item getItemById(long itemId) {
        // 如果启用了缓存，先从缓存中获取
        if (cacheEnabled) {
            String cacheKey = String.format(CACHE_KEY_ITEM, itemId);
            Item cachedItem = cacheManager.get(cacheKey, Item.class);
            if (cachedItem != null) {
                logger.debug("Cache hit for item: {}", itemId);
                return cachedItem;
            }
        }
        
        // 缓存未命中，从数据库获取
        ItemEntity itemEntity = itemMapper.findById(itemId);
        Item item = convertToItem(itemEntity);
        
        // 如果启用了缓存且查询结果不为空，则缓存结果
        if (cacheEnabled && item != null) {
            String cacheKey = String.format(CACHE_KEY_ITEM, itemId);
            cacheManager.set(cacheKey, item, itemCacheExpireSeconds);
            logger.debug("Cached item: {}", itemId);
        }
        
        return item;
    }

    @Override
    public void updateItem(Item item) {
        // 验证商品名称
        if (item.getName() == null || item.getName().trim().isEmpty()) {
            throw new ItemNameInvalidException("商品名称不能为空");
        }
        
        // 验证名称长度
        if (item.getName().length() > MAX_NAME_LENGTH) {
            throw new ItemNameInvalidException("商品名称不能超过64个字符");
        }
        
        // 验证名称是否包含特殊字符
        if (!VALID_NAME_PATTERN.matcher(item.getName()).matches()) {
            throw new ItemNameInvalidException("商品名称不能包含特殊字符");
        }
        
        // 验证名称是否重复（排除当前商品）
        ItemEntity existingItem = itemMapper.findByNameExcludeId(item.getName(), item.getId());
        if (existingItem != null) {
            throw new ItemNameInvalidException("商品名称已存在");
        }
        
        ItemEntity itemEntity = convertToItemEntity(item);
        itemMapper.updateItem(itemEntity);
        
        // 更新缓存
        if (cacheEnabled) {
            // 更新单个商品缓存
            String cacheKey = String.format(CACHE_KEY_ITEM, item.getId());
            cacheManager.set(cacheKey, item, itemCacheExpireSeconds);
            
            // 清除列表缓存
            clearItemListCache();
        }
    }

    @Override
    public void deleteItem(long itemId) {
        itemMapper.deleteItem(itemId);
        
        // 删除缓存
        if (cacheEnabled) {
            // 删除单个商品缓存
            String cacheKey = String.format(CACHE_KEY_ITEM, itemId);
            cacheManager.delete(cacheKey);
            
            // 清除列表缓存
            clearItemListCache();
        }
    }

    @Override
    public List<Item> getAllItems(int page, int size) {

        // 缓存未命中，从数据库获取
        int offset = (page - 1) * size;
        List<ItemEntity> itemEntities = itemMapper.findAllWithPagination(offset, size);
        List<Item> items = itemEntities.stream()
                .map(this::convertToItem)
                .collect(Collectors.toList());

        return items;
    }
    
    @Override
    public List<Item> queryItems(ItemQueryDTO queryDTO) {

        
        // 缓存未命中，从数据库获取
        int offset = (queryDTO.getPage() - 1) * queryDTO.getSize();
        List<ItemEntity> itemEntities = itemMapper.findByCondition(
            queryDTO.getCategoryId(), 
            queryDTO.getName(), 
            offset, 
            queryDTO.getSize()
        );
        
        List<Item> items = itemEntities.stream()
                .map(this::convertToItem)
                .collect(Collectors.toList());
        

        return items;
    }
    
    @Override
    public long countItems(ItemQueryDTO queryDTO) {
        return itemMapper.countByCondition(
            queryDTO.getCategoryId(),
            queryDTO.getName()
        );
    }
    
    @Override
    public void addSkuToItem(Long itemId, Sku sku) {
        // 设置商品ID
        sku.setItemId(itemId);
        skuService.addSku(sku);
        
        // 如果是第一个SKU，更新商品的默认SKU ID
        Item item = getItemById(itemId);
        if (item.getSkuId() == null) {
            item.setSkuId(sku.getId());
            updateItem(item);
        }
        
        // 清除商品缓存
        if (cacheEnabled) {
            String cacheKey = String.format(CACHE_KEY_ITEM, itemId);
            cacheManager.delete(cacheKey);
        }
    }
    
    @Override
    public List<Sku> getSkusByItemId(Long itemId) {
        return skuService.getSkusByItemId(itemId);
    }
    
    @Override
    public void updateItemSku(Sku sku) {
        skuService.updateSku(sku);
        
        // 清除商品缓存
        if (cacheEnabled) {
            String cacheKey = String.format(CACHE_KEY_ITEM, sku.getItemId());
            cacheManager.delete(cacheKey);
        }
    }
    
    @Override
    public void deleteItemSku(Long skuId) {
        // 获取SKU信息，用于后续清除缓存
        Sku sku = skuService.getSkuById(skuId);
        Long itemId = sku != null ? sku.getItemId() : null;
        
        skuService.deleteSku(skuId);
        
        // 清除商品缓存
        if (cacheEnabled && itemId != null) {
            String cacheKey = String.format(CACHE_KEY_ITEM, itemId);
            cacheManager.delete(cacheKey);
        }
    }

    /**
     * 清除所有商品列表相关的缓存
     */
    private void clearItemListCache() {
        if (!cacheEnabled) {
            return;
        }
        
        // 这里可以使用更精细的缓存清除策略，但为了简单起见，我们使用通配符删除所有列表缓存
        // 在实际生产环境中，可能需要更精确的缓存失效策略
        logger.debug("Clearing all item list caches");
        cacheManager.clear();
    }

    private Item convertToItem(ItemEntity itemEntity) {
        if (itemEntity == null) {
            return null;
        }
        
        if (StringUtils.endsWithIgnoreCase(itemEntity.getName(), "test-item")){
            return new VirtualItem();
        }

        Item item = new Item();
        BeanCopier copier = BeanCopier.create(ItemEntity.class, Item.class, false);
        copier.copy(itemEntity, item, null);

        return item;
    }

    protected ItemEntity convertToItemEntity(Item item) {
        if (item == null) {
            return null;
        }
        
        ItemEntity itemEntity = new ItemEntity();
        BeanCopier copier = BeanCopier.create(Item.class, ItemEntity.class, false);
        copier.copy(item, itemEntity, null);
        return itemEntity;
    }
    
    @SuppressWarnings("unused")
    private ItemEntity buildItemEntity(String userId, String name, String description, String image, String secondaryName, String pingJia, Long skuId, Map<String, Map<String, String>> itemAttributes, Map<String, Map<String, String>> itemExtensions) {
        ItemEntity entity = new ItemEntity();
        entity.setName(name);
        entity.setDescription(description);
        entity.setImage(image);
        entity.setSecondaryName(secondaryName);
        entity.setSkuId(skuId);
        return entity;
    }
}
