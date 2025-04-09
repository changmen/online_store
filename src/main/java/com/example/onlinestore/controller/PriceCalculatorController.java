package com.example.onlinestore.controller;

import com.example.onlinestore.dto.Response;
import com.example.onlinestore.utils.PriceCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

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
        
        logger.debug("Calculate price: price={}, quantity={}, discount={}, promotion={}",
                originalPrice, quantity, discountRate, promotionCode);
        
        try {
            Map<String, BigDecimal> extraFees = new HashMap<>();
            extraFees.put("shipping", new BigDecimal("5.99"));
            extraFees.put("handling", new BigDecimal("2.50"));
            
            BigDecimal finalPrice;
            PriceCalculator calculator = PriceCalculator.getInstance();
            // 计算最终价格
            finalPrice = calculator.calculateFinalPrice(originalPrice, quantity, discountRate, extraFees);

            // 构建响应
            Map<String, Object> result = new HashMap<>();
            result.put("originalPrice", originalPrice);
            result.put("quantity", quantity);
            result.put("discountRate", discountRate);
            result.put("finalPrice", finalPrice);

            return Response.success(result);
        } catch (Exception e) {
            logger.error("Failed to calculate price", e);
            return Response.fail("计算价格失败: " + e.getMessage());
        }
    }

} 