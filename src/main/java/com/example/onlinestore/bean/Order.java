package com.example.onlinestore.bean;

import com.example.onlinestore.enums.OrderStatus;
import com.example.onlinestore.enums.PaymentMethod;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Order implements Serializable {
    @Serial
    private static final long serialVersionUID = 8328093958488219106L;

    /**
     * 订单唯一标识符
     */
    private Long id;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 会员ID
     */
    private Long memberId;

    /**
     * 订单总金额
     */
    private BigDecimal totalAmount;

    /**
     * 实际支付金额
     */
    private BigDecimal actualAmount;

    /**
     * 优惠金额
     */
    private BigDecimal discountAmount;

    /**
     * 运费
     */
    private BigDecimal shippingFee;

    /**
     * 收货地址ID
     */
    private Long addressId;

    /**
     * 订单状态
     */
    private OrderStatus status;

    /**
     * 支付方式
     */
    private PaymentMethod paymentMethod;

    /**
     * 支付时间
     */
    private LocalDateTime paymentTime;

    /**
     * 发货时间
     */
    private LocalDateTime shippingTime;

    /**
     * 完成时间
     */
    private LocalDateTime completionTime;

    /**
     * 取消时间
     */
    private LocalDateTime cancelTime;

    /**
     * 取消原因
     */
    private String cancelReason;

    /**
     * 订单备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;

    /**
     * 订单项列表
     */
    private List<OrderItem> items;
} 