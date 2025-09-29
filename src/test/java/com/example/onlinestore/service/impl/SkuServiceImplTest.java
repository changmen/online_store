package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.Attribute;
import com.example.onlinestore.bean.AttributeValue;
import com.example.onlinestore.bean.Item;
import com.example.onlinestore.bean.Sku;
import com.example.onlinestore.dto.CreateSkuRequest;
import com.example.onlinestore.dto.ItemAttributeRequest;
import com.example.onlinestore.entity.ItemAttributeRelationEntity;
import com.example.onlinestore.entity.SkuEntity;
import com.example.onlinestore.enums.AttributeInputType;
import com.example.onlinestore.enums.AttributeType;
import com.example.onlinestore.errors.ErrorCode;
import com.example.onlinestore.exceptions.BizException;
import com.example.onlinestore.mapper.ItemAttributeRelationMapper;
import com.example.onlinestore.mapper.SkuMapper;
import com.example.onlinestore.service.AttributeService;
import com.example.onlinestore.service.ItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SkuServiceImplTest {

    @Mock
    private ItemService itemService;

    @Mock
    private SkuMapper skuMapper;

    @Mock
    private AttributeService attributeService;

    @Mock
    private ItemAttributeRelationMapper itemAttributeRelationMapper;

    @InjectMocks
    private SkuServiceImpl skuService;

    private CreateSkuRequest createSkuRequest;
    private SkuEntity skuEntity;
    private Attribute attribute;
    private AttributeValue attributeValue;
    private ItemAttributeRequest itemAttributeRequest;
    private Item item;

    @BeforeEach
    void setUp() {
        // 创建SKU请求
        createSkuRequest = new CreateSkuRequest();
        createSkuRequest.setItemId(1L);
        createSkuRequest.setSkuCode("TEST001");
        createSkuRequest.setName("测试SKU");
        createSkuRequest.setDescription("测试SKU描述");
        createSkuRequest.setPrice(new BigDecimal("99.99"));
        createSkuRequest.setStockQuantity(100);
        createSkuRequest.setWarningQuantity(10);
        createSkuRequest.setDefaultSku(1);
        createSkuRequest.setImage("http://example.com/sku.jpg");

        // 商品属性请求
        itemAttributeRequest = new ItemAttributeRequest();
        itemAttributeRequest.setAttributeId(1L);
        itemAttributeRequest.setAttributeValueId(1L);
        itemAttributeRequest.setValue("红色");
        createSkuRequest.setAttributes(Arrays.asList(itemAttributeRequest));

        // SKU实体
        skuEntity = new SkuEntity();
        skuEntity.setId(1L);
        skuEntity.setItemId(1L);
        skuEntity.setSkuCode("TEST001");
        skuEntity.setName("测试SKU");
        skuEntity.setDescription("测试SKU描述");
        skuEntity.setPrice(new BigDecimal("99.99"));
        skuEntity.setStockQuantity(100);
        skuEntity.setWarningQuantity(10);
        skuEntity.setDefaultSku(1);
        skuEntity.setSoldQuantity(0);
        skuEntity.setImage("http://example.com/sku.jpg");
        skuEntity.setCreatedAt(LocalDateTime.now());
        skuEntity.setUpdatedAt(LocalDateTime.now());

        // 属性对象
        attribute = new Attribute();
        attribute.setId(1L);
        attribute.setName("颜色");
        attribute.setAttributeType(AttributeType.SKU);
        attribute.setInputType(AttributeInputType.SINGLE_SELECT);
        attribute.setRequired(1);

        // 属性值对象
        attributeValue = new AttributeValue();
        attributeValue.setId(1L);
        attributeValue.setAttributeId(1L);
        attributeValue.setValue("红色");
        attributeValue.setSortScore(100);

        // 商品对象
        item = new Item();
        item.setId(1L);
        item.setName("测试商品");
    }

    @Test
    void testCreateSku_Success() {
        // Given
        when(itemService.getItemById(1L)).thenReturn(item);
        when(skuMapper.findBySkuCode("TEST001")).thenReturn(null);
        when(attributeService.getAttributeById(1L)).thenReturn(attribute);
        when(attributeService.getAttributeValueById(1L)).thenReturn(attributeValue);
        when(skuMapper.insert(any(SkuEntity.class))).thenReturn(1);
        when(itemAttributeRelationMapper.findByItemIdAndSkuId(1L, anyLong())).thenReturn(Collections.emptyList());
        when(itemAttributeRelationMapper.batchInsert(anyList())).thenReturn(1);

        // When
        Sku result = skuService.createSku(createSkuRequest);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getItemId());
        assertEquals("TEST001", result.getSkuCode());
        assertEquals("测试SKU", result.getName());
        assertEquals(new BigDecimal("99.99"), result.getPrice());
        assertEquals(1, result.getDefaultSku());

        verify(itemService).getItemById(1L);
        verify(skuMapper).findBySkuCode("TEST001");
        verify(attributeService).getAttributeById(1L);
        verify(attributeService).getAttributeValueById(1L);
        verify(skuMapper).insert(any(SkuEntity.class));
        verify(itemAttributeRelationMapper).batchInsert(anyList());
    }

    @Test
    void testCreateSku_SkuCodeExists() {
        // Given
        when(itemService.getItemById(1L)).thenReturn(item);
        when(skuMapper.findBySkuCode("TEST001")).thenReturn(skuEntity);

        // When & Then
        BizException exception = assertThrows(BizException.class, () -> {
            skuService.createSku(createSkuRequest);
        });
        assertEquals(ErrorCode.SKU_CODE_EXISTS, exception.getErrorCode());

        verify(itemService).getItemById(1L);
        verify(skuMapper).findBySkuCode("TEST001");
        verify(skuMapper, never()).insert(any(SkuEntity.class));
    }

    @Test
    void testCreateSku_WarningQuantityExceedsStock() {
        // Given
        createSkuRequest.setWarningQuantity(150); // 大于库存数量100
        when(itemService.getItemById(1L)).thenReturn(item);
        when(skuMapper.findBySkuCode("TEST001")).thenReturn(null);

        // When & Then
        BizException exception = assertThrows(BizException.class, () -> {
            skuService.createSku(createSkuRequest);
        });
        assertEquals(ErrorCode.SKU_WARNING_QUANTITY_EXCEEDS_STOCK_QUANTITY, exception.getErrorCode());

        verify(itemService).getItemById(1L);
        verify(skuMapper).findBySkuCode("TEST001");
        verify(skuMapper, never()).insert(any(SkuEntity.class));
    }

    @Test
    void testCreateSku_AttributeTypeNotSku() {
        // Given
        attribute.setAttributeType(AttributeType.SALE); // 不是SKU类型
        when(itemService.getItemById(1L)).thenReturn(item);
        when(skuMapper.findBySkuCode("TEST001")).thenReturn(null);
        when(attributeService.getAttributeById(1L)).thenReturn(attribute);

        // When & Then
        BizException exception = assertThrows(BizException.class, () -> {
            skuService.createSku(createSkuRequest);
        });
        assertEquals(ErrorCode.ATTRIBUTE_TYPE_NOT_SKU, exception.getErrorCode());

        verify(attributeService).getAttributeById(1L);
        verify(skuMapper, never()).insert(any(SkuEntity.class));
    }

    @Test
    void testCreateSku_AttributeInputTypeInvalid() {
        // Given
        attribute.setInputType(AttributeInputType.INPUT); // 不是选择类型
        when(itemService.getItemById(1L)).thenReturn(item);
        when(skuMapper.findBySkuCode("TEST001")).thenReturn(null);
        when(attributeService.getAttributeById(1L)).thenReturn(attribute);

        // When & Then
        BizException exception = assertThrows(BizException.class, () -> {
            skuService.createSku(createSkuRequest);
        });
        assertEquals(ErrorCode.SKU_ATTRIBUTE_INPUT_TYPE_INVALID, exception.getErrorCode());

        verify(attributeService).getAttributeById(1L);
        verify(skuMapper, never()).insert(any(SkuEntity.class));
    }

    @Test
    void testCreateSku_AttributeValueEmpty() {
        // Given
        itemAttributeRequest.setAttributeValueId(null);
        when(itemService.getItemById(1L)).thenReturn(item);
        when(skuMapper.findBySkuCode("TEST001")).thenReturn(null);
        when(attributeService.getAttributeById(1L)).thenReturn(attribute);

        // When & Then
        BizException exception = assertThrows(BizException.class, () -> {
            skuService.createSku(createSkuRequest);
        });
        assertEquals(ErrorCode.SKU_ATTRIBUTE_VALUE_EMPTY, exception.getErrorCode());

        verify(attributeService).getAttributeById(1L);
        verify(skuMapper, never()).insert(any(SkuEntity.class));
    }

    @Test
    void testCreateSku_InsertFailed() {
        // Given
        when(itemService.getItemById(1L)).thenReturn(item);
        when(skuMapper.findBySkuCode("TEST001")).thenReturn(null);
        when(attributeService.getAttributeById(1L)).thenReturn(attribute);
        when(attributeService.getAttributeValueById(1L)).thenReturn(attributeValue);
        when(skuMapper.insert(any(SkuEntity.class))).thenReturn(0); // 插入失败

        // When & Then
        BizException exception = assertThrows(BizException.class, () -> {
            skuService.createSku(createSkuRequest);
        });
        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());

        verify(skuMapper).insert(any(SkuEntity.class));
        verify(itemAttributeRelationMapper, never()).batchInsert(anyList());
    }

    @Test
    void testGetSkusByItemId_Success() {
        // Given
        List<SkuEntity> skuEntities = Arrays.asList(skuEntity);
        when(skuMapper.findByItemId(1L)).thenReturn(skuEntities);
        when(itemAttributeRelationMapper.findByItemIdAndSkuId(1L, 1L)).thenReturn(Collections.emptyList());

        // When
        List<Sku> result = skuService.getSkusByItemId(1L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("TEST001", result.get(0).getSkuCode());
        assertEquals("测试SKU", result.get(0).getName());

        verify(skuMapper).findByItemId(1L);
    }

    @Test
    void testGetSkusByItemId_EmptyResult() {
        // Given
        when(skuMapper.findByItemId(1L)).thenReturn(Collections.emptyList());

        // When
        List<Sku> result = skuService.getSkusByItemId(1L);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(skuMapper).findByItemId(1L);
    }

    @Test
    void testUpdateStockQuantity_Success() {
        // Given
        when(skuMapper.findById(1L)).thenReturn(skuEntity);
        when(itemAttributeRelationMapper.findByItemIdAndSkuId(1L, 1L)).thenReturn(Collections.emptyList());
        when(skuMapper.updateStockQuantity(1L, 150)).thenReturn(1);

        // When
        skuService.updateStockQuantity(1L, 150);

        // Then
        verify(skuMapper).findById(1L);
        verify(skuMapper).updateStockQuantity(1L, 150);
    }

    @Test
    void testUpdateStockQuantity_WarningQuantityExceedsStock() {
        // Given
        when(skuMapper.findById(1L)).thenReturn(skuEntity);
        when(itemAttributeRelationMapper.findByItemIdAndSkuId(1L, 1L)).thenReturn(Collections.emptyList());

        // When & Then
        BizException exception = assertThrows(BizException.class, () -> {
            skuService.updateStockQuantity(1L, 5); // 小于预警数量10
        });
        assertEquals(ErrorCode.SKU_WARNING_QUANTITY_EXCEEDS_STOCK_QUANTITY, exception.getErrorCode());

        verify(skuMapper).findById(1L);
        verify(skuMapper, never()).updateStockQuantity(anyLong(), anyInt());
    }

    @Test
    void testUpdateStockQuantity_UpdateFailed() {
        // Given
        when(skuMapper.findById(1L)).thenReturn(skuEntity);
        when(itemAttributeRelationMapper.findByItemIdAndSkuId(1L, 1L)).thenReturn(Collections.emptyList());
        when(skuMapper.updateStockQuantity(1L, 150)).thenReturn(0); // 更新失败

        // When & Then
        BizException exception = assertThrows(BizException.class, () -> {
            skuService.updateStockQuantity(1L, 150);
        });
        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());

        verify(skuMapper).updateStockQuantity(1L, 150);
    }

    @Test
    void testGetSkuById_Success() {
        // Given
        when(skuMapper.findById(1L)).thenReturn(skuEntity);
        when(itemAttributeRelationMapper.findByItemIdAndSkuId(1L, 1L)).thenReturn(Collections.emptyList());

        // When
        Sku result = skuService.getSkuById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("TEST001", result.getSkuCode());
        assertEquals("测试SKU", result.getName());
        assertEquals(new BigDecimal("99.99"), result.getPrice());

        verify(skuMapper).findById(1L);
    }

    @Test
    void testGetSkuById_NotFound() {
        // Given
        when(skuMapper.findById(1L)).thenReturn(null);

        // When & Then
        BizException exception = assertThrows(BizException.class, () -> {
            skuService.getSkuById(1L);
        });
        assertEquals(ErrorCode.SKU_NOT_FOUND, exception.getErrorCode());

        verify(skuMapper).findById(1L);
    }
}