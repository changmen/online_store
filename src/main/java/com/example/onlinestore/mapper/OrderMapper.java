package com.example.onlinestore.mapper;

import com.example.onlinestore.entity.OrderEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderMapper {
    void insertOrder(OrderEntity order);
    OrderEntity findById(Long id);
    OrderEntity findByOrderNo(String orderNo);
    List<OrderEntity> findByUserId(Long userId);
    void updateOrder(OrderEntity order);
    void deleteOrder(Long id);
    
    /**
     * 更新订单状态
     */
    void updateStatus(
        @Param("id") Long id,
        @Param("status") Integer status
    );
    
    /**
     * 按条件查询订单
     */
    List<OrderEntity> findByCondition(
        @Param("userId") Long userId,
        @Param("status") Integer status,
        @Param("startTime") java.time.LocalDateTime startTime,
        @Param("endTime") java.time.LocalDateTime endTime,
        @Param("offset") int offset,
        @Param("limit") int limit
    );
    
    /**
     * 按条件统计订单总数
     */
    long countByCondition(
        @Param("userId") Long userId,
        @Param("status") Integer status,
        @Param("startTime") java.time.LocalDateTime startTime,
        @Param("endTime") java.time.LocalDateTime endTime
    );
    
    /**
     * 查询超时未支付的订单
     */
    List<OrderEntity> findTimeoutOrders(
        @Param("timeoutMinutes") int timeoutMinutes,
        @Param("limit") int limit
    );
} 