package com.example.onlinestore.service.impl;

import com.example.onlinestore.service.PriceCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 基于枚举的价格计算器单例实现
 * 
 * 枚举实现单例是最安全的方式，它天然防止了：
 * 1. 多线程安全问题
 * 2. 序列化/反序列化问题
 * 3. 反射攻击
 * 4. 克隆攻击
 */
public enum EnumPriceCalculator implements PriceCalculator {
    
    INSTANCE;
    
    // 使用线程安全的ConcurrentHashMap
    private final ConcurrentHashMap<String, BigDecimal> promotionDiscounts;
    
    // 使用AtomicReference和AtomicInteger保证线程安全
    private final AtomicReference<BigDecimal> lastCalculatedPrice;
    private final AtomicInteger calculationCount;
    
    // 日志记录器
    private final Logger logger;
    
    /**
     * 枚举构造函数
     */
    EnumPriceCalculator() {
        // 初始化日志记录器
        this.logger = LoggerFactory.getLogger(EnumPriceCalculator.class);
        
        // 初始化促销折扣
        promotionDiscounts = new ConcurrentHashMap<>();
        promotionDiscounts.put("SAVE10", new BigDecimal("0.10"));
        promotionDiscounts.put("SAVE20", new BigDecimal("0.20"));
        promotionDiscounts.put("SAVE30", new BigDecimal("0.30"));
        promotionDiscounts.put("HALF", new BigDecimal("0.50"));
        
        lastCalculatedPrice = new AtomicReference<>(BigDecimal.ZERO);
        calculationCount = new AtomicInteger(0);
        
        logger.info("EnumPriceCalculator initialized");
    }
    
    @Override
    public BigDecimal calculateFinalPrice(BigDecimal originalPrice, int quantity, BigDecimal discountRate, Map<String, BigDecimal> extraFees) {
        // 计算折扣价格
        BigDecimal discountPrice = calculateDiscountPrice(originalPrice, discountRate);
        
        // 计算总价
        BigDecimal totalPrice = calculateTotalPrice(discountPrice, quantity);
        
        // 计算额外费用
        BigDecimal totalExtraFees = calculateExtraFees(extraFees);
        
        // 计算最终价格
        BigDecimal finalPrice = totalPrice.add(totalExtraFees);
        
        // 线程安全地更新状态
        lastCalculatedPrice.set(finalPrice);
        int count = calculationCount.incrementAndGet();
        
        logger.debug("Calculated final price: {}, count: {}", finalPrice, count);
        
        return finalPrice;
    }
    
    @Override
    public BigDecimal calculateDiscountPrice(BigDecimal originalPrice, BigDecimal discountRate) {
        if (originalPrice == null || discountRate == null) {
            return originalPrice;
        }
        
        BigDecimal discountAmount = originalPrice.multiply(discountRate);
        return originalPrice.subtract(discountAmount).setScale(2, RoundingMode.HALF_UP);
    }
    
    @Override
    public BigDecimal calculateTotalPrice(BigDecimal price, int quantity) {
        if (price == null) {
            return BigDecimal.ZERO;
        }
        
        return price.multiply(new BigDecimal(quantity)).setScale(2, RoundingMode.HALF_UP);
    }
    
    @Override
    public BigDecimal calculateExtraFees(Map<String, BigDecimal> extraFees) {
        if (extraFees == null || extraFees.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        return extraFees.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }
    
    @Override
    public BigDecimal applyPromotion(BigDecimal price, String promotionCode) {
        if (price == null || promotionCode == null || promotionCode.isEmpty()) {
            return price;
        }
        
        BigDecimal discountRate = promotionDiscounts.get(promotionCode);
        if (discountRate == null) {
            return price;
        }
        
        BigDecimal discountAmount = price.multiply(discountRate);
        return price.subtract(discountAmount).setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * 添加新的促销代码（线程安全）
     */
    public void addPromotion(String code, BigDecimal discountRate) {
        promotionDiscounts.put(code, discountRate);
    }
    
    /**
     * 获取最后计算的价格（线程安全）
     */
    public BigDecimal getLastCalculatedPrice() {
        return lastCalculatedPrice.get();
    }
    
    /**
     * 获取计算次数（线程安全）
     */
    public int getCalculationCount() {
        return calculationCount.get();
    }
} 