package com.example.onlinestore.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ItemEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 7848273722376679390L;

    /**
     * 商品唯一标识ID
     */
    private Long id;
    /**
     * 商品关联的品牌ID
     */
    private Long brandId;
    /**
     * 商品关联的类目ID
     */
    private Long categoryId;
    /**
     * 商品名称（最大长度255字符）
     */
    private String name;
    /**
     * 商品详细描述文本（支持富文本）
     */
    private String description;

    /**
     * 商品详细描述URL地址
     */
    private String descriptionURL;
    /**
     * 商品主图URL地址
     */
    private String mainImageURL;
    /**
     * 商品子图URL地址集合（多个URL用逗号分隔）
     */
    private String subImageURLs;
    /**
     * 商品状态（如：ON_SALE-售卖中/OFF_SALE-已下架）
     */
    private String status;
    /**
     * 排序权重分（值越大排序越靠前）
     */
    private Integer sortScore;
    /**
     * 商品创建时间（ISO8601格式）
     */
    private LocalDateTime createdAt;
    /**
     * 商品最后更新时间（ISO8601格式）
     */
    private LocalDateTime updatedAt;
}
