package com.example.onlinestore.mapper;

import com.example.onlinestore.entity.OrderItemEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderItemMapper {
    List<OrderItemEntity> findByOrderId(Long orderId);
    int insert(OrderItemEntity item);
    int insertBatch(List<OrderItemEntity> items);
} 