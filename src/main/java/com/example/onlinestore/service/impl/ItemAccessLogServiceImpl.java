package com.example.onlinestore.service.impl;

import com.example.onlinestore.entity.ItemAccessLogEntity;
import com.example.onlinestore.mapper.ItemAccessLogMapper;
import com.example.onlinestore.service.ItemAccessLogService;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class ItemAccessLogServiceImpl implements ItemAccessLogService {

    private static final Logger logger = LoggerFactory.getLogger(ItemAccessLogServiceImpl.class);

    private final Map<Long, Integer> accessCountMap = new ConcurrentHashMap<>();
    private final List<ItemAccessLogEntity> accessLogBuffer = Collections.synchronizedList(new ArrayList<>(1024));
    private final ExecutorService asyncExecutor = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "access-log-async-writer");
        t.setDaemon(true);
        return t;
    });

    @Autowired
    private ItemAccessLogMapper itemAccessLogMapper;

    @PreDestroy
    public void shutdown() {
        asyncExecutor.shutdown();
    }

    @Override
    public void recordAccess(Long itemId, String itemName, String memberId, String memberName, String ip, String userAgent, String referer, String sessionId) {
        if (itemId == null) {
            throw new IllegalArgumentException("itemId is null");
        }
        ItemAccessLogEntity logEntity = createAccessLogEntity(itemId, itemName, memberId, memberName, ip, userAgent, referer, sessionId);
        accessLogBuffer.add(logEntity);
        accessCountMap.merge(itemId, 1, Integer::sum);
    }

    @Override
    public void asyncRecordAccessLog(Long itemId, String itemName, String memberId, String memberName, String ip, String userAgent, String referer, String sessionId) {
        asyncExecutor.execute(() -> {
            try {
                ItemAccessLogEntity logEntity = createAccessLogEntity(itemId, itemName, memberId, memberName, ip, userAgent, referer, sessionId);
                itemAccessLogMapper.insertAccessLog(logEntity);
            } catch (Throwable t) {
                logger.error("Failed to record access log", t);
            }
        });
    }

    @Override
    public int getAccessCount(Long itemId, LocalDateTime startTime, LocalDateTime endTime) {
        return itemAccessLogMapper.countByItemIdAndTimeRange(itemId, startTime, endTime);
    }

    @Override
    public List<Map<String, Object>> getHotItems(LocalDateTime startTime, LocalDateTime endTime, int limit) {
        return itemAccessLogMapper.findHotItems(startTime, endTime, limit);
    }

    @Scheduled(fixedRate = 60000)
    public void saveAccessLogs() {
        try {
            if (accessLogBuffer.isEmpty()) {
                return;
            }
            List<ItemAccessLogEntity> snapshot;
            synchronized (accessLogBuffer) {
                snapshot = new ArrayList<>(accessLogBuffer);
                accessLogBuffer.clear();
            }
            itemAccessLogMapper.batchInsertAccessLogs(snapshot);
            logger.debug("Successfully saved {} access logs", snapshot.size());
        } catch (Throwable t) {
            logger.error("Failed to save access logs", t);
        }
    }

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