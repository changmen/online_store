package com.example.onlinestore.service;

import com.example.onlinestore.dto.UserItemStatDTO;

import java.util.List;
import java.util.Map;

/**
 * 用户商品统计服务接口
 */
public interface UserItemStatService {
    
    /**
     * 获取单个用户的商品统计信息
     * 
     * @param userId 用户ID
     * @return 用户商品统计DTO
     */
    UserItemStatDTO getUserItemStat(String userId);
    
    /**
     * 批量获取多个用户的商品统计信息
     * 
     * @param userIds 用户ID列表
     * @return 用户ID到统计信息的映射
     */
    Map<String, UserItemStatDTO> getUserItemStats(List<String> userIds);
    
    /**
     * 批量获取多个用户的商品统计信息
     * 
     * @param userIds 用户ID列表
     * @return 用户ID到统计信息的映射
     */
    Map<String, UserItemStatDTO> getUserItemStatsNew(List<String> userIds);
    
    /**
     * 获取购买了指定商品的用户统计信息
     * 
     * @param itemId 商品ID
     * @return 用户商品统计DTO列表
     */
    List<UserItemStatDTO> getUserStatsByItemId(Long itemId);
    
    /**
     * 获取最活跃的用户（基于查看和购买行为）
     * 
     * @param limit 返回的用户数量
     * @return 用户商品统计DTO列表
     */
    List<UserItemStatDTO> getMostActiveUsers(int limit);
} 