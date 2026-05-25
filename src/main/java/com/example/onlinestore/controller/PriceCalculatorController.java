package com.example.onlinestore.controller;

import com.example.onlinestore.dto.Response;
import com.example.onlinestore.service.impl.PriceCalculatorSingleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/price-calculator")
public class PriceCalculatorController {

    private static final Logger logger = LoggerFactory.getLogger(PriceCalculatorController.class);

    @PostMapping("/calculate")
    public Response<Map<String, Object>> calculatePrice(
            @RequestParam("price") BigDecimal originalPrice,
            @RequestParam("quantity") int quantity,
            @RequestParam(value = "discount", required = false, defaultValue = "0") BigDecimal discountRate,
            @RequestParam(value = "promotion", required = false) String promotionCode) {

        logger.info("Calculate price: price={}, quantity={}, discount={}, promotion={}",
                originalPrice, quantity, discountRate, promotionCode);

        Map<String, BigDecimal> extraFees = new HashMap<>();
        extraFees.put("shipping", new BigDecimal("5.99"));
        extraFees.put("handling", new BigDecimal("2.50"));

        PriceCalculatorSingleton calculator = PriceCalculatorSingleton.getInstance();

        if (promotionCode != null && !promotionCode.isEmpty()) {
            originalPrice = calculator.applyPromotion(originalPrice, promotionCode);
        }

        BigDecimal finalPrice = calculator.calculateFinalPrice(originalPrice, quantity, discountRate, extraFees);
        int calculationCount = calculator.getCalculationCount();

        Map<String, Object> result = new HashMap<>();
        result.put("originalPrice", originalPrice);
        result.put("quantity", quantity);
        result.put("discountRate", discountRate);
        result.put("finalPrice", finalPrice);
        result.put("calculationCount", calculationCount);

        return Response.success(result);
    }

}
