package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.Item;
import com.example.onlinestore.bean.ItemDetail;
import com.example.onlinestore.bean.Sku;
import com.example.onlinestore.errors.ErrorCode;
import com.example.onlinestore.exceptions.BizException;
import com.example.onlinestore.service.ItemService;
import com.example.onlinestore.service.SkuService;
import com.example.onlinestore.utils.JacksonJsonUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemDetailServiceImplTest {

    @Mock
    private ItemService itemService;

    @Mock
    private SkuService skuService;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    private ItemDetailServiceImpl itemDetailService;

    @BeforeEach
    void setUp() {
        itemDetailService = new ItemDetailServiceImpl(itemService, skuService, redisTemplate);
    }

    @Test
    void getItemDetail_nullItemId_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> itemDetailService.getItemDetail(null));
    }

    @Test
    void getItemDetail_cacheHit_returnsFromCache() throws Exception {
        Item item = new Item();
        item.setId(1L);
        item.setName("Test Item");

        Sku sku = new Sku();
        sku.setId(10L);
        sku.setItemId(1L);

        ItemDetail detail = new ItemDetail();
        detail.setItem(item);
        detail.setSkus(List.of(sku));

        String json = JacksonJsonUtils.toString(detail);

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("ITEM_DETAIL:1")).thenReturn(json);

        ItemDetail result = itemDetailService.getItemDetail(1L);

        assertNotNull(result);
        assertEquals(1L, result.getItem().getId());
        assertEquals("Test Item", result.getItem().getName());
        assertEquals(1, result.getSkus().size());
        verify(itemService, never()).getItemById(anyLong());
    }

    @Test
    void getItemDetail_nullCacheValue_throwsBizException() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("ITEM_DETAIL:999")).thenReturn("NULL");

        BizException ex = assertThrows(BizException.class,
                () -> itemDetailService.getItemDetail(999L));
        assertEquals(ErrorCode.ITEM_NOT_FOUND, ex.getErrorCode());
        verify(itemService, never()).getItemById(anyLong());
    }

    @Test
    void getItemDetail_cacheMiss_loadsFromDbAndCaches() throws Exception {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("ITEM_DETAIL:1")).thenReturn(null);

        Item item = new Item();
        item.setId(1L);
        item.setName("DB Item");

        Sku sku = new Sku();
        sku.setId(20L);
        sku.setItemId(1L);

        when(itemService.getItemById(1L)).thenReturn(item);
        when(skuService.getSkusByItemId(1L)).thenReturn(Collections.singletonList(sku));

        ItemDetail result = itemDetailService.getItemDetail(1L);

        assertNotNull(result);
        assertEquals(1L, result.getItem().getId());
        assertEquals("DB Item", result.getItem().getName());
        assertEquals(1, result.getSkus().size());
        assertEquals(20L, result.getSkus().get(0).getId());

        verify(valueOperations).set(eq("ITEM_DETAIL:1"), anyString(), eq(30L), eq(TimeUnit.MINUTES));
    }

    @Test
    void getItemDetail_itemNotFoundInDb_cachesNullValue() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("ITEM_DETAIL:888")).thenReturn(null);
        when(itemService.getItemById(888L)).thenThrow(new BizException(ErrorCode.ITEM_NOT_FOUND));

        assertThrows(BizException.class, () -> itemDetailService.getItemDetail(888L));

        verify(valueOperations).set(eq("ITEM_DETAIL:888"), eq("NULL"), eq(5L), eq(TimeUnit.MINUTES));
    }

    @Test
    void getItemDetail_corruptedCache_deletesAndReloads() throws Exception {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("ITEM_DETAIL:1"))
                .thenReturn("{invalid json!!!")
                .thenReturn(null);

        Item item = new Item();
        item.setId(1L);
        item.setName("Reloaded Item");

        when(itemService.getItemById(1L)).thenReturn(item);
        when(skuService.getSkusByItemId(1L)).thenReturn(Collections.emptyList());

        ItemDetail result = itemDetailService.getItemDetail(1L);

        assertNotNull(result);
        assertEquals("Reloaded Item", result.getItem().getName());
        verify(redisTemplate).delete("ITEM_DETAIL:1");
    }
}
