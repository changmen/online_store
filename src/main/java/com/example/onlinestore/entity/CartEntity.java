package com.example.onlinestore.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class CartEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 7848273722376679390L;

    /**
     * 购物车项ID
     */
    private Long id;
    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 商品ID
     */
    private Long itemId;
    /**
     * 商品SKU ID
     */
    private Long skuId;
    /**
     * 商品数量
     */
    private Integer quantity;
    /**
     * 商品单价
     */
    private BigDecimal price;
    /**
     * 商品总价
     */
    private BigDecimal totalPrice;
    /**
     * 是否选中（0-未选中，1-已选中）
     */
    private Integer selected;
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
} 