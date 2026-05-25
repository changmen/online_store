package com.example.onlinestore.controller;

import com.example.onlinestore.dto.CreateOrderFromCartRequest;
import com.example.onlinestore.dto.CreateOrderRequest;
import com.example.onlinestore.dto.OrderDetailDTO;
import com.example.onlinestore.dto.Response;
import com.example.onlinestore.entity.OrderEntity;
import com.example.onlinestore.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Response<String> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        String orderNo = orderService.createOrder(
            request.getUserId(),
            request.getReceiverName(),
            request.getReceiverPhone(),
            request.getReceiverAddress(),
            request.getPaymentMethod(),
            request.getRemark()
        );
        return Response.success(orderNo);
    }

    @PostMapping("/from-cart")
    public Response<String> createOrderFromCart(@Valid @RequestBody CreateOrderFromCartRequest request) {
        String orderNo = orderService.createOrderFromCart(
            request.getUserId(),
            request.getCartItemIds(),
            request.getReceiverName(),
            request.getReceiverPhone(),
            request.getReceiverAddress(),
            request.getPaymentMethod(),
            request.getRemark()
        );
        return Response.success(orderNo);
    }

    @GetMapping("/{orderNo}")
    public Response<OrderDetailDTO> getOrderDetail(
            @PathVariable String orderNo,
            @RequestParam(required = false) Long userId) {
        OrderDetailDTO orderDetail = orderService.getOrderDetail(orderNo, userId);
        return Response.success(orderDetail);
    }

    @GetMapping("/user/{userId}")
    public Response<List<OrderEntity>> getUserOrders(
            @PathVariable Long userId,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<OrderEntity> orders = orderService.getUserOrders(userId, status, page, size);
        return Response.success(orders);
    }

    @PostMapping("/{orderId}/cancel")
    public Response<Void> cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return Response.success();
    }

    @PostMapping("/{orderNo}/pay")
    public Response<Void> payOrder(@PathVariable String orderNo) {
        orderService.payOrder(orderNo);
        return Response.success();
    }

    @PostMapping("/{orderNo}/confirm")
    public Response<Void> confirmReceive(@PathVariable String orderNo) {
        orderService.confirmReceive(orderNo);
        return Response.success();
    }
}
