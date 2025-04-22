package com.example.onlinestore.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class OrderPaymentEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 支付记录唯一标识符（通常为主键）
     */
    private Long id;

    /**
     * 关联的订单ID，指向业务主订单
     */
    private Long orderId;

    /**
     * 系统生成的支付流水号，具有唯一性
     */
    private String paymentNo;

    /**
     * 支付方式（如：ALIPAY/WECHAT_PAY/UNION_PAY）
     */
    private String paymentMethod;

    /**
     * 支付金额（单位：元，精确到分）
     */
    private BigDecimal amount;

    /**
     * 支付状态（如：SUCCESS/FAILED/PROCESSING）
     */
    private String status;

    /**
     * 支付成功时间（第三方回调时更新）
     */
    private LocalDateTime paymentTime;

    /**
     * 第三方支付平台生成的交易号
     */
    private String thirdPartyPaymentNo;

    /**
     * 记录创建时间（数据库自动填充）
     */
    private LocalDateTime createdAt;

    /**
     * 最后更新时间（数据库自动更新）
     */
    private LocalDateTime updatedAt;
} 