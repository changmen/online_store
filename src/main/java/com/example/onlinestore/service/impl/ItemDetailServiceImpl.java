package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.Category;
import com.example.onlinestore.bean.Item;
import com.example.onlinestore.bean.Sku;
import com.example.onlinestore.cache.CacheManager;
import com.example.onlinestore.dto.CategoryDTO;
import com.example.onlinestore.dto.ItemDetailDTO;
import com.example.onlinestore.dto.ReviewStatisticsDTO;
import com.example.onlinestore.service.CategoryService;
import com.example.onlinestore.service.ItemService;
import com.example.onlinestore.service.ItemDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * 商品详情服务实现类
 */
@Service
public class ItemDetailServiceImpl implements ItemDetailService {
    
    private static final Logger logger = LoggerFactory.getLogger(ItemDetailServiceImpl.class);
    
    // 缓存相关常量
    private static final String CACHE_KEY_PRODUCT_DETAIL = "product:detail:%d";
    private static final String CACHE_KEY_PRODUCT_DETAIL_CONTENT = "product:detail:content:%d";
    
    @Value("${cache.product.detail.expire-seconds:1800}")
    private long productDetailCacheExpireSeconds;
    
    @Value("${cache.enabled:true}")
    private boolean cacheEnabled;
    
    // 创建线程池用于并行加载数据
    private final ExecutorService executorService = Executors.newFixedThreadPool(5);
    
    @Autowired
    private ItemService itemService;

    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private CacheManager cacheManager;
    
    @Override
    public ItemDetailDTO getItemDetail(Long itemId, Long skuId) {
        // 如果启用了缓存，先从缓存中获取
        if (cacheEnabled) {
            String cacheKey = String.format(CACHE_KEY_PRODUCT_DETAIL, itemId);
            ItemDetailDTO cachedDetail = cacheManager.get(cacheKey, ItemDetailDTO.class);
            if (cachedDetail != null) {
                logger.debug("Cache hit for product detail: {}", itemId);
                
                // 如果指定了SKU ID，需要更新选中的SKU
                if (skuId != null && cachedDetail.getSkus() != null) {
                    for (Sku sku : cachedDetail.getSkus()) {
                        if (skuId.equals(sku.getId())) {
                            cachedDetail.setSelectedSku(sku);
                            break;
                        }
                    }
                }
                
                return cachedDetail;
            }
        }
        
        // 缓存未命中，构建商品详情
        // 使用安全版本的方法，避免线程泄漏
        ItemDetailDTO detailDTO = buildItemDetail(itemId, skuId);
        
        // 如果启用了缓存且构建成功，则缓存结果
        if (cacheEnabled && detailDTO != null) {
            String cacheKey = String.format(CACHE_KEY_PRODUCT_DETAIL, itemId);
            cacheManager.set(cacheKey, detailDTO, productDetailCacheExpireSeconds);
            logger.debug("Cached product detail: {}", itemId);
        }
        
        return detailDTO;
    }
    
    @Override
    public ItemDetailDTO getItemDetailWithRecommendation(Long itemId, Long skuId, String userId) {
        // 获取基本商品详情
        ItemDetailDTO detailDTO = getItemDetail(itemId, skuId);
        
        if (detailDTO != null) {
            // 异步加载个性化推荐商品
            CompletableFuture.runAsync(() -> {
                try {
                    // 这里可以调用推荐系统API获取个性化推荐
                    // 为了演示，这里使用同类目下的其他商品作为推荐
                    if (detailDTO.getCategory() != null) {
                        List<Item> recommendedItems = getRecommendedItems(itemId, detailDTO.getCategory().getId(), userId);
                        detailDTO.setRecommendedItems(recommendedItems);
                    }
                } catch (Exception e) {
                    logger.error("Failed to load recommended items for item: {}, user: {}", itemId, userId, e);
                }
            }, executorService);
        }
        
        return detailDTO;
    }
    
    @Override
    public void incrementViewCount(Long itemId) {
        // 这里可以实现增加商品浏览量的逻辑
        // 通常会使用异步方式记录，避免影响主流程
        CompletableFuture.runAsync(() -> {
            try {
                // 记录浏览量的逻辑，可能需要调用统计服务
                logger.debug("Increment view count for item: {}", itemId);
            } catch (Exception e) {
                logger.error("Failed to increment view count for item: {}", itemId, e);
            }
        }, executorService);
    }
    
