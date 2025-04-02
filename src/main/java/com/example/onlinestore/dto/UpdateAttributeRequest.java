package com.example.onlinestore.dto;

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
public class UpdateAttributeRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 6910540355902093920L;

    /**
     * 属性项名称（前端展示标签名）
     */
    private String name;

    /**
     * 排序权重分值，数值越大排序越靠前
     */
    private Integer sortScore;

    /**
     * 可见性状态：0-不可见，1-可见
     */
    private Integer visible;

    /**
     * 属性类型（如：basic-基础属性，extend-扩展属性）
     */
    private String attributeType;

    /**
     * 前端输入控件类型（如：text-文本框，select-下拉框）
     */
    private String inputType;

    /**
     * 必填标识：0-非必填，1-必填
     */
    private Integer required;

    /**
     * 可搜索标识：0-不可搜索，1-可被搜索
     */
    private Integer searchable;

}
