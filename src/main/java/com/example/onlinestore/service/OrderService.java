package com.example.onlinestore.service;

import com.example.onlinestore.bean.Order;
import com.example.onlinestore.dto.OrderRequest;
import com.example.onlinestore.dto.Page;
import com.example.onlinestore.dto.PaymentRequest;
import com.example.onlinestore.dto.RefundRequest;
import com.example.onlinestore.enums.OrderStatus;
import com.example.onlinestore.enums.PaymentMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public interface OrderService {
    /**
     * 创建订单
     *
     * @param request 订单创建请求
     * @return 订单
     * @throws com.example.onlinestore.exceptions.BizException 当订单创建的时候校验失败，如SKU不存在，库存不足，或者操作DB失败，则抛出该异常
     */
    Order createOrder(@NotNull @Valid OrderRequest request);

    /**
     * 根据订单ID获取订单
     *
     * @param id 订单ID
     * @return 订单
     * @throws com.example.onlinestore.exceptions.BizException 当订单不存在或者查询DB失败，则抛出该异常
     */
    Order getOrderById(@NotNull @Min(value = 1, message = "订单ID要大于0") Long id);

    /**
     * 根据订单号获取订单
     *
     * @param orderNo 订单号
     * @return 订单Order
     * @throws com.example.onlinestore.exceptions.BizException 当订单不存在或者查询DB失败，则抛出该异常
     */
    Order getOrderByNo(@NotBlank(message = "订单号不能为空") @Size(max = 32, message = "订单号长度不能超过32个字符") String orderNo);

    /**
     * 获取会员的订单列表
     *
     * @param memberId 会员ID
     * @return 订单列表
     * @throws com.example.onlinestore.exceptions.BizException 当查询DB失败，则抛出该异常
     */
    Page<Order> getOrdersByMemberId(@NotNull @Min(value = 1, message = "会员ID要大于0") Long memberId,
                                    @NotNull @Min(value = 1, message = "页码最小为1") Integer page,
                                    @NotNull @Min(value = 1, message = "每页大小最小为1") Integer size,
                                    OrderStatus status);

    /**
     * 取消订单
     *
     * @param id       订单ID
     * @param memberId 会员ID
     * @param reason   取消原因
     * @throws com.example.onlinestore.exceptions.BizException 当订单不存在或者查询DB失败，则抛出该异常
     */
    void cancelOrder(@NotNull @Min(value = 1, message = "订单ID要大于0") Long id, @NotNull @Min(value = 1, message = "会员ID要大于0") Long memberId,
                     @Size(max = 128, message = "取消原因长度不能超过255个字符") String reason);

    /**
     * 支付订单
     *
     * @param id            订单ID
     * @param memberId      会员ID
     * @param paymentMethod 支付方式
     * @throws com.example.onlinestore.exceptions.BizException 当订单不存在或者查询DB失败，则抛出该异常
     */
    void payOrder(@NotNull @Min(value = 1, message = "订单ID要大于0") Long id, @NotNull @Min(value = 1, message = "会员ID要大于0") Long memberId, @NotNull PaymentMethod paymentMethod);

    /**
     * 申请退款
     *
     * @param id       订单ID
     * @param memberId 会员ID
     * @param reason   退款原因
     * @throws com.example.onlinestore.exceptions.BizException 当订单不存在或者查询DB失败，则抛出该异常
     */
    void refundOrder(@NotNull @Min(value = 1, message = "订单ID要大于0") Long id, @NotNull @Min(value = 1, message = "会员ID要大于0") Long memberId, @NotBlank @Size(max = 255, message = "退款原因长度不能超过255个字符") String reason);
} 