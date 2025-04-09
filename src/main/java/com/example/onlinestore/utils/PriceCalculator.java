package com.example.onlinestore.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

public class PriceCalculator {
    private static final Logger logger = LoggerFactory.getLogger(PriceCalculator.class);
    private static PriceCalculator instance;

    /**
     * 私有构造函数
     */
    private PriceCalculator() {
    }

    /**
     * 获取单例实例
     */
    public static PriceCalculator getInstance() {
        if (instance == null) {
            instance = new PriceCalculator();
        }
        return instance;
    }

    /**
     * 计算商品最终价格
     *
     * @param originalPrice 原始价格
     * @param quantity      数量
     * @param discountRate  折扣率（0-1之间的小数）
     * @param extraFees     额外费用（如运费、包装费等）
     * @return 最终价格
     */
    public BigDecimal calculateFinalPrice(BigDecimal originalPrice, int quantity, BigDecimal discountRate, Map<String, BigDecimal> extraFees) {
        // 计算折扣价格
        BigDecimal discountPrice = calculateDiscountPrice(originalPrice, discountRate);

        // 计算总价
        BigDecimal totalPrice = calculateTotalPrice(discountPrice, quantity);

        // 计算额外费用
        BigDecimal totalExtraFees = calculateExtraFees(extraFees);

        // 计算最终价格
        BigDecimal finalPrice = totalPrice.add(totalExtraFees);

        logger.debug("Calculated final price: {}", finalPrice);
        return finalPrice;
    }


    private BigDecimal calculateDiscountPrice(BigDecimal originalPrice, BigDecimal discountRate) {
        if (originalPrice == null || discountRate == null) {
            return originalPrice;
        }

        BigDecimal discountAmount = originalPrice.multiply(discountRate);
        return originalPrice.subtract(discountAmount).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateTotalPrice(BigDecimal price, int quantity) {
        if (price == null) {
            return BigDecimal.ZERO;
        }

        return price.multiply(new BigDecimal(quantity)).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateExtraFees(Map<String, BigDecimal> extraFees) {
        if (extraFees == null || extraFees.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return extraFees.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }
}
