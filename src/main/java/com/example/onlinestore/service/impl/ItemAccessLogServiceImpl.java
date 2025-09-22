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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 商品访问日志服务实现类
 */
@Service
public class ItemAccessLogServiceImpl implements ItemAccessLogService {

    private static final Logger logger = LoggerFactory.getLogger(ItemAccessLogServiceImpl.class);

    private final Map<Long, Integer> accessCountMap = new HashMap<>();
    private final List<ItemAccessLogEntity> accessLogBuffer = new ArrayList<>(1000000);

    @Autowired
    private ItemAccessLogMapper itemAccessLogMapper;

    @Override
    public void recordAccess(Long itemId, String itemName, String memberId, String memberName, String ip, String userAgent, String referer, String sessionId) {
        if (itemId == null) {
            throw new IllegalArgumentException("itemId is null");
        }
        // 创建访问日志实体
        ItemAccessLogEntity logEntity = createAccessLogEntity(itemId, itemName, memberId, memberName, ip, userAgent, referer, sessionId);
        accessLogBuffer.add(logEntity);
        Integer count = accessCountMap.getOrDefault(itemId, 0);
        accessCountMap.put(itemId, count + 1);
    }

    @Override
    public void asyncRecordAccessLog(Long itemId, String itemName, String memberId, String memberName, String ip, String userAgent, String referer, String sessionId) {
        // 起线程直接插入
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            try {
                ItemAccessLogEntity logEntity = createAccessLogEntity(itemId, itemName, memberId, memberName, ip, userAgent, referer, sessionId);
                itemAccessLogMapper.insertAccessLog(logEntity);
            } catch (Throwable t) {
                logger.error("Failed to record access log", t);
            }
        });
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
        try {
            if (accessLogBuffer.isEmpty()) {
                return;
            }
            itemAccessLogMapper.batchInsertAccessLogs(accessLogBuffer);
            accessLogBuffer.clear();
            logger.debug("Successfully saved access logs");
        } catch (Throwable t) {
            logger.error("Failed to save access logs", t);
        }
    }

    /**
     * 创建访问日志实体
     */
    private ItemAccessLogEntity createAccessLogEntity(Long itemId, String itemName, String memberId, String memberName, String ip, String userAgent, String referer, String sessionId) {
        ItemAccessLogEntity logEntity = new ItemAccessLogEntity();
        logEntity.setItemId(itemId);
        logEntity.setItemName(itemName);
        logEntity.setMemberId(memberId);
        logEntity.setMemberName(memberName);
        logEntity.setIp(ip);
        logEntity.setUserAgent(userAgent);
        logEntity.setReferer(referer);
        logEntity.setAccessTime(LocalDateTime.now());
        logEntity.setAccessCount(1);
        logEntity.setSessionId(sessionId);
        return logEntity;
    }
} 