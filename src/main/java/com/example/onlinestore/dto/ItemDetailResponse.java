package com.example.onlinestore.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ItemDetailResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = -2657899676204944545L;
    /**
     * 商品主信息响应对象
     * 包含商品基础属性如商品ID、名称、描述等核心信息
     *
     * @see ItemResponse 包含完整的商品数据结构和字段定义
     */
    private ItemResponse item;

    /**
     * 商品SKU信息列表
     * 存储该商品对应的所有SKU规格数据，包含价格、库存、规格属性等详细信息
     *
     * @see SkuResponse 包含SKU维度的完整数据结构和字段定义
     * 注意：当商品为多规格商品时，该列表会包含多个SKU条目
     */
    private List<SkuResponse> skus;

} 