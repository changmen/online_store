package com.example.onlinestore.controller;

import com.example.onlinestore.bean.Member;
import com.example.onlinestore.bean.Order;
import com.example.onlinestore.dto.*;
import com.example.onlinestore.enums.OrderStatus;
import com.example.onlinestore.enums.PaymentMethod;
import com.example.onlinestore.errors.ErrorCode;
import com.example.onlinestore.exceptions.BizException;
import com.example.onlinestore.service.MemberService;
import com.example.onlinestore.service.OrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final Logger logger = LoggerFactory.getLogger(OrderController.class);
    @Autowired
    private OrderService orderService;

    @Autowired
    private MemberService memberService;

    @PostMapping
    public Response<OrderResponse> createOrder(@NotNull @Valid @RequestBody OrderRequest request) {
        Member member = memberService.getLoginMember();
        request.setMemberId(member.getId());
        Order order = orderService.createOrder(request);
        return Response.success(OrderResponse.of(order));
    }

    @GetMapping("/{id}")
    public Response<OrderResponse> getOrderById(@PathVariable @NotNull(message = "订单ID不能为空") @Min(value = 1, message = "订单ID不能小于1") Long id) {
        Order order = orderService.getOrderById(id);
        return Response.success(OrderResponse.of(order));
    }

    @GetMapping("/no/{orderNo}")
    public Response<OrderResponse> getOrderByNo(@PathVariable @NotBlank(message = "订单号不能为空") @Size(max = 32, message = "订单号长度不能超过32个字符") String orderNo) {
        Order order = orderService.getOrderByNo(orderNo);
        return Response.success(OrderResponse.of(order));
    }

    @GetMapping("/member")
    public Response<Page<OrderResponse>> getOrdersByMemberId(@RequestParam @NotNull @Min(value = 1, message = "页码最小为1") Integer page,
                                                             @RequestParam @NotNull @Min(value = 1, message = "每页大小最小为1") Integer size,
                                                             @RequestParam(required = false) String orderStatus) {
        Member member = memberService.getLoginMember();
        OrderStatus status = null;
        try {
            status = OrderStatus.valueOf(orderStatus);
        } catch (IllegalArgumentException e) {
            logger.error("不支持的订单状态: {}", orderStatus, e);
            throw new BizException(ErrorCode.ORDER_STATUS_NOT_SUPPORTED);
        }
        Page<Order> orders = orderService.getOrdersByMemberId(member.getId(), page, size, status);
        return Response.success(Page.of(orders.getItems().stream().map(OrderResponse::of).collect(Collectors.toList()), orders.getTotalCount(), orders.getPageNum(), orders.getPageSize()));
    }

    @PostMapping("/{id}/cancel")
    public Response<Void> cancelOrder(
            @PathVariable @NotNull(message = "订单ID不能为空") @Min(value = 1, message = "订单ID不能小于1") Long id,
            @RequestParam(required = false) @Size(max = 255, message = "取消原因长度不能超过255个字符") String reason) {
        Member member = memberService.getLoginMember();
        orderService.cancelOrder(id, member.getId(), reason);
        return Response.success();
    }

    @PostMapping("/{id}/pay")
    public Response<Void> payOrder(@PathVariable @NotNull(message = "订单ID不能为空") @Min(value = 1, message = "订单ID不能小于1") Long id,
                                   @RequestParam @NotBlank(message = "支付方式不能为空") String paymentMethod) {
        Member member = memberService.getLoginMember();

        PaymentMethod method;
        try {
            method = PaymentMethod.valueOf(paymentMethod);
        } catch (IllegalArgumentException e) {
            logger.error("不支持的支付方式: {}", paymentMethod, e);
            throw new BizException(ErrorCode.PAY_METHOD_NOT_SUPPORTED);
        }

        orderService.payOrder(id, member.getId(), method);
        return Response.success();
    }

    @PostMapping("/{id}/refund")
    public Response<Void> refundOrder(@PathVariable @NotNull(message = "订单ID不能为空") @Min(value = 1, message = "订单ID不能小于1") Long id,
                                      @RequestParam @NotBlank(message = "退款原因不能为空") @Size(max = 255, message = "退款原因长度不能超过255个字符") String reason) {
        Member member = memberService.getLoginMember();
        orderService.refundOrder(id, member.getId(), reason);
        return Response.success();
    }
} 