    @Override
    public String getItemDetailContent(Long itemId) {
        // 如果启用了缓存，先从缓存中获取
        if (cacheEnabled) {
            String cacheKey = String.format(CACHE_KEY_PRODUCT_DETAIL_CONTENT, itemId);
            String cachedContent = cacheManager.get(cacheKey, String.class);
            if (cachedContent != null) {
                logger.debug("Cache hit for product detail content: {}", itemId);
                return cachedContent;
            }
        }
        
        // 缓存未命中，从数据库或其他服务获取
        // 这里简化处理，实际可能需要从CMS系统或其他服务获取
        String detailContent = fetchProductDetailContent(itemId);
        
        // 如果启用了缓存且获取成功，则缓存结果
        if (cacheEnabled) {
            String cacheKey = String.format(CACHE_KEY_PRODUCT_DETAIL_CONTENT, itemId);
            cacheManager.set(cacheKey, detailContent, productDetailCacheExpireSeconds);
            logger.debug("Cached product detail content: {}", itemId);
        }
        
        return detailContent;
    }
    
    /**
     * 构建商品详情DTO
     */
    private ItemDetailDTO buildItemDetail(Long itemId, Long skuId) {
        // 获取商品基本信息
        Item item = itemService.getItemById(itemId);
        if (item == null) {
            logger.warn("Item not found: {}", itemId);
            return null;
        }
        
        ItemDetailDTO detailDTO = new ItemDetailDTO();
        detailDTO.setItem(item);
        

        ExecutorService categoryExecutor = Executors.newFixedThreadPool(2);
        ExecutorService skusExecutor = Executors.newFixedThreadPool(2);
        ExecutorService imagesExecutor = Executors.newFixedThreadPool(2);
        ExecutorService reviewExecutor = Executors.newFixedThreadPool(2);
        ExecutorService contentExecutor = Executors.newFixedThreadPool(2);
        
        // 并行加载类目信息
        CompletableFuture<Void> categoryFuture = CompletableFuture.runAsync(() -> {
            try {
                if (item.getCategoryId() != null) {
                    Category category = categoryService.getCategoryById(item.getCategoryId());
                    if (category != null) {
                        CategoryDTO categoryDTO = new CategoryDTO();
                        categoryDTO.setId(category.getId());
                        categoryDTO.setName(category.getName());
                        detailDTO.setCategory(categoryDTO);
                    }
                }
            } catch (Exception e) {
                logger.error("Failed to load category for item: {}", itemId, e);
            }
        }, categoryExecutor);
        
        // 并行加载SKU信息
        CompletableFuture<Void> skusFuture = CompletableFuture.runAsync(() -> {
            try {

                List<Sku> skus = itemService.getSkusByItemId(itemId);
                detailDTO.setSkus(skus);
                
                // 设置选中的SKU
                if (skuId != null && skus != null) {
                    for (Sku sku : skus) {
                        if (skuId.equals(sku.getId())) {
                            detailDTO.setSelectedSku(sku);
                            break;
                        }
                    }
                } else if (item.getSkuId() != null && skus != null) {
                    // 如果没有指定SKU ID，使用商品默认SKU
                    for (Sku sku : skus) {
                        if (item.getSkuId().equals(sku.getId())) {
                            detailDTO.setSelectedSku(sku);
                            break;
                        }
                    }
                } else if (skus != null && !skus.isEmpty()) {
                    // 如果没有默认SKU，使用第一个SKU
                    detailDTO.setSelectedSku(skus.get(0));
                }
                
                // 提取规格信息
                if (skus != null && !skus.isEmpty()) {
                    detailDTO.setSpecifications(extractSpecifications(skus));
                }
            } catch (Exception e) {
                logger.error("Failed to load SKUs for item: {}", itemId, e);
            }
        }, skusExecutor);
        
        // 并行加载图片信息
        CompletableFuture<Void> imagesFuture = CompletableFuture.runAsync(() -> {
            try {

                // 这里可以从图片服务获取商品图片列表
                // 为了演示，使用商品主图和SKU图片作为图片列表
                List<String> images = new ArrayList<>();
                if (item.getImage() != null) {
                    images.add(item.getImage());
                }
                
                // 添加SKU图片
                if (detailDTO.getSkus() != null) {
                    for (Sku sku : detailDTO.getSkus()) {
                        if (sku.getImages() != null) {
                            // 假设SKU的images字段是JSON数组字符串
                            // 实际应用中可能需要解析JSON
                            images.add(sku.getImages());
                        }
                    }
                }
                
                detailDTO.setImages(images);
            } catch (Exception e) {
                logger.error("Failed to load images for item: {}", itemId, e);
            }
        }, imagesExecutor);
        
        // 并行加载评价统计信息
        CompletableFuture<Void> reviewStatsFuture = CompletableFuture.runAsync(() -> {
            try {

                // 这里可以从评价服务获取评价统计信息
                // 为了演示，使用模拟数据
                ReviewStatisticsDTO reviewStats = createMockReviewStatistics();
                detailDTO.setReviewStatistics(reviewStats);
            } catch (Exception e) {
                logger.error("Failed to load review statistics for item: {}", itemId, e);
            }
        }, reviewExecutor);
        
        // 并行加载商品详情内容
        CompletableFuture<Void> detailContentFuture = CompletableFuture.runAsync(() -> {
            try {
                
                String detailContent = getItemDetailContent(itemId);
                detailDTO.setDetailContent(detailContent);
            } catch (Exception e) {
                logger.error("Failed to load detail content for item: {}", itemId, e);
            }
        }, contentExecutor);
        
        // 等待所有异步任务完成
        try {
            CompletableFuture.allOf(
                    categoryFuture, 
                    skusFuture, 
                    imagesFuture, 
                    reviewStatsFuture, 
                    detailContentFuture
            ).get();
        } catch (Exception e) {
            logger.error("Error while building product detail for item: {}", itemId, e);
        }
        
        return detailDTO;
    }

    
    /**
     * 从SKU列表中提取规格信息
     */
    private Map<String, List<String>> extractSpecifications(List<Sku> skus) {
        // 实际应用中，规格信息可能存储在SKU的属性中
        // 这里简化处理，假设SKU的name包含规格信息，格式如："iPhone 13 Pro 128GB 银色"
        Map<String, List<String>> specs = new HashMap<>();
        
        // 模拟提取规格信息
        List<String> colors = new ArrayList<>();
        List<String> storages = new ArrayList<>();
        
        for (Sku sku : skus) {
            String name = sku.getName();
            if (name != null) {
                // 模拟提取颜色
                if (name.contains("银色")) {
                    colors.add("银色");
                } else if (name.contains("金色")) {
                    colors.add("金色");
                } else if (name.contains("黑色")) {
                    colors.add("黑色");
                }
                
                // 模拟提取存储容量
                if (name.contains("128GB")) {
                    storages.add("128GB");
                } else if (name.contains("256GB")) {
                    storages.add("256GB");
                } else if (name.contains("512GB")) {
                    storages.add("512GB");
                }
            }
        }
        
        // 去重
        colors = colors.stream().distinct().collect(Collectors.toList());
        storages = storages.stream().distinct().collect(Collectors.toList());
        
        if (!colors.isEmpty()) {
            specs.put("颜色", colors);
        }
        
        if (!storages.isEmpty()) {
            specs.put("存储容量", storages);
        }
        
        return specs;
    }
    
