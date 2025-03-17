package com.example.onlinestore.service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 商品价格计算器接口
 */
public interface PriceCalculator {
    
    /**
     * 计算商品最终价格
     * 
     * @param originalPrice 原始价格
     * @param quantity 数量
     * @param discountRate 折扣率（0-1之间的小数）
     * @param extraFees 额外费用（如运费、包装费等）
     * @return 最终价格
     */
    BigDecimal calculateFinalPrice(BigDecimal originalPrice, int quantity, BigDecimal discountRate, Map<String, BigDecimal> extraFees);
    
    /**
     * 计算商品折扣价格
     * 
     * @param originalPrice 原始价格
     * @param discountRate 折扣率（0-1之间的小数）
     * @return 折扣价格
     */
    BigDecimal calculateDiscountPrice(BigDecimal originalPrice, BigDecimal discountRate);
    
    /**
     * 计算商品总价（数量 * 单价）
     * 
     * @param price 单价
     * @param quantity 数量
     * @return 总价
     */
    BigDecimal calculateTotalPrice(BigDecimal price, int quantity);
    
    /**
     * 计算额外费用总和
     * 
     * @param extraFees 额外费用映射
     * @return 额外费用总和
     */
    BigDecimal calculateExtraFees(Map<String, BigDecimal> extraFees);
    
    /**
     * 应用促销规则
     * 
     * @param price 价格
     * @param promotionCode 促销代码
     * @return 应用促销后的价格
     */
    BigDecimal applyPromotion(BigDecimal price, String promotionCode);
} 