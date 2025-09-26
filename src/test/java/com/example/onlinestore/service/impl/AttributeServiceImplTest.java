package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.Attribute;
import com.example.onlinestore.bean.AttributeValue;
import com.example.onlinestore.dto.CreateAttributeRequest;
import com.example.onlinestore.dto.ItemAttributeRequest;
import com.example.onlinestore.dto.UpdateAttributeRequest;
import com.example.onlinestore.entity.AttributeEntity;
import com.example.onlinestore.entity.AttributeValueEntity;
import com.example.onlinestore.entity.ItemAttributeRelationEntity;
import com.example.onlinestore.enums.AttributeInputType;
import com.example.onlinestore.enums.AttributeType;
import com.example.onlinestore.errors.ErrorCode;
import com.example.onlinestore.exceptions.BizException;
import com.example.onlinestore.mapper.AttributeMapper;
import com.example.onlinestore.mapper.AttributeValueMapper;
import com.example.onlinestore.mapper.ItemAttributeRelationMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AttributeServiceImplTest {

    @Mock
    private AttributeMapper attributeMapper;

    @Mock
    private AttributeValueMapper attributeValueMapper;

    @Mock
    private ItemAttributeRelationMapper itemAttributeRelationMapper;

    @InjectMocks
    private AttributeServiceImpl attributeService;

    private CreateAttributeRequest createRequest;
    private UpdateAttributeRequest updateRequest;
    private AttributeEntity attributeEntity;
    private AttributeValueEntity attributeValueEntity;
    private ItemAttributeRequest itemAttributeRequest;

    @BeforeEach
    void setUp() {
        // 创建属性请求
        createRequest = new CreateAttributeRequest();
        createRequest.setName("测试属性");
        createRequest.setAttributeType("SKU");
        createRequest.setInputType("SINGLE_SELECT");
        createRequest.setRequired(1);
        createRequest.setSearchable(1);
        createRequest.setSortScore(100);
        createRequest.setVisible(1);

        // 更新属性请求
        updateRequest = new UpdateAttributeRequest();
        updateRequest.setName("更新后的属性");
        updateRequest.setAttributeType("SALE");
        updateRequest.setInputType("MULTI_SELECT");
        updateRequest.setRequired(0);
        updateRequest.setSearchable(0);
        updateRequest.setSortScore(200);
        updateRequest.setVisible(0);

        // 属性实体
        attributeEntity = new AttributeEntity();
        attributeEntity.setId(1L);
        attributeEntity.setName("测试属性");
        attributeEntity.setAttributeType("SKU");
        attributeEntity.setInputType("SINGLE_SELECT");
        attributeEntity.setRequired(1);
        attributeEntity.setSearchable(1);
        attributeEntity.setSortScore(100);
        attributeEntity.setVisible(1);
        attributeEntity.setCreatedAt(LocalDateTime.now());
        attributeEntity.setUpdatedAt(LocalDateTime.now());

        // 属性值实体
        attributeValueEntity = new AttributeValueEntity();
        attributeValueEntity.setId(1L);
        attributeValueEntity.setAttributeId(1L);
        attributeValueEntity.setValue("测试值");
        attributeValueEntity.setSortScore(100);

        // 商品属性请求
        itemAttributeRequest = new ItemAttributeRequest();
        itemAttributeRequest.setAttributeId(1L);
        itemAttributeRequest.setAttributeValueId(1L);
        itemAttributeRequest.setValue("测试值");
    }

    @Test
    void testCreateAttribute_Success() {
        // Given
        when(attributeMapper.findByName("测试属性")).thenReturn(null);
        when(attributeMapper.insert(any(AttributeEntity.class))).thenReturn(1);

        // When
        Attribute result = attributeService.createAttribute(createRequest);

        // Then
        assertNotNull(result);
        assertEquals("测试属性", result.getName());
        assertEquals(AttributeType.SKU, result.getAttributeType());
        assertEquals(AttributeInputType.SINGLE_SELECT, result.getInputType());
        assertEquals(1, result.getRequired());
        assertEquals(1, result.getSearchable());
        assertEquals(100, result.getSortScore());
        assertEquals(1, result.getVisible());

        verify(attributeMapper).findByName("测试属性");
        verify(attributeMapper).insert(any(AttributeEntity.class));
    }

    @Test
    void testCreateAttribute_InsertFailed() {
        // Given
        when(attributeMapper.findByName("测试属性")).thenReturn(null);
        when(attributeMapper.insert(any(AttributeEntity.class))).thenReturn(0);

        // When & Then
        BizException exception = assertThrows(BizException.class, () -> {
            attributeService.createAttribute(createRequest);
        });
        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());

        verify(attributeMapper).findByName("测试属性");
        verify(attributeMapper).insert(any(AttributeEntity.class));
    }

    @Test
    void testCreateAttribute_NameDuplicated() {
        // Given
        when(attributeMapper.findByName("测试属性")).thenReturn(attributeEntity);

        // When & Then
        BizException exception = assertThrows(BizException.class, () -> {
            attributeService.createAttribute(createRequest);
        });
        assertEquals(ErrorCode.ATTRIBUTE_NAME_DUPLICATED, exception.getErrorCode());

        verify(attributeMapper).findByName("测试属性");
        verify(attributeMapper, never()).insert(any(AttributeEntity.class));
    }

    @Test
    void testUpdateAttribute_Success() {
        // Given
        when(attributeMapper.findById(1L)).thenReturn(attributeEntity);
        when(attributeMapper.update(any(AttributeEntity.class))).thenReturn(1);

        // When
        attributeService.updateAttribute(1L, updateRequest);

        // Then
        verify(attributeMapper).findById(1L);
        verify(attributeMapper).update(any(AttributeEntity.class));
    }

    @Test
    void testUpdateAttribute_NoChangeNeeded() {
        // Given
        updateRequest.setName("测试属性"); // 与原有名称相同
        updateRequest.setAttributeType("SKU"); // 与原有类型相同
        updateRequest.setInputType("SINGLE_SELECT"); // 与原有输入类型相同 
        updateRequest.setRequired(1); // 与原有值相同
        updateRequest.setSearchable(1); // 与原有值相同
        updateRequest.setSortScore(100); // 与原有值相同
        updateRequest.setVisible(1); // 与原有值相同
        
        when(attributeMapper.findById(1L)).thenReturn(attributeEntity);

        // When
        attributeService.updateAttribute(1L, updateRequest);

        // Then
        verify(attributeMapper).findById(1L);
        verify(attributeMapper, never()).update(any(AttributeEntity.class));
    }

    @Test
    void testUpdateAttribute_UpdateFailed() {
        // Given
        when(attributeMapper.findById(1L)).thenReturn(attributeEntity);
        when(attributeMapper.update(any(AttributeEntity.class))).thenReturn(0);

        // When & Then
        BizException exception = assertThrows(BizException.class, () -> {
            attributeService.updateAttribute(1L, updateRequest);
        });
        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());

        verify(attributeMapper).findById(1L);
        verify(attributeMapper).update(any(AttributeEntity.class));
    }

    @Test
    void testDeleteAttribute_Success() {
        // Given
        when(attributeMapper.findById(1L)).thenReturn(attributeEntity);
        when(itemAttributeRelationMapper.findByAttributeId(1L, 0, 1)).thenReturn(Collections.emptyList());
        when(attributeMapper.deleteById(1L)).thenReturn(1);
        when(attributeValueMapper.countValuesByAttributeId(1L)).thenReturn(2);
        when(attributeValueMapper.deleteByAttributeId(1L)).thenReturn(2);

        // When
        attributeService.deleteAttribute(1L);

        // Then
        verify(attributeMapper).findById(1L);
        verify(itemAttributeRelationMapper).findByAttributeId(1L, 0, 1);
        verify(attributeMapper).deleteById(1L);
        verify(attributeValueMapper).countValuesByAttributeId(1L);
        verify(attributeValueMapper).deleteByAttributeId(1L);
    }

    @Test
    void testDeleteAttribute_SuccessWithNoValues() {
        // Given
        when(attributeMapper.findById(1L)).thenReturn(attributeEntity);
        when(itemAttributeRelationMapper.findByAttributeId(1L, 0, 1)).thenReturn(Collections.emptyList());
        when(attributeMapper.deleteById(1L)).thenReturn(1);
        when(attributeValueMapper.countValuesByAttributeId(1L)).thenReturn(0);

        // When
        attributeService.deleteAttribute(1L);

        // Then
        verify(attributeMapper).findById(1L);
        verify(itemAttributeRelationMapper).findByAttributeId(1L, 0, 1);
        verify(attributeMapper).deleteById(1L);
        verify(attributeValueMapper).countValuesByAttributeId(1L);
        verify(attributeValueMapper, never()).deleteByAttributeId(1L);
    }

    @Test
    void testDeleteAttribute_ReferencedByItem() {
        // Given
        ItemAttributeRelationEntity relationEntity = new ItemAttributeRelationEntity();
        relationEntity.setItemId(100L);
        when(attributeMapper.findById(1L)).thenReturn(attributeEntity);
        when(itemAttributeRelationMapper.findByAttributeId(1L, 0, 1))
            .thenReturn(Arrays.asList(relationEntity));

        // When & Then
        BizException exception = assertThrows(BizException.class, () -> {
            attributeService.deleteAttribute(1L);
        });
        assertEquals(ErrorCode.ATTRIBUTE_IS_REFERENCE_BY_ITEM, exception.getErrorCode());

        verify(attributeMapper).findById(1L);
        verify(itemAttributeRelationMapper).findByAttributeId(1L, 0, 1);
        verify(attributeMapper, never()).deleteById(1L);
    }

    @Test
    void testDeleteAttribute_DeleteFailed() {
        // Given
        when(attributeMapper.findById(1L)).thenReturn(attributeEntity);
        when(itemAttributeRelationMapper.findByAttributeId(1L, 0, 1)).thenReturn(Collections.emptyList());
        when(attributeMapper.deleteById(1L)).thenReturn(0);

        // When & Then
        BizException exception = assertThrows(BizException.class, () -> {
            attributeService.deleteAttribute(1L);
        });
        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());

        verify(attributeMapper).findById(1L);
        verify(itemAttributeRelationMapper).findByAttributeId(1L, 0, 1);
        verify(attributeMapper).deleteById(1L);
    }

    @Test
    void testGetAttributeById_Success() {
        // Given
        when(attributeMapper.findById(1L)).thenReturn(attributeEntity);

        // When
        Attribute result = attributeService.getAttributeById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("测试属性", result.getName());
        assertEquals(AttributeType.SKU, result.getAttributeType());
        assertEquals(AttributeInputType.SINGLE_SELECT, result.getInputType());
        assertEquals(1, result.getRequired());
        assertEquals(1, result.getSearchable());
        assertEquals(100, result.getSortScore());
        assertEquals(1, result.getVisible());

        verify(attributeMapper).findById(1L);
    }

    @Test
    void testGetAttributeById_NotFound() {
        // Given
        when(attributeMapper.findById(1L)).thenReturn(null);

        // When & Then
        BizException exception = assertThrows(BizException.class, () -> {
            attributeService.getAttributeById(1L);
        });
        assertEquals(ErrorCode.ATTRIBUTE_NOT_FOUND, exception.getErrorCode());

        verify(attributeMapper).findById(1L);
    }

    @Test
    void testGetAttributeByIdWithValues_WithSelectType() {
        // Given
        attributeEntity.setInputType("SINGLE_SELECT");
        when(attributeMapper.findById(1L)).thenReturn(attributeEntity);
        when(attributeValueMapper.findAllAttributeValuesByAttributeId(1L))
            .thenReturn(Arrays.asList(attributeValueEntity));

        // When
        Attribute result = attributeService.getAttributeByIdWithValues(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("测试属性", result.getName());
        assertNotNull(result.getValues());
        assertEquals(1, result.getValues().size());
        assertEquals("测试值", result.getValues().get(0).getValue());

        verify(attributeMapper).findById(1L);
        verify(attributeValueMapper).findAllAttributeValuesByAttributeId(1L);
    }

    @Test
    void testGetAttributeByIdWithValues_WithInputType() {
        // Given
        attributeEntity.setInputType("INPUT");
        when(attributeMapper.findById(1L)).thenReturn(attributeEntity);

        // When
        Attribute result = attributeService.getAttributeByIdWithValues(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("测试属性", result.getName());
        assertNull(result.getValues());

        verify(attributeMapper).findById(1L);
        verify(attributeValueMapper, never()).findAllAttributeValuesByAttributeId(1L);
    }

    @Test
    void testFindAllAttributeValuesByAttributeId_WithSelectType() {
        // Given
        attributeEntity.setInputType("SINGLE_SELECT");
        when(attributeMapper.findById(1L)).thenReturn(attributeEntity);
        when(attributeValueMapper.findAllAttributeValuesByAttributeId(1L))
            .thenReturn(Arrays.asList(attributeValueEntity));

        // When
        List<AttributeValue> result = attributeService.findAllAttributeValuesByAttributeId(1L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(1L, result.get(0).getAttributeId());
        assertEquals("测试值", result.get(0).getValue());
        assertEquals(100, result.get(0).getSortScore());

        verify(attributeMapper).findById(1L);
        verify(attributeValueMapper).findAllAttributeValuesByAttributeId(1L);
    }

    @Test
    void testFindAllAttributeValuesByAttributeId_WithInputType() {
        // Given
        attributeEntity.setInputType("INPUT");
        when(attributeMapper.findById(1L)).thenReturn(attributeEntity);

        // When
        List<AttributeValue> result = attributeService.findAllAttributeValuesByAttributeId(1L);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(attributeMapper).findById(1L);
        verify(attributeValueMapper, never()).findAllAttributeValuesByAttributeId(1L);
    }

    @Test
    void testGetAttributeValueById_Success() {
        // Given
        when(attributeValueMapper.findById(1L)).thenReturn(attributeValueEntity);

        // When
        AttributeValue result = attributeService.getAttributeValueById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1L, result.getAttributeId());
        assertEquals("测试值", result.getValue());
        assertEquals(100, result.getSortScore());

        verify(attributeValueMapper).findById(1L);
    }

    @Test
    void testGetAttributeValueById_NotFound() {
        // Given
        when(attributeValueMapper.findById(1L)).thenReturn(null);

        // When & Then
        BizException exception = assertThrows(BizException.class, () -> {
            attributeService.getAttributeValueById(1L);
        });
        assertEquals(ErrorCode.ATTRIBUTE_VALUE_NOT_FOUND, exception.getErrorCode());

        verify(attributeValueMapper).findById(1L);
    }

    @Test
    void testEnsureItemAttributes_NewItem() {
        // Given
        List<ItemAttributeRequest> attributes = Arrays.asList(itemAttributeRequest);
        when(itemAttributeRelationMapper.findByItemIdAndSkuId(100L, 200L))
            .thenReturn(Collections.emptyList());
        when(itemAttributeRelationMapper.batchInsert(anyList())).thenReturn(1);

        // When
        attributeService.ensureItemAttributes(100L, 200L, attributes);

        // Then
        verify(itemAttributeRelationMapper).findByItemIdAndSkuId(100L, 200L);
        verify(itemAttributeRelationMapper).batchInsert(anyList());
        verify(itemAttributeRelationMapper, never()).deleteByItemIdAndAttributeIds(anyLong(), anyList());
    }

    @Test
    void testEnsureItemAttributes_UpdateExisting() {
        // Given
        ItemAttributeRelationEntity existingRelation = new ItemAttributeRelationEntity();
        existingRelation.setAttributeId(2L); // 不同的属性ID
        existingRelation.setItemId(100L);
        existingRelation.setSkuId(200L);
        
        List<ItemAttributeRequest> attributes = Arrays.asList(itemAttributeRequest);
        when(itemAttributeRelationMapper.findByItemIdAndSkuId(100L, 200L))
            .thenReturn(Arrays.asList(existingRelation));
        when(itemAttributeRelationMapper.deleteByItemIdAndAttributeIds(eq(100L), anyList())).thenReturn(1);
        when(itemAttributeRelationMapper.batchInsert(anyList())).thenReturn(1);

        // When
        attributeService.ensureItemAttributes(100L, 200L, attributes);

        // Then
        verify(itemAttributeRelationMapper).findByItemIdAndSkuId(100L, 200L);
        verify(itemAttributeRelationMapper).deleteByItemIdAndAttributeIds(eq(100L), anyList());
        verify(itemAttributeRelationMapper).batchInsert(anyList());
    }

    @Test
    void testEnsureItemAttributes_SameAttribute() {
        // Given
        ItemAttributeRelationEntity existingRelation = new ItemAttributeRelationEntity();
        existingRelation.setAttributeId(1L); // 相同的属性ID
        existingRelation.setItemId(100L);
        existingRelation.setSkuId(200L);
        
        List<ItemAttributeRequest> attributes = Arrays.asList(itemAttributeRequest);
        when(itemAttributeRelationMapper.findByItemIdAndSkuId(100L, 200L))
            .thenReturn(Arrays.asList(existingRelation));
        when(itemAttributeRelationMapper.deleteByItemIdAndAttributeIds(eq(100L), anyList())).thenReturn(1);

        // When
        attributeService.ensureItemAttributes(100L, 200L, attributes);

        // Then
        verify(itemAttributeRelationMapper).findByItemIdAndSkuId(100L, 200L);
        verify(itemAttributeRelationMapper).deleteByItemIdAndAttributeIds(eq(100L), anyList());
        verify(itemAttributeRelationMapper, never()).batchInsert(anyList());
    }

    @Test
    void testEnsureItemAttributes_BatchInsertFailed() {
        // Given
        List<ItemAttributeRequest> attributes = Arrays.asList(itemAttributeRequest);
        when(itemAttributeRelationMapper.findByItemIdAndSkuId(100L, 200L))
            .thenReturn(Collections.emptyList());
        when(itemAttributeRelationMapper.batchInsert(anyList())).thenReturn(0);

        // When & Then
        BizException exception = assertThrows(BizException.class, () -> {
            attributeService.ensureItemAttributes(100L, 200L, attributes);
        });
        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());

        verify(itemAttributeRelationMapper).findByItemIdAndSkuId(100L, 200L);
        verify(itemAttributeRelationMapper).batchInsert(anyList());
    }

    @Test
    void testEnsureItemAttributes_DeleteFailed() {
        // Given
        ItemAttributeRelationEntity existingRelation = new ItemAttributeRelationEntity();
        existingRelation.setAttributeId(2L);
        existingRelation.setItemId(100L);
        existingRelation.setSkuId(200L);
        
        List<ItemAttributeRequest> attributes = Arrays.asList(itemAttributeRequest);
        when(itemAttributeRelationMapper.findByItemIdAndSkuId(100L, 200L))
            .thenReturn(Arrays.asList(existingRelation));
        when(itemAttributeRelationMapper.deleteByItemIdAndAttributeIds(eq(100L), anyList())).thenReturn(0);

        // When & Then
        BizException exception = assertThrows(BizException.class, () -> {
            attributeService.ensureItemAttributes(100L, 200L, attributes);
        });
        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());

        verify(itemAttributeRelationMapper).findByItemIdAndSkuId(100L, 200L);
        verify(itemAttributeRelationMapper).deleteByItemIdAndAttributeIds(eq(100L), anyList());
        verify(itemAttributeRelationMapper, never()).batchInsert(anyList());
    }
}