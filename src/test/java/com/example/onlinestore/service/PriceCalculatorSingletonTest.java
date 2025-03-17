package com.example.onlinestore.service;

import com.example.onlinestore.service.impl.EnumPriceCalculator;
import com.example.onlinestore.service.impl.SafePriceCalculatorSingleton;
import com.example.onlinestore.service.impl.UnsafePriceCalculatorSingleton;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 价格计算器单例测试
 */
public class PriceCalculatorSingletonTest {
    
    private static final Logger logger = LoggerFactory.getLogger(PriceCalculatorSingletonTest.class);
    
    private static final int THREAD_COUNT = 20;
    private static final int ITERATIONS = 100;
    
    @BeforeEach
    public void setUp() {
        // 重置单例实例
        UnsafePriceCalculatorSingleton.reset();
        SafePriceCalculatorSingleton.reset();
    }
    
    /**
     * 测试不安全单例的并发问题
     */
    @Test
    public void testUnsafeSingletonConcurrencyIssues() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
        AtomicInteger exceptionCount = new AtomicInteger(0);
        
        // 创建并发任务
        for (int i = 0; i < THREAD_COUNT; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    for (int j = 0; j < ITERATIONS; j++) {
                        BigDecimal price = new BigDecimal("100.00").add(new BigDecimal(threadId));
                        int quantity = j % 5 + 1;
                        BigDecimal discount = new BigDecimal("0.1").multiply(new BigDecimal(j % 3 + 1));
                        
                        Map<String, BigDecimal> extraFees = new HashMap<>();
                        extraFees.put("shipping", new BigDecimal("5.99"));
                        extraFees.put("handling", new BigDecimal("2.50"));
                        
                        UnsafePriceCalculatorSingleton calculator = UnsafePriceCalculatorSingleton.getInstance();
                        calculator.calculateFinalPrice(price, quantity, discount, extraFees);
                        
                        // 在一些线程中添加新的促销代码（可能导致并发修改异常）
                        if (threadId % 3 == 0) {
                            calculator.addPromotion("THREAD" + threadId + "-" + j, new BigDecimal("0.05"));
                        }
                    }
                } catch (Exception e) {
                    logger.error("Error in thread {}: {}", threadId, e.getMessage());
                    exceptionCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }
        
        // 等待所有任务完成
        latch.await();
        executor.shutdown();
        
        // 获取结果
        UnsafePriceCalculatorSingleton calculator = UnsafePriceCalculatorSingleton.getInstance();
        int calculationCount = calculator.getCalculationCount();
        
        logger.info("Unsafe singleton test completed. Calculation count: {}, Expected: {}, Exceptions: {}",
                calculationCount, THREAD_COUNT * ITERATIONS, exceptionCount.get());
        
        // 注意：由于并发问题，计算次数可能不等于预期值
        // 这里我们不断言相等，而是记录差异
        if (calculationCount != THREAD_COUNT * ITERATIONS) {
            logger.warn("Calculation count mismatch! Expected: {}, Actual: {}, Difference: {}",
                    THREAD_COUNT * ITERATIONS, calculationCount, THREAD_COUNT * ITERATIONS - calculationCount);
        }
    }
    
    /**
     * 测试安全单例的并发行为
     */
    @Test
    public void testSafeSingletonConcurrency() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
        AtomicInteger exceptionCount = new AtomicInteger(0);
        
        // 创建并发任务
        for (int i = 0; i < THREAD_COUNT; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    for (int j = 0; j < ITERATIONS; j++) {
                        BigDecimal price = new BigDecimal("100.00").add(new BigDecimal(threadId));
                        int quantity = j % 5 + 1;
                        BigDecimal discount = new BigDecimal("0.1").multiply(new BigDecimal(j % 3 + 1));
                        
                        Map<String, BigDecimal> extraFees = new HashMap<>();
                        extraFees.put("shipping", new BigDecimal("5.99"));
                        extraFees.put("handling", new BigDecimal("2.50"));
                        
                        SafePriceCalculatorSingleton calculator = SafePriceCalculatorSingleton.getInstance();
                        calculator.calculateFinalPrice(price, quantity, discount, extraFees);
                        
                        // 在一些线程中添加新的促销代码（安全实现）
                        if (threadId % 3 == 0) {
                            calculator.addPromotion("THREAD" + threadId + "-" + j, new BigDecimal("0.05"));
                        }
                    }
                } catch (Exception e) {
                    logger.error("Error in thread {}: {}", threadId, e.getMessage());
                    exceptionCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }
        
        // 等待所有任务完成
        latch.await();
        executor.shutdown();
        
        // 获取结果
        SafePriceCalculatorSingleton calculator = SafePriceCalculatorSingleton.getInstance();
        int calculationCount = calculator.getCalculationCount();
        
        logger.info("Safe singleton test completed. Calculation count: {}, Expected: {}, Exceptions: {}",
                calculationCount, THREAD_COUNT * ITERATIONS, exceptionCount.get());
        
        // 安全实现应该没有并发问题，计算次数应该等于预期值
        assertEquals(THREAD_COUNT * ITERATIONS, calculationCount, "Safe implementation should have correct calculation count");
        assertEquals(0, exceptionCount.get(), "Safe implementation should not throw exceptions");
    }
    
    /**
     * 测试枚举单例的并发行为
     */
    @Test
    public void testEnumSingletonConcurrency() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
        AtomicInteger exceptionCount = new AtomicInteger(0);
        
        // 创建并发任务
        for (int i = 0; i < THREAD_COUNT; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    for (int j = 0; j < ITERATIONS; j++) {
                        BigDecimal price = new BigDecimal("100.00").add(new BigDecimal(threadId));
                        int quantity = j % 5 + 1;
                        BigDecimal discount = new BigDecimal("0.1").multiply(new BigDecimal(j % 3 + 1));
                        
                        Map<String, BigDecimal> extraFees = new HashMap<>();
                        extraFees.put("shipping", new BigDecimal("5.99"));
                        extraFees.put("handling", new BigDecimal("2.50"));
                        
                        EnumPriceCalculator calculator = EnumPriceCalculator.INSTANCE;
                        calculator.calculateFinalPrice(price, quantity, discount, extraFees);
                        
                        // 在一些线程中添加新的促销代码（安全实现）
                        if (threadId % 3 == 0) {
                            calculator.addPromotion("THREAD" + threadId + "-" + j, new BigDecimal("0.05"));
                        }
                    }
                } catch (Exception e) {
                    logger.error("Error in thread {}: {}", threadId, e.getMessage());
                    exceptionCount.incrementAndGet();
                } finally {
                    latch.countDown();
                }
            });
        }
        
        // 等待所有任务完成
        latch.await();
        executor.shutdown();
        
        // 获取结果
        EnumPriceCalculator calculator = EnumPriceCalculator.INSTANCE;
        int calculationCount = calculator.getCalculationCount();
        
        logger.info("Enum singleton test completed. Calculation count: {}, Expected: {}, Exceptions: {}",
                calculationCount, THREAD_COUNT * ITERATIONS, exceptionCount.get());
        
        // 枚举实现应该没有并发问题，计算次数应该等于预期值
        assertEquals(THREAD_COUNT * ITERATIONS, calculationCount, "Enum implementation should have correct calculation count");
        assertEquals(0, exceptionCount.get(), "Enum implementation should not throw exceptions");
    }
} 