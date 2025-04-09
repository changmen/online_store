package com.example.onlinestore.bean;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@EqualsAndHashCode
@ToString
public class CartItem {
    // 主键ID
    private Long id;

    // 会员ID
    private Long memberId;

    // 商品唯一标识ID
    private Long skuId;

    // 商品编码（产品编号）
    private String skuCode;

    // 商品名称
    private String skuName;

    // 商品图片路径或URL
    private String skuImage;

    // 商品数量
    private Integer quantity;

    // 商品单价
    private BigDecimal price;

    // 商品总价（数量×单价）
    private BigDecimal totalPrice;

    // 商品选中状态（如购物车勾选状态）
    private Integer selected;
}