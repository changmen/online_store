package com.example.onlinestore.bean;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@EqualsAndHashCode
@ToString
public class Sku implements Serializable {
    @Serial
    private static final long serialVersionUID = -972236861587580960L;
    /**
     * 实体唯一标识符（主键）
     */
    private Long id;

    /**
     * 关联商品ID，对应商品表主键
     */
    private Long itemId;

    /**
     * SKU编码，商品规格唯一标识（格式：SPU编号+规格参数）
     */
    private String skuCode;

    /**
     * 规格名称（如：颜色分类/尺寸规格）
     */
    private String name;

    /**
     * 规格详细描述（如：珍珠白/星空蓝）
     */
    private String description;

    /**
     * 规格价格（单位：元，精度支持小数点后两位）
     */
    private BigDecimal price;

    /**
     * 是否为默认规格（1：默认展示规格）
     */
    private Integer defaultSku;

    /**
     * 当前库存数量（实时库存）
     */
    private Integer stockQuantity;

    /**
     * 累计销售数量（下单即增加）
     */
    private Integer soldQuantity;

    /**
     * 库存预警阈值（库存低于该值时触发预警）
     */
    private Integer warningQuantity;

    /**
     * 规格主图URL地址
     */
    private String image;

    /**
     * 规格参数
     */
    private List<ItemAttributeAndValue> attributes;

} 