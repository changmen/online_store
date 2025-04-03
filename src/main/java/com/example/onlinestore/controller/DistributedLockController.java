package com.example.onlinestore.controller;

import com.example.onlinestore.dto.Response;
import com.example.onlinestore.service.DistributedLockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁示例控制器
 */
@RestController
@RequestMapping("/api/lock")
@Deprecated
public class DistributedLockController {

    private static final Logger logger = LoggerFactory.getLogger(DistributedLockController.class);
    
    @Autowired
    private DistributedLockService distributedLockService;
    
    /**
     * 模拟需要加锁的业务操作
     * 
     * @param resourceId 资源ID
     * @return 操作结果
     */
    @PostMapping("/resource/{resourceId}")
    public Response<Map<String, Object>> processResource(@PathVariable("resourceId") String resourceId) {
        String lockKey = "resource_lock:" + resourceId;
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 使用分布式锁执行操作
            String operationResult = distributedLockService.executeWithLock(
                lockKey, 
                10, 
                TimeUnit.SECONDS, 
                () -> {
                    // 模拟耗时操作
                    logger.info("Processing resource: {}", resourceId);
                    Thread.sleep(2000);
                    return "Resource processed successfully";
                }
            );
            
            result.put("success", true);
            result.put("message", operationResult);
            return Response.success(result);
            
        } catch (Exception e) {
            logger.error("Error processing resource: {}", resourceId, e);
            result.put("success", false);
            result.put("message", "Failed to process resource: " + e.getMessage());
            return Response.fail(e.getMessage());
        }
    }
    
    /**
     * 模拟需要加锁的业务操作（带重试机制）
     * 
     * @param resourceId 资源ID
     * @return 操作结果
     */
    @PostMapping("/resource/{resourceId}/retry")
    public Response<Map<String, Object>> processResourceWithRetry(@PathVariable String resourceId) {
        String lockKey = "resource_lock:" + resourceId;
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 使用分布式锁执行操作（带重试机制）
            String operationResult = distributedLockService.executeWithLockWithRetry(
                lockKey, 
                10, 
                TimeUnit.SECONDS,
                3,  // 重试3次
                500, // 每次重试间隔500毫秒
                () -> {
                    // 模拟耗时操作
                    logger.info("Processing resource with retry: {}", resourceId);
                    Thread.sleep(2000);
                    return "Resource processed successfully with retry";
                }
            );
            
            result.put("success", true);
            result.put("message", operationResult);
            return Response.success(result);
            
        } catch (Exception e) {
            logger.error("Error processing resource with retry: {}", resourceId, e);
            result.put("success", false);
            result.put("message", "Failed to process resource with retry: " + e.getMessage());
            return Response.fail(e.getMessage());
        }
    }
} 