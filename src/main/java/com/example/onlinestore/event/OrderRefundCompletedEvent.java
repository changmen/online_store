package com.example.onlinestore.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.Map;

/**
 * 订单退款完成事件
 * 当订单退款处理完成时，系统会发布此事件
 * 事件包含了完整的退款信息，包括订单信息、退款记录、会员信息等
 *
 * @author example
 * @version 1.0
 * @since 2024-04-22
 */
@Getter
public class OrderRefundCompletedEvent extends ApplicationEvent {

    @Serial
    private static final long serialVersionUID = 3824898071010039384L;

    /**
     * 退款完成事件数据
     * 包含了退款相关的所有信息
     */
    private final RefundCompletedEventData eventData;

    /**
     * 构造函数
     *
     * @param eventData 退款完成事件数据
     */
    public OrderRefundCompletedEvent(RefundCompletedEventData eventData) {
        super(eventData);
        this.eventData = eventData;
    }
} 