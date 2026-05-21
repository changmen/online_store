package com.example.onlinestore.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("EnumPriceCalculator 价格计算器单元测试")
class EnumPriceCalculatorTest {

    private EnumPriceCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = EnumPriceCalculator.INSTANCE;
        // 重置可能被其他测试修改的促销码
        calculator.addPromotion("SAVE10", new BigDecimal("0.10"));
        calculator.addPromotion("SAVE20", new BigDecimal("0.20"));
        calculator.addPromotion("SAVE30", new BigDecimal("0.30"));
        calculator.addPromotion("HALF", new BigDecimal("0.50"));
    }

    @Nested
    @DisplayName("calculateDiscountPrice - 折扣价格计算")
    class DiscountPriceTests {

        @Test
        @DisplayName("正常计算折扣价格")
        void shouldCalculateDiscountPriceCorrectly() {
            BigDecimal original = new BigDecimal("100.00");
            BigDecimal discountRate = new BigDecimal("0.20");

            BigDecimal result = calculator.calculateDiscountPrice(original, discountRate);

            assertEquals(new BigDecimal("80.00"), result);
        }

        @Test
        @DisplayName("折扣率为0时返回原价")
        void shouldReturnOriginalPriceWhenDiscountRateIsZero() {
            BigDecimal original = new BigDecimal("200.00");
            BigDecimal discountRate = BigDecimal.ZERO;

            BigDecimal result = calculator.calculateDiscountPrice(original, discountRate);

            assertEquals(new BigDecimal("200.00"), result);
        }

        @Test
        @DisplayName("折扣率为1时返回0")
        void shouldReturnZeroWhenDiscountRateIsOne() {
            BigDecimal original = new BigDecimal("150.00");
            BigDecimal discountRate = BigDecimal.ONE;

            BigDecimal result = calculator.calculateDiscountPrice(original, discountRate);

            assertEquals(new BigDecimal("0.00"), result);
        }

        @Test
        @DisplayName("原价为null时返回null")
        void shouldReturnNullWhenOriginalPriceIsNull() {
            BigDecimal result = calculator.calculateDiscountPrice(null, new BigDecimal("0.10"));

            assertNull(result);
        }

        @Test
        @DisplayName("折扣率为null时返回原价")
        void shouldReturnOriginalPriceWhenDiscountRateIsNull() {
            BigDecimal original = new BigDecimal("88.00");

            BigDecimal result = calculator.calculateDiscountPrice(original, null);

            assertEquals(new BigDecimal("88.00"), result);
        }

        @Test
        @DisplayName("精确计算带小数的折扣（四舍五入到两位）")
        void shouldRoundToTwoDecimals() {
            BigDecimal original = new BigDecimal("99.99");
            BigDecimal discountRate = new BigDecimal("0.33");

            BigDecimal result = calculator.calculateDiscountPrice(original, discountRate);

            assertEquals(new BigDecimal("66.99"), result);
        }
    }

    @Nested
    @DisplayName("calculateTotalPrice - 总价计算")
    class TotalPriceTests {

        @Test
        @DisplayName("正常计算总价")
        void shouldCalculateTotalPriceCorrectly() {
            BigDecimal price = new BigDecimal("25.50");
            int quantity = 3;

            BigDecimal result = calculator.calculateTotalPrice(price, quantity);

            assertEquals(new BigDecimal("76.50"), result);
        }

        @Test
        @DisplayName("数量为0时总价为0")
        void shouldReturnZeroWhenQuantityIsZero() {
            BigDecimal price = new BigDecimal("100.00");

            BigDecimal result = calculator.calculateTotalPrice(price, 0);

            assertEquals(new BigDecimal("0.00"), result);
        }

        @Test
        @DisplayName("数量为1时总价等于单价")
        void shouldReturnSamePriceWhenQuantityIsOne() {
            BigDecimal price = new BigDecimal("49.99");

            BigDecimal result = calculator.calculateTotalPrice(price, 1);

            assertEquals(new BigDecimal("49.99"), result);
        }

        @Test
        @DisplayName("单价为null时返回0")
        void shouldReturnZeroWhenPriceIsNull() {
            BigDecimal result = calculator.calculateTotalPrice(null, 5);

            assertEquals(BigDecimal.ZERO, result);
        }
    }

    @Nested
    @DisplayName("calculateExtraFees - 额外费用计算")
    class ExtraFeesTests {

        @Test
        @DisplayName("正常计算额外费用总和")
        void shouldSumExtraFeesCorrectly() {
            Map<String, BigDecimal> fees = new HashMap<>();
            fees.put("shipping", new BigDecimal("15.00"));
            fees.put("packaging", new BigDecimal("5.50"));

            BigDecimal result = calculator.calculateExtraFees(fees);

            assertEquals(new BigDecimal("20.50"), result);
        }

        @Test
        @DisplayName("额外费用为空Map时返回0")
        void shouldReturnZeroWhenFeesMapIsEmpty() {
            Map<String, BigDecimal> fees = new HashMap<>();

            BigDecimal result = calculator.calculateExtraFees(fees);

            assertEquals(BigDecimal.ZERO, result);
        }

        @Test
        @DisplayName("额外费用为null时返回0")
        void shouldReturnZeroWhenFeesMapIsNull() {
            BigDecimal result = calculator.calculateExtraFees(null);

            assertEquals(BigDecimal.ZERO, result);
        }

        @Test
        @DisplayName("单项额外费用计算正确")
        void shouldReturnSingleFee() {
            Map<String, BigDecimal> fees = new HashMap<>();
            fees.put("tax", new BigDecimal("8.75"));

            BigDecimal result = calculator.calculateExtraFees(fees);

            assertEquals(new BigDecimal("8.75"), result);
        }
    }

    @Nested
    @DisplayName("applyPromotion - 促销应用")
    class PromotionTests {

        @Test
        @DisplayName("应用已知促销码 SAVE10")
        void shouldApplySave10Promotion() {
            BigDecimal price = new BigDecimal("100.00");

            BigDecimal result = calculator.applyPromotion(price, "SAVE10");

            assertEquals(new BigDecimal("90.00"), result);
        }

        @Test
        @DisplayName("应用已知促销码 SAVE20")
        void shouldApplySave20Promotion() {
            BigDecimal price = new BigDecimal("200.00");

            BigDecimal result = calculator.applyPromotion(price, "SAVE20");

            assertEquals(new BigDecimal("160.00"), result);
        }

        @Test
        @DisplayName("应用已知促销码 SAVE30")
        void shouldApplySave30Promotion() {
            BigDecimal price = new BigDecimal("50.00");

            BigDecimal result = calculator.applyPromotion(price, "SAVE30");

            assertEquals(new BigDecimal("35.00"), result);
        }

        @Test
        @DisplayName("应用已知促销码 HALF")
        void shouldApplyHalfPromotion() {
            BigDecimal price = new BigDecimal("80.00");

            BigDecimal result = calculator.applyPromotion(price, "HALF");

            assertEquals(new BigDecimal("40.00"), result);
        }

        @Test
        @DisplayName("未知促销码返回原价")
        void shouldReturnOriginalPriceForUnknownPromotion() {
            BigDecimal price = new BigDecimal("100.00");

            BigDecimal result = calculator.applyPromotion(price, "UNKNOWN");

            assertEquals(new BigDecimal("100.00"), result);
        }

        @Test
        @DisplayName("促销码为null返回原价")
        void shouldReturnOriginalPriceWhenPromotionCodeIsNull() {
            BigDecimal price = new BigDecimal("100.00");

            BigDecimal result = calculator.applyPromotion(price, null);

            assertEquals(new BigDecimal("100.00"), result);
        }

        @Test
        @DisplayName("促销码为空字符串返回原价")
        void shouldReturnOriginalPriceWhenPromotionCodeIsEmpty() {
            BigDecimal price = new BigDecimal("100.00");

            BigDecimal result = calculator.applyPromotion(price, "");

            assertEquals(new BigDecimal("100.00"), result);
        }

        @Test
        @DisplayName("价格为null时返回null")
        void shouldReturnNullWhenPriceIsNull() {
            BigDecimal result = calculator.applyPromotion(null, "SAVE10");

            assertNull(result);
        }
    }

    @Nested
    @DisplayName("calculateFinalPrice - 最终价格计算（集成方法）")
    class FinalPriceTests {

        @Test
        @DisplayName("正常计算包含折扣、数量和额外费用的最终价格")
        void shouldCalculateFinalPriceWithAllComponents() {
            BigDecimal originalPrice = new BigDecimal("100.00");
            int quantity = 3;
            BigDecimal discountRate = new BigDecimal("0.10");
            Map<String, BigDecimal> fees = new HashMap<>();
            fees.put("shipping", new BigDecimal("20.00"));

            BigDecimal result = calculator.calculateFinalPrice(originalPrice, quantity, discountRate, fees);

            // 折扣后: 100 * 0.9 = 90, 总价: 90 * 3 = 270, 加运费: 270 + 20 = 290
            assertEquals(new BigDecimal("290.00"), result);
        }

        @Test
        @DisplayName("无额外费用时仅计算折扣总价")
        void shouldCalculateFinalPriceWithoutExtraFees() {
            BigDecimal originalPrice = new BigDecimal("50.00");
            int quantity = 2;
            BigDecimal discountRate = BigDecimal.ZERO;
            Map<String, BigDecimal> fees = new HashMap<>();

            BigDecimal result = calculator.calculateFinalPrice(originalPrice, quantity, discountRate, fees);

            assertEquals(new BigDecimal("100.00"), result);
        }

        @Test
        @DisplayName("计算后更新状态")
        void shouldUpdateStateAfterCalculation() {
            BigDecimal originalPrice = new BigDecimal("10.00");
            int quantity = 1;
            BigDecimal discountRate = BigDecimal.ZERO;
            Map<String, BigDecimal> fees = new HashMap<>();

            calculator.calculateFinalPrice(originalPrice, quantity, discountRate, fees);

            assertEquals(new BigDecimal("10.00"), calculator.getLastCalculatedPrice());
            assertTrue(calculator.getCalculationCount() > 0);
        }
    }

    @Nested
    @DisplayName("addPromotion - 动态添加促销")
    class AddPromotionTests {

        @Test
        @DisplayName("添加新促销码后可正常使用")
        void shouldApplyNewlyAddedPromotion() {
            calculator.addPromotion("VIP50", new BigDecimal("0.50"));

            BigDecimal result = calculator.applyPromotion(new BigDecimal("200.00"), "VIP50");

            assertEquals(new BigDecimal("100.00"), result);
        }

        @Test
        @DisplayName("覆盖已有促销码")
        void shouldOverrideExistingPromotion() {
            calculator.addPromotion("SAVE10", new BigDecimal("0.50"));

            BigDecimal result = calculator.applyPromotion(new BigDecimal("100.00"), "SAVE10");

            assertEquals(new BigDecimal("50.00"), result);
        }
    }
}
