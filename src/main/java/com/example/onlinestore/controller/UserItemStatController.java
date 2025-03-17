package com.example.onlinestore.controller;

import com.example.onlinestore.dto.Response;
import com.example.onlinestore.dto.UserItemStatDTO;
import com.example.onlinestore.service.UserItemStatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    
    @Autowired
    private UserItemStatService userItemStatService;
    
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
    
    /**
     * 并发测试端点（展示不安全实现的问题）
     */
    @GetMapping("/concurrent-test")
    public Response<String> concurrentTest(
            @RequestParam(value = "userCount", defaultValue = "100") int userCount) {

        try {
            // 生成测试数据
            List<String> userIds = new java.util.ArrayList<>();
            for (int i = 1; i <= userCount; i++) {
                userIds.add("test-user-" + i);
            }
            
            // 记录开始时间
            long startTime = System.currentTimeMillis();
            
            // 执行并发测试
            Map<String, UserItemStatDTO> result =userItemStatService.getUserItemStatsNew(userIds);

            
            // 记录结束时间
            long endTime = System.currentTimeMillis();
            
            // 返回结果
            return Response.success(String.format(
                    "Concurrent test completed in %d ms. Retrieved %d user stats.",
                    (endTime - startTime), result.size()));
        } catch (Exception e) {
            logger.error("Failed to run concurrent test", e);
            return Response.fail("并发测试失败: " + e.getMessage());
        }
    }
} 