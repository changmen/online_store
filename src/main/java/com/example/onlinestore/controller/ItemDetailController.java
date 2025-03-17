package com.example.onlinestore.controller;

import com.example.onlinestore.dto.ItemDetailDTO;
import com.example.onlinestore.dto.Response;
import com.example.onlinestore.service.ItemAccessLogService;
import com.example.onlinestore.service.ItemDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    
    @Autowired
    private ItemAccessLogService itemAccessLogService;
    
    @Value("${item.access.log.bad-case:false}")
    private boolean useAccessLogBadCase;
    
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
            // 记录商品访问
            recordItemAccess(itemId, userId, request);
            
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
    public Response<String> getProductDetailContent(
            @PathVariable("itemId") Long itemId,
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            HttpServletRequest request) {
        
        logger.info("Get product detail content: itemId={}", itemId);
        
        try {
            // 记录商品访问
            recordItemAccess(itemId, userId, request);
            
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
    
    /**
     * 记录商品访问
     */
    private void recordItemAccess(Long itemId, String userId, HttpServletRequest request) {
        try {
            String ip = getClientIp(request);
            String userAgent = request.getHeader("User-Agent");
            String referer = request.getHeader("Referer");
            String sessionId = request.getSession().getId();
            
            if (useAccessLogBadCase) {
                // 使用不良案例实现（共享变量未正确加锁）
                itemAccessLogService.recordAccessBadCase(itemId, userId, ip, userAgent, referer, sessionId);
            } else {
                // 使用安全实现
                itemAccessLogService.recordAccess(itemId, userId, ip, userAgent, referer, sessionId);
            }
        } catch (Exception e) {
            // 记录访问失败不应影响主流程
            logger.error("Failed to record item access", e);
        }
    }
    
    /**
     * 获取客户端IP地址
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
} 