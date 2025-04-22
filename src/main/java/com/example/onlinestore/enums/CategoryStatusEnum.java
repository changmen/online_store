package com.example.onlinestore.enums;

import lombok.Getter;

/**
 * 商品分类状态枚举
 * 定义商品分类是否可用的状态
 */
@Getter
public enum CategoryStatusEnum {
    /**
     * 启用 - 分类当前可用
     */
    ENABLE(0),

    /**
     * 禁用 - 分类已停用
     */
    DISABLE(1);

    private final int status;

    CategoryStatusEnum(int status) {
        this.status = status;
    }
}
