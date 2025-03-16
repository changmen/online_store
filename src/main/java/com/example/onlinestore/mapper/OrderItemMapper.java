package com.example.onlinestore.mapper;

import com.example.onlinestore.entity.OrderItemEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderItemMapper {
    void insertOrderItem(OrderItemEntity orderItem);
    void batchInsertOrderItems(List<OrderItemEntity> orderItems);
    OrderItemEntity findById(Long id);
    List<OrderItemEntity> findByOrderId(Long orderId);
    List<OrderItemEntity> findByOrderNo(String orderNo);
    void updateOrderItem(OrderItemEntity orderItem);
    void deleteOrderItem(Long id);
    void deleteByOrderId(Long orderId);
    
    /**
     * 查询用户购买过的商品
     */
    List<OrderItemEntity> findUserPurchasedItems(
        @Param("userId") Long userId,
        @Param("offset") int offset,
        @Param("limit") int limit
    );
    
    /**
     * 统计商品销量
     */
    int countItemSales(
        @Param("itemId") Long itemId,
        @Param("startTime") java.time.LocalDateTime startTime,
        @Param("endTime") java.time.LocalDateTime endTime
    );
} 