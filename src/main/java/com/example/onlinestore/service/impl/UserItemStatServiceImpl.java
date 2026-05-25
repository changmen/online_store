package com.example.onlinestore.service.impl;

import com.example.onlinestore.dto.UserItemStatDTO;
import com.example.onlinestore.mapper.UserItemStatMapper;
import com.example.onlinestore.service.UserItemStatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserItemStatServiceImpl implements UserItemStatService {
    
    private static final Logger logger = LoggerFactory.getLogger(UserItemStatServiceImpl.class);
    private static final int RECENT_ITEMS_LIMIT = 5;
    
    private final UserItemStatMapper userItemStatMapper;

    public UserItemStatServiceImpl(UserItemStatMapper userItemStatMapper) {
        this.userItemStatMapper = userItemStatMapper;
    }
    
    @Override
    @Cacheable(value = "userItemStat", key = "#userId")
    public UserItemStatDTO getUserItemStat(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return null;
        }
        
        try {
            // 获取用户查看商品的数量
            int viewCount = userItemStatMapper.countUserViewedItems(userId);
            
            // 获取用户购买商品的数量
            int purchaseCount = userItemStatMapper.countUserPurchasedItems(userId);
            
            // 获取用户最近查看的商品ID列表
            List<Long> recentViewedItemIds = userItemStatMapper.findRecentViewedItemIds(userId, RECENT_ITEMS_LIMIT);
            
            // 获取用户最近购买的商品ID列表
            List<Long> recentPurchasedItemIds = userItemStatMapper.findRecentPurchasedItemIds(userId, RECENT_ITEMS_LIMIT);
            
            // 获取用户总消费金额
            long totalSpent = userItemStatMapper.sumUserTotalSpent(userId);
            
            // 创建并返回DTO
            return new UserItemStatDTO(
                    userId,
                    viewCount,
                    purchaseCount,
                    recentViewedItemIds,
                    recentPurchasedItemIds,
                    totalSpent
            );
        } catch (Exception e) {
            logger.error("Failed to get user item stat for user: {}", userId, e);
            return null;
        }
    }
    
    @Override
    public Map<String, UserItemStatDTO> getUserItemStats(List<String> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return Collections.emptyMap();
        }
        
        try {
            Map<String, UserItemStatDTO> result = new HashMap<>();
            
            // 批量获取用户查看商品的数量
            List<Map<String, Object>> viewCounts = userItemStatMapper.batchCountUserViewedItems(userIds);
            Map<String, Integer> viewCountMap = viewCounts.stream()
                    .collect(Collectors.toMap(
                            m -> (String) m.get("user_id"),
                            m -> ((Number) m.get("view_count")).intValue()
                    ));
            
            // 批量获取用户购买商品的数量
            List<Map<String, Object>> purchaseCounts = userItemStatMapper.batchCountUserPurchasedItems(userIds);
            Map<String, Integer> purchaseCountMap = purchaseCounts.stream()
                    .collect(Collectors.toMap(
                            m -> (String) m.get("user_id"),
                            m -> ((Number) m.get("purchase_count")).intValue()
                    ));
            
            // 批量获取用户总消费金额
            List<Map<String, Object>> totalSpents = userItemStatMapper.batchSumUserTotalSpent(userIds);
            Map<String, Long> totalSpentMap = totalSpents.stream()
                    .collect(Collectors.toMap(
                            m -> (String) m.get("user_id"),
                            m -> ((Number) m.get("total_spent")).longValue()
                    ));
            
            // 为每个用户创建DTO
            for (String userId : userIds) {
                // 获取用户最近查看的商品ID列表
                List<Long> recentViewedItemIds = userItemStatMapper.findRecentViewedItemIds(userId, RECENT_ITEMS_LIMIT);
                
                // 获取用户最近购买的商品ID列表
                List<Long> recentPurchasedItemIds = userItemStatMapper.findRecentPurchasedItemIds(userId, RECENT_ITEMS_LIMIT);
                
                // 创建DTO
                UserItemStatDTO dto = new UserItemStatDTO(
                        userId,
                        viewCountMap.getOrDefault(userId, 0),
                        purchaseCountMap.getOrDefault(userId, 0),
                        recentViewedItemIds,
                        recentPurchasedItemIds,
                        totalSpentMap.getOrDefault(userId, 0L)
                );
                
                // 添加到结果集
                result.put(userId, dto);
            }
            
            return result;
        } catch (Exception e) {
            logger.error("Failed to get user item stats", e);
            return Collections.emptyMap();
        }
    }
    
    /**
     * 按用户统计商品
     */
    @Override
    public Map<String, UserItemStatDTO> getUserItemStatsNew(List<String> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return Collections.emptyMap();
        }
        
        try {
            Map<String, UserItemStatDTO> result = new HashMap<>();
            
            userIds.parallelStream().forEach(userId -> {
                try {
                    // 获取用户查看商品的数量
                    int viewCount = userItemStatMapper.countUserViewedItems(userId);
                    
                    // 获取用户购买商品的数量
                    int purchaseCount = userItemStatMapper.countUserPurchasedItems(userId);
                    
                    // 获取用户最近查看的商品ID列表
                    List<Long> recentViewedItemIds = userItemStatMapper.findRecentViewedItemIds(userId, RECENT_ITEMS_LIMIT);
                    
                    // 获取用户最近购买的商品ID列表
                    List<Long> recentPurchasedItemIds = userItemStatMapper.findRecentPurchasedItemIds(userId, RECENT_ITEMS_LIMIT);
                    
                    // 获取用户总消费金额
                    long totalSpent = userItemStatMapper.sumUserTotalSpent(userId);
                    
                    // 创建DTO
                    UserItemStatDTO dto = new UserItemStatDTO(
                            userId,
                            viewCount,
                            purchaseCount,
                            recentViewedItemIds,
                            recentPurchasedItemIds,
                            totalSpent
                    );
                    
                    result.put(userId, dto);
                    
                } catch (Exception e) {
                    logger.error("Failed to get user item stat for user: {}", userId, e);
                }
            });
            
            for (String userId : userIds) {
                // 检查结果集中是否已存在该用户的统计信息
                if (!result.containsKey(userId)) {
                    // 如果不存在，创建一个空的统计信息
                    UserItemStatDTO emptyDto = new UserItemStatDTO(
                            userId,
                            0,
                            0,
                            Collections.emptyList(),
                            Collections.emptyList(),
                            0L
                    );
                    
                    result.put(userId, emptyDto);
                }
            }
            
            return result;
        } catch (Exception e) {
            logger.error("Failed to get user item stats ", e);
            return Collections.emptyMap();
        }
    }
    
    @Override
    public List<UserItemStatDTO> getUserStatsByItemId(Long itemId) {
        if (itemId == null) {
            return Collections.emptyList();
        }
        
        try {
            // 获取购买了指定商品的用户ID列表
            List<String> userIds = userItemStatMapper.findUserIdsByPurchasedItemId(itemId);
            
            if (userIds.isEmpty()) {
                return Collections.emptyList();
            }
            
            // 获取这些用户的统计信息
            Map<String, UserItemStatDTO> userStats = getUserItemStats(userIds);
            
            // 转换为列表并返回
            return userIds.stream()
                    .map(userStats::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Failed to get user stats by item id: {}", itemId, e);
            return Collections.emptyList();
        }
    }
    
    @Override
    public List<UserItemStatDTO> getMostActiveUsers(int limit) {
        if (limit <= 0) {
            return Collections.emptyList();
        }
        
        try {
            // 获取最活跃的用户ID列表
            List<String> userIds = userItemStatMapper.findMostActiveUserIds(limit);
            
            if (userIds.isEmpty()) {
                return Collections.emptyList();
            }
            
            // 获取这些用户的统计信息
            Map<String, UserItemStatDTO> userStats = getUserItemStats(userIds);
            
            // 按照原始顺序转换为列表并返回
            return userIds.stream()
                    .map(userStats::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Failed to get most active users", e);
            return Collections.emptyList();
        }
    }
} 