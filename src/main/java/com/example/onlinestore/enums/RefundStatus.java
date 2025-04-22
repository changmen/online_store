package com.example.onlinestore.enums;

import lombok.Getter;

/**
 * 退款状态枚举
 * 定义退款申请在整个处理过程中可能处于的各种状态
 */
@Getter
public enum RefundStatus {
    /**
     * 待退款 - 退款申请已提交，等待处理
     */
    PENDING,

    /**
     * 退款成功 - 退款已处理完成且成功
     */
    SUCCESS,

    /**
     * 退款失败 - 退款处理过程中发生错误
     */
    FAILED;
} 