package com.example.onlinestore.service;

import com.example.onlinestore.entity.OrderEntity;
import com.example.onlinestore.entity.OrderItemEntity;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Component
public class OrderAmountCalculator {

    /**
     * 根据订单项重新计算订单总金额
     */
    public void recalculateOrderAmount(OrderEntity order, List<OrderItemEntity> orderItems) {
        BigDecimal totalAmount = orderItems.stream()
                .filter(Objects::nonNull)
                .map(OrderItemEntity::getTotalAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalAmount(totalAmount);

        BigDecimal shippingFee = Objects.requireNonNullElse(order.getShippingFee(), BigDecimal.ZERO);
        BigDecimal discountAmount = Objects.requireNonNullElse(order.getDiscountAmount(), BigDecimal.ZERO);
        order.setPayAmount(totalAmount.add(shippingFee).subtract(discountAmount));
    }

    /**
     * 计算单个订单项的总金额
     */
    public BigDecimal calculateItemTotalAmount(BigDecimal price, int quantity) {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
