package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.Order;
import com.example.onlinestore.bean.OrderItem;
import com.example.onlinestore.dto.OrderItemRequest;
import com.example.onlinestore.dto.OrderRequest;
import com.example.onlinestore.dto.PaymentRequest;
import com.example.onlinestore.dto.RefundRequest;
import com.example.onlinestore.entity.OrderEntity;
import com.example.onlinestore.entity.OrderItemEntity;
import com.example.onlinestore.entity.OrderPaymentEntity;
import com.example.onlinestore.entity.OrderRefundEntity;
import com.example.onlinestore.enums.OrderStatus;
import com.example.onlinestore.enums.PaymentMethod;
import com.example.onlinestore.enums.PaymentStatus;
import com.example.onlinestore.enums.RefundStatus;
import com.example.onlinestore.errors.ErrorCode;
import com.example.onlinestore.exceptions.BizException;
import com.example.onlinestore.mapper.OrderItemMapper;
import com.example.onlinestore.mapper.OrderMapper;
import com.example.onlinestore.mapper.OrderPaymentMapper;
import com.example.onlinestore.mapper.OrderRefundMapper;
import com.example.onlinestore.service.OrderService;
import com.example.onlinestore.service.SkuService;
import com.example.onlinestore.utils.OrderNoGenerator;
import com.example.onlinestore.utils.TimestampOrderNoGenerator;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final static OrderNoGenerator orderNoGenerator = new TimestampOrderNoGenerator();

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

    @Override
    @Transactional
    public Order createOrder(@NotNull @Valid OrderRequest request) {
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
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderItemRequest item : request.getItems()) {
            totalAmount = totalAmount.add(item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        }
        order.setTotalAmount(totalAmount.setScale(2, RoundingMode.HALF_UP));
        order.setActualAmount(totalAmount.setScale(2, RoundingMode.HALF_UP)); // 暂时没有优惠和运费

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
    public List<Order> getOrdersByMemberId(@NotNull @Min(value = 1, message = "会员ID要大于0") Long memberId) {
        List<OrderEntity> orders = orderMapper.findByMemberId(memberId);
        return orders.stream()
                .map(this::convertToOrder)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void cancelOrder(@NotNull @Min(value = 1, message = "订单ID要大于0") Long id, @NotNull @Min(value = 1, message = "会员ID要大于0") Long memberId, String reason) {
        OrderEntity order = orderMapper.findByIdAndMemberId(id, memberId);
        if (order == null) {
            throw new BizException(ErrorCode.ORDER_NOT_FOUND, id);
        }

        if (OrderStatus.PENDING_PAYMENT != OrderStatus.valueOf(order.getStatus())) {
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
    public void payOrder(@Valid PaymentRequest request) {
        OrderEntity order = orderMapper.findByIdAndMemberId(request.getOrderId(), request.getMemberId());
        if (order == null) {
            throw new BizException(ErrorCode.ORDER_NOT_FOUND, request.getOrderId());
        }
        if (OrderStatus.PENDING_PAYMENT != OrderStatus.valueOf(order.getStatus())) {
            throw new BizException(ErrorCode.ORDER_CANNOT_PAY);
        }
        // 创建支付记录
        OrderPaymentEntity payment = new OrderPaymentEntity();
        payment.setOrderId(order.getId());
        payment.setPaymentNo(generatePaymentNo());
        payment.setPaymentMethod(request.getPaymentMethod().name());
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
        order.setPaymentMethod(request.getPaymentMethod().name());
        order.setPaymentTime(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        effectRows = orderMapper.update(order);
        if (effectRows != 1) {
            logger.error("Failed to update order status. Effect rows: {}", effectRows);
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
    public void refundOrder(@Valid RefundRequest request) {
        OrderEntity order = orderMapper.findByIdAndMemberId(request.getOrderId(), request.getMemberId());
        if (order == null) {
            throw new BizException(ErrorCode.ORDER_NOT_FOUND, request.getOrderId());
        }

        if (OrderStatus.PAID != OrderStatus.valueOf(order.getStatus())) {
            throw new BizException(ErrorCode.ORDER_CANNOT_REFUND);
        }

        // 获取支付记录
        OrderPaymentEntity payment = orderPaymentMapper.findByOrderId(order.getId());
        if (payment == null) {
            throw new BizException(ErrorCode.PAYMENT_NOT_FOUND);
        }

        // 创建退款记录
        OrderRefundEntity refund = new OrderRefundEntity();
        refund.setOrderId(order.getId());
        refund.setPaymentId(payment.getId());
        refund.setRefundNo(generateRefundNo());
        refund.setAmount(order.getActualAmount());
        refund.setReason(request.getReason());
        refund.setStatus(RefundStatus.PENDING.name());
        refund.setCreatedAt(LocalDateTime.now());
        refund.setUpdatedAt(LocalDateTime.now());

        int effectRows = orderRefundMapper.insert(refund);
        if (effectRows != 1) {
            logger.error("Failed to create refund record. Effect rows: {}", effectRows);
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        // 更新订单状态
        order.setStatus(OrderStatus.REFUNDING.name());
        order.setUpdatedAt(LocalDateTime.now());

        effectRows = orderMapper.update(order);
        if (effectRows != 1) {
            logger.error("Failed to update order status. Effect rows: {}", effectRows);
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        // 更新退款状态
        refund.setStatus(RefundStatus.SUCCESS.name());
        refund.setRefundTime(LocalDateTime.now());
        refund.setUpdatedAt(LocalDateTime.now());

        effectRows = orderRefundMapper.update(refund);
        if (effectRows != 1) {
            logger.error("Failed to update refund status. Effect rows: {}", effectRows);
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        // 更新订单状态为已退款
        order.setStatus(OrderStatus.REFUNDED.name());
        order.setUpdatedAt(LocalDateTime.now());

        effectRows = orderMapper.update(order);
        if (effectRows != 1) {
            logger.error("Failed to update order status. Effect rows: {}", effectRows);
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private String generatePaymentNo() {
        return "P" + UUID.randomUUID().toString().replace("-", "").substring(0, 15);
    }

    private String generateRefundNo() {
        return "R" + UUID.randomUUID().toString().replace("-", "").substring(0, 15);
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
} 