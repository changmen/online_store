package com.example.onlinestore.dto;

import com.example.onlinestore.enums.PaymentMethod;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class PaymentRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @NotNull(message = "会员ID不能为空")
    @Min(value = 1, message = "会员ID要大于0")
    private Long memberId;

    @NotNull(message = "订单ID不能为空")
    private Long orderId;

    @NotNull(message = "支付方式不能为空")
    private PaymentMethod paymentMethod;
} 