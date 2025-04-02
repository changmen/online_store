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
public class AttributeEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1071950200762803926L;

    /**
     * 实体唯一标识符
     */
    private Long id;

    /**
     * 属性名称
     */
    private String name;

    /**
     * 属性类型, 见{@link com.example.onlinestore.enums.AttributeType}
     */
    private String attributeType;

    /**
     * 是否必填属性标记
     * <p>0-非必填 1-必填，或使用布尔值语义</p>
     */
    private Integer required;

    /**
     * 是否支持搜索标记
     * <p>0-不可搜索 1-可搜索，或使用布尔值语义</p>
     */
    private Integer searchable;

    /**
     * 属性值的输入类型, 见{@link com.example.onlinestore.enums.AttributeInputType}
     */
    private String inputType;

    /**
     * 排序序号, 越大越排在前面
     */
    private Integer sortScore;

    /**
     * 是否可见标记
     * <p>0-不可见 1-可见</p>
     */
    private Integer visible;

    /**
     * 记录创建时间戳
     */
    private LocalDateTime createdAt;

    /**
     * 记录最后更新时间戳
     */
    private LocalDateTime updatedAt;



}
