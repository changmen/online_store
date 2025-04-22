package com.example.onlinestore.entity;

import com.example.onlinestore.enums.RefundStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单退款实体类
 * 用于记录订单退款的相关信息，包括退款状态、金额、原因等
 *
 * @author example
 * @version 1.0
 * @since 2024-04-22
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class OrderRefundEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 退款记录ID，唯一标识符
     */
    private Long id;

    /**
     * 关联的订单ID
     */
    private Long orderId;

    /**
     * 退款单号，系统生成的唯一退款编号
     */
    private String refundNo;

    /**
     * 关联的支付记录ID
     */
    private Long paymentId;

    /**
     * 退款金额
     */
    private BigDecimal amount;

    /**
     * 退款原因，用户或系统填写的退款说明
     */
    private String reason;

    /**
     * 退款状态，参考{@link RefundStatus}
     */
    private RefundStatus status;

    /**
     * 退款时间，退款完成的时间
     */
    private LocalDateTime refundTime;

    /**
     * 第三方支付平台的退款单号
     */
    private String thirdPartyRefundNo;

    /**
     * 创建时间，退款记录创建的时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间，退款记录最后更新的时间
     */
    private LocalDateTime updatedAt;
} 