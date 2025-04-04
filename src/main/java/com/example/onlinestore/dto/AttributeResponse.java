package com.example.onlinestore.dto;

import com.example.onlinestore.bean.Attribute;
import com.example.onlinestore.enums.AttributeInputType;
import com.example.onlinestore.enums.AttributeType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.collections.CollectionUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttributeResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = -4181404620555938859L;

    /**
     * 属性唯一标识符，通常由系统自动生成
     */
    private Long id;

    /**
     * 属性名称，用于业务展示和识别
     */
    private String name;

    /**
     * 排序分值，用于控制属性展示顺序
     * 分值越大排序越靠前，相同分值按创建时间排序
     */
    private Integer sortScore;

    /**
     * 可见性标识，控制属性是否在前端展示
     * 0=不可见，1=可见（默认）
     */
    private Integer visible;

    /**
     * 属性类型枚举值，定义属性的业务分类
     * 示例：商品属性/规格属性/分类属性等
     */
    private AttributeType attributeType;

    /**
     * 输入类型枚举值，定义用户输入方式
     * 示例：文本框/单选按钮/多选列表/文件上传等
     */
    private AttributeInputType inputType;

    /**
     * 必填标识，控制是否强制要求用户填写
     * 0=非必填，1=必填（默认）
     */
    private Integer required;

    /**
     * 可搜索标识，控制是否加入搜索过滤条件
     * 0=不可搜索，1=可搜索（默认）
     */
    private Integer searchable;

    /**
     * 属性值列表，用于展示属性的可选选项
     */
    private List<AttributeValueResponse> values;

    /**
     * 根据给定的属性对象创建并返回属性响应实例
     *
     * @param attribute 用于创建AttributeResponse的源属性对象，应包含必要的属性信息
     * @return 新构建的AttributeResponse实例，封装了从给定属性转换后的响应数据
     */
    public static AttributeResponse of(Attribute attribute) {

        // 创建响应对象并复制基础属性
        AttributeResponse response = new AttributeResponse();
        response.setId(attribute.getId());
        response.setName(attribute.getName());
        response.setAttributeType(attribute.getAttributeType());
        response.setInputType(attribute.getInputType());
        response.setRequired(attribute.getRequired());
        response.setSearchable(attribute.getSearchable());
        response.setSortScore(attribute.getSortScore());
        response.setVisible(attribute.getVisible());

        // 转换属性值列表：当源对象存在有效值列表时，将其转换为响应对象的DTO格式
        if (CollectionUtils.isNotEmpty(attribute.getValues())) {
            response.setValues(attribute.getValues().stream().map(AttributeValueResponse::Of).toList());
        }

        return response;
    }


}
