package com.example.onlinestore.service.impl;

import com.example.onlinestore.dto.OrderDetailDTO;
import com.example.onlinestore.entity.CartItemEntity;
import com.example.onlinestore.entity.OrderEntity;
import com.example.onlinestore.entity.OrderItemEntity;
import com.example.onlinestore.enums.OrderStatus;
import com.example.onlinestore.exception.OrderException;
import com.example.onlinestore.mapper.OrderItemMapper;
import com.example.onlinestore.mapper.OrderMapper;
import com.example.onlinestore.service.CartService;
import com.example.onlinestore.service.InventoryService;
import com.example.onlinestore.service.ItemService;
import com.example.onlinestore.service.OrderAmountCalculator;
import com.example.onlinestore.service.OrderService;
import com.example.onlinestore.service.SkuService;
import com.example.onlinestore.util.OrderNoGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final CartService cartService;
    private final ItemService itemService;
    private final SkuService skuService;
    private final InventoryService inventoryService;
    private final OrderNoGenerator orderNoGenerator;
    private final OrderAmountCalculator amountCalculator;
    private final OrderService self;

    @Value("${order.shipping-fee:10.00}")
    private BigDecimal defaultShippingFee;

    public OrderServiceImpl(OrderMapper orderMapper,
                            OrderItemMapper orderItemMapper,
                            CartService cartService,
                            ItemService itemService,
                            SkuService skuService,
                            InventoryService inventoryService,
                            OrderNoGenerator orderNoGenerator,
                            OrderAmountCalculator amountCalculator,
                            @Lazy OrderService self) {
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.cartService = cartService;
        this.itemService = itemService;
        this.skuService = skuService;
        this.inventoryService = inventoryService;
        this.orderNoGenerator = orderNoGenerator;
        this.amountCalculator = amountCalculator;
        this.self = self;
    }

    @Override
    @Transactional
    public String createOrder(
        Long userId,
        String receiverName,
        String receiverPhone,
        String receiverAddress,
        String paymentMethod,
        String remark
    ) {
        if (userId == null) {
            throw new OrderException("用户ID不能为空");
        }
        if (receiverName == null || receiverName.trim().isEmpty()) {
            throw new OrderException("收货人姓名不能为空");
        }
        if (receiverPhone == null || receiverPhone.trim().isEmpty()) {
            throw new OrderException("收货人电话不能为空");
        }
        if (receiverAddress == null || receiverAddress.trim().isEmpty()) {
            throw new OrderException("收货地址不能为空");
        }

        OrderEntity order = new OrderEntity();
        order.setOrderNo(orderNoGenerator.generate());
        order.setUserId(userId);
        order.setTotalAmount(BigDecimal.ZERO);
        order.setPayAmount(BigDecimal.ZERO);
        order.setDiscountAmount(BigDecimal.ZERO);
        order.setShippingFee(BigDecimal.ZERO);
        order.setStatus(OrderStatus.PENDING_PAYMENT.getCode());
        order.setReceiverName(receiverName);
        order.setReceiverPhone(receiverPhone);
        order.setReceiverAddress(receiverAddress);
        order.setPaymentMethod(paymentMethod);
        order.setRemark(remark);

        orderMapper.insertOrder(order);

        return order.getOrderNo();
    }

    @Override
    @Transactional
    public String createOrderFromCart(
        Long userId,
        List<Long> cartItemIds,
        String receiverName,
        String receiverPhone,
        String receiverAddress,
        String paymentMethod,
        String remark
    ) {
        if (userId == null) {
            throw new OrderException("用户ID不能为空");
        }
        if (cartItemIds == null || cartItemIds.isEmpty()) {
            throw new OrderException("购物车项不能为空");
        }
        if (receiverName == null || receiverName.trim().isEmpty()) {
            throw new OrderException("收货人姓名不能为空");
        }
        if (receiverPhone == null || receiverPhone.trim().isEmpty()) {
            throw new OrderException("收货人电话不能为空");
        }
        if (receiverAddress == null || receiverAddress.trim().isEmpty()) {
            throw new OrderException("收货地址不能为空");
        }

        List<CartItemEntity> cartItems = cartService.getCartItems(userId);
        List<CartItemEntity> selectedCartItems = cartItems.stream()
                .filter(item -> cartItemIds.contains(item.getId()))
                .collect(Collectors.toList());

        if (selectedCartItems.isEmpty()) {
            throw new OrderException("未找到选中的购物车项");
        }

        OrderEntity order = new OrderEntity();
        order.setOrderNo(orderNoGenerator.generate());
        order.setUserId(userId);
        order.setStatus(OrderStatus.PENDING_PAYMENT.getCode());
        order.setReceiverName(receiverName);
        order.setReceiverPhone(receiverPhone);
        order.setReceiverAddress(receiverAddress);
        order.setPaymentMethod(paymentMethod);
        order.setRemark(remark);

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItemEntity> orderItems = new ArrayList<>();

        for (CartItemEntity cartItem : selectedCartItems) {
            com.example.onlinestore.bean.Item item = itemService.getItemById(cartItem.getItemId());
            if (item == null) {
                throw new OrderException("商品不存在: " + cartItem.getItemId());
            }

            if (inventoryService.getInventoryById(cartItem.getSkuId()) == null) {
                throw new OrderException("商品库存不存在: " + cartItem.getSkuId());
            }

            com.example.onlinestore.bean.Sku sku = skuService.getSkuById(cartItem.getSkuId());
            if (sku == null || sku.getPrice() == null) {
                throw new OrderException("商品价格未配置: " + cartItem.getSkuId());
            }

            BigDecimal price = sku.getPrice();
            BigDecimal totalItemAmount = amountCalculator.calculateItemTotalAmount(price, cartItem.getQuantity());

            OrderItemEntity orderItem = new OrderItemEntity();
            orderItem.setOrderNo(order.getOrderNo());
            orderItem.setItemId(cartItem.getItemId());
            orderItem.setSkuId(cartItem.getSkuId());
            orderItem.setItemName(item.getName());
            orderItem.setItemImage(item.getImage());
            orderItem.setSkuProperties(sku.getProperties());
            orderItem.setPrice(price);
            orderItem.setOriginalPrice(sku.getOriginalPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setTotalAmount(totalItemAmount);

            orderItems.add(orderItem);
            totalAmount = totalAmount.add(totalItemAmount);
        }

        order.setTotalAmount(totalAmount);
        order.setPayAmount(totalAmount.add(defaultShippingFee));
        order.setDiscountAmount(BigDecimal.ZERO);
        order.setShippingFee(defaultShippingFee);

        orderMapper.insertOrder(order);

        for (OrderItemEntity orderItem : orderItems) {
            orderItem.setOrderId(order.getId());
        }
        orderItemMapper.batchInsertOrderItems(orderItems);

        for (CartItemEntity cartItem : selectedCartItems) {
            cartService.removeFromCart(userId, cartItem.getItemId());
        }

        for (OrderItemEntity orderItem : orderItems) {
            inventoryService.lockInventory(orderItem.getSkuId(), orderItem.getQuantity());
        }

        return order.getOrderNo();
    }

    @Override
    public OrderEntity getOrderById(Long orderId) {
        return orderMapper.findById(orderId);
    }

    @Override
    public OrderEntity getOrderByOrderNo(String orderNo) {
        return orderMapper.findByOrderNo(orderNo);
    }

    @Override
    public List<OrderItemEntity> getOrderItems(Long orderId) {
        return orderItemMapper.findByOrderId(orderId);
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId) {
        OrderEntity order = orderMapper.findById(orderId);
        if (order == null) {
            throw new OrderException("订单不存在");
        }

        if (order.getStatus() != OrderStatus.PENDING_PAYMENT.getCode()) {
            throw new OrderException("只能取消待付款的订单");
        }

        order.setStatus(OrderStatus.CANCELLED.getCode());
        order.setCancelTime(LocalDateTime.now());
        orderMapper.updateOrder(order);

        List<OrderItemEntity> orderItems = orderItemMapper.findByOrderId(orderId);
        for (OrderItemEntity orderItem : orderItems) {
            inventoryService.unlockInventory(orderItem.getSkuId(), orderItem.getQuantity());
        }
    }

    @Override
    @Transactional
    public void payOrder(String orderNo) {
        OrderEntity order = orderMapper.findByOrderNo(orderNo);
        if (order == null) {
            throw new OrderException("订单不存在");
        }

        if (order.getStatus() != OrderStatus.PENDING_PAYMENT.getCode()) {
            throw new OrderException("订单状态不正确");
        }

        order.setStatus(OrderStatus.PENDING_SHIPMENT.getCode());
        order.setPayTime(LocalDateTime.now());
        orderMapper.updateOrder(order);

        List<OrderItemEntity> orderItems = orderItemMapper.findByOrderNo(orderNo);
        for (OrderItemEntity orderItem : orderItems) {
            inventoryService.deductInventory(orderItem.getSkuId(), orderItem.getQuantity());
        }
    }

    @Override
    @Transactional
    public void shipOrder(String orderNo, String trackingNo, String logisticsCompany) {
        OrderEntity order = orderMapper.findByOrderNo(orderNo);
        if (order == null) {
            throw new OrderException("订单不存在");
        }

        if (order.getStatus() != OrderStatus.PENDING_SHIPMENT.getCode()) {
            throw new OrderException("订单状态不正确");
        }

        order.setStatus(OrderStatus.PENDING_RECEIPT.getCode());
        order.setDeliveryTime(LocalDateTime.now());
        orderMapper.updateOrder(order);
    }

    @Override
    @Transactional
    public void confirmReceive(String orderNo) {
        OrderEntity order = orderMapper.findByOrderNo(orderNo);
        if (order == null) {
            throw new OrderException("订单不存在");
        }

        if (order.getStatus() != OrderStatus.PENDING_RECEIPT.getCode()) {
            throw new OrderException("订单状态不正确");
        }

        order.setStatus(OrderStatus.COMPLETED.getCode());
        order.setCompleteTime(LocalDateTime.now());
        orderMapper.updateOrder(order);
    }

    @Override
    public List<OrderEntity> getUserOrders(Long userId, Integer status, int page, int size) {
        int offset = (page - 1) * size;
        return orderMapper.findByCondition(userId, status, null, null, offset, size);
    }

    @Override
    public long countUserOrders(Long userId, Integer status) {
        return orderMapper.countByCondition(userId, status, null, null);
    }

    @Override
    public void processTimeoutOrders(int timeoutMinutes, int batchSize) {
        List<OrderEntity> timeoutOrders = orderMapper.findTimeoutOrders(timeoutMinutes, batchSize);
        for (OrderEntity order : timeoutOrders) {
            try {
                self.cancelOrder(order.getId());
                logger.info("自动取消超时订单: {}", order.getOrderNo());
            } catch (Exception e) {
                logger.error("取消超时订单失败: {}", order.getOrderNo(), e);
            }
        }
    }

    @Override
    public List<OrderItemEntity> getUserPurchasedItems(Long userId, int page, int size) {
        int offset = (page - 1) * size;
        return orderItemMapper.findUserPurchasedItems(userId, offset, size);
    }

    @Override
    public int countItemSales(Long itemId, LocalDateTime startTime, LocalDateTime endTime) {
        return orderItemMapper.countItemSales(itemId, startTime, endTime);
    }

    @Override
    @Transactional
    public void updateQuantity(Long orderItemId, Integer quantity) {
        OrderItemEntity orderItem = orderItemMapper.findById(orderItemId);
        if (orderItem == null) {
            throw new OrderException("订单项不存在");
        }

        if (quantity > 0) {
            orderItem.setQuantity(quantity);
            orderItem.setTotalAmount(amountCalculator.calculateItemTotalAmount(orderItem.getPrice(), quantity));
            orderItemMapper.updateOrderItem(orderItem);
            recalculateOrderAmount(orderItem.getOrderId());
            logger.info("更新订单项数量: orderItemId={}, quantity={}", orderItemId, quantity);
        } else {
            orderItemMapper.deleteOrderItem(orderItemId);
            recalculateOrderAmount(orderItem.getOrderId());
            logger.info("删除订单项: orderItemId={}", orderItemId);
        }
    }

    private void recalculateOrderAmount(Long orderId) {
        OrderEntity order = orderMapper.findById(orderId);
        if (order == null) {
            throw new OrderException("订单不存在");
        }
        List<OrderItemEntity> orderItems = orderItemMapper.findByOrderId(orderId);
        amountCalculator.recalculateOrderAmount(order, orderItems);
        orderMapper.updateOrder(order);
    }

    @Override
    public OrderDetailDTO getOrderDetail(String orderNo, Long userId) {
        OrderEntity order = orderMapper.findByOrderNo(orderNo);
        if (order == null) {
            throw new OrderException("订单不存在");
        }

        List<OrderItemEntity> orderItems = orderItemMapper.findByOrderNo(orderNo);
        logger.info("查询订单详情: orderNo={}, userId={}", orderNo, userId);

        return new OrderDetailDTO(order, orderItems);
    }

}
