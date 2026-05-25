package com.example.onlinestore.controller;

import com.example.onlinestore.dto.Response;
import com.example.onlinestore.dto.UserItemStatDTO;
import com.example.onlinestore.service.UserItemStatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户商品统计控制器
 */
@RestController
@RequestMapping("/api/v1/user-item-stats")
public class UserItemStatController {
    
    private static final Logger logger = LoggerFactory.getLogger(UserItemStatController.class);
    
    private final UserItemStatService userItemStatService;

    public UserItemStatController(UserItemStatService userItemStatService) {
        this.userItemStatService = userItemStatService;
    }
    
    /**
     * 获取单个用户的商品统计信息
     */
    @GetMapping("/{userId}")
    public Response<UserItemStatDTO> getUserItemStat(@PathVariable("userId") String userId) {
        logger.info("API - Get user item stat for user: {}", userId);
        
        UserItemStatDTO stat = userItemStatService.getUserItemStat(userId);
        if (stat == null) {
            return Response.fail("用户不存在或没有商品统计信息");
        }
        
        return Response.success(stat);
    }
    
    /**
     * 批量获取多个用户的商品统计信息
     */
    @GetMapping("/batch")
    public Response<Map<String, UserItemStatDTO>> getUserItemStats(
            @RequestParam("userIds") String userIdsStr) {
        
        List<String> userIds = Arrays.stream(userIdsStr.split(","))
                .map(String::trim)
                .collect(Collectors.toList());

        
        Map<String, UserItemStatDTO> stats =  userItemStatService.getUserItemStatsNew(userIds);

        
        return Response.success(stats);
    }
    
    /**
     * 获取购买了指定商品的用户统计信息
     */
    @GetMapping("/by-item/{itemId}")
    public Response<List<UserItemStatDTO>> getUserStatsByItemId(@PathVariable("itemId") Long itemId) {
        logger.info("API - Get user stats by item id: {}", itemId);
        
        List<UserItemStatDTO> stats = userItemStatService.getUserStatsByItemId(itemId);
        return Response.success(stats);
    }
    
    /**
     * 获取最活跃的用户
     */
    @GetMapping("/most-active")
    public Response<List<UserItemStatDTO>> getMostActiveUsers(
            @RequestParam(value = "limit", defaultValue = "10") int limit) {
        logger.info("API - Get most active users, limit: {}", limit);
        
        List<UserItemStatDTO> stats = userItemStatService.getMostActiveUsers(limit);
        return Response.success(stats);
    }

    @GetMapping("/user/items")
    public Response<String> concurrentTest(
            @RequestParam(value = "userCount", defaultValue = "100") int userCount) {

        List<String> userIds = new java.util.ArrayList<>();
        for (int i = 1; i <= userCount; i++) {
            userIds.add("test-user-" + i);
        }

        long startTime = System.currentTimeMillis();
        Map<String, UserItemStatDTO> result = userItemStatService.getUserItemStatsNew(userIds);
        long endTime = System.currentTimeMillis();

        return Response.success(String.format(
                "Concurrent test completed in %d ms. Retrieved %d user stats.",
                (endTime - startTime), result.size()));
    }
} 