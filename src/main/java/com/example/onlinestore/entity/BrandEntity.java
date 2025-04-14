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
public class BrandEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = -7991442075493692536L;

    /**
     * 品牌ID
     */
    private Long id;

    /**
     * 品牌名称
     */
    private String name;

    /**
     * 品牌描述
     */
    private String description;

    /**
     * 品牌logo
     */
    private String logo;

    /**
     * 品牌故事
     */
    private String story;

    /**
     * 排序分值，越大越靠前
     */
    private Integer sortScore;

    /**
     * 显示状态，0-不显示，1-显示
     */
    private Integer visible;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

}
