package com.example.onlinestore.service.impl;

import com.example.onlinestore.entity.CartItemEntity;
import com.example.onlinestore.exception.CartException;
import com.example.onlinestore.mapper.CartItemMapper;
import com.example.onlinestore.service.CartService;
import com.example.onlinestore.service.InventoryService;
import com.example.onlinestore.service.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);
    
    @Autowired
    private CartItemMapper cartItemMapper;
    
    @Autowired
    private ItemService itemService;
    
    @Autowired
    private InventoryService inventoryService;

    @Override
    @Transactional
    public void addToCart(Long userId, Long itemId, Long skuId, Integer quantity) {
        // 检查商品是否存在
        if (itemService.getItemById(itemId) == null) {
            throw new CartException("商品不存在");
        }
        
        // 检查库存
        if (inventoryService.getInventoryById(skuId) == null) {
            throw new CartException("商品库存不存在");
        }
        
        // 检查购物车是否已存在该商品
        CartItemEntity existingItem = cartItemMapper.findByUserIdAndItemId(userId, itemId);
        if (existingItem != null) {
            // 更新数量
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            cartItemMapper.updateCartItem(existingItem);
        } else {
            // 创建新的购物车项
            CartItemEntity cartItem = new CartItemEntity();
            cartItem.setUserId(userId);
            cartItem.setItemId(itemId);
            cartItem.setSkuId(skuId);
            cartItem.setQuantity(quantity);
            cartItem.setSelected(true);
            cartItemMapper.insertCartItem(cartItem);
        }
    }

    @Override
    @Transactional
    public void updateQuantity(Long userId, Long itemId, Integer quantity) {
        CartItemEntity cartItem = cartItemMapper.findByUserIdAndItemId(userId, itemId);
        if (cartItem == null) {
            throw new CartException("购物车商品不存在");
        }
        
        cartItem.setQuantity(quantity);
        cartItemMapper.updateCartItem(cartItem);
    }

    @Override
    @Transactional
    public void removeFromCart(Long userId, Long itemId) {
        CartItemEntity cartItem = cartItemMapper.findByUserIdAndItemId(userId, itemId);
        if (cartItem != null) {
            cartItemMapper.deleteCartItem(cartItem.getId());
        }
    }

    @Override
    @Transactional
    public void clearCart(Long userId) {
        cartItemMapper.deleteByUserId(userId);
    }

    @Override
    public List<CartItemEntity> getCartItems(Long userId) {
        return cartItemMapper.findByUserId(userId);
    }

    @Override
    @Transactional
    public void updateSelected(Long userId, Long itemId, Boolean selected) {
        CartItemEntity cartItem = cartItemMapper.findByUserIdAndItemId(userId, itemId);
        if (cartItem != null) {
            cartItemMapper.updateSelected(cartItem.getId(), selected);
        }
    }

    @Override
    @Transactional
    public void batchUpdateSelected(Long userId, Boolean selected) {
        cartItemMapper.batchUpdateSelected(userId, selected);
    }
} 