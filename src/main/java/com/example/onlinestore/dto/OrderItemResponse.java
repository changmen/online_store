package com.example.onlinestore.dto;

import com.example.onlinestore.bean.OrderItem;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Builder
@EqualsAndHashCode
@ToString
public class OrderItemResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long orderId;
    private Long skuId;
    private String skuCode;
    private String skuName;
    private String skuImage;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal subtotal;

    public static OrderItemResponse of(OrderItem item) {
        if (item == null) {
            return null;
        }
        return OrderItemResponse.builder()
                .id(item.getId())
                .orderId(item.getOrderId())
                .skuId(item.getSkuId())
                .skuCode(item.getSkuCode())
                .skuName(item.getSkuName())
                .skuImage(item.getSkuImage())
                .price(item.getPrice())
                .quantity(item.getQuantity())
                .subtotal(item.getSubtotal())
                .build();
    }
} 