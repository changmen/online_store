package com.example.onlinestore.entity;

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

/**
 * 订单实体类
 * 用于表示系统中的订单信息，包含订单的基本信息、状态、金额等
 *
 * @author example
 * @version 1.0
 * @since 2024-04-22
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class OrderEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 订单ID，唯一标识符
     */
    private Long id;

    /**
     * 订单编号，系统生成的唯一订单号
     */
    private String orderNo;

    /**
     * 会员ID，下单用户的唯一标识
     */
    private Long memberId;

    /**
     * 订单总金额，包含所有商品金额
     */
    private BigDecimal totalAmount;

    /**
     * 实际支付金额，扣除优惠后的金额
     */
    private BigDecimal actualAmount;

    /**
     * 优惠金额，订单享受的优惠总额
     */
    private BigDecimal discountAmount;

    /**
     * 运费，订单的配送费用
     */
    private BigDecimal shippingFee;

    /**
     * 收货地址ID，关联用户的收货地址
     */
    private Long addressId;

    /**
     * 订单状态，参考{@link OrderStatus}
     */
    private String status;

    /**
     * 支付方式，参考{@link PaymentMethod}
     */
    private String paymentMethod;

    /**
     * 支付时间，订单完成支付的时间
     */
    private LocalDateTime paymentTime;

    /**
     * 发货时间，订单发货的时间
     */
    private LocalDateTime shippingTime;

    /**
     * 完成时间，订单完成的时间
     */
    private LocalDateTime completionTime;

    /**
     * 取消时间，订单取消的时间
     */
    private LocalDateTime cancelTime;

    /**
     * 取消原因，订单取消的具体原因
     */
    private String cancelReason;

    /**
     * 订单备注，用户或系统添加的备注信息
     */
    private String remark;

    /**
     * 创建时间，订单创建的时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间，订单最后更新的时间
     */
    private LocalDateTime updatedAt;
}