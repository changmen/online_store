package com.example.onlinestore.service.impl;

import com.example.onlinestore.entity.ItemAccessLogEntity;
import com.example.onlinestore.mapper.ItemAccessLogMapper;
import com.example.onlinestore.service.ItemAccessLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 商品访问日志服务实现类
 */
@Service
public class ItemAccessLogServiceImpl implements ItemAccessLogService {

    private static final Logger logger = LoggerFactory.getLogger(ItemAccessLogServiceImpl.class);
    
    // 安全实现：使用线程安全的集合类
    private final CopyOnWriteArrayList<ItemAccessLogEntity> accessLogBuffer = new CopyOnWriteArrayList<>();
    
    // 安全实现：使用ConcurrentHashMap存储商品访问计数
    private final ConcurrentHashMap<Long, Integer> itemAccessCountMap = new ConcurrentHashMap<>();
    
    // 不良案例：使用普通HashMap和ArrayList作为共享变量，但未正确加锁
    private final Map<Long, Integer> badCaseAccessCountMap = new HashMap<>();
    private final List<ItemAccessLogEntity> badCaseAccessLogBuffer = new ArrayList<>();
    
    // 不良案例：使用单个锁保护多个共享资源，可能导致不必要的竞争
    private final Lock badCaseLock = new ReentrantLock();
    
    @Autowired
    private ItemAccessLogMapper itemAccessLogMapper;
    
    /**
     * 记录商品访问（安全实现）
     */
    @Override
    public void recordAccess(Long itemId, String userId, String ip, String userAgent, String referer, String sessionId) {
        if (itemId == null) {
            return;
        }
        
        // 创建访问日志实体
        ItemAccessLogEntity logEntity = createAccessLogEntity(itemId, userId, ip, userAgent, referer, sessionId);
        
        // 添加到缓冲区
        accessLogBuffer.add(logEntity);
        
        // 更新访问计数
        itemAccessCountMap.compute(itemId, (key, oldValue) -> (oldValue == null) ? 1 : oldValue + 1);
        
        // 如果缓冲区过大，触发保存
        if (accessLogBuffer.size() > 1000) {
            saveAccessLogs();
        }
    }
    
    /**
     * 记录商品访问（不良案例 - 共享变量未正确加锁）
     * 
     * BAD CASE: 
     * 1. 使用普通HashMap和ArrayList作为共享变量，但未正确加锁
     * 2. 锁的粒度过大，导致不必要的竞争
     * 3. 在锁内执行耗时操作，增加锁持有时间
     * 4. 未使用线程安全的集合类
     */
    @Override
    public void recordAccessBadCase(Long itemId, String userId, String ip, String userAgent, String referer, String sessionId) {
        if (itemId == null) {
            return;
        }
        
        // 创建访问日志实体
        ItemAccessLogEntity logEntity = createAccessLogEntity(itemId, userId, ip, userAgent, referer, sessionId);
        
        badCaseAccessLogBuffer.add(logEntity);
        
        Integer count = badCaseAccessCountMap.getOrDefault(itemId, 0);
        badCaseAccessCountMap.put(itemId, count + 1);
        
        badCaseLock.lock();
        try {
            // 如果缓冲区过大，触发保存
            if (badCaseAccessLogBuffer.size() > 1000) {
                // BAD CASE: 在锁内执行IO操作
                for (ItemAccessLogEntity log : badCaseAccessLogBuffer) {
                    itemAccessLogMapper.insertAccessLog(log);
                }
                badCaseAccessLogBuffer.clear();
            }
        } finally {
            badCaseLock.unlock();
        }
    }
    
    /**
     * 获取商品访问次数
     */
    @Override
    public int getAccessCount(Long itemId) {
        // 从数据库获取历史访问次数
        int historyCount = itemAccessLogMapper.countByItemId(itemId);
        
        // 加上内存中的计数
        Integer memoryCount = itemAccessCountMap.getOrDefault(itemId, 0);
        
        return historyCount + memoryCount;
    }
    
    /**
     * 获取商品在指定时间范围内的访问次数
     */
    @Override
    public int getAccessCount(Long itemId, LocalDateTime startTime, LocalDateTime endTime) {
        return itemAccessLogMapper.countByItemIdAndTimeRange(itemId, startTime, endTime);
    }
    
    /**
     * 获取热门商品列表
     */
    @Override
    public List<Map<String, Object>> getHotItems(LocalDateTime startTime, LocalDateTime endTime, int limit) {
        return itemAccessLogMapper.findHotItems(startTime, endTime, limit);
    }
    
    /**
     * 批量保存访问日志（安全实现）
     */
    @Override
    @Scheduled(fixedRate = 60000) // 每分钟执行一次
    public void saveAccessLogs() {
        List<ItemAccessLogEntity> logsToSave = new ArrayList<>(accessLogBuffer);
        if (logsToSave.isEmpty()) {
            return;
        }
        
        try {
            // 批量保存日志
            itemAccessLogMapper.batchInsertAccessLogs(logsToSave);
            
            // 清空缓冲区
            accessLogBuffer.removeAll(logsToSave);
            
            logger.info("Successfully saved {} access logs", logsToSave.size());
        } catch (Exception e) {
            logger.error("Failed to save access logs", e);
        }
    }
    
    /**
     * 批量保存访问日志（不良案例）
     */
    @Scheduled(fixedRate = 60000) // 每分钟执行一次
    public void saveAccessLogsBadCase() {
        // BAD CASE: 锁的粒度过大，整个方法都在锁内执行
        badCaseLock.lock();
        try {
            if (badCaseAccessLogBuffer.isEmpty()) {
                return;
            }
            
            // BAD CASE: 在锁内执行IO操作，增加锁持有时间
            itemAccessLogMapper.batchInsertAccessLogs(badCaseAccessLogBuffer);
            
            badCaseAccessLogBuffer.clear();
            
            logger.info("Successfully saved access logs (bad case)");
        } catch (Exception e) {
            logger.error("Failed to save access logs (bad case)", e);
        } finally {
            badCaseLock.unlock();
        }
    }
    
    /**
     * 创建访问日志实体
     */
    private ItemAccessLogEntity createAccessLogEntity(Long itemId, String userId, String ip, String userAgent, String referer, String sessionId) {
        ItemAccessLogEntity logEntity = new ItemAccessLogEntity();
        logEntity.setItemId(itemId);
        logEntity.setUserId(userId);
        logEntity.setIp(ip);
        logEntity.setUserAgent(userAgent);
        logEntity.setReferer(referer);
        logEntity.setSessionId(sessionId);
        logEntity.setAccessTime(LocalDateTime.now());
        logEntity.setAccessCount(1);
        return logEntity;
    }
} 