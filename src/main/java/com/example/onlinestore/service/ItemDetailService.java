package com.example.onlinestore.service;

import com.example.onlinestore.dto.ItemDetailDTO;

/**
 * 商品详情服务接口
 */
public interface ItemDetailService {
    
    /**
     * 获取商品详情
     * @param itemId 商品ID
     * @param skuId 指定的SKU ID，可为null
     * @return 商品详情DTO
     */
    ItemDetailDTO getItemDetail(Long itemId, Long skuId);
    
    /**
     * 获取商品详情（包含用户个性化推荐）
     * @param itemId 商品ID
     * @param skuId 指定的SKU ID，可为null
     * @param userId 用户ID
     * @return 商品详情DTO
     */
    ItemDetailDTO getItemDetailWithRecommendation(Long itemId, Long skuId, String userId);
    
    /**
     * 增加商品浏览量
     * @param itemId 商品ID
     */
    void incrementViewCount(Long itemId);
    
    /**
     * 获取商品详情内容（富文本）
     * @param itemId 商品ID
     * @return 商品详情内容
     */
    String getItemDetailContent(Long itemId);
} 