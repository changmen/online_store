package com.example.onlinestore.dto;

import com.example.onlinestore.entity.OrderEntity;
import com.example.onlinestore.entity.OrderItemEntity;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class OrderDetailDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private OrderEntity order;
    private List<OrderItemEntity> orderItems;

    public OrderDetailDTO() {
    }

    public OrderDetailDTO(OrderEntity order, List<OrderItemEntity> orderItems) {
        this.order = order;
        this.orderItems = orderItems;
    }

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
    }

    public List<OrderItemEntity> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemEntity> orderItems) {
        this.orderItems = orderItems;
    }
}
