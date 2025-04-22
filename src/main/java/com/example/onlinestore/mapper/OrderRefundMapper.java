package com.example.onlinestore.mapper;

import com.example.onlinestore.entity.OrderRefundEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderRefundMapper {
    OrderRefundEntity findById(Long id);
    OrderRefundEntity findByRefundNo(String refundNo);
    OrderRefundEntity findByOrderId(Long orderId);
    int insert(OrderRefundEntity refund);
    int update(OrderRefundEntity refund);
    int updateStatus(@Param("id") Long id, @Param("status") String status);
} 