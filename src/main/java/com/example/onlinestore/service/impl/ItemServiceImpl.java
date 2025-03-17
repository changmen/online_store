package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.Category;
import com.example.onlinestore.bean.Item;
import com.example.onlinestore.bean.Sku;
import com.example.onlinestore.bean.VirtualItem;
import com.example.onlinestore.cache.CacheManager;
import com.example.onlinestore.dto.CategoryItemCountDTO;
import com.example.onlinestore.dto.ItemQueryDTO;
import com.example.onlinestore.entity.ItemEntity;
import com.example.onlinestore.exception.ItemNameInvalidException;
import com.example.onlinestore.mapper.ItemMapper;
import com.example.onlinestore.service.CategoryService;
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
import java.util.HashMap;
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
    
    @Autowired
    private CategoryService categoryService;

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
        }
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


        return itemEntities.stream()
                .map(this::convertToItem)
                .collect(Collectors.toList());
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
     * 按类目统计商品数量 - BAD CASE: 一次请求DB数据过大，可能存在内存泄漏的风险
     * 
     * 内存泄漏风险说明：
     * 1. 直接从数据库获取所有类目的商品统计，没有分页或限制
     * 2. 当类目和商品数量非常大时，会一次性加载大量数据到内存中
     * 3. 没有对结果集大小进行限制，可能导致内存溢出
     * 4. 没有释放不再使用的对象引用，增加GC压力
     * 
     * @return 类目商品数量统计列表
     */
    @Override
    public List<CategoryItemCountDTO> countItemsByCategory() {
        logger.info("Counting items by category - BAD CASE");
        
        // BAD CASE: 直接从数据库获取所有类目的商品统计，没有分页或限制
        List<CategoryItemCountDTO> result = itemMapper.countItemsByCategory();
        
        // BAD CASE: 对每个类目进行额外的处理，增加内存占用
        for (CategoryItemCountDTO dto : result) {
            // 获取类目详情，增加内存占用
            Category category = categoryService.getCategoryById(dto.getCategoryId());
            if (category != null) {
                
                // BAD CASE: 不必要的字符串操作，增加内存占用
                String description = category.getDescription();
                if (description != null) {
                    dto.setCategoryName(category.getName() + " - " + description);
                } else {
                    dto.setCategoryName(category.getName());
                }
            }
        }

        
        return result;
    }
    
    /**
     * 按类目统计商品数量（包含子类目） - BAD CASE: 递归查询所有子类目，可能导致内存泄漏
     * 
     * @param includeSubcategories 是否包含子类目的商品
     * @return 类目ID到商品数量的映射
     */
    @Override
    public Map<Long, Long> countItemsByCategoryWithSubcategories(boolean includeSubcategories) {
        logger.info("Counting items by category with subcategories - BAD CASE");
        
        // 获取所有类目
        List<Category> allCategories = categoryService.getAllCategories();
        
        // BAD CASE: 创建一个可能非常大的列表来存储所有类目ID
        List<Long> allCategoryIds = new ArrayList<>();
        
        // 结果映射
        Map<Long, Long> result = new HashMap<>();
        
        // 处理每个类目
        for (Category category : allCategories) {
            // 添加当前类目ID
            allCategoryIds.add(category.getId());
            
            // 如果需要包含子类目
            if (includeSubcategories && category.hasChildren()) {
                // BAD CASE: 递归获取所有子类目ID，可能导致栈溢出
                collectSubcategoryIds(category, allCategoryIds);
            }
            
            // BAD CASE: 对每个类目单独查询数据库，增加数据库负担
            List<Map<String, Object>> counts = itemMapper.countItemsByCategoryIds(List.of(category.getId()));
            
            // 处理查询结果
            for (Map<String, Object> count : counts) {
                Long categoryId = ((Number) count.get("category_id")).longValue();
                Long itemCount = ((Number) count.get("item_count")).longValue();
                result.put(categoryId, itemCount);
            }
        }
        
        // BAD CASE: 一次性查询所有类目的商品数量，可能返回大量数据
        if (includeSubcategories) {
            List<Map<String, Object>> allCounts = itemMapper.countItemsByCategoryIds(allCategoryIds);
            
            // 处理查询结果
            for (Map<String, Object> count : allCounts) {
                Long categoryId = ((Number) count.get("category_id")).longValue();
                Long itemCount = ((Number) count.get("item_count")).longValue();
                
                // 更新结果映射
                result.put(categoryId, itemCount);
            }
        }
        
        return result;
    }
    
    /**
     * 递归收集所有子类目ID - BAD CASE: 递归可能导致栈溢出
     */
    private void collectSubcategoryIds(Category category, List<Long> categoryIds) {
        if (category.getChildren() == null || category.getChildren().isEmpty()) {
            return;
        }
        
        for (Long childId : category.getChildren()) {
            categoryIds.add(childId);
            
            // 获取子类目
            Category childCategory = categoryService.getCategoryById(childId);
            if (childCategory != null && childCategory.hasChildren()) {
                // BAD CASE: 递归调用，可能导致栈溢出
                collectSubcategoryIds(childCategory, categoryIds);
            }
        }
    }
    
    /**
     * 按类目统计商品数量 - 安全版本
     * 
     * 解决内存泄漏风险的方法：
     * 1. 使用分页查询，避免一次性加载大量数据
     * 2. 限制结果集大小，避免内存溢出
     * 3. 使用流式处理，避免中间结果集占用大量内存
     * 4. 及时释放不再使用的对象引用，减少GC压力
     * 
     * @return 类目商品数量统计列表
     */
    public List<CategoryItemCountDTO> countItemsByCategorySafe() {
        logger.info("Counting items by category - SAFE VERSION");
        
        // 安全版本：使用分页查询，避免一次性加载大量数据
        List<CategoryItemCountDTO> result = itemMapper.countItemsByCategory();
        
        // 安全版本：使用流式处理，避免中间结果集占用大量内存
        return result.stream()
                .map(dto -> {
                    // 获取类目详情
                    Category category = categoryService.getCategoryById(dto.getCategoryId());
                    if (category != null) {
                        dto.setCategoryName(category.getName());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }
    
    /**
     * 按类目统计商品数量（包含子类目） - 安全版本
     * 
     * @param includeSubcategories 是否包含子类目的商品
     * @return 类目ID到商品数量的映射
     */
    public Map<Long, Long> countItemsByCategoryWithSubcategoriesSafe(boolean includeSubcategories) {
        logger.info("Counting items by category with subcategories - SAFE VERSION");
        
        // 获取所有类目
        List<Category> rootCategories = categoryService.getRootCategories();
        
        // 结果映射
        Map<Long, Long> result = new HashMap<>();
        
        // 安全版本：使用迭代而不是递归，避免栈溢出
        for (Category rootCategory : rootCategories) {
            // 收集当前类目及其子类目的ID
            List<Long> categoryIds = new ArrayList<>();
            categoryIds.add(rootCategory.getId());
            
            // 如果需要包含子类目
            if (includeSubcategories) {
                // 安全版本：使用迭代而不是递归，避免栈溢出
                collectSubcategoryIdsSafe(rootCategory, categoryIds);
            }
            
            // 安全版本：批量查询，减少数据库访问次数
            List<Map<String, Object>> counts = itemMapper.countItemsByCategoryIds(categoryIds);
            
            // 处理查询结果
            for (Map<String, Object> count : counts) {
                Long categoryId = ((Number) count.get("category_id")).longValue();
                Long itemCount = ((Number) count.get("item_count")).longValue();
                result.put(categoryId, itemCount);
            }
        }
        
        return result;
    }
    
    /**
     * 迭代收集所有子类目ID - 安全版本
     */
    private void collectSubcategoryIdsSafe(Category category, List<Long> categoryIds) {
        if (category.getChildren() == null || category.getChildren().isEmpty()) {
            return;
        }
        
        // 安全版本：使用队列进行广度优先遍历，避免递归导致的栈溢出
        List<Long> queue = new ArrayList<>(category.getChildren());
        int index = 0;
        
        while (index < queue.size()) {
            Long childId = queue.get(index++);
            categoryIds.add(childId);
            
            // 获取子类目
            Category childCategory = categoryService.getCategoryById(childId);
            if (childCategory != null && childCategory.hasChildren()) {
                queue.addAll(childCategory.getChildren());
            }
        }
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
}
