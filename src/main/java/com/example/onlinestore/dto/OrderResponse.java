package com.example.onlinestore.dto;

import com.example.onlinestore.bean.Order;
import com.example.onlinestore.enums.OrderStatus;
import com.example.onlinestore.enums.PaymentMethod;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.collections.CollectionUtils;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@EqualsAndHashCode
@ToString
public class OrderResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 3431117263460369074L;

    private Long id;
    private String orderNo;
    private Long memberId;
    private BigDecimal totalAmount;
    private BigDecimal actualAmount;
    private BigDecimal discountAmount;
    private BigDecimal shippingFee;
    private Long addressId;
    private OrderStatus status;
    private PaymentMethod paymentMethod;
    private LocalDateTime paymentTime;
    private LocalDateTime shippingTime;
    private LocalDateTime completionTime;
    private LocalDateTime cancelTime;
    private String cancelReason;
    private String remark;

    private List<OrderItemResponse> items;

    public static OrderResponse of(Order order) {
        if (order == null) {
            return null;
        }
        return OrderResponse.builder()
                .id(order.getId())
                .orderNo(order.getOrderNo())
                .memberId(order.getMemberId())
                .totalAmount(order.getTotalAmount())
                .actualAmount(order.getActualAmount())
                .discountAmount(order.getDiscountAmount())
                .shippingFee(order.getShippingFee())
                .addressId(order.getAddressId())
                .status(order.getStatus())
                .paymentMethod(order.getPaymentMethod())
                .paymentTime(order.getPaymentTime())
                .shippingTime(order.getShippingTime())
                .completionTime(order.getCompletionTime())
                .cancelTime(order.getCancelTime())
                .cancelReason(order.getCancelReason())
                .remark(order.getRemark())
                .items(CollectionUtils.isNotEmpty(order.getItems()) ? order.getItems().stream()
                        .map(OrderItemResponse::of)
                        .collect(Collectors.toList()) : Collections.emptyList())
                .build();
    }
} 