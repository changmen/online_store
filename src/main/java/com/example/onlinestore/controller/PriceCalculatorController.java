package com.example.onlinestore.controller;

import com.example.onlinestore.dto.Response;
import com.example.onlinestore.service.impl.PriceCalculatorSingleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 价格计算器控制器
 */
@RestController
@RequestMapping("/api/price-calculator")
public class PriceCalculatorController {
    
    private static final Logger logger = LoggerFactory.getLogger(PriceCalculatorController.class);

    /**
     * 计算商品最终价格
     */
    @PostMapping("/calculate")
    public Response<Map<String, Object>> calculatePrice(
            @RequestParam("price") BigDecimal originalPrice,
            @RequestParam("quantity") int quantity,
            @RequestParam(value = "discount", required = false, defaultValue = "0") BigDecimal discountRate,
            @RequestParam(value = "promotion", required = false) String promotionCode) {
        
        logger.info("Calculate price: price={}, quantity={}, discount={}, promotion={}",
                originalPrice, quantity, discountRate, promotionCode);
        
        try {
            Map<String, BigDecimal> extraFees = new HashMap<>();
            extraFees.put("shipping", new BigDecimal("5.99"));
            extraFees.put("handling", new BigDecimal("2.50"));
            
            BigDecimal finalPrice;
            int calculationCount;

            PriceCalculatorSingleton calculator = PriceCalculatorSingleton.getInstance();

            // 应用促销代码
            if (promotionCode != null && !promotionCode.isEmpty()) {
                originalPrice = calculator.applyPromotion(originalPrice, promotionCode);
            }

            // 计算最终价格
            finalPrice = calculator.calculateFinalPrice(originalPrice, quantity, discountRate, extraFees);
            calculationCount = calculator.getCalculationCount();
            
            // 构建响应
            Map<String, Object> result = new HashMap<>();
            result.put("originalPrice", originalPrice);
            result.put("quantity", quantity);
            result.put("discountRate", discountRate);
            result.put("finalPrice", finalPrice);
            result.put("calculationCount", calculationCount);
            
            return Response.success(result);
        } catch (Exception e) {
            logger.error("Failed to calculate price", e);
            return Response.fail("计算价格失败: " + e.getMessage());
        }
    }

} 