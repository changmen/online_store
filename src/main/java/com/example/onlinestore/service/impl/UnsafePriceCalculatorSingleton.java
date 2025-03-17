package com.example.onlinestore.service.impl;

import com.example.onlinestore.service.PriceCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 不安全的价格计算器单例实现
 * 
 * BAD CASE: 该实现存在以下并发安全问题
 * 1. 懒加载实现不是线程安全的（双重检查锁定模式的错误实现）
 * 2. 使用非线程安全的HashMap存储促销代码
 * 3. 计算过程中的状态可能被多线程修改
 */
public class UnsafePriceCalculatorSingleton implements PriceCalculator {
    
    private static final Logger logger = LoggerFactory.getLogger(UnsafePriceCalculatorSingleton.class);
    
    // BAD CASE: 单例实例没有使用volatile修饰，可能导致部分初始化问题
    private static UnsafePriceCalculatorSingleton instance;
    
    // BAD CASE: 使用非线程安全的HashMap
    private Map<String, BigDecimal> promotionDiscounts;
    
    // BAD CASE: 可变状态，在多线程环境下可能导致数据不一致
    private BigDecimal lastCalculatedPrice;
    private int calculationCount;
    
    /**
     * 私有构造函数
     */
    private UnsafePriceCalculatorSingleton() {
        // 初始化促销折扣
        promotionDiscounts = new HashMap<>();
        promotionDiscounts.put("SAVE10", new BigDecimal("0.10"));
        promotionDiscounts.put("SAVE20", new BigDecimal("0.20"));
        promotionDiscounts.put("SAVE30", new BigDecimal("0.30"));
        promotionDiscounts.put("HALF", new BigDecimal("0.50"));
        
        lastCalculatedPrice = BigDecimal.ZERO;
        calculationCount = 0;
        
        logger.info("UnsafePriceCalculatorSingleton initialized");
    }
    
    /**
     * 获取单例实例
     * BAD CASE: 错误的双重检查锁定模式实现，没有使用volatile，可能导致部分初始化问题
     */
    public static UnsafePriceCalculatorSingleton getInstance() {
        if (instance == null) {
            // BAD CASE: 多个线程可能同时通过第一次检查
            synchronized (UnsafePriceCalculatorSingleton.class) {
                if (instance == null) {
                    // BAD CASE: 创建对象的过程不是原子的，可能导致其他线程看到部分初始化的对象
                    instance = new UnsafePriceCalculatorSingleton();
                }
            }
        }
        return instance;
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
        
        // BAD CASE: 更新共享状态，在多线程环境下可能导致数据不一致
        lastCalculatedPrice = finalPrice;
        calculationCount++;
        
        logger.debug("Calculated final price: {}, count: {}", finalPrice, calculationCount);
        
        return finalPrice;
    }
    
    @Override
    public BigDecimal calculateDiscountPrice(BigDecimal originalPrice, BigDecimal discountRate) {
        if (originalPrice == null || discountRate == null) {
            return originalPrice;
        }
        
        // BAD CASE: 模拟耗时操作，增加并发问题出现的可能性
        try {
            TimeUnit.MILLISECONDS.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        BigDecimal discountAmount = originalPrice.multiply(discountRate);
        return originalPrice.subtract(discountAmount).setScale(2, RoundingMode.HALF_UP);
    }
    
    @Override
    public BigDecimal calculateTotalPrice(BigDecimal price, int quantity) {
        if (price == null) {
            return BigDecimal.ZERO;
        }
        
        // BAD CASE: 模拟耗时操作，增加并发问题出现的可能性
        try {
            TimeUnit.MILLISECONDS.sleep(30);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return price.multiply(new BigDecimal(quantity)).setScale(2, RoundingMode.HALF_UP);
    }
    
    @Override
    public BigDecimal calculateExtraFees(Map<String, BigDecimal> extraFees) {
        if (extraFees == null || extraFees.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        // BAD CASE: 模拟耗时操作，增加并发问题出现的可能性
        try {
            TimeUnit.MILLISECONDS.sleep(20);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
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
        
        // BAD CASE: 在多线程环境下，promotionDiscounts可能被并发修改
        BigDecimal discountRate = promotionDiscounts.get(promotionCode);
        if (discountRate == null) {
            return price;
        }
        
        // BAD CASE: 模拟耗时操作，增加并发问题出现的可能性
        try {
            TimeUnit.MILLISECONDS.sleep(40);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        BigDecimal discountAmount = price.multiply(discountRate);
        return price.subtract(discountAmount).setScale(2, RoundingMode.HALF_UP);
    }
    
    /**
     * 添加新的促销代码
     * BAD CASE: 在多线程环境下，可能导致数据不一致
     */
    public void addPromotion(String code, BigDecimal discountRate) {
        // BAD CASE: 非线程安全的操作
        promotionDiscounts.put(code, discountRate);
    }
    
    /**
     * 获取最后计算的价格
     * BAD CASE: 在多线程环境下，可能返回不一致的数据
     */
    public BigDecimal getLastCalculatedPrice() {
        return lastCalculatedPrice;
    }
    
    /**
     * 获取计算次数
     * BAD CASE: 在多线程环境下，可能返回不一致的数据
     */
    public int getCalculationCount() {
        return calculationCount;
    }
    
    /**
     * 重置单例实例（仅用于测试）
     */
    public static void reset() {
        instance = null;
    }
} 