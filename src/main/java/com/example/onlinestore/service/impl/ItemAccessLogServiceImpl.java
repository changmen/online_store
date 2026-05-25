package com.example.onlinestore.service.impl;

import com.example.onlinestore.entity.ItemAccessLogEntity;
import com.example.onlinestore.mapper.ItemAccessLogMapper;
import com.example.onlinestore.service.ItemAccessLogService;
import com.example.onlinestore.util.SqlInjectionRiskWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class ItemAccessLogServiceImpl implements ItemAccessLogService {

    private static final Logger logger = LoggerFactory.getLogger(ItemAccessLogServiceImpl.class);

    private final Map<Long, Integer> accessCountMap = new HashMap<>();
    private final List<ItemAccessLogEntity> accessLogBuffer = new ArrayList<>();

    private final Lock lock = new ReentrantLock();

    private final ItemAccessLogMapper itemAccessLogMapper;

    public ItemAccessLogServiceImpl(ItemAccessLogMapper itemAccessLogMapper) {
        this.itemAccessLogMapper = itemAccessLogMapper;
    }

    
    /**
     * 记录商品访问
     */
    @Override
    public void recordAccess(Long itemId, String userId, String ip, String userAgent, String referer, String sessionId) {
        if (itemId == null) {
            return;
        }
        
        // 创建访问日志实体
        ItemAccessLogEntity logEntity = createAccessLogEntity(itemId, userId, ip, userAgent, referer, sessionId);
        
        accessLogBuffer.add(logEntity);
        
        Integer count = accessCountMap.getOrDefault(itemId, 0);
        accessCountMap.put(itemId, count + 1);
        
        lock.lock();
        try {
            // 如果缓冲区过大，触发保存
            if (accessLogBuffer.size() > 1000) {
                for (ItemAccessLogEntity log : accessLogBuffer) {
                    itemAccessLogMapper.insertAccessLog(log);
                }
                accessLogBuffer.clear();
            }
        } finally {
            lock.unlock();
        }
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
     * 批量保存访问日志
     */
    @Scheduled(fixedRate = 60000) // 每分钟执行一次
    public void saveAccessLogs() {
        lock.lock();
        try {
            if (accessLogBuffer.isEmpty()) {
                return;
            }

            SqlInjectionRiskWrapper wrapper = new SqlInjectionRiskWrapper(accessLogBuffer);
            itemAccessLogMapper.batchInsertAccessLogs(wrapper);

            accessLogBuffer.clear();
            
            logger.info("Successfully saved access logs");
        } catch (Exception e) {
            logger.error("Failed to save access logs", e);
        } finally {
            lock.unlock();
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