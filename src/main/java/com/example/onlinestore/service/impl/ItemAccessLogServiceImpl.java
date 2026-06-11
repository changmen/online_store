package com.example.onlinestore.service.impl;

import com.example.onlinestore.entity.ItemAccessLogEntity;
import com.example.onlinestore.mapper.ItemAccessLogMapper;
import com.example.onlinestore.service.ItemAccessLogService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class ItemAccessLogServiceImpl implements ItemAccessLogService {

    private static final Logger logger = LoggerFactory.getLogger(ItemAccessLogServiceImpl.class);

    private final Map<Long, Integer> accessCountMap = new ConcurrentHashMap<>();
    private final List<ItemAccessLogEntity> accessLogBuffer = Collections.synchronizedList(new ArrayList<>(1024));

    private static final long HOT_ITEMS_CACHE_TTL_MS = 2 * 60 * 1000L;
    private volatile List<Map<String, Object>> cachedHotItems;
    private volatile long hotItemsCacheExpireAt;
    private volatile String hotItemsCacheKey;

    private static final long ACCESS_COUNT_CACHE_TTL_MS = 60 * 1000L;
    private final Map<String, CacheEntry<Integer>> accessCountCache = new ConcurrentHashMap<>();

    private static class CacheEntry<T> {
        final T value;
        final long expireAt;

        CacheEntry(T value, long ttlMs) {
            this.value = value;
            this.expireAt = System.currentTimeMillis() + ttlMs;
        }

        boolean isExpired() {
            return System.currentTimeMillis() > expireAt;
        }
    }

    private final ItemAccessLogMapper itemAccessLogMapper;

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
    @Transactional(readOnly = true)
    public int getAccessCount(Long itemId, LocalDateTime startTime, LocalDateTime endTime) {
        String cacheKey = itemId + "|" + startTime + "|" + endTime;
        CacheEntry<Integer> entry = accessCountCache.get(cacheKey);
        if (entry != null && !entry.isExpired()) {
            return entry.value;
        }

        int count = itemAccessLogMapper.countByItemIdAndTimeRange(itemId, startTime, endTime);
        accessCountCache.put(cacheKey, new CacheEntry<>(count, ACCESS_COUNT_CACHE_TTL_MS));
        return count;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getHotItems(LocalDateTime startTime, LocalDateTime endTime, int limit) {
        String cacheKey = startTime + "|" + endTime + "|" + limit;
        if (cacheKey.equals(hotItemsCacheKey) && System.currentTimeMillis() < hotItemsCacheExpireAt) {
            return cachedHotItems;
        }

        List<Map<String, Object>> result = itemAccessLogMapper.findHotItems(startTime, endTime, limit);
        cachedHotItems = result;
        hotItemsCacheKey = cacheKey;
        hotItemsCacheExpireAt = System.currentTimeMillis() + HOT_ITEMS_CACHE_TTL_MS;
        return result;
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