package com.example.onlinestore.controller;

import com.example.onlinestore.dto.Response;
import com.example.onlinestore.service.impl.EnumPriceCalculator;
import com.example.onlinestore.service.impl.SafePriceCalculatorSingleton;
import com.example.onlinestore.service.impl.UnsafePriceCalculatorSingleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
    
    @Value("${price.calculator.use-bad-case:false}")
    private boolean useBadCase;
    
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
            
            if (useBadCase) {
                // 使用不安全的单例实现（可能导致并发问题）
                UnsafePriceCalculatorSingleton calculator = UnsafePriceCalculatorSingleton.getInstance();
                
                // 应用促销代码
                if (promotionCode != null && !promotionCode.isEmpty()) {
                    originalPrice = calculator.applyPromotion(originalPrice, promotionCode);
                }
                
                // 计算最终价格
                finalPrice = calculator.calculateFinalPrice(originalPrice, quantity, discountRate, extraFees);
                calculationCount = calculator.getCalculationCount();
            } else {
                // 使用安全的枚举单例实现
                EnumPriceCalculator calculator = EnumPriceCalculator.INSTANCE;
                
                // 应用促销代码
                if (promotionCode != null && !promotionCode.isEmpty()) {
                    originalPrice = calculator.applyPromotion(originalPrice, promotionCode);
                }
                
                // 计算最终价格
                finalPrice = calculator.calculateFinalPrice(originalPrice, quantity, discountRate, extraFees);
                calculationCount = calculator.getCalculationCount();
            }
            
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
    
    /**
     * 并发测试端点（展示不安全单例的问题）
     */
    @GetMapping("/concurrent-test")
    public Response<Map<String, Object>> concurrentTest(
            @RequestParam(value = "threads", defaultValue = "10") int threads,
            @RequestParam(value = "iterations", defaultValue = "100") int iterations) {
        
        logger.info("Running concurrent test with {} threads and {} iterations", threads, iterations);
        
        try {
            ExecutorService executor = Executors.newFixedThreadPool(threads);
            
            // 重置计数器
            if (useBadCase) {
                UnsafePriceCalculatorSingleton.reset();
            } else {
                SafePriceCalculatorSingleton.reset();
            }
            
            // 创建并发任务
            CompletableFuture<?>[] futures = new CompletableFuture[threads];
            for (int i = 0; i < threads; i++) {
                final int threadId = i;
                futures[i] = CompletableFuture.runAsync(() -> {
                    try {
                        for (int j = 0; j < iterations; j++) {
                            BigDecimal price = new BigDecimal("100.00").add(new BigDecimal(threadId));
                            int quantity = j % 5 + 1;
                            BigDecimal discount = new BigDecimal("0.1").multiply(new BigDecimal(j % 3 + 1));
                            
                            Map<String, BigDecimal> extraFees = new HashMap<>();
                            extraFees.put("shipping", new BigDecimal("5.99"));
                            extraFees.put("handling", new BigDecimal("2.50"));
                            
                            if (useBadCase) {
                                UnsafePriceCalculatorSingleton calculator = UnsafePriceCalculatorSingleton.getInstance();
                                calculator.calculateFinalPrice(price, quantity, discount, extraFees);
                                
                                // 在一些线程中添加新的促销代码（可能导致并发修改异常）
                                if (threadId % 3 == 0) {
                                    calculator.addPromotion("THREAD" + threadId, new BigDecimal("0.05"));
                                }
                            } else {
                                SafePriceCalculatorSingleton calculator = SafePriceCalculatorSingleton.getInstance();
                                calculator.calculateFinalPrice(price, quantity, discount, extraFees);
                                
                                // 在一些线程中添加新的促销代码（安全实现）
                                if (threadId % 3 == 0) {
                                    calculator.addPromotion("THREAD" + threadId, new BigDecimal("0.05"));
                                }
                            }
                        }
                    } catch (Exception e) {
                        logger.error("Error in thread {}: {}", threadId, e.getMessage());
                    }
                }, executor);
            }
            
            // 等待所有任务完成
            CompletableFuture.allOf(futures).join();
            
            // 关闭线程池
            executor.shutdown();
            
            // 获取结果
            Map<String, Object> result = new HashMap<>();
            if (useBadCase) {
                UnsafePriceCalculatorSingleton calculator = UnsafePriceCalculatorSingleton.getInstance();
                result.put("calculationCount", calculator.getCalculationCount());
                result.put("lastCalculatedPrice", calculator.getLastCalculatedPrice());
                result.put("expectedCount", threads * iterations);
                result.put("implementation", "unsafe");
            } else {
                SafePriceCalculatorSingleton calculator = SafePriceCalculatorSingleton.getInstance();
                result.put("calculationCount", calculator.getCalculationCount());
                result.put("lastCalculatedPrice", calculator.getLastCalculatedPrice());
                result.put("expectedCount", threads * iterations);
                result.put("implementation", "safe");
            }
            
            return Response.success(result);
        } catch (Exception e) {
            logger.error("Failed to run concurrent test", e);
            return Response.fail("并发测试失败: " + e.getMessage());
        }
    }
} 