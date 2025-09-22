package com.example.onlinestore.bean;

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
public class ItemDetail implements Serializable {
    @Serial
    private static final long serialVersionUID = -1677547719030835460L;

    /**
     * 商品项核心对象
     * <p>
     * 存储商品基础信息及业务属性，包含商品标题、分类、规格等基础数据，
     * 作为商品操作的核心数据载体
     */
    private Item item;

    /**
     * 商品SKU集合
     * <p>
     * 存储商品所有库存单元，每个SKU表示具有独立库存和价格的商品变体，
     * 例如不同颜色、尺寸等规格对应的商品子项
     */
    private List<Sku> skus;

}
