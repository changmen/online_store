package com.example.onlinestore.bean;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class OrderItem implements Serializable {
    @Serial
    private static final long serialVersionUID = 8328093958488219107L;

    /**
     * 主键ID - 唯一标识订单明细项
     */
    private Long id;

    /**
     * 关联订单ID - 对应订单表主键
     */
    private Long orderId;

    /**
     * SKU唯一标识 - 商品最小库存单位ID
     */
    private Long skuId;

    /**
     * SKU编码 - 商品规格编码（如：颜色+尺码组合编码）
     */
    private String skuCode;

    /**
     * SKU名称 - 商品规格显示名称（如：红色 XL）
     */
    private String skuName;

    /**
     * SKU图片URL - 商品规格展示图地址
     */
    private String skuImage;

    /**
     * 商品单价 - 使用BigDecimal保证金额计算精度
     */
    private BigDecimal price;

    /**
     * 购买数量 - 当前SKU的购买件数
     */
    private Integer quantity;

    /**
     * 小计金额 - 计算公式：price * quantity
     */
    private BigDecimal subtotal;
}