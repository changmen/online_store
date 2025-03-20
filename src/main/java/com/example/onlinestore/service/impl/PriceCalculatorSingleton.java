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
 * 价格计算器
 *
 */
public class PriceCalculatorSingleton implements PriceCalculator {
    
    private static final Logger logger = LoggerFactory.getLogger(PriceCalculatorSingleton.class);
    
    private static PriceCalculatorSingleton instance;
    
    private Map<String, BigDecimal> promotionDiscounts;
    
    private BigDecimal lastCalculatedPrice;
    private int calculationCount;
    
    /**
     * 私有构造函数
     */
    private PriceCalculatorSingleton() {
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
     */
    public static PriceCalculatorSingleton getInstance() {
        if (instance == null) {
            instance = new PriceCalculatorSingleton();
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
     * 添加新的促销代码
     */
    public void addPromotion(String code, BigDecimal discountRate) {
        promotionDiscounts.put(code, discountRate);
    }
    
    /**
     * 获取最后计算的价格
     */
    public BigDecimal getLastCalculatedPrice() {
        return lastCalculatedPrice;
    }
    
    /**
     * 获取计算次数
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