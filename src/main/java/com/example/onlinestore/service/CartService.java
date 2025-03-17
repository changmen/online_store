package com.example.onlinestore.service;

import com.example.onlinestore.entity.CartItemEntity;
import java.util.List;
import java.util.Map;

public interface CartService {
    /**
     * 添加商品到购物车
     */
    void addToCart(Long userId, Long itemId, Long skuId, Integer quantity);
    
    /**
     * 更新购物车商品数量
     */
    void updateQuantity(Long userId, Long itemId, Integer quantity);
    
    /**
     * 删除购物车商品
     */
    void removeFromCart(Long userId, Long itemId);
    
    /**
     * 清空购物车
     */
    void clearCart(Long userId);
    
    /**
     * 获取购物车列表
     */
    List<CartItemEntity> getCartItems(Long userId);
    
    /**
     * 更新购物车商品选中状态
     */
    void updateSelected(Long userId, Long itemId, Boolean selected);
    
    /**
     * 批量更新购物车商品选中状态
     */
    void batchUpdateSelected(Long userId, Boolean selected);

    /**
     * 批量删除购物车商品
     * 
     * @param userId 用户ID
     * @param itemIds 要删除的商品ID列表
     * @return 成功删除的商品数量
     */
    int batchRemoveFromCart(Long userId, List<Long> itemIds);
    
    /**
     * 批量添加商品到购物车
     * 
     * @param userId 用户ID
     * @param items 商品信息映射，key为商品ID，value为[skuId, quantity]数组
     * @return 成功添加的商品数量
     */
    int batchAddToCart(Long userId, Map<Long, Object[]> items);
} 