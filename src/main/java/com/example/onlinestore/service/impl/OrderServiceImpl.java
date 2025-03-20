package com.example.onlinestore.service.impl;

import com.example.onlinestore.entity.CartItemEntity;
import com.example.onlinestore.entity.ItemEntity;
import com.example.onlinestore.entity.OrderEntity;
import com.example.onlinestore.entity.OrderItemEntity;
import com.example.onlinestore.exception.OrderException;
import com.example.onlinestore.mapper.OrderItemMapper;
import com.example.onlinestore.mapper.OrderMapper;
import com.example.onlinestore.service.CartService;
import com.example.onlinestore.service.InventoryService;
import com.example.onlinestore.service.ItemService;
import com.example.onlinestore.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    
    @Autowired
    private OrderMapper orderMapper;
    
    @Autowired
    private OrderItemMapper orderItemMapper;
    
    @Autowired
    private CartService cartService;
    
    @Autowired
    private ItemService itemService;
    
    @Autowired
    private InventoryService inventoryService;

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
        // 参数验证
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
        
        // 生成订单号
        String orderNo = generateOrderNo();
        
        // 创建订单
        OrderEntity order = new OrderEntity();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setTotalAmount(BigDecimal.ZERO);
        order.setPayAmount(BigDecimal.ZERO);
        order.setDiscountAmount(BigDecimal.ZERO);
        order.setShippingFee(BigDecimal.ZERO);
        order.setStatus(0); // 待付款
        order.setReceiverName(receiverName);
        order.setReceiverPhone(receiverPhone);
        order.setReceiverAddress(receiverAddress);
        order.setPaymentMethod(paymentMethod);
        order.setRemark(remark);
        
        orderMapper.insertOrder(order);
        
        return orderNo;
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
        // 参数验证
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
        
        // 获取购物车项
        List<CartItemEntity> cartItems = cartService.getCartItems(userId);
        List<CartItemEntity> selectedCartItems = cartItems.stream()
                .filter(item -> cartItemIds.contains(item.getId()))
                .collect(Collectors.toList());
        
        if (selectedCartItems.isEmpty()) {
            throw new OrderException("未找到选中的购物车项");
        }
        
        // 生成订单号
        String orderNo = generateOrderNo();
        
        // 创建订单
        OrderEntity order = new OrderEntity();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setStatus(0); // 待付款
        order.setReceiverName(receiverName);
        order.setReceiverPhone(receiverPhone);
        order.setReceiverAddress(receiverAddress);
        order.setPaymentMethod(paymentMethod);
        order.setRemark(remark);
        
        // 计算订单金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal shippingFee = new BigDecimal("10.00"); // 默认运费
        
        // 创建订单项
        List<OrderItemEntity> orderItems = new ArrayList<>();
        
        for (CartItemEntity cartItem : selectedCartItems) {
            // 获取商品信息
            com.example.onlinestore.bean.Item item = itemService.getItemById(cartItem.getItemId());
            if (item == null) {
                throw new OrderException("商品不存在: " + cartItem.getItemId());
            }
            
            // 检查库存
            if (inventoryService.getInventoryById(cartItem.getSkuId()) == null) {
                throw new OrderException("商品库存不存在: " + cartItem.getSkuId());
            }
            
            // TODO: 获取商品价格，这里简化处理，实际应该从SKU中获取
            BigDecimal price = new BigDecimal("100.00");
            BigDecimal originalPrice = new BigDecimal("120.00");
            
            // 创建订单项
            OrderItemEntity orderItem = new OrderItemEntity();
            orderItem.setOrderId(null); // 订单保存后设置
            orderItem.setOrderNo(orderNo);
            orderItem.setItemId(cartItem.getItemId());
            orderItem.setSkuId(cartItem.getSkuId());
            orderItem.setItemName(item.getName());
            orderItem.setItemImage(item.getImage());
            orderItem.setSkuProperties(""); // TODO: 从SKU中获取属性
            orderItem.setPrice(price);
            orderItem.setOriginalPrice(originalPrice);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setTotalAmount(price.multiply(new BigDecimal(cartItem.getQuantity())));
            
            orderItems.add(orderItem);
            
            // 累加总金额
            totalAmount = totalAmount.add(orderItem.getTotalAmount());
        }
        
        // 设置订单金额
        order.setTotalAmount(totalAmount);
        order.setPayAmount(totalAmount.add(shippingFee));
        order.setDiscountAmount(BigDecimal.ZERO);
        order.setShippingFee(shippingFee);
        
        // 保存订单
        orderMapper.insertOrder(order);
        
        // 设置订单项的订单ID并保存
        for (OrderItemEntity orderItem : orderItems) {
            orderItem.setOrderId(order.getId());
        }
        orderItemMapper.batchInsertOrderItems(orderItems);
        
        // 从购物车中删除已下单的商品
        for (CartItemEntity cartItem : selectedCartItems) {
            cartService.removeFromCart(userId, cartItem.getItemId());
        }
        
        // 锁定库存
        for (OrderItemEntity orderItem : orderItems) {
            inventoryService.lockInventory(orderItem.getSkuId(), orderItem.getQuantity());
        }
        
        return orderNo;
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
        
        if (order.getStatus() != 0) {
            throw new OrderException("只能取消待付款的订单");
        }
        
        // 更新订单状态
        order.setStatus(4); // 已取消
        order.setCancelTime(LocalDateTime.now());
        orderMapper.updateOrder(order);
        
        // 解锁库存
        List<OrderItemEntity> orderItems = orderItemMapper.findByOrderId(orderId);
        for (OrderItemEntity orderItem : orderItems) {
            inventoryService.unlockInventory(orderItem.getSkuId(), orderItem.getQuantity());
        }
    }

    @Override
    @Transactional
    public void payOrder(String orderNo) {
        OrderEntity order = orderMapper.findByOrderNo(orderNo);
        
        if (order.getStatus() != 0) {
            throw new OrderException("订单状态不正确");
        }
        
        // 更新订单状态
        order.setStatus(1); // 待发货
        order.setPayTime(LocalDateTime.now());
        orderMapper.updateOrder(order);
        
        // 扣减库存
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
        
        if (order.getStatus() != 1) {
            throw new OrderException("订单状态不正确");
        }
        
        // 更新订单状态
        order.setStatus(2); // 待收货
        order.setDeliveryTime(LocalDateTime.now());
        // TODO: 保存物流信息
        orderMapper.updateOrder(order);
    }

    @Override
    @Transactional
    public void confirmReceive(String orderNo) {
        OrderEntity order = orderMapper.findByOrderNo(orderNo);
        if (order == null) {
            throw new OrderException("订单不存在");
        }
        
        if (order.getStatus() != 2) {
            throw new OrderException("订单状态不正确");
        }
        
        // 更新订单状态
        order.setStatus(3); // 已完成
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
    @Transactional
    public void processTimeoutOrders(int timeoutMinutes, int batchSize) {
        List<OrderEntity> timeoutOrders = orderMapper.findTimeoutOrders(timeoutMinutes, batchSize);
        for (OrderEntity order : timeoutOrders) {
            try {
                cancelOrder(order.getId());
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
    
    /**
     * 生成订单号
     */
    private String generateOrderNo() {
        // 生成格式: 年月日时分秒 + 4位随机数
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = LocalDateTime.now().format(formatter);
        
        // 生成4位随机数
        Random random = new Random();
        int randomNum = random.nextInt(10000);
        String randomStr = String.format("%04d", randomNum);
        
        return timestamp + randomStr;
    }

    /**
     * 更新订单项数量
     *
     */
    @Override
    @Transactional
    public void updateQuantity(Long orderItemId, Integer quantity) {
        // 获取订单项
        OrderItemEntity orderItem = orderItemMapper.findById(orderItemId);

        if (quantity > 0) {
            // 更新数量
            orderItem.setQuantity(quantity);
            
            // 计算新的总金额
            BigDecimal totalAmount = orderItem.getPrice().multiply(new BigDecimal(quantity));
            orderItem.setTotalAmount(totalAmount);
            
            // 更新订单项
            orderItemMapper.updateOrderItem(orderItem);
            
            // 更新订单总金额
            updateOrderAmount(orderItem.getOrderId());
            
            logger.info("更新订单项数量: orderItemId={}, quantity={}", orderItemId, quantity);
        } else {
            // 如果数量为0，删除订单项
            orderItemMapper.deleteOrderItem(orderItemId);
            
            // 更新订单总金额
            updateOrderAmount(orderItem.getOrderId());
            
            logger.info("删除订单项: orderItemId={}", orderItemId);
        }
    }

    
    /**
     * 更新订单金额
     */
    private void updateOrderAmount(Long orderId) {
        // 参数验证
        if (orderId == null) {
            throw new OrderException("订单ID不能为空");
        }
        
        OrderEntity order = orderMapper.findById(orderId);
        if (order == null) {
            throw new OrderException("订单不存在");
        }
        
        // 获取订单项
        List<OrderItemEntity> orderItems = orderItemMapper.findByOrderId(orderId);
        
        // 计算总金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderItemEntity item : orderItems) {
            // 安全地处理可能为null的值
            if (item != null && item.getTotalAmount() != null) {
                totalAmount = totalAmount.add(item.getTotalAmount());
            }
        }
        
        // 更新订单金额
        order.setTotalAmount(totalAmount);
        
        // 安全地处理可能为null的值
        BigDecimal shippingFee = Objects.requireNonNullElse(order.getShippingFee(), BigDecimal.ZERO);
        order.setPayAmount(totalAmount.add(shippingFee));
        
        orderMapper.updateOrder(order);
    }
} 