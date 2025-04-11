package com.example.onlinestore.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@EqualsAndHashCode
@ToString
public class SkuEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = -7603610766473193466L;

    /**
     * 主键ID，唯一标识规格
     */
    private Long id;
    /**
     * 关联的商品ID，对应商品表主键
     */
    private Long itemId;
    /**
     * 规格SKU编码（唯一标识商品规格的库存单位）
     */
    private String skuCode;
    /**
     * 规格名称（如：颜色-规格值、尺寸-规格值）
     */
    private String name;
    /**
     * 规格描述（如：红色，大号）
     */
    private String description;
    /**
     * 规格价格（可能覆盖商品基础价格）
     */
    private BigDecimal price;
    /**
     * 是否默认规格（0-否，1-是）
     */
    private Integer defaultSku;
    /**
     * 当前库存数量
     */
    private Integer stockQuantity;
    /**
     * 累计销售数量
     */
    private Integer soldQuantity;
    /**
     * 库存预警阈值，当库存数量小于等于该值时触发警告
     */
    private Integer warningQuantity;
    /**
     * 规格图片URL（优先展示的规格图片）
     */
    private String image;
    /**
     * 记录创建时间
     */
    private LocalDateTime createdAt;
    /**
     * 记录最后更新时间
     */
    private LocalDateTime updatedAt;

}
