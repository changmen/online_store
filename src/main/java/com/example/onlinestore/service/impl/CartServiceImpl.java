package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.CartItem;
import com.example.onlinestore.bean.Sku;
import com.example.onlinestore.dto.AddToCartRequest;
import com.example.onlinestore.dto.UpdateCartItemRequest;
import com.example.onlinestore.entity.CartEntity;
import com.example.onlinestore.errors.ErrorCode;
import com.example.onlinestore.exceptions.BizException;
import com.example.onlinestore.mapper.CartMapper;
import com.example.onlinestore.metrics.CartMetrics;
import com.example.onlinestore.service.CartService;
import com.example.onlinestore.service.SkuService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {
    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);
    /**
     * 购物车项加锁对象
     */
    private final Object ITEM_LOCK = new Object();
    /**
     * 购物车商品SKU锁
     */
    private final Object ITEM_SKU_LOCK = new Object();

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private SkuService skuService;

    @Autowired
    private CartMetrics cartMetrics;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CartItem addToCart(@NotNull Long memberId, @Valid AddToCartRequest request) {
        // 检查SKU是否存在
        Sku sku = skuService.getSkuById(request.getSkuId());

        // 检查库存是否充足
        if (!skuService.checkStock(request.getSkuId(), request.getQuantity())) {
            throw new BizException(ErrorCode.SKU_STOCK_INSUFFICIENT);
        }

        // 检查购物车是否已存在该商品
        CartEntity existingCart = cartMapper.findByMemberIdAndSkuId(memberId, request.getSkuId());
        if (existingCart != null) {
            // 更新数量
            int newQuantity = existingCart.getQuantity() + request.getQuantity();
            cartMapper.updateQuantity(existingCart.getId(), newQuantity);
            cartMetrics.incrementUpdateCart();
            return convertToCartItem(existingCart, sku);
        }

        // 创建新的购物车项
        CartEntity cartEntity = new CartEntity();
        cartEntity.setMemberId(memberId);
        cartEntity.setSkuId(request.getSkuId());
        cartEntity.setQuantity(request.getQuantity());
        cartEntity.setPrice(sku.getPrice());
        cartEntity.setTotalPrice(sku.getPrice().multiply(new BigDecimal(request.getQuantity())));
        cartEntity.setSelected(1);
        cartEntity.setCreatedAt(LocalDateTime.now());
        cartEntity.setUpdatedAt(LocalDateTime.now());

        int effectRows = cartMapper.insert(cartEntity);
        if (effectRows != 1) {
            logger.error("insert cart failed. because effect rows is 0. skuId:{}", request.getSkuId());
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        cartMetrics.incrementAddToCart();
        return convertToCartItem(cartEntity, sku);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCartItem(@NotNull Long memberId, @NotNull Long cartItemId, @Valid UpdateCartItemRequest request) {
        // 获取购物车项锁
        synchronized (ITEM_SKU_LOCK) {
            // 检查购物车项是否存在且属于该会员
            CartEntity cartEntity = cartMapper.findById(cartItemId);
            if (cartEntity == null || !cartEntity.getMemberId().equals(memberId)) {
                throw new BizException(ErrorCode.CART_ITEM_NOT_FOUND);
            }

            // 获取SKU锁
            synchronized (ITEM_LOCK) {
                // 检查SKU库存是否充足
                if (!skuService.checkStock(cartEntity.getSkuId(), request.getQuantity())) {
                    throw new BizException(ErrorCode.SKU_STOCK_INSUFFICIENT);
                }

                // 更新购物车项
                cartMapper.updateQuantity(cartItemId, request.getQuantity());
                cartMapper.updateSelected(cartItemId, request.getSelected() ? 1 : 0);

                cartMetrics.incrementUpdateCart();

                // 获取最新的SKU信息
                Sku sku = skuService.getSkuById(cartEntity.getSkuId());
                convertToCartItem(cartEntity, sku);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCartItem(@NotNull Long memberId, @NotNull Long cartItemId) {
        // 检查购物车项是否存在且属于该会员
        CartEntity cartEntity = cartMapper.findById(cartItemId);
        if (cartEntity == null || !cartEntity.getMemberId().equals(memberId)) {
            throw new BizException(ErrorCode.CART_ITEM_NOT_FOUND);
        }

        int effectRows = cartMapper.deleteById(cartItemId);
        if (effectRows != 1) {
            logger.error("delete cart item failed. because effect rows is 0. cartItemId:{}", cartItemId);
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        cartMetrics.incrementDeleteCart();
    }

    @Override
    public List<CartItem> getCartItems(@NotNull Long memberId) {
        List<CartEntity> cartEntities = cartMapper.findByMemberId(memberId);
        return cartEntities.stream()
                .map(cartEntity -> {
                    Sku sku = skuService.getSkuById(cartEntity.getSkuId());
                    return convertToCartItem(cartEntity, sku);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void clearCart(@NotNull Long memberId) {
        int effectRows = cartMapper.clearByMemberId(memberId);
        if (effectRows < 0) {
            logger.error("clear cart failed. because effect rows is {}", effectRows);
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        cartMetrics.incrementClearCart();
    }

    @Override
    public BigDecimal getSelectedTotalAmount(@NotNull Long memberId) {
        List<CartEntity> cartEntities = cartMapper.findByMemberId(memberId);
        return cartEntities.stream()
                .filter(cartEntity -> cartEntity.getSelected() == 1)
                .map(CartEntity::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public CartItem getCartItemById(@NotNull Long cartItemId) {
        CartEntity cartEntity = cartMapper.findById(cartItemId);
        if (cartEntity != null) {
            Sku sku = skuService.getSkuById(cartEntity.getSkuId());
            return convertToCartItem(cartEntity, sku);
        }
        throw new BizException(ErrorCode.CART_ITEM_NOT_FOUND, cartItemId);
    }

    @Override
    public void updateCartItemQuantity(Long memberId, Long cartItemId, Integer quantity) {
        // 获取购物车项锁
        synchronized (ITEM_LOCK) {
            // 检查购物车项是否存在且属于该会员
            CartEntity cartEntity = cartMapper.findById(cartItemId);
            if (cartEntity == null || !cartEntity.getMemberId().equals(memberId)) {
                throw new BizException(ErrorCode.CART_ITEM_NOT_FOUND);
            }

            // 获取SKU锁
            synchronized (ITEM_SKU_LOCK) {
                // 检查SKU库存是否充足
                if (!skuService.checkStock(cartEntity.getSkuId(), quantity)) {
                    throw new BizException(ErrorCode.SKU_STOCK_INSUFFICIENT);
                }

                // 更新购物车项数量
                cartMapper.updateQuantity(cartItemId, quantity);
                cartMetrics.incrementUpdateCart();
            }
        }
    }

    private CartItem convertToCartItem(CartEntity cartEntity, Sku sku) {
        CartItem cartItem = new CartItem();
        BeanUtils.copyProperties(cartEntity, cartItem);
        cartItem.setSelected(cartEntity.getSelected());
        cartItem.setSkuCode(sku.getSkuCode());
        cartItem.setSkuName(sku.getName());
        cartItem.setSkuImage(sku.getImage());
        return cartItem;
    }
} 