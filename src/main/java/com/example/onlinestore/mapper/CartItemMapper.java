package com.example.onlinestore.mapper;

import com.example.onlinestore.entity.CartItemEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CartItemMapper {
    void insertCartItem(CartItemEntity cartItem);
    CartItemEntity findById(Long id);
    List<CartItemEntity> findByUserId(Long userId);
    void updateCartItem(CartItemEntity cartItem);
    void deleteCartItem(Long id);
    
    /**
     * 根据用户ID和商品ID查询购物车项
     */
    CartItemEntity findByUserIdAndItemId(
        @Param("userId") Long userId,
        @Param("itemId") Long itemId
    );
    
    /**
     * 根据用户ID和SKU ID查询购物车项
     */
    CartItemEntity findByUserIdAndSkuId(
        @Param("userId") Long userId,
        @Param("skuId") Long skuId
    );
    
    /**
     * 更新购物车项数量
     */
    void updateQuantity(
        @Param("id") Long id,
        @Param("quantity") Integer quantity
    );
    
    /**
     * 更新购物车项选中状态
     */
    void updateSelected(
        @Param("id") Long id,
        @Param("selected") Boolean selected
    );
    
    /**
     * 批量更新购物车项选中状态
     */
    void batchUpdateSelected(
        @Param("userId") Long userId,
        @Param("selected") Boolean selected
    );
    
    /**
     * 删除用户的所有购物车项
     */
    void deleteByUserId(Long userId);
} 