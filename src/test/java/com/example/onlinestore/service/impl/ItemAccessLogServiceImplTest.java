package com.example.onlinestore.service.impl;

import com.example.onlinestore.entity.ItemAccessLogEntity;
import com.example.onlinestore.mapper.ItemAccessLogMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemAccessLogServiceImplTest {

    @Mock
    private ItemAccessLogMapper itemAccessLogMapper;

    private Cache<String, Integer> accessCountQueryCache;
    private Cache<String, List<Map<String, Object>>> hotItemsCache;

    private ItemAccessLogServiceImpl itemAccessLogService;

    @BeforeEach
    void setUp() {
        accessCountQueryCache = Caffeine.newBuilder().build();
        hotItemsCache = Caffeine.newBuilder().build();
        itemAccessLogService = new ItemAccessLogServiceImpl(accessCountQueryCache, hotItemsCache, itemAccessLogMapper);
    }

    @Test
    void recordAccess_withNullItemId_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> itemAccessLogService.recordAccess(null, "item", "m1", "name", "127.0.0.1", "ua", "ref", "sid"));
    }

    @Test
    void recordAccess_addsToBuffer() {
        itemAccessLogService.recordAccess(1L, "商品A", "m1", "会员1", "127.0.0.1", "ua", "ref", "sid");
        itemAccessLogService.recordAccess(1L, "商品A", "m1", "会员1", "127.0.0.1", "ua", "ref", "sid");

        doAnswer(invocation -> {
            List<ItemAccessLogEntity> logs = invocation.getArgument(0);
            assertEquals(2, logs.size());
            return null;
        }).when(itemAccessLogMapper).batchInsertAccessLogs(anyList());

        itemAccessLogService.saveAccessLogs();

        verify(itemAccessLogMapper).batchInsertAccessLogs(anyList());
    }

    @Test
    void saveAccessLogs_emptyBuffer_skipsInsert() {
        itemAccessLogService.saveAccessLogs();

        verify(itemAccessLogMapper, never()).batchInsertAccessLogs(anyList());
    }

    @Test
    void saveAccessLogs_clearsBufferAfterSave() {
        itemAccessLogService.recordAccess(1L, "商品A", "m1", "会员1", "127.0.0.1", "ua", "ref", "sid");
        doNothing().when(itemAccessLogMapper).batchInsertAccessLogs(anyList());

        itemAccessLogService.saveAccessLogs();
        itemAccessLogService.saveAccessLogs();

        verify(itemAccessLogMapper, times(1)).batchInsertAccessLogs(anyList());
    }

    @Test
    void recordAccess_concurrentAccess_threadSafe() throws InterruptedException {
        int threadCount = 10;
        int logsPerThread = 100;
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);

        for (int t = 0; t < threadCount; t++) {
            new Thread(() -> {
                try {
                    startLatch.await();
                    for (int i = 0; i < logsPerThread; i++) {
                        itemAccessLogService.recordAccess(1L, "商品A", "m1", "会员1", "127.0.0.1", "ua", "ref", "sid");
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    doneLatch.countDown();
                }
            }).start();
        }

        startLatch.countDown();
        assertTrue(doneLatch.await(10, TimeUnit.SECONDS));

        doAnswer(invocation -> null).when(itemAccessLogMapper).batchInsertAccessLogs(anyList());

        itemAccessLogService.saveAccessLogs();

        verify(itemAccessLogMapper).batchInsertAccessLogs(anyList());
    }
}
