package com.example.onlinestore.service;

import com.example.onlinestore.entity.OrderEntity;
import com.example.onlinestore.entity.OrderItemEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderAmountCalculatorTest {

    private OrderAmountCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new OrderAmountCalculator();
    }

    @Test
    void calculateItemTotalAmount_shouldReturnCorrectValue() {
        BigDecimal price = new BigDecimal("99.50");
        BigDecimal total = calculator.calculateItemTotalAmount(price, 3);
        assertEquals(new BigDecimal("298.50"), total);
    }

    @Test
    void calculateItemTotalAmount_shouldHandleZero() {
        BigDecimal price = new BigDecimal("99.50");
        BigDecimal total = calculator.calculateItemTotalAmount(price, 0);
        assertEquals(0, total.compareTo(BigDecimal.ZERO));
    }

    @Test
    void recalculateOrderAmount_shouldSumAllItems() {
        OrderEntity order = new OrderEntity();
        order.setShippingFee(new BigDecimal("10.00"));
        order.setDiscountAmount(BigDecimal.ZERO);

        OrderItemEntity item1 = new OrderItemEntity();
        item1.setTotalAmount(new BigDecimal("100.00"));

        OrderItemEntity item2 = new OrderItemEntity();
        item2.setTotalAmount(new BigDecimal("200.00"));

        List<OrderItemEntity> items = Arrays.asList(item1, item2);

        calculator.recalculateOrderAmount(order, items);

        assertEquals(new BigDecimal("300.00"), order.getTotalAmount());
        assertEquals(new BigDecimal("310.00"), order.getPayAmount());
    }

    @Test
    void recalculateOrderAmount_shouldHandleNullItemAmounts() {
        OrderEntity order = new OrderEntity();
        order.setShippingFee(BigDecimal.ZERO);
        order.setDiscountAmount(BigDecimal.ZERO);

        OrderItemEntity item1 = new OrderItemEntity();
        item1.setTotalAmount(new BigDecimal("50.00"));

        OrderItemEntity item2 = new OrderItemEntity();
        item2.setTotalAmount(null);

        List<OrderItemEntity> items = Arrays.asList(item1, item2);

        calculator.recalculateOrderAmount(order, items);

        assertEquals(new BigDecimal("50.00"), order.getTotalAmount());
    }

    @Test
    void recalculateOrderAmount_shouldHandleEmptyItems() {
        OrderEntity order = new OrderEntity();
        order.setShippingFee(new BigDecimal("5.00"));
        order.setDiscountAmount(BigDecimal.ZERO);

        calculator.recalculateOrderAmount(order, Collections.emptyList());

        assertEquals(BigDecimal.ZERO, order.getTotalAmount());
        assertEquals(new BigDecimal("5.00"), order.getPayAmount());
    }

    @Test
    void recalculateOrderAmount_shouldHandleNullShippingFee() {
        OrderEntity order = new OrderEntity();
        order.setShippingFee(null);
        order.setDiscountAmount(BigDecimal.ZERO);

        OrderItemEntity item = new OrderItemEntity();
        item.setTotalAmount(new BigDecimal("100.00"));

        calculator.recalculateOrderAmount(order, List.of(item));

        assertEquals(new BigDecimal("100.00"), order.getTotalAmount());
        assertEquals(new BigDecimal("100.00"), order.getPayAmount());
    }
}
