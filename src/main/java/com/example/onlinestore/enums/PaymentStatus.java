package com.example.onlinestore.enums;

import lombok.Getter;

/**
 * 支付状态枚举
 * 定义支付交易可能处于的各种状态
 */
@Getter
public enum PaymentStatus {
    /**
     * 待支付 - 支付交易已创建，等待用户完成支付
     */
    PENDING,

    /**
     * 支付成功 - 用户已完成支付且支付成功
     */
    SUCCESS,

    /**
     * 支付失败 - 支付过程中发生错误或用户取消支付
     */
    FAILED
} 