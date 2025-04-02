package com.example.onlinestore.dto;

import com.example.onlinestore.bean.AttributeValue;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttributeValueResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 8492011007569585760L;
    /**
     * 实体唯一标识符，通常用于数据库主键或业务层唯一性标识
     * 类型为Long以支持大范围数值，采用自增策略时由数据库自动生成
     */
    private Long id;

    /**
     * 存储核心数据值的字符串字段
     * 根据业务场景可能包含JSON、配置参数或业务主体内容，允许为空值
     */
    private String value;

    /**
     * 排序权重评分，决定实体在列表中的展示顺序
     * 数值越大排序越靠前，空值表示未设置排序规则
     */
    private Integer sortScore;


    /**
     * 从AttributeValue对象创建对应的AttributeValueResponse实例
     *
     * @param attributeValue 源AttributeValue对象，提供需要复制的属性值
     * @return AttributeValueResponse 新构建的响应对象，包含从源对象复制的以下属性：
     * id - 唯一标识符
     * value - 属性值数据
     * sortScore - 排序权重值
     */
    public static AttributeValueResponse Of(AttributeValue attributeValue) {
        // 创建新响应对象并复制核心属性
        AttributeValueResponse response = new AttributeValueResponse();
        response.setId(attributeValue.getId());
        response.setValue(attributeValue.getValue());
        response.setSortScore(attributeValue.getSortScore());
        return response;
    }

}

