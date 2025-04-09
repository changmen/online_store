package com.example.onlinestore.service;

import com.example.onlinestore.bean.CartItem;
import com.example.onlinestore.dto.AddToCartRequest;
import com.example.onlinestore.dto.UpdateCartItemRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public interface CartService {
    /**
     * 添加商品到购物车
     *
     * @param memberId 会员ID
     * @param request 添加购物车请求
     * @return 购物车项
     */
    CartItem addToCart(@NotNull Long memberId, @Valid AddToCartRequest request);

    /**
     * 更新购物车项
     *
     * @param memberId   会员ID
     * @param cartItemId 购物车项ID
     */
    void updateCartItem(@NotNull Long memberId, @NotNull Long cartItemId, @Valid UpdateCartItemRequest request);

    /**
     * 删除购物车项
     *
     * @param memberId 会员ID
     * @param cartItemId 购物车项ID
     */
    void deleteCartItem(@NotNull Long memberId, @NotNull Long cartItemId);

    /**
     * 获取会员的购物车列表
     *
     * @param memberId 会员ID
     * @return 购物车项列表
     */
    List<CartItem> getCartItems(@NotNull Long memberId);

    /**
     * 清空购物车
     *
     * @param memberId 会员ID
     */
    void clearCart(@NotNull Long memberId);

    /**
     * 获取购物车选中项的总金额
     *
     * @param memberId 会员ID
     * @return 总金额
     */
    BigDecimal getSelectedTotalAmount(@NotNull Long memberId);

    /**
     * 根据购物车项ID获取购物车项
     *
     * @param cartItemId 购物车项ID
     * @return 购物车项
     */
    CartItem getCartItemById(@NotNull Long cartItemId);
    /**
     * 更新购物车项的数量
     *
     * @param memberId   会员ID
     * @param cartItemId 购物车项ID
     * @param quantity   数量
     */
    void updateCartItemQuantity(@NotNull Long memberId, @NotNull Long cartItemId, @NotNull Integer quantity);
} 