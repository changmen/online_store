package com.example.onlinestore.controller;

import com.example.onlinestore.bean.Member;
import com.example.onlinestore.bean.Order;
import com.example.onlinestore.dto.*;
import com.example.onlinestore.service.MemberService;
import com.example.onlinestore.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private MemberService memberService;

    @PostMapping
    public Response<OrderResponse> createOrder(@Valid @RequestBody OrderRequest request) {
        Member member = memberService.getLoginMember();
        request.setMemberId(member.getId());
        Order order = orderService.createOrder(request);
        return Response.success(OrderResponse.of(order));
    }

    @GetMapping("/{id}")
    public Response<OrderResponse> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        return Response.success(OrderResponse.of(order));
    }

    @GetMapping("/no/{orderNo}")
    public Response<OrderResponse> getOrderByNo(@PathVariable String orderNo) {
        Order order = orderService.getOrderByNo(orderNo);
        return Response.success(OrderResponse.of(order));
    }

    @GetMapping("/member")
    public Response<List<OrderResponse>> getOrdersByMemberId() {
        Member member = memberService.getLoginMember();
        List<Order> orders = orderService.getOrdersByMemberId(member.getId());
        List<OrderResponse> orderResponses = orders.stream()
                .map(OrderResponse::of)
                .collect(Collectors.toList());
        return Response.success(orderResponses);
    }

    @PostMapping("/{id}/cancel")
    public Response<Void> cancelOrder(
            @PathVariable Long id,
            @RequestParam(required = false) String reason) {
        Member member = memberService.getLoginMember();
        orderService.cancelOrder(id, member.getId(), reason);
        return Response.success();
    }

    @PostMapping("/pay")
    public Response<Void> payOrder(@Valid @RequestBody PaymentRequest request) {
        Member member = memberService.getLoginMember();
        request.setMemberId(member.getId());

        orderService.payOrder(request);
        return Response.success();
    }

    @PostMapping("/refund")
    public Response<Void> refundOrder(@Valid @RequestBody RefundRequest request) {
        Member member = memberService.getLoginMember();
        request.setMemberId(member.getId());
        orderService.refundOrder(request);
        return Response.success();
    }
} 