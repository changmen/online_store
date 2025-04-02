package com.example.onlinestore.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

// 商品类目表
/**
 * CategoryEntity 类用于表示分类实体，实现了 Serializable 接口以支持序列化。
 */
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class CategoryEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 3749778633252581885L;

    /**
     * 分类的唯一标识符。
     */
    private Long id;

    /**
     * 分类的名称。
     */
    private String name;

    /**
     * 分类的描述信息。
     */
    private String description;

    /**
     * 父分类的唯一标识符，用于表示分类的层级关系。
     */
    private Long parentId;

    /**
     * 分类是否可见的标志。
     */
    private Boolean visible;

    /**
     * 分类的状态，通常用于表示分类的启用或禁用状态。
     */
    private Integer status;

    /**
     * 分类的权重，用于排序或优先级控制。
     */
    private Integer weight;

    /**
     * 分类的创建时间。
     */
    private LocalDateTime createdAt;

    /**
     * 分类的最后更新时间。
     */
    private LocalDateTime updatedAt;
}

