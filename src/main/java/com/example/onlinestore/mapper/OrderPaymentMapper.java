package com.example.onlinestore.mapper;

import com.example.onlinestore.entity.OrderPaymentEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderPaymentMapper {
    OrderPaymentEntity findById(Long id);
    OrderPaymentEntity findByPaymentNo(String paymentNo);
    OrderPaymentEntity findByOrderId(Long orderId);
    int insert(OrderPaymentEntity payment);
    int update(OrderPaymentEntity payment);
    int updateStatus(@Param("id") Long id, @Param("status") String status);
} 