package com.example.onlinestore.enums;

import lombok.Getter;

/**
 * 订单状态枚举
 * 定义订单在整个生命周期中可能处于的各种状态
 */
@Getter
public enum OrderStatus {
    /**
     * 待支付 - 订单创建后等待用户支付
     */
    PENDING_PAYMENT,

    /**
     * 已支付 - 用户已完成支付
     */
    PAID,

    /**
     * 已发货 - 商家已发货
     */
    SHIPPED,

    /**
     * 已完成 - 用户已确认收货
     */
    COMPLETED,

    /**
     * 已取消 - 订单被取消
     */
    CANCELLED,

    /**
     * 退款中 - 正在处理退款
     */
    REFUNDING,

    /**
     * 已退款 - 退款已完成
     */
    REFUNDED
}