package com.example.onlinestore.event;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 退款完成事件数据类
 * 用于封装退款完成事件的所有相关数据
 *
 * @author example
 * @version 1.0
 * @since 2024-04-22
 */
@Data
@Builder
public class RefundCompletedEventData {
    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 退款记录ID
     */
    private Long refundId;

    /**
     * 退款单号
     */
    private String refundNo;

    /**
     * 退款金额
     */
    private BigDecimal amount;

    /**
     * 退款原因
     */
    private String reason;

    /**
     * 退款时间
     */
    private LocalDateTime refundTime;

    /**
     * 事件类型
     */
    private String eventType;

    /**
     * 事件发生时间
     */
    private LocalDateTime eventTime;
} 