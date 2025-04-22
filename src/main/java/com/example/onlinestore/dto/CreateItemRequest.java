package com.example.onlinestore.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class CreateItemRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = -7902924596304849256L;

    /**
     * 品牌唯一标识符
     * 关联品牌表主键ID
     */
    @NotNull(message = "brandId不能为空")
    @Min(value = 1, message = "brandId的值只能为正整数")
    private Long brandId;

    /**
     * 商品分类唯一标识符
     * 关联分类表主键ID
     */
    @NotNull(message = "categoryId不能为空")
    @Min(value = 1, message = "categoryId的值只能为正整数")
    private Long categoryId;

    /**
     * 商品名称
     * 需符合唯一性约束
     */
    @NotNull(message = "name不能为空")
    private String name;

    /**
     * 商品详细描述
     * 支持富文本格式内容
     */
    private String description;

    /**
     * 商品主图URL
     * 用于商品列表页展示
     */
    @NotNull(message = "mainImageUrl不能为空")
    private String mainImageUrl;

    /**
     * 商品子图URL集合
     * 多个URL用逗号分隔存储
     */
    @Size(max = 5, message = "子图不能超过5个")
    private List<String> subImageUrls;

    /**
     * 商品属性集合
     */
    @NotNull(message = "attributes不能为空")
    private List<ItemAttributeRequest> attributes;

    @NotNull(message = "sortScore不能为空")
    @Min(value = 1, message = "sortScore的值只能为正整数")
    private Integer sortScore;

}
