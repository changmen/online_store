package com.example.onlinestore.controller;

import com.example.onlinestore.bean.CartItem;
import com.example.onlinestore.dto.AddToCartRequest;
import com.example.onlinestore.dto.CartItemResponse;
import com.example.onlinestore.dto.Response;
import com.example.onlinestore.dto.UpdateCartItemRequest;
import com.example.onlinestore.dto.converter.CartItemConverter;
import com.example.onlinestore.service.CartService;
import com.example.onlinestore.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/carts")
public class CartController {
    @Autowired
    private CartService cartService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private CartItemConverter cartItemConverter;

    /**
     * 添加商品到购物车
     *
     * @param request  添加购物车请求
     * @return 添加的购物车项
     */
    @PostMapping("/items")
    public Response<CartItemResponse> addToCart(@Valid @RequestBody AddToCartRequest request) {
        CartItem cartItem = cartService.addToCart(memberService.getLoginMember().getId(), request);
        return Response.success(cartItemConverter.convert(cartItem));
    }

    /**
     * 更新购物车项
     *
     * @param cartItemId 购物车项ID
     * @param request    更新购物车项请求
     * @return 更新后的购物车项
     */
    @PutMapping("/items/{cartItemId}")
    public Response<Void> updateCartItem(
            @PathVariable("cartItemId") Long cartItemId,
            @Valid @RequestBody UpdateCartItemRequest request) {
        cartService.updateCartItem(memberService.getLoginMember().getId(), cartItemId, request);
        return Response.success();
    }

    /**
     * 删除购物车项
     *
     * @param cartItemId 购物车项ID
     * @return 操作结果
     */
    @DeleteMapping("/items/{cartItemId}")
    public Response<Void> deleteCartItem(@PathVariable("cartItemId") Long cartItemId) {
        cartService.deleteCartItem(memberService.getLoginMember().getId(), cartItemId);
        return Response.success();
    }
    /**
     * 获取购物车商品信息
     *
     * @param memberId 会员ID
     * @return 购物车项列表
     */
    @GetMapping("/items/{cartItemId}")
    public Response<CartItemResponse> getCartItems(@PathVariable("cartItemId") Long cartItemId) {
        CartItem cartItem = cartService.getCartItemById(cartItemId);
        return Response.success(cartItemConverter.convert(cartItem));
    }
    /**
     * 获取购物车列表
     *
     * @param memberId 会员ID
     * @return 购物车项列表
     */
    @GetMapping("/items")
    public Response<List<CartItemResponse>> getCartItems() {
        List<CartItem> cartItems =  cartService.getCartItems(memberService.getLoginMember().getId());
        if (cartItems != null) {
            return Response.success(cartItems.stream().map(cartItemConverter::convert).toList());
        } else {
            return Response.success(Collections.emptyList());
        }
    }

    /**
     * 清空购物车
     *
     * @param memberId 会员ID
     * @return 操作结果
     */
    @DeleteMapping("/items")
    public Response<Void> clearCart() {
        cartService.clearCart(memberService.getLoginMember().getId());
        return Response.success();
    }

    /**
     * 获取购物车选中项的总金额
     *
     * @param memberId 会员ID
     * @return 总金额
     */
    @GetMapping("/total-amount")
    public Response<BigDecimal> getSelectedTotalAmount() {
        return Response.success(cartService.getSelectedTotalAmount(memberService.getLoginMember().getId()));
    }
} 