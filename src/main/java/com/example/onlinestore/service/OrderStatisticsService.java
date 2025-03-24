package com.example.onlinestore.service;

import com.example.onlinestore.bean.OrderStatistics;
import com.example.onlinestore.entity.OrderStatisticsEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface OrderStatisticsService {
    /**
     * 生成订单统计
     * @param startDate 开始日期
     * @param endDate 结束日期
     */
    void generateStatistics(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 获取用户订单统计
     * @param userId 用户ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     */
    List<OrderStatistics> getUserStatistics(Long userId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 获取商品订单统计
     * @param itemId 商品ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     */
    List<OrderStatistics> getItemStatistics(Long itemId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 清理历史统计数据
     * @param beforeDate 清理该日期之前的数据
     */
    void cleanHistoryStatistics(LocalDateTime beforeDate);

    /**
     * 获取用户订单统计汇总
     * @param userId 用户ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     */
    Map<String, Object> getUserStatisticsSummary(Long userId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 获取商品订单统计汇总
     * @param itemId 商品ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     */
    Map<String, Object> getItemStatisticsSummary(Long itemId, LocalDateTime startDate, LocalDateTime endDate);
} 