package com.example.onlinestore.bean;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class AttributeValue implements Serializable {
    @Serial
    private static final long serialVersionUID = -6406404010990486133L;

    /**
     * 唯一标识符，通常用于数据库主键或业务逻辑中的唯一标识
     */
    private Long id;

    /**
     * 关联属性ID，表示该值所属的属性类型标识
     * 通常对应属性表(attribute)的主键
     */
    private Long attributeId;

    /**
     * 属性值内容存储字段，保存具体的属性值信息
     * 例如颜色属性可能存储"红色"等具体取值
     */
    private String value;

    /**
     * 排序权重分数，用于控制同类属性值的显示顺序
     * 数值越大通常表示排序越靠前
     */
    private Integer sortScore;

}
