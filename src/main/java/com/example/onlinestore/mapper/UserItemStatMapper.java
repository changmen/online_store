package com.example.onlinestore.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 用户商品统计Mapper接口
 */
@Mapper
public interface UserItemStatMapper {
    
    /**
     * 获取用户查看商品的数量
     * 
     * @param userId 用户ID
     * @return 查看数量
     */
    int countUserViewedItems(@Param("userId") String userId);
    
    /**
     * 获取用户购买商品的数量
     * 
     * @param userId 用户ID
     * @return 购买数量
     */
    int countUserPurchasedItems(@Param("userId") String userId);
    
    /**
     * 获取用户最近查看的商品ID列表
     * 
     * @param userId 用户ID
     * @param limit 返回的记录数量
     * @return 商品ID列表
     */
    List<Long> findRecentViewedItemIds(@Param("userId") String userId, @Param("limit") int limit);
    
    /**
     * 获取用户最近购买的商品ID列表
     * 
     * @param userId 用户ID
     * @param limit 返回的记录数量
     * @return 商品ID列表
     */
    List<Long> findRecentPurchasedItemIds(@Param("userId") String userId, @Param("limit") int limit);
    
    /**
     * 获取用户总消费金额
     * 
     * @param userId 用户ID
     * @return 总消费金额
     */
    long sumUserTotalSpent(@Param("userId") String userId);
    
    /**
     * 批量获取多个用户的查看商品数量
     * 
     * @param userIds 用户ID列表
     * @return 用户ID和查看数量的映射列表
     */
    List<Map<String, Object>> batchCountUserViewedItems(@Param("userIds") List<String> userIds);
    
    /**
     * 批量获取多个用户的购买商品数量
     * 
     * @param userIds 用户ID列表
     * @return 用户ID和购买数量的映射列表
     */
    List<Map<String, Object>> batchCountUserPurchasedItems(@Param("userIds") List<String> userIds);
    
    /**
     * 批量获取多个用户的总消费金额
     * 
     * @param userIds 用户ID列表
     * @return 用户ID和总消费金额的映射列表
     */
    List<Map<String, Object>> batchSumUserTotalSpent(@Param("userIds") List<String> userIds);
    
    /**
     * 获取购买了指定商品的用户ID列表
     * 
     * @param itemId 商品ID
     * @return 用户ID列表
     */
    List<String> findUserIdsByPurchasedItemId(@Param("itemId") Long itemId);
    
    /**
     * 获取最活跃的用户ID列表（基于查看和购买行为）
     * 
     * @param limit 返回的用户数量
     * @return 用户ID列表
     */
    List<String> findMostActiveUserIds(@Param("limit") int limit);
} 