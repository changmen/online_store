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

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class OrderRefundEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long orderId;
    private String refundNo;
    private Long paymentId;
    private BigDecimal amount;
    private String reason;
    private RefundStatus status;
    private LocalDateTime refundTime;
    private String thirdPartyRefundNo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 