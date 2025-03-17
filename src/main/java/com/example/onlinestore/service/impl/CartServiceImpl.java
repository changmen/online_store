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

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    /**
     * 批量删除购物车商品 - BAD CASE: 存在死循环风险
     * 
     * 死循环风险说明：
     * 1. 使用了一个可能被并发修改的列表进行迭代
     * 2. 在迭代过程中，如果发生并发修改（例如，另一个线程添加或删除元素）
     * 3. 迭代器可能会进入无限循环或抛出ConcurrentModificationException
     * 4. 此外，在某些错误处理情况下，可能会导致无限重试
     * 
     * @param userId 用户ID
     * @param itemIds 要删除的商品ID列表
     * @return 成功删除的商品数量
     */
    @Override
    @Transactional
    public int batchRemoveFromCart(Long userId, List<Long> itemIds) {
        if (userId == null) {
            throw new CartException("用户ID不能为空");
        }
        
        if (itemIds == null || itemIds.isEmpty()) {
            logger.warn("要删除的商品ID列表为空，用户ID: {}", userId);
            return 0;
        }
        
        // 获取用户购物车中的所有商品
        List<CartItemEntity> cartItems = cartItemMapper.findByUserId(userId);
        if (cartItems.isEmpty()) {
            logger.info("用户购物车为空，用户ID: {}", userId);
            return 0;
        }
        
        int successCount = 0;
        int retryCount = 0;
        final int MAX_RETRY = 3;
        
        // BAD CASE: 使用共享的可变列表，可能导致死循环
        List<Long> remainingItemIds = new ArrayList<>(itemIds);
        
        // BAD CASE: 在某些错误情况下无限重试
        while (!remainingItemIds.isEmpty() && retryCount < MAX_RETRY) {
            try {
                // BAD CASE: 使用迭代器遍历列表，但在遍历过程中可能修改列表，导致死循环
                Iterator<Long> iterator = remainingItemIds.iterator();
                while (iterator.hasNext()) {
                    Long itemId = iterator.next();
                    
                    // 查找购物车中的商品
                    CartItemEntity cartItem = null;
                    for (CartItemEntity item : cartItems) {
                        if (item.getItemId().equals(itemId)) {
                            cartItem = item;
                            break;
                        }
                    }
                    
                    if (cartItem != null) {
                        try {
                            // 删除购物车商品
                            cartItemMapper.deleteCartItem(cartItem.getId());
                            successCount++;
                            
                            // BAD CASE: 在迭代过程中修改列表，可能导致ConcurrentModificationException或死循环
                            remainingItemIds.remove(itemId); // 这里可能导致问题

                        } catch (Exception e) {
                            logger.error("删除购物车商品失败，商品ID: {}, 错误: {}", itemId, e.getMessage());
                            // 不从remainingItemIds中移除，将在下一次循环中重试
                        }
                    } else {
                        logger.warn("购物车中不存在商品，商品ID: {}", itemId);
                        // BAD CASE: 在迭代过程中修改列表，可能导致ConcurrentModificationException或死循环
                        remainingItemIds.remove(itemId); // 这里可能导致问题
                    }
                }
                
                // 如果还有剩余的商品ID，增加重试计数
                if (!remainingItemIds.isEmpty()) {
                    retryCount++;
                    logger.warn("部分商品删除失败，将重试。剩余商品数: {}, 重试次数: {}", remainingItemIds.size(), retryCount);
                    
                    // 模拟一些处理时间，增加并发修改的可能性
                    Thread.sleep(100);
                }
            } catch (ConcurrentModificationException e) {
                // 捕获并发修改异常，但继续循环，可能导致无限循环
                logger.error("发生并发修改异常，将重试。错误: {}", e.getMessage());
                retryCount++;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("线程被中断，批量删除操作中止");
                break;
            }
        }
        
        // 如果达到最大重试次数但仍有剩余商品，记录警告
        if (!remainingItemIds.isEmpty()) {
            logger.warn("达到最大重试次数，仍有{}个商品未能删除", remainingItemIds.size());
        }
        
        logger.info("批量删除购物车商品完成，用户ID: {}, 成功删除: {}, 请求删除: {}", 
            userId, successCount, itemIds.size());
        
        return successCount;
    }
    
    /**
     * 批量添加商品到购物车 - BAD CASE: 存在数组越界异常风险
     * 
     * 数组越界风险说明：
     * 1. 方法期望每个商品的信息是一个包含两个元素的数组 [skuId, quantity]
     * 2. 但没有对数组长度进行检查，直接访问索引0和1
     * 3. 如果传入的数组长度小于2，将导致ArrayIndexOutOfBoundsException
     * 4. 此外，没有对数组元素类型进行检查，可能导致ClassCastException
     * 
     * @param userId 用户ID
     * @param items 商品信息映射，key为商品ID，value为[skuId, quantity]数组
     * @return 成功添加的商品数量
     */
    @Override
    @Transactional
    public int batchAddToCart(Long userId, Map<Long, Object[]> items) {
        if (userId == null) {
            throw new CartException("用户ID不能为空");
        }
        
        if (items == null || items.isEmpty()) {
            logger.warn("要添加的商品列表为空，用户ID: {}", userId);
            return 0;
        }
        
        int successCount = 0;
        Map<Long, String> failedItems = new HashMap<>();
        
        // 遍历商品映射
        for (Map.Entry<Long, Object[]> entry : items.entrySet()) {
            Long itemId = entry.getKey();
            Object[] itemInfo = entry.getValue();
            
            try {
                // BAD CASE: 直接访问数组元素，没有检查数组长度，可能导致ArrayIndexOutOfBoundsException
                // 正确的做法应该是先检查 itemInfo != null && itemInfo.length >= 2
                Long skuId = (Long) itemInfo[0]; // 可能抛出ArrayIndexOutOfBoundsException或ClassCastException
                Integer quantity = (Integer) itemInfo[1]; // 可能抛出ArrayIndexOutOfBoundsException或ClassCastException
                
                // 检查商品是否存在
                if (itemService.getItemById(itemId) == null) {
                    failedItems.put(itemId, "商品不存在");
                    continue;
                }
                
                // 检查库存
                if (inventoryService.getInventoryById(skuId) == null) {
                    failedItems.put(itemId, "商品库存不存在");
                    continue;
                }
                
                // 检查数量
                if (quantity == null || quantity <= 0) {
                    failedItems.put(itemId, "商品数量必须大于0");
                    continue;
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
                
                successCount++;
                logger.debug("成功添加商品到购物车，商品ID: {}, SKU ID: {}, 数量: {}", itemId, skuId, quantity);
                
            } catch (ArrayIndexOutOfBoundsException e) {
                // 捕获数组越界异常
                logger.error("添加商品到购物车失败，数组越界异常，商品ID: {}, 错误: {}", itemId, e.getMessage());
                failedItems.put(itemId, "数组越界异常: " + e.getMessage());
            } catch (ClassCastException e) {
                // 捕获类型转换异常
                logger.error("添加商品到购物车失败，类型转换异常，商品ID: {}, 错误: {}", itemId, e.getMessage());
                failedItems.put(itemId, "类型转换异常: " + e.getMessage());
            } catch (Exception e) {
                // 捕获其他异常
                logger.error("添加商品到购物车失败，商品ID: {}, 错误: {}", itemId, e.getMessage());
                failedItems.put(itemId, "未知异常: " + e.getMessage());
            }
        }
        
        // 记录失败的商品
        if (!failedItems.isEmpty()) {
            logger.warn("部分商品添加失败，用户ID: {}, 失败商品数: {}", userId, failedItems.size());
            for (Map.Entry<Long, String> entry : failedItems.entrySet()) {
                logger.warn("商品添加失败，商品ID: {}, 原因: {}", entry.getKey(), entry.getValue());
            }
        }
        
        logger.info("批量添加购物车商品完成，用户ID: {}, 成功添加: {}, 请求添加: {}", 
            userId, successCount, items.size());
        
        return successCount;
    }

} 