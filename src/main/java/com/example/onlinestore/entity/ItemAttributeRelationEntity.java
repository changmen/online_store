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
public class ItemAttributeRelationEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 4040514916416817888L;

    /**
     * 实体唯一标识符，主键，自增
     */
    private Long id;

    /**
     * 关联商品项ID，对应item表的主键
     */
    private Long itemId;

    /**
     * 关联SKU ID，对应sku表的主键
     */
    private Long skuId;

    /**
     * 商品属性类型ID，对应attribute表的主键
     */
    private Long attributeId;

    /**
     * 预定义属性值ID，对应attribute_value表的主键（当使用预定义值时有效）
     */
    private Long valueId;

    /**
     * 用户自定义输入值（当value_id为空时使用该字段）
     */
    private String inputValue;

    /**
     * 记录创建时间，由数据库自动生成
     */
    private LocalDateTime createdAt;
    /**
     * 记录最后更新时间，由数据库自动更新
     */
    private LocalDateTime updatedAt;

}
