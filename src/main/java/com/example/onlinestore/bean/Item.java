package com.example.onlinestore.bean;

import com.example.onlinestore.enums.ItemStatus;
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
public class Item implements Serializable {
    @Serial
    private static final long serialVersionUID = 8328093958488219105L;

    /**
     * 商品唯一标识符，通常由数据库自动生成
     */
    private Long id;

    /**
     * 关联的品牌信息对象，包含品牌名称、Logo等详细信息
     */
    private Long brandId;

    /**
     * 关联的商品分类对象，描述商品所属的类目信息
     */
    private Long categoryId;

    /**
     * 商品名称，最大长度应符合业务规范要求
     */
    private String name;

    /**
     * 商品详细描述，支持富文本格式内容
     */
    private String description;

    /**
     * 主图URL地址，用于商品列表展示的首图
     */
    private String mainImageURL;

    /**
     * 子图URL集合（JSON格式存储），包含商品详情页展示的附加图片
     */
    private List<String> subImageURLs;

    /**
     * 商品状态枚举值（如：上架/下架/待审核等）
     */
    private ItemStatus status;

    /**
     * 排序权重值，数值越大在排序列表中位置越靠前
     */
    private Integer sortScore;

    /**
     * 商品属性集合，用于描述商品的各种属性信息
     */
    private List<ItemAttributeAndValue> attributes;



}
