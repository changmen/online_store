package com.example.onlinestore.service;

import com.example.onlinestore.dto.UserItemStatDTO;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 用户商品统计服务测试
 */
@SpringBootTest
public class UserItemStatServiceTest {
    
    private static final Logger logger = LoggerFactory.getLogger(UserItemStatServiceTest.class);
    
    @Autowired
    private UserItemStatService userItemStatService;
    
    private static final int USER_COUNT = 100;
    private static final int THREAD_COUNT = 10;
    
    /**
     * 测试安全实现的并发行为
     */
    @Test
    public void testSafeImplementation() throws InterruptedException {
        // 准备测试数据
        List<String> userIds = generateTestUserIds(USER_COUNT);
        
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
        AtomicInteger exceptionCount = new AtomicInteger(0);
        
        // 创建并发任务
        for (int i = 0; i < THREAD_COUNT; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    // 每个线程处理一部分用户
                    int start = threadId * (USER_COUNT / THREAD_COUNT);
                    int end = (threadId + 1) * (USER_COUNT / THREAD_COUNT);
                    List<String> threadUserIds = userIds.subList(start, end);
                    
                    // 使用安全实现
                    Map<String, UserItemStatDTO> result = userItemStatService.getUserItemStats(threadUserIds);
                    
                    logger.info("Thread {} processed {} users", threadId, result.size());
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
        
        // 安全实现应该没有异常
        assertEquals(0, exceptionCount.get(), "Safe implementation should not throw exceptions");
    }
    
    /**
     * 测试不安全实现的并发问题
     */
    @Test
    public void testUnsafeImplementation() throws InterruptedException {
        // 准备测试数据
        List<String> userIds = generateTestUserIds(USER_COUNT);
        
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
        AtomicInteger exceptionCount = new AtomicInteger(0);
        
        // 创建并发任务
        for (int i = 0; i < THREAD_COUNT; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    // 每个线程处理一部分用户
                    int start = threadId * (USER_COUNT / THREAD_COUNT);
                    int end = (threadId + 1) * (USER_COUNT / THREAD_COUNT);
                    List<String> threadUserIds = userIds.subList(start, end);
                    
                    // 使用不安全实现
                    Map<String, UserItemStatDTO> result = userItemStatService.getUserItemStatsNew(threadUserIds);
                    
                    logger.info("Thread {} processed {} users", threadId, result.size());
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
        
        // 记录异常数量
        logger.info("Unsafe implementation threw {} exceptions", exceptionCount.get());
        
        // 注意：不安全实现可能会抛出ConcurrentModificationException
        // 但我们不断言异常数量，因为这取决于线程调度和运行环境
    }
    
    /**
     * 测试高并发下的性能和稳定性
     */
    @Test
    public void testHighConcurrency() throws InterruptedException {
        // 准备测试数据
        List<String> userIds = generateTestUserIds(USER_COUNT);
        
        int highThreadCount = 50;
        ExecutorService executor = Executors.newFixedThreadPool(highThreadCount);
        CountDownLatch latch = new CountDownLatch(highThreadCount);
        AtomicInteger safeExceptionCount = new AtomicInteger(0);
        AtomicInteger unsafeExceptionCount = new AtomicInteger(0);
        
        // 记录开始时间
        long startTime = System.currentTimeMillis();
        
        // 创建并发任务
        for (int i = 0; i < highThreadCount; i++) {
            final int threadId = i;
            executor.submit(() -> {
                try {
                    // 每个线程随机选择一些用户
                    List<String> randomUserIds = new ArrayList<>();
                    for (int j = 0; j < 10; j++) {
                        int randomIndex = (threadId * 10 + j) % USER_COUNT;
                        randomUserIds.add(userIds.get(randomIndex));
                    }
                    
                    // 使用安全实现
                    userItemStatService.getUserItemStats(randomUserIds);
                } catch (Exception e) {
                    safeExceptionCount.incrementAndGet();
                }
                
                try {
                    // 每个线程随机选择一些用户
                    List<String> randomUserIds = new ArrayList<>();
                    for (int j = 0; j < 10; j++) {
                        int randomIndex = (threadId * 10 + j) % USER_COUNT;
                        randomUserIds.add(userIds.get(randomIndex));
                    }
                    
                    // 使用不安全实现
                    userItemStatService.getUserItemStatsNew(randomUserIds);
                } catch (Exception e) {
                    unsafeExceptionCount.incrementAndGet();
                }
                
                latch.countDown();
            });
        }
        
        // 等待所有任务完成
        latch.await();
        executor.shutdown();
        
        // 记录结束时间
        long endTime = System.currentTimeMillis();
        
        // 记录结果
        logger.info("High concurrency test completed in {} ms", (endTime - startTime));
        logger.info("Safe implementation threw {} exceptions", safeExceptionCount.get());
        logger.info("Unsafe implementation threw {} exceptions", unsafeExceptionCount.get());
        
        // 安全实现应该没有异常
        assertEquals(0, safeExceptionCount.get(), "Safe implementation should not throw exceptions");
        
        // 不安全实现可能会抛出异常
        if (unsafeExceptionCount.get() > 0) {
            logger.warn("Unsafe implementation threw {} exceptions as expected", unsafeExceptionCount.get());
        }
    }
    
    /**
     * 生成测试用户ID列表
     */
    private List<String> generateTestUserIds(int count) {
        List<String> userIds = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            userIds.add("test-user-" + i);
        }
        return userIds;
    }
} 