package com.example.onlinestore.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartItemResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 1248301417057065756L;
    /**
     * 购物车商品ID。
     */
    private Long id;
    /**
     * 商品信息响应对象。
     */
    private ItemResponse item;

    /**
     * SKU信息响应对象。
     */
    private SkuResponse sku;

    /**
     * 商品数量。
     */
    private Integer quantity;

    /**
     * 是否被选中状态。
     */
    private Integer selected;
}
