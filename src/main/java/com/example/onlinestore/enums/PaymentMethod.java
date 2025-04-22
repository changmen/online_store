package com.example.onlinestore.enums;

import lombok.Getter;

/**
 * 支付方式枚举
 * 定义系统支持的支付方式类型
 */
@Getter
public enum PaymentMethod {
    /**
     * 支付宝支付
     */
    ALIPAY,

    /**
     * 微信支付
     */
    WECHAT
} 