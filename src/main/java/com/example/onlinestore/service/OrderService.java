package com.example.onlinestore.service;

import com.example.onlinestore.entity.OrderEntity;
import com.example.onlinestore.entity.OrderItemEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    /**
     * 创建订单
     * @param userId 用户ID
     * @param receiverName 收货人姓名
     * @param receiverPhone 收货人电话
     * @param receiverAddress 收货地址
     * @param paymentMethod 支付方式
     * @param remark 备注
     * @return 订单号
     */
    String createOrder(
        Long userId,
        String receiverName,
        String receiverPhone,
        String receiverAddress,
        String paymentMethod,
        String remark
    );
    
    /**
     * 从购物车创建订单
     * @param userId 用户ID
     * @param cartItemIds 购物车项ID列表
     * @param receiverName 收货人姓名
     * @param receiverPhone 收货人电话
     * @param receiverAddress 收货地址
     * @param paymentMethod 支付方式
     * @param remark 备注
     * @return 订单号
     */
    String createOrderFromCart(
        Long userId,
        List<Long> cartItemIds,
        String receiverName,
        String receiverPhone,
        String receiverAddress,
        String paymentMethod,
        String remark
    );
    
    /**
     * 获取订单详情
     */
    OrderEntity getOrderById(Long orderId);
    
    /**
     * 根据订单号获取订单
     */
    OrderEntity getOrderByOrderNo(String orderNo);
    
    /**
     * 获取订单项列表
     */
    List<OrderItemEntity> getOrderItems(Long orderId);
    
    /**
     * 取消订单
     */
    void cancelOrder(Long orderId);
    
    /**
     * 支付订单
     */
    void payOrder(String orderNo);
    
    /**
     * 发货
     */
    void shipOrder(String orderNo, String trackingNo, String logisticsCompany);
    
    /**
     * 确认收货
     */
    void confirmReceive(String orderNo);
    
    /**
     * 查询用户订单列表
     */
    List<OrderEntity> getUserOrders(Long userId, Integer status, int page, int size);
    
    /**
     * 统计用户订单数量
     */
    long countUserOrders(Long userId, Integer status);
    
    /**
     * 处理超时未支付订单
     */
    void processTimeoutOrders(int timeoutMinutes, int batchSize);
    
    /**
     * 查询用户购买过的商品
     */
    List<OrderItemEntity> getUserPurchasedItems(Long userId, int page, int size);
    
    /**
     * 统计商品销量
     */
    int countItemSales(Long itemId, LocalDateTime startTime, LocalDateTime endTime);
} 