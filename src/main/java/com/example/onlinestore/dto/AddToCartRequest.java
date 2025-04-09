package com.example.onlinestore.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AddToCartRequest {

    @NotNull(message = "商品ID不能为空")
    @Min(value = 1, message = "商品ID的值只能为正整数")
    private Long itemId;

    @NotNull(message = "SKU ID不能为空")
    @Min(value = 1, message = "SKU ID的值只能为正整数")
    private Long skuId;

    @NotNull(message = "商品数量不能为空")
    @Min(value = 1, message = "商品数量必须大于0")
    private Integer quantity;
} 