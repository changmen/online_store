package com.example.onlinestore.controller;

import com.example.onlinestore.dto.ItemDetailDTO;
import com.example.onlinestore.dto.Response;
import com.example.onlinestore.service.ItemDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 商品详情控制器
 */
@RestController
@RequestMapping("/api/products")
public class ItemDetailController {
    
    private static final Logger logger = LoggerFactory.getLogger(ItemDetailController.class);
    
    @Autowired
    private ItemDetailService itemDetailService;
    
    /**
     * 获取商品详情
     */
    @GetMapping("/{itemId}")
    public Response<ItemDetailDTO> getProductDetail(
            @PathVariable("itemId") Long itemId,
            @RequestParam(value = "skuId", required = false) Long skuId,
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            HttpServletRequest request) {
        
        logger.info("Get product detail: itemId={}, skuId={}, userId={}", itemId, skuId, userId);
        
        try {
            // 增加商品浏览量（异步）
            itemDetailService.incrementViewCount(itemId);
            
            ItemDetailDTO detailDTO;
            if (userId != null) {
                // 如果有用户ID，获取带个性化推荐的商品详情
                detailDTO = itemDetailService.getItemDetailWithRecommendation(itemId, skuId, userId);
            } else {
                // 否则获取基本商品详情
                detailDTO = itemDetailService.getItemDetail(itemId, skuId);
            }
            
            if (detailDTO == null) {
                return Response.fail("商品不存在");
            }
            
            return Response.success(detailDTO);
        } catch (Exception e) {
            logger.error("Failed to get product detail", e);
            return Response.fail("获取商品详情失败");
        }
    }
    
    /**
     * 获取商品详情内容（富文本）
     */
    @GetMapping("/{itemId}/content")
    public Response<String> getProductDetailContent(@PathVariable("itemId") Long itemId) {
        logger.info("Get product detail content: itemId={}", itemId);
        
        try {
            String content = itemDetailService.getItemDetailContent(itemId);
            if (content == null) {
                return Response.fail("商品详情内容不存在");
            }
            
            return Response.success(content);
        } catch (Exception e) {
            logger.error("Failed to get product detail content", e);
            return Response.fail("获取商品详情内容失败");
        }
    }
} 