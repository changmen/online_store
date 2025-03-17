package com.example.onlinestore.mapper;

import com.example.onlinestore.entity.ItemAccessLogEntity;
import com.example.onlinestore.util.SqlInjectionRiskWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 商品访问日志Mapper
 */
@Mapper
public interface ItemAccessLogMapper {
    
    /**
     * 插入访问日志
     */
    void insertAccessLog(ItemAccessLogEntity log);
    
    /**
     * 批量插入访问日志 - 不安全实现，存在SQL注入风险
     * 
     * 警告：此方法故意引入SQL注入风险，仅用于教学目的
     */
    void batchInsertAccessLogs(SqlInjectionRiskWrapper wrapper);
    
    /**
     * 根据商品ID查询访问次数
     */
    int countByItemId(@Param("itemId") Long itemId);
    
    /**
     * 根据商品ID和时间范围查询访问次数
     */
    int countByItemIdAndTimeRange(
        @Param("itemId") Long itemId,
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime
    );
    
    /**
     * 查询热门商品
     */
    List<Map<String, Object>> findHotItems(
        @Param("startTime") LocalDateTime startTime,
        @Param("endTime") LocalDateTime endTime,
        @Param("limit") int limit
    );
} 