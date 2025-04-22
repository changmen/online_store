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
public class OrderEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String orderNo;
    private Long memberId;
    private BigDecimal totalAmount;
    private BigDecimal actualAmount;
    private BigDecimal discountAmount;
    private BigDecimal shippingFee;
    private Long addressId;
    private String status;
    private String paymentMethod;
    private LocalDateTime paymentTime;
    private LocalDateTime shippingTime;
    private LocalDateTime completionTime;
    private LocalDateTime cancelTime;
    private String cancelReason;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}