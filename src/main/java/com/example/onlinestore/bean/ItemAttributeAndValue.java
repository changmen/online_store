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
public class ItemAttributeAndValue implements Serializable {
    @Serial
    private static final long serialVersionUID = 4489146596381270494L;
    /**
     * 当前处理的属性对象，用于存储业务相关的属性元数据
     */
    private Attribute attribute;

    /**
     * 经过解析处理的属性值对象，包含类型转换后的属性值信息
     */
    private AttributeValue attributeValue;

    /**
     * 用户输入的原始字符串值，需要经过验证和格式化处理
     */
    private String inputValue;

}
