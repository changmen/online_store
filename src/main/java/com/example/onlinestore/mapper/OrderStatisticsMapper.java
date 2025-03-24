package com.example.onlinestore.mapper;

import com.example.onlinestore.entity.OrderStatisticsEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderStatisticsMapper {
    /**
     * 插入统计记录
     */
    void insertStatistics(OrderStatisticsEntity statistics);

    /**
     * 批量插入统计记录
     */
    void batchInsertStatistics(List<OrderStatisticsEntity> statisticsList);

    /**
     * 按用户ID查询统计记录
     */
    List<OrderStatisticsEntity> findByUserId(
        @Param("userId") Long userId,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );

    /**
     * 按商品ID查询统计记录
     */
    List<OrderStatisticsEntity> findByItemId(
        @Param("itemId") Long itemId,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate") LocalDateTime endDate
    );

    /**
     * 删除指定日期之前的统计记录
     */
    void deleteBeforeDate(@Param("date") LocalDateTime date);
} 