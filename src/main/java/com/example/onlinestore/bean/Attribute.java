package com.example.onlinestore.bean;

import com.example.onlinestore.enums.AttributeInputType;
import com.example.onlinestore.enums.AttributeType;
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
public class Attribute implements Serializable {
    @Serial
    private static final long serialVersionUID = -3914106368587152434L;

    /**
     * 属性唯一标识符，通常由数据库自动生成或业务层分配
     */
    private Long id;

    /**
     * 属性名称，用于前端展示和业务逻辑处理的标识
     */
    private String name;

    /**
     * 排序权重分值，数值越大在列表中排序越靠前
     */
    private Integer sortScore;

    /**
     * 可见性标识，0-不可见，1-可见
     * 控制属性是否在客户端展示
     */
    private Integer visible;

    /**
     * 属性类型枚举值，标识属性所属的业务分类
     * 示例：商品基础属性/销售属性/规格属性等
     */
    private AttributeType attributeType;

    /**
     * 输入控件类型枚举值，决定前端如何渲染输入组件
     * 示例：单行文本/多选下拉框/图片上传器等
     */
    private AttributeInputType inputType;

    /**
     * 必填标识，0-非必填，1-必填
     * 控制提交时是否需要强制填写该属性
     */
    private Integer required;

    /**
     * 可搜索标识，0-不可搜索，1-可搜索
     * 决定该属性是否作为筛选条件加入搜索功能
     */
    private Integer searchable;

    /**
     * 属性值列表
     */
    private List<AttributeValue> values;
}
