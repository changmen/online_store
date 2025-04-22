package com.example.onlinestore.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
public class OrderItemRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "商品SKU ID不能为空")
    private Long skuId;

    @NotNull(message = "商品SKU编码不能为空")
    private String skuCode;

    @NotNull(message = "商品SKU名称不能为空")
    private String skuName;

    private String skuImage;

    @NotNull(message = "商品价格不能为空")
    private BigDecimal price;

    @NotNull(message = "购买数量不能为空")
    @Min(value = 1, message = "购买数量必须大于0")
    private Integer quantity;
} 