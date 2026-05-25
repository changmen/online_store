package com.example.onlinestore.controller;

import com.example.onlinestore.dto.ItemDetailDTO;
import com.example.onlinestore.dto.Response;
import com.example.onlinestore.service.ItemAccessLogService;
import com.example.onlinestore.service.ItemDetailService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ItemDetailController {

    private static final Logger logger = LoggerFactory.getLogger(ItemDetailController.class);

    private final ItemDetailService itemDetailService;
    private final ItemAccessLogService itemAccessLogService;

    public ItemDetailController(ItemDetailService itemDetailService,
                                ItemAccessLogService itemAccessLogService) {
        this.itemDetailService = itemDetailService;
        this.itemAccessLogService = itemAccessLogService;
    }

    @GetMapping("/{itemId}")
    public Response<ItemDetailDTO> getProductDetail(
            @PathVariable("itemId") Long itemId,
            @RequestParam(value = "skuId", required = false) Long skuId,
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            HttpServletRequest request) {

        logger.info("Get product detail: itemId={}, skuId={}, userId={}", itemId, skuId, userId);

        recordItemAccess(itemId, userId, request);
        itemDetailService.incrementViewCount(itemId);

        ItemDetailDTO detailDTO;
        if (userId != null) {
            detailDTO = itemDetailService.getItemDetailWithRecommendation(itemId, skuId, userId);
        } else {
            detailDTO = itemDetailService.getItemDetail(itemId, skuId);
        }

        if (detailDTO == null) {
            return Response.fail("商品不存在");
        }

        return Response.success(detailDTO);
    }

    @GetMapping("/{itemId}/content")
    public Response<String> getProductDetailContent(
            @PathVariable("itemId") Long itemId,
            @RequestHeader(value = "X-User-Id", required = false) String userId,
            HttpServletRequest request) {

        logger.info("Get product detail content: itemId={}", itemId);

        recordItemAccess(itemId, userId, request);

        String content = itemDetailService.getItemDetailContent(itemId);
        if (content == null) {
            return Response.fail("商品详情内容不存在");
        }

        return Response.success(content);
    }

    private void recordItemAccess(Long itemId, String userId, HttpServletRequest request) {
        try {
            String ip = getClientIp(request);
            String userAgent = request.getHeader("User-Agent");
            String referer = request.getHeader("Referer");
            String sessionId = request.getSession().getId();

            itemAccessLogService.recordAccess(itemId, userId, ip, userAgent, referer, sessionId);
        } catch (Exception e) {
            logger.error("Failed to record item access", e);
        }
    }

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
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
