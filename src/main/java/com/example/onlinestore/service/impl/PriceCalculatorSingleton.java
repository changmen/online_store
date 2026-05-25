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

public class PriceCalculatorSingleton implements PriceCalculator {

    private static final Logger logger = LoggerFactory.getLogger(PriceCalculatorSingleton.class);

    private static volatile PriceCalculatorSingleton instance;

    private final ConcurrentHashMap<String, BigDecimal> promotionDiscounts;
    private final AtomicReference<BigDecimal> lastCalculatedPrice;
    private final AtomicInteger calculationCount;

    private PriceCalculatorSingleton() {
        promotionDiscounts = new ConcurrentHashMap<>();
        promotionDiscounts.put("SAVE10", new BigDecimal("0.10"));
        promotionDiscounts.put("SAVE20", new BigDecimal("0.20"));
        promotionDiscounts.put("SAVE30", new BigDecimal("0.30"));
        promotionDiscounts.put("HALF", new BigDecimal("0.50"));

        lastCalculatedPrice = new AtomicReference<>(BigDecimal.ZERO);
        calculationCount = new AtomicInteger(0);

        logger.info("PriceCalculatorSingleton initialized");
    }

    public static PriceCalculatorSingleton getInstance() {
        if (instance == null) {
            synchronized (PriceCalculatorSingleton.class) {
                if (instance == null) {
                    instance = new PriceCalculatorSingleton();
                }
            }
        }
        return instance;
    }

    @Override
    public BigDecimal calculateFinalPrice(BigDecimal originalPrice, int quantity, BigDecimal discountRate, Map<String, BigDecimal> extraFees) {
        BigDecimal discountPrice = calculateDiscountPrice(originalPrice, discountRate);
        BigDecimal totalPrice = calculateTotalPrice(discountPrice, quantity);
        BigDecimal totalExtraFees = calculateExtraFees(extraFees);
        BigDecimal finalPrice = totalPrice.add(totalExtraFees);

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

    public void addPromotion(String code, BigDecimal discountRate) {
        promotionDiscounts.put(code, discountRate);
    }

    public BigDecimal getLastCalculatedPrice() {
        return lastCalculatedPrice.get();
    }

    public int getCalculationCount() {
        return calculationCount.get();
    }

    public static void reset() {
        synchronized (PriceCalculatorSingleton.class) {
            instance = null;
        }
    }
}
