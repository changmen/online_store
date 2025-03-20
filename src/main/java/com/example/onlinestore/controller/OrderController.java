package com.example.onlinestore.controller;

import com.example.onlinestore.dto.Response;
import com.example.onlinestore.entity.OrderEntity;
import com.example.onlinestore.service.OrderService;
import com.example.onlinestore.service.impl.OrderServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 订单控制器
 */
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    
    @Autowired
    private OrderService orderService;
    
    /**
     * 创建订单
     */
    @PostMapping
    public Response<String> createOrder(@RequestBody Map<String, Object> params) {
        try {
            Long userId = Long.valueOf(params.get("userId").toString());
            String receiverName = (String) params.get("receiverName");
            String receiverPhone = (String) params.get("receiverPhone");
            String receiverAddress = (String) params.get("receiverAddress");
            String paymentMethod = (String) params.get("paymentMethod");
            String remark = (String) params.get("remark");
            
            String orderNo = orderService.createOrder(
                userId, receiverName, receiverPhone, receiverAddress, paymentMethod, remark
            );
            
            return Response.success(orderNo);
        } catch (Exception e) {
            logger.error("创建订单失败", e);
            return Response.fail(e.getMessage());
        }
    }
    
    /**
     * 从购物车创建订单
     */
    @PostMapping("/from-cart")
    public Response<String> createOrderFromCart(@RequestBody Map<String, Object> params) {
        try {
            Long userId = Long.valueOf(params.get("userId").toString());
            List<Long> cartItemIds = (List<Long>) params.get("cartItemIds");
            String receiverName = (String) params.get("receiverName");
            String receiverPhone = (String) params.get("receiverPhone");
            String receiverAddress = (String) params.get("receiverAddress");
            String paymentMethod = (String) params.get("paymentMethod");
            String remark = (String) params.get("remark");
            
            String orderNo = orderService.createOrderFromCart(
                userId, cartItemIds, receiverName, receiverPhone, receiverAddress, paymentMethod, remark
            );
            
            return Response.success(orderNo);
        } catch (Exception e) {
            logger.error("从购物车创建订单失败", e);
            return Response.fail(e.getMessage());
        }
    }
    
    /**
     * 获取订单详情
     */
    @GetMapping("/{orderNo}")
    public Response<Map<String, Object>> getOrderDetail(
            @PathVariable String orderNo,
            @RequestParam(required = false) Long userId) {
        try {
            Map<String, Object> orderDetail = orderService.getOrderDetail(orderNo, userId);
            return Response.success(orderDetail);
        } catch (Exception e) {
            logger.error("获取订单详情失败", e);
            return Response.fail(e.getMessage());
        }
    }
    
    /**
     * 获取用户订单列表
     */
    @GetMapping("/user/{userId}")
    public Response<List<OrderEntity>> getUserOrders(
            @PathVariable Long userId,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            List<OrderEntity> orders = orderService.getUserOrders(userId, status, page, size);
            return Response.success(orders);
        } catch (Exception e) {
            logger.error("获取用户订单列表失败", e);
            return Response.fail(e.getMessage());
        }
    }
    
    /**
     * 取消订单
     */
    @PostMapping("/{orderId}/cancel")
    public Response<Void> cancelOrder(@PathVariable Long orderId) {
        try {
            orderService.cancelOrder(orderId);
            return Response.success();
        } catch (Exception e) {
            logger.error("取消订单失败", e);
            return Response.fail(e.getMessage());
        }
    }
    
    /**
     * 支付订单
     */
    @PostMapping("/{orderNo}/pay")
    public Response<Void> payOrder(@PathVariable String orderNo) {
        try {
            orderService.payOrder(orderNo);
            return Response.success();
        } catch (Exception e) {
            logger.error("支付订单失败", e);
            return Response.fail(e.getMessage());
        }
    }
    
    /**
     * 确认收货
     */
    @PostMapping("/{orderNo}/confirm")
    public Response<Void> confirmReceive(@PathVariable String orderNo) {
        try {
            orderService.confirmReceive(orderNo);
            return Response.success();
        } catch (Exception e) {
            logger.error("确认收货失败", e);
            return Response.fail(e.getMessage());
        }
    }
} 