package com.example.onlinestore.enums;

import lombok.Getter;

/**
 * 属性值类型枚举
 * 定义商品属性值的不同类型
 */
@Getter
public enum AttributeValueType {
    /**
     * 文本类型 - 自由输入的文本值
     */
    TEXT(0),

    /**
     * 数字类型 - 数值类型的属性值
     */
    NUMBER(1),

    /**
     * 布尔类型 - 是/否类型的属性值
     */
    BOOLEAN(2),

    /**
     * 多选类型 - 可从多个选项中选择多个值
     */
    MULTI_OPTIONS(3),

    /**
     * 单选类型 - 从多个选项中选择一个值
     */
    SINGLE_OPTION(4);

    private final int valueType;

    AttributeValueType(int value) {
        this.valueType = value;
    }
}
