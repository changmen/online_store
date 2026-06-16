package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.Member;
import com.example.onlinestore.entity.ItemAccessLogEntity;
import com.example.onlinestore.mapper.ItemAccessLogMapper;
import com.example.onlinestore.security.CustomUserDetails;
import com.example.onlinestore.service.ItemAccessLogService;
import com.example.onlinestore.utils.WebUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
    private final Object hotItemsLock = new Object();
    private List<Map<String, Object>> cachedHotItems;
    private long hotItemsCacheExpireAt;
    private String hotItemsCacheKey;

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
    public void recordItemDetailAccess(Long itemId, String itemName, HttpServletRequest request) {
        String ip = WebUtils.getClientIp(request);
        String userAgent = request.getHeader("User-Agent");
        String referer = request.getHeader("Referer");
        HttpSession session = request.getSession(false);
        String sessionId = session != null ? session.getId() : "";

        String memberId = "";
        String memberName = "";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)
                && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            Member member = userDetails.getMember();
            memberId = String.valueOf(member.getId());
            memberName = member.getBaseInfo().getName();
        }

        recordAccess(itemId, itemName, memberId, memberName, ip, userAgent, referer, sessionId);
    }

    @Override
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
    public List<Map<String, Object>> getHotItems(LocalDateTime startTime, LocalDateTime endTime, int limit) {
        String cacheKey = startTime + "|" + endTime + "|" + limit;
        synchronized (hotItemsLock) {
            if (cacheKey.equals(hotItemsCacheKey) && System.currentTimeMillis() < hotItemsCacheExpireAt) {
                return cachedHotItems;
            }
        }

        List<Map<String, Object>> result = itemAccessLogMapper.findHotItems(startTime, endTime, limit);
        synchronized (hotItemsLock) {
            cachedHotItems = result;
            hotItemsCacheKey = cacheKey;
            hotItemsCacheExpireAt = System.currentTimeMillis() + HOT_ITEMS_CACHE_TTL_MS;
        }
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