package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.Member;
import com.example.onlinestore.bean.Order;
import com.example.onlinestore.bean.OrderItem;
import com.example.onlinestore.dto.OrderRequest;
import com.example.onlinestore.dto.Page;
import com.example.onlinestore.entity.OrderEntity;
import com.example.onlinestore.entity.OrderItemEntity;
import com.example.onlinestore.entity.OrderPaymentEntity;
import com.example.onlinestore.entity.OrderRefundEntity;
import com.example.onlinestore.enums.OrderStatus;
import com.example.onlinestore.enums.PaymentMethod;
import com.example.onlinestore.enums.PaymentStatus;
import com.example.onlinestore.enums.RefundStatus;
import com.example.onlinestore.errors.ErrorCode;
import com.example.onlinestore.event.OrderRefundCompletedEvent;
import com.example.onlinestore.event.RefundCompletedEventData;
import com.example.onlinestore.exceptions.BizException;
import com.example.onlinestore.mapper.OrderItemMapper;
import com.example.onlinestore.mapper.OrderMapper;
import com.example.onlinestore.mapper.OrderPaymentMapper;
import com.example.onlinestore.mapper.OrderRefundMapper;
import com.example.onlinestore.service.MemberService;
import com.example.onlinestore.service.OrderService;
import com.example.onlinestore.service.SkuService;
import com.example.onlinestore.utils.OrderNoGenerator;
import com.example.onlinestore.utils.TimestampOrderNoGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private static final String DEFAULT_ORDER_LIST_QUERY_ORDER = "id DESC";

    private final static OrderNoGenerator orderNoGenerator = new TimestampOrderNoGenerator();
    private static final int MAX_REFUND_COUNT = 5;
    private static final int REFUND_DAYS_LIMIT = 2;
    private static final String REFUND_EVENT_TYPE = "ORDER_REFUND_COMPLETED";
    private static final String REFUND_LIMIT_EXCEEDED_MESSAGE = "您最近2天内的退款次数已达到上限，请稍后再试";
    private static final String REFUND_INVALID_REASON_MESSAGE = "退款原因不能为空";
    private static final String REFUND_INVALID_REASON_LENGTH_MESSAGE = "退款原因长度不能超过255个字符";
    private static final String REFUND_INVALID_REASON_CONTENT_MESSAGE = "退款原因包含非法字符";
    private static final String REFUND_INVALID_REASON_FORMAT_MESSAGE = "退款原因格式不正确";
    private static final String REFUND_INVALID_REASON_BLACKLIST_MESSAGE = "退款原因包含敏感词";
    private static final String REFUND_INVALID_REASON_PATTERN_MESSAGE = "退款原因不符合规则";
    private static final String REFUND_INVALID_REASON_LENGTH_MIN_MESSAGE = "退款原因长度不能小于5个字符";
    private static final String REFUND_INVALID_REASON_LENGTH_MAX_MESSAGE = "退款原因长度不能超过255个字符";

    @Value("${order.discount-rate:0.9}")
    private BigDecimal orderDiscountRate;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private OrderPaymentMapper orderPaymentMapper;

    @Autowired
    private OrderRefundMapper orderRefundMapper;

    @Autowired
    private SkuService skuService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public Order createOrder(@NotNull @Valid OrderRequest request) {
        Member member = memberService.getMemberById(request.getMemberId());
        //校验订单里的商品
        request.getItems().forEach(item -> {
            skuService.getSkuById(item.getSkuId());
            if (!skuService.checkStock(item.getSkuId(), item.getQuantity())) {
                throw new BizException(ErrorCode.SKU_OUT_OF_STOCK, item.getSkuId());
            }
        });
        // 创建订单实体
        OrderEntity order = new OrderEntity();
        order.setOrderNo(orderNoGenerator.generateOrderNo());
        order.setMemberId(request.getMemberId());
        order.setAddressId(request.getAddressId());
        order.setStatus(OrderStatus.PENDING_PAYMENT.name());
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        order.setRemark(request.getRemark());

        // 计算订单金额
        BigDecimal totalAmount = request.getItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);

        order.setTotalAmount(totalAmount.setScale(2, RoundingMode.HALF_UP));
        order.setActualAmount(calculateOrderActualAmount(order.getOrderNo(), totalAmount, member).setScale(2, RoundingMode.HALF_UP));
        order.setDiscountAmount(totalAmount.subtract(order.getActualAmount()).setScale(2, RoundingMode.HALF_UP));

        // 保存订单
        int effectRows = orderMapper.insert(order);
        if (effectRows != 1) {
            logger.error("Failed to create order. Effect rows: {}", effectRows);
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        // 保存订单项
        List<OrderItemEntity> items = request.getItems().stream()
                .map(item -> {
                    OrderItemEntity orderItem = new OrderItemEntity();
                    orderItem.setOrderId(order.getId());
                    orderItem.setSkuId(item.getSkuId());
                    orderItem.setSkuCode(item.getSkuCode());
                    orderItem.setSkuName(item.getSkuName());
                    orderItem.setSkuImage(item.getSkuImage());
                    orderItem.setPrice(item.getPrice());
                    orderItem.setQuantity(item.getQuantity());
                    orderItem.setSubtotal(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
                    return orderItem;
                })
                .collect(Collectors.toList());

        effectRows = orderItemMapper.insertBatch(items);
        if (effectRows != items.size()) {
            logger.error("Failed to create order items. Effect rows: {}, expected: {}", effectRows, items.size());
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }


        return convertToOrder(order);
    }

    @Override
    public Order getOrderById(@NotNull @Min(value = 1, message = "订单ID要大于0") Long id) {
        OrderEntity order = orderMapper.findById(id);
        if (order == null) {
            throw new BizException(ErrorCode.ORDER_NOT_FOUND, id);
        }
        return convertToOrder(order);
    }

    @Override
    public Order getOrderByNo(@NotNull String orderNo) {
        OrderEntity order = orderMapper.findByOrderNo(orderNo);
        if (order == null) {
            throw new BizException(ErrorCode.ORDER_NOT_FOUND, orderNo);
        }
        return convertToOrder(order);
    }

    @Override
    public Page<Order> getOrdersByMemberId(@NotNull @Min(value = 1, message = "会员ID要大于0") Long memberId,
                                           @NotNull @Min(value = 1, message = "页码最小为1") Integer page,
                                           @NotNull @Min(value = 1, message = "每页大小最小为1") Integer size,
                                           OrderStatus status) {
        PageHelper.startPage(page, size, DEFAULT_ORDER_LIST_QUERY_ORDER);
        List<OrderEntity> orders = orderMapper.findByMemberIdAndStatus(memberId, status != null ? status.name() : null);
        PageInfo<OrderEntity> pageInfo = new PageInfo<>(orders);
        return Page.of(orders.stream().map(this::convertToOrder).collect(Collectors.toList()), pageInfo.getTotal(), page, size);
    }

    @Override
    @Transactional
    public void cancelOrder(@NotNull @Min(value = 1, message = "订单ID要大于0") Long id, @NotNull @Min(value = 1, message = "会员ID要大于0") Long memberId, String reason) {
        OrderEntity order = orderMapper.findByIdAndMemberId(id, memberId);
        if (order == null) {
            throw new BizException(ErrorCode.ORDER_NOT_FOUND, id);
        }

        if (!OrderStatus.PENDING_PAYMENT.name().equals(order.getStatus())) {
            throw new BizException(ErrorCode.ORDER_CANNOT_CANCEL);
        }

        order.setStatus(OrderStatus.CANCELLED.name());
        order.setCancelReason(reason);
        order.setCancelTime(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        int effectRows = orderMapper.update(order);
        if (effectRows != 1) {
            logger.error("Failed to cancel order. Effect rows: {}", effectRows);
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional
    public void payOrder(@NotNull @Min(value = 1, message = "订单ID要大于0") Long id, @NotNull @Min(value = 1, message = "会员ID要大于0") Long memberId, @NotNull PaymentMethod paymentMethod) {
        OrderEntity order = orderMapper.findByIdAndMemberId(id, memberId);
        if (order == null) {
            logger.error("Failed to pay order. Order not found. OrderId: {}, memberId:{}", id, memberId);
            throw new BizException(ErrorCode.ORDER_NOT_FOUND, id);
        }
        if (!OrderStatus.PENDING_PAYMENT.name().equals(order.getStatus())) {
            throw new BizException(ErrorCode.ORDER_CANNOT_PAY);
        }
        // 创建支付记录
        OrderPaymentEntity payment = new OrderPaymentEntity();
        payment.setOrderId(order.getId());
        payment.setPaymentNo(generatePaymentNo());
        payment.setPaymentMethod(paymentMethod.name());
        payment.setAmount(order.getActualAmount());
        payment.setStatus(PaymentStatus.PENDING.name());
        payment.setCreatedAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());

        int effectRows = orderPaymentMapper.insert(payment);
        if (effectRows != 1) {
            logger.error("Failed to create payment record. Effect rows: {}", effectRows);
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        // 更新订单状态
        order.setStatus(OrderStatus.PAID.name());
        order.setPaymentMethod(paymentMethod.name());
        order.setPaymentTime(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        effectRows = orderPaymentMapper.update(payment);
        if (effectRows != 1) {
            logger.error("Failed to update payment status. Effect rows: {}", effectRows);
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        // 更新支付状态
        payment.setStatus(PaymentStatus.SUCCESS.name());
        payment.setPaymentTime(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());

        effectRows = orderPaymentMapper.update(payment);
        if (effectRows != 1) {
            logger.error("Failed to update payment status. Effect rows: {}", effectRows);
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional
    public void refundOrder(@NotNull @Min(value = 1, message = "订单ID要大于0") Long id, @NotNull @Min(value = 1, message = "会员ID要大于0") Long memberId, @NotBlank @Size(max = 255, message = "退款原因长度不能超过255个字符") String reason) {
        try {
            // 1. 检查订单是否存在
            OrderEntity order = orderMapper.findByIdAndMemberId(id, memberId);
            if (order == null) {
                logger.error("订单不存在，订单ID：{}，会员ID：{}", id, memberId);
                throw new BizException(ErrorCode.ORDER_NOT_FOUND, memberId);
            }

            // 2. 检查订单状态
            if (!OrderStatus.PAID.name().equals(order.getStatus())) {
                logger.error("订单状态不允许退款，订单ID：{}，当前状态：{}", id, order.getStatus());
                throw new BizException(ErrorCode.ORDER_CANNOT_REFUND);
            }

            // 3. 检查退款原因
            if (reason == null || reason.trim().isEmpty()) {
                logger.error("退款原因不能为空，订单ID：{}", id);
                throw new BizException(ErrorCode.INVALID_PARAMETER, REFUND_INVALID_REASON_MESSAGE);
            }

            if (reason.length() > 255) {
                logger.error("退款原因长度超过限制，订单ID：{}，原因长度：{}", id, reason.length());
                throw new BizException(ErrorCode.INVALID_PARAMETER, REFUND_INVALID_REASON_LENGTH_MESSAGE);
            }

            if (reason.length() < 5) {
                logger.error("退款原因长度不足，订单ID：{}，原因长度：{}", id, reason.length());
                throw new BizException(ErrorCode.INVALID_PARAMETER, REFUND_INVALID_REASON_LENGTH_MIN_MESSAGE);
            }

            if (reason.matches(".*[<>\"'&].*")) {
                logger.error("退款原因包含非法字符，订单ID：{}，原因：{}", id, reason);
                throw new BizException(ErrorCode.INVALID_PARAMETER, REFUND_INVALID_REASON_CONTENT_MESSAGE);
            }

            if (!reason.matches("^[\\u4e00-\\u9fa5a-zA-Z0-9\\s,.!?，。！？]+$")) {
                logger.error("退款原因格式不正确，订单ID：{}，原因：{}", id, reason);
                throw new BizException(ErrorCode.INVALID_PARAMETER, REFUND_INVALID_REASON_FORMAT_MESSAGE);
            }

            // 4. 检查最近2天的退款次数
            LocalDateTime twoDaysAgo = LocalDateTime.now().minusDays(REFUND_DAYS_LIMIT);
            List<OrderRefundEntity> recentRefunds = orderRefundMapper.findByMemberIdAndCreatedAtAfter(memberId, twoDaysAgo);
            if (recentRefunds != null && recentRefunds.size() >= MAX_REFUND_COUNT) {
                logger.error("退款次数超过限制，会员ID：{}，最近退款次数：{}", memberId, recentRefunds.size());
                throw new BizException(ErrorCode.REFUND_LIMIT_EXCEEDED, REFUND_LIMIT_EXCEEDED_MESSAGE);
            }

            // 5. 获取支付记录
            OrderPaymentEntity payment = orderPaymentMapper.findByOrderId(order.getId());
            if (payment == null) {
                logger.error("支付记录不存在，订单ID：{}", id);
                throw new BizException(ErrorCode.PAYMENT_NOT_FOUND);
            }

            // 6. 创建退款记录
            OrderRefundEntity refund = new OrderRefundEntity();
            refund.setOrderId(order.getId());
            refund.setPaymentId(payment.getId());
            refund.setRefundNo(generateRefundNo());
            refund.setAmount(order.getActualAmount());
            refund.setReason(reason);
            refund.setStatus(RefundStatus.PENDING.name());
            refund.setCreatedAt(LocalDateTime.now());
            refund.setUpdatedAt(LocalDateTime.now());

            int effectRows = orderRefundMapper.insert(refund);
            if (effectRows != 1) {
                logger.error("创建退款记录失败，订单ID：{}，影响行数：{}", id, effectRows);
                throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
            }

            // 7. 更新订单状态
            order.setStatus(OrderStatus.REFUNDING.name());
            order.setUpdatedAt(LocalDateTime.now());

            effectRows = orderMapper.update(order);
            if (effectRows != 1) {
                logger.error("更新订单状态失败，订单ID：{}，影响行数：{}", id, effectRows);
                throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
            }

            // 8. 更新退款状态
            refund.setStatus(RefundStatus.SUCCESS.name());
            refund.setRefundTime(LocalDateTime.now());
            refund.setUpdatedAt(LocalDateTime.now());

            effectRows = orderRefundMapper.update(refund);
            if (effectRows != 1) {
                logger.error("更新退款状态失败，订单ID：{}，影响行数：{}", id, effectRows);
                throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
            }

            // 9. 更新订单状态为已退款
            order.setStatus(OrderStatus.REFUNDED.name());
            order.setUpdatedAt(LocalDateTime.now());

            effectRows = orderMapper.update(order);
            if (effectRows != 1) {
                logger.error("更新订单状态失败，订单ID：{}，影响行数：{}", id, effectRows);
                throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
            }

            // 10. 发送退款完成事件
            try {
                RefundCompletedEventData eventData = RefundCompletedEventData.builder()
                    .orderId(order.getId())
                    .orderNo(order.getOrderNo())
                    .memberId(order.getMemberId())
                    .refundId(refund.getId())
                    .refundNo(refund.getRefundNo())
                    .amount(refund.getAmount())
                    .reason(refund.getReason())
                    .refundTime(refund.getRefundTime())
                    .eventType(REFUND_EVENT_TYPE)
                    .eventTime(LocalDateTime.now())
                    .build();

                eventPublisher.publishEvent(new OrderRefundCompletedEvent(eventData));
                logger.info("退款完成事件发送成功，订单ID：{}，退款ID：{}", id, refund.getId());
            } catch (Exception e) {
                logger.error("发送退款完成事件失败，订单ID：{}，退款ID：{}，错误：{}", id, refund.getId(), e.getMessage());
                // 事件发送失败不影响主流程
            }

            // 11. 记录成功日志
            logger.info("退款申请处理成功，订单ID：{}，退款ID：{}，金额：{}，原因：{}", id, refund.getId(), refund.getAmount(), refund.getReason());
        } catch (BizException e) {
            logger.error("退款申请处理失败，订单ID：{}", id, e);
            throw e;
        } catch (Exception e) {
            logger.error("退款申请处理异常，订单ID：{}", id, e);
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private String generatePaymentNo() {
        return "P" + UUID.randomUUID().toString().replace("-", "").substring(0, 22);
    }

    private String generateRefundNo() {
        return "R" + UUID.randomUUID().toString().replace("-", "").substring(0, 22);
    }

    private Order convertToOrder(OrderEntity entity) {
        Order order = new Order();
        order.setId(entity.getId());
        order.setOrderNo(entity.getOrderNo());
        order.setMemberId(entity.getMemberId());
        order.setTotalAmount(entity.getTotalAmount());
        order.setActualAmount(entity.getActualAmount());
        order.setDiscountAmount(entity.getDiscountAmount());
        order.setShippingFee(entity.getShippingFee());
        order.setAddressId(entity.getAddressId());
        order.setStatus(OrderStatus.valueOf(entity.getStatus()));
        order.setPaymentMethod(PaymentMethod.valueOf(entity.getPaymentMethod()));
        order.setPaymentTime(entity.getPaymentTime());
        order.setShippingTime(entity.getShippingTime());
        order.setCompletionTime(entity.getCompletionTime());
        order.setCancelTime(entity.getCancelTime());
        order.setCancelReason(entity.getCancelReason());
        order.setRemark(entity.getRemark());
        order.setCreatedAt(entity.getCreatedAt());
        order.setUpdatedAt(entity.getUpdatedAt());

        // Convert order items
        List<OrderItemEntity> itemEntities = orderItemMapper.findByOrderId(entity.getId());
        List<OrderItem> items = itemEntities.stream()
                .map(itemEntity -> {
                    OrderItem item = new OrderItem();
                    item.setId(itemEntity.getId());
                    item.setOrderId(itemEntity.getOrderId());
                    item.setSkuId(itemEntity.getSkuId());
                    item.setSkuCode(itemEntity.getSkuCode());
                    item.setSkuName(itemEntity.getSkuName());
                    item.setSkuImage(itemEntity.getSkuImage());
                    item.setPrice(itemEntity.getPrice());
                    item.setQuantity(itemEntity.getQuantity());
                    item.setSubtotal(itemEntity.getSubtotal());
                    return item;
                })
                .collect(Collectors.toList());
        order.setItems(items);

        return order;
    }

    private BigDecimal calculateOrderActualAmount(String orderNo, BigDecimal totalAmount, Member member) {
        //
        BigDecimal actualAmount = totalAmount;
        if (orderDiscountRate.compareTo(BigDecimal.ONE) < 0 && orderDiscountRate.compareTo(BigDecimal.ZERO) > 0) {
            actualAmount = actualAmount.multiply(orderDiscountRate);
        }
        //
        BigDecimal points = memberService.getMemberPointBalance(member.getId());
        // 积分抵扣， 一个积分抵用1元
        if (points.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal pointsToUse = points.min(actualAmount);
            actualAmount = actualAmount.subtract(pointsToUse);

            memberService.consumePoints(member.getId(), orderNo, pointsToUse, "订单使用");
        }

        // 如果优惠金额小于等于0，则直接返回0
        if (actualAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO;
        }

        return actualAmount;

    }
} 