    /**
     * 获取推荐商品列表
     */
    private List<Item> getRecommendedItems(Long itemId, Long categoryId, String userId) {
        // 实际应用中，可能需要调用推荐系统API
        // 这里简化处理，使用同类目下的其他商品作为推荐
        try {
            // 构建查询条件
            com.example.onlinestore.dto.ItemQueryDTO queryDTO = new com.example.onlinestore.dto.ItemQueryDTO();
            queryDTO.setCategoryId(categoryId);
            queryDTO.setPage(1);
            queryDTO.setSize(10);
            
            List<Item> items = itemService.queryItems(queryDTO);
            
            // 过滤掉当前商品
            return items.stream()
                    .filter(item -> !item.getId().equals(itemId))
                    .limit(5)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Failed to get recommended items", e);
            return Collections.emptyList();
        }
    }
    
    /**
     * 获取商品详情内容
     */
    private String fetchProductDetailContent(Long itemId) {
        // 实际应用中，可能需要从CMS系统或其他服务获取
        // 这里简化处理，返回模拟数据
        return "<div class=\"product-detail\">" +
                "<h2>商品详情</h2>" +
                "<p>这是商品ID为" + itemId + "的详细描述内容。</p>" +
                "<div class=\"specs\">" +
                "<h3>规格参数</h3>" +
                "<ul>" +
                "<li>品牌: Apple</li>" +
                "<li>型号: iPhone 13 Pro</li>" +
                "<li>上市年份: 2021年</li>" +
                "<li>操作系统: iOS</li>" +
                "</ul>" +
                "</div>" +
                "<div class=\"features\">" +
                "<h3>产品特点</h3>" +
                "<p>超视网膜XDR显示屏，ProMotion自适应刷新率技术，最高120Hz刷新率</p>" +
                "<p>A15仿生芯片，6核CPU，5核GPU</p>" +
                "<p>Pro级摄像头系统：超广角、广角和长焦三摄</p>" +
                "</div>" +
                "</div>";
    }
    
    /**
     * 创建模拟评价统计数据
     */
    private ReviewStatisticsDTO createMockReviewStatistics() {
        ReviewStatisticsDTO stats = new ReviewStatisticsDTO();
        stats.setTotalCount(128);
        stats.setPositiveCount(100);
        stats.setNeutralCount(20);
        stats.setNegativeCount(8);
        stats.setAverageRating(new BigDecimal("4.7"));
        stats.setWithImageCount(45);
        stats.setWithAdditionalCount(30);
        
        Map<Integer, Integer> distribution = new HashMap<>();
        distribution.put(5, 90);
        distribution.put(4, 10);
        distribution.put(3, 20);
        distribution.put(2, 5);
        distribution.put(1, 3);
        stats.setRatingDistribution(distribution);
        
        return stats;
    }
} 