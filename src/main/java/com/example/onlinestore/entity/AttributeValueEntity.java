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
public class AttributeValueEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = -5219278659394992409L;

    /**
     * 实体唯一标识符（主键）
     */
    private Long id;

    /**
     * 关联属性ID（外键）
     * 指向关联业务属性的唯一标识
     */
    private Long attributeId;

    /**
     * 属性值存储字段
     * 保存具体业务属性的取值内容
     */
    private String value;

    /**
     * 排序权重分值
     * 数值越大表示在排序列表中位置越靠前
     */
    private Integer sortScore;

    /**
     * 记录创建时间戳
     * 由系统在数据创建时自动生成
     */
    private LocalDateTime createdAt;

    /**
     * 最后更新时间戳
     * 由系统在数据更新时自动维护
     */
    private LocalDateTime updatedAt;


}
