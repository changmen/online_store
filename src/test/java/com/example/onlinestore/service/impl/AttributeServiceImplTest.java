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

    private AttributeEntity mockAttributeEntity;
    private Attribute mockAttribute;
    private CreateAttributeRequest createRequest;
    private UpdateAttributeRequest updateRequest;
    private AttributeValueEntity mockAttributeValueEntity;
    private AttributeValue mockAttributeValue;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        mockAttributeEntity = new AttributeEntity();
        mockAttributeEntity.setId(1L);
        mockAttributeEntity.setName("颜色");
        mockAttributeEntity.setAttributeType("SKU");
        mockAttributeEntity.setInputType("SINGLE_SELECT");
        mockAttributeEntity.setRequired(1);
        mockAttributeEntity.setSearchable(1);
        mockAttributeEntity.setSortScore(100);
        mockAttributeEntity.setVisible(1);
        mockAttributeEntity.setCreatedAt(LocalDateTime.now());
        mockAttributeEntity.setUpdatedAt(LocalDateTime.now());

        mockAttribute = new Attribute();
        mockAttribute.setId(1L);
        mockAttribute.setName("颜色");
        mockAttribute.setAttributeType(AttributeType.SKU);
        mockAttribute.setInputType(AttributeInputType.SINGLE_SELECT);
        mockAttribute.setRequired(1);
        mockAttribute.setSearchable(1);
        mockAttribute.setSortScore(100);
        mockAttribute.setVisible(1);

        createRequest = new CreateAttributeRequest();
        createRequest.setName("尺寸");
        createRequest.setAttributeType("SKU");
        createRequest.setInputType("SINGLE_SELECT");
        createRequest.setRequired(1);
        createRequest.setSearchable(1);
        createRequest.setSortScore(90);
        createRequest.setVisible(1);

        updateRequest = new UpdateAttributeRequest();
        updateRequest.setName("新颜色");
        updateRequest.setAttributeType("SALE");
        updateRequest.setInputType("MULTI_SELECT");
        updateRequest.setRequired(0);
        updateRequest.setSearchable(0);
        updateRequest.setSortScore(80);
        updateRequest.setVisible(0);

        mockAttributeValueEntity = new AttributeValueEntity();
        mockAttributeValueEntity.setId(1L);
        mockAttributeValueEntity.setAttributeId(1L);
        mockAttributeValueEntity.setValue("红色");
        mockAttributeValueEntity.setSortScore(100);

        mockAttributeValue = new AttributeValue();
        mockAttributeValue.setId(1L);
        mockAttributeValue.setAttributeId(1L);
        mockAttributeValue.setValue("红色");
        mockAttributeValue.setSortScore(100);
    }

    // ==================== createAttribute方法测试 ====================

    @Test
    void createAttribute_Success() {
        // Given
        when(attributeMapper.findByName(createRequest.getName())).thenReturn(null);
        when(attributeMapper.insert(any(AttributeEntity.class))).thenReturn(1);

        // When
        Attribute result = attributeService.createAttribute(createRequest);

        // Then
        assertNotNull(result);
        assertEquals(createRequest.getName(), result.getName());
        assertEquals(AttributeType.SKU, result.getAttributeType());
        assertEquals(AttributeInputType.SINGLE_SELECT, result.getInputType());
        assertEquals(createRequest.getRequired(), result.getRequired());
        assertEquals(createRequest.getSearchable(), result.getSearchable());
        assertEquals(createRequest.getSortScore(), result.getSortScore());
        assertEquals(createRequest.getVisible(), result.getVisible());

        verify(attributeMapper, times(1)).findByName(createRequest.getName());
        verify(attributeMapper, times(1)).insert(any(AttributeEntity.class));
    }

    @Test
    void createAttribute_ThrowsBizException_WhenNameDuplicated() {
        // Given
        when(attributeMapper.findByName(createRequest.getName())).thenReturn(mockAttributeEntity);

        // When & Then
        BizException exception = assertThrows(BizException.class, () -> {
            attributeService.createAttribute(createRequest);
        });

        assertEquals(ErrorCode.ATTRIBUTE_NAME_DUPLICATED, exception.getErrorCode());
        verify(attributeMapper, times(1)).findByName(createRequest.getName());
        verify(attributeMapper, never()).insert(any(AttributeEntity.class));
    }

    @Test
    void createAttribute_ThrowsBizException_WhenInsertFailed() {
        // Given
        when(attributeMapper.findByName(createRequest.getName())).thenReturn(null);
        when(attributeMapper.insert(any(AttributeEntity.class))).thenReturn(0);

        // When & Then
        BizException exception = assertThrows(BizException.class, () -> {
            attributeService.createAttribute(createRequest);
        });

        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());
        verify(attributeMapper, times(1)).findByName(createRequest.getName());
        verify(attributeMapper, times(1)).insert(any(AttributeEntity.class));
    }

    @Test
    void createAttribute_ThrowsException_WhenRequestIsNull() {
        // When & Then
        assertThrows(Exception.class, () -> {
            attributeService.createAttribute(null);
        });

        verify(attributeMapper, never()).findByName(anyString());
        verify(attributeMapper, never()).insert(any(AttributeEntity.class));
    }

    // ==================== updateAttribute方法测试 ====================

    @Test
    void updateAttribute_Success() {
        // Given
        Long attributeId = 1L;
        when(attributeMapper.findById(attributeId)).thenReturn(mockAttributeEntity);
        when(attributeMapper.update(any(AttributeEntity.class))).thenReturn(1);

        // When
        assertDoesNotThrow(() -> {
            attributeService.updateAttribute(attributeId, updateRequest);
        });

        // Then
        verify(attributeMapper, times(1)).findById(attributeId);
        verify(attributeMapper, times(1)).update(any(AttributeEntity.class));
    }

    @Test
    void updateAttribute_NoUpdate_WhenNoFieldsChanged() {
        // Given
        Long attributeId = 1L;
        UpdateAttributeRequest noChangeRequest = new UpdateAttributeRequest();
        noChangeRequest.setName(mockAttributeEntity.getName());
        noChangeRequest.setAttributeType(mockAttributeEntity.getAttributeType());
        noChangeRequest.setInputType(mockAttributeEntity.getInputType());
        noChangeRequest.setRequired(mockAttributeEntity.getRequired());
        noChangeRequest.setSearchable(mockAttributeEntity.getSearchable());
        noChangeRequest.setSortScore(mockAttributeEntity.getSortScore());
        noChangeRequest.setVisible(mockAttributeEntity.getVisible());

        when(attributeMapper.findById(attributeId)).thenReturn(mockAttributeEntity);

        // When
        assertDoesNotThrow(() -> {
            attributeService.updateAttribute(attributeId, noChangeRequest);
        });

        // Then
        verify(attributeMapper, times(1)).findById(attributeId);
        verify(attributeMapper, never()).update(any(AttributeEntity.class));
    }

    @Test
    void updateAttribute_ThrowsBizException_WhenAttributeNotFound() {
        // Given
        Long attributeId = 999L;
        when(attributeMapper.findById(attributeId)).thenReturn(null);

        // When & Then
        BizException exception = assertThrows(BizException.class, () -> {
            attributeService.updateAttribute(attributeId, updateRequest);
        });

        assertEquals(ErrorCode.ATTRIBUTE_NOT_FOUND, exception.getErrorCode());
        verify(attributeMapper, times(1)).findById(attributeId);
        verify(attributeMapper, never()).update(any(AttributeEntity.class));
    }

    @Test
    void updateAttribute_ThrowsBizException_WhenUpdateFailed() {
        // Given
        Long attributeId = 1L;
        when(attributeMapper.findById(attributeId)).thenReturn(mockAttributeEntity);
        when(attributeMapper.update(any(AttributeEntity.class))).thenReturn(0);

        // When & Then
        BizException exception = assertThrows(BizException.class, () -> {
            attributeService.updateAttribute(attributeId, updateRequest);
        });

        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());
        verify(attributeMapper, times(1)).findById(attributeId);
        verify(attributeMapper, times(1)).update(any(AttributeEntity.class));
    }

    @Test
    void updateAttribute_ThrowsException_WhenIdIsNull() {
        // When & Then
        assertThrows(Exception.class, () -> {
            attributeService.updateAttribute(null, updateRequest);
        });
    }

    @Test
    void updateAttribute_ThrowsException_WhenRequestIsNull() {
        // When & Then
        assertThrows(Exception.class, () -> {
            attributeService.updateAttribute(1L, null);
        });
    }

    // ==================== deleteAttribute方法测试 ====================

    @Test
    void deleteAttribute_Success_WithNoValues() {
        // Given
        Long attributeId = 1L;
        when(attributeMapper.findById(attributeId)).thenReturn(mockAttributeEntity);
        when(itemAttributeRelationMapper.findByAttributeId(attributeId, 0, 1)).thenReturn(Collections.emptyList());
        when(attributeMapper.deleteById(attributeId)).thenReturn(1);
        when(attributeValueMapper.countValuesByAttributeId(attributeId)).thenReturn(0);

        // When
        assertDoesNotThrow(() -> {
            attributeService.deleteAttribute(attributeId);
        });

        // Then
        verify(attributeMapper, times(1)).findById(attributeId);
        verify(itemAttributeRelationMapper, times(1)).findByAttributeId(attributeId, 0, 1);
        verify(attributeMapper, times(1)).deleteById(attributeId);
        verify(attributeValueMapper, times(1)).countValuesByAttributeId(attributeId);
        verify(attributeValueMapper, never()).deleteByAttributeId(attributeId);
    }

    @Test
    void deleteAttribute_Success_WithValues() {
        // Given
        Long attributeId = 1L;
        int valueCount = 3;
        when(attributeMapper.findById(attributeId)).thenReturn(mockAttributeEntity);
        when(itemAttributeRelationMapper.findByAttributeId(attributeId, 0, 1)).thenReturn(Collections.emptyList());
        when(attributeMapper.deleteById(attributeId)).thenReturn(1);
        when(attributeValueMapper.countValuesByAttributeId(attributeId)).thenReturn(valueCount);
        when(attributeValueMapper.deleteByAttributeId(attributeId)).thenReturn(valueCount);

        // When
        assertDoesNotThrow(() -> {
            attributeService.deleteAttribute(attributeId);
        });

        // Then
        verify(attributeMapper, times(1)).findById(attributeId);
        verify(itemAttributeRelationMapper, times(1)).findByAttributeId(attributeId, 0, 1);
        verify(attributeMapper, times(1)).deleteById(attributeId);
        verify(attributeValueMapper, times(1)).countValuesByAttributeId(attributeId);
        verify(attributeValueMapper, times(1)).deleteByAttributeId(attributeId);
    }

    @Test
    void deleteAttribute_ThrowsBizException_WhenAttributeNotFound() {
        // Given
        Long attributeId = 999L;
        when(attributeMapper.findById(attributeId)).thenReturn(null);

        // When & Then
        BizException exception = assertThrows(BizException.class, () -> {
            attributeService.deleteAttribute(attributeId);
        });

        assertEquals(ErrorCode.ATTRIBUTE_NOT_FOUND, exception.getErrorCode());
        verify(attributeMapper, times(1)).findById(attributeId);
        verify(itemAttributeRelationMapper, never()).findByAttributeId(anyLong(), anyInt(), anyInt());
    }

    @Test
    void deleteAttribute_ThrowsBizException_WhenAttributeIsReferenced() {
        // Given
        Long attributeId = 1L;
        ItemAttributeRelationEntity relationEntity = new ItemAttributeRelationEntity();
        relationEntity.setItemId(100L);
        relationEntity.setAttributeId(attributeId);
        List<ItemAttributeRelationEntity> relationEntities = Arrays.asList(relationEntity);

        when(attributeMapper.findById(attributeId)).thenReturn(mockAttributeEntity);
        when(itemAttributeRelationMapper.findByAttributeId(attributeId, 0, 1)).thenReturn(relationEntities);

        // When & Then
        BizException exception = assertThrows(BizException.class, () -> {
            attributeService.deleteAttribute(attributeId);
        });

        assertEquals(ErrorCode.ATTRIBUTE_IS_REFERENCE_BY_ITEM, exception.getErrorCode());
        verify(attributeMapper, times(1)).findById(attributeId);
        verify(itemAttributeRelationMapper, times(1)).findByAttributeId(attributeId, 0, 1);
        verify(attributeMapper, never()).deleteById(attributeId);
    }

    @Test
    void deleteAttribute_ThrowsBizException_WhenDeleteAttributeFailed() {
        // Given
        Long attributeId = 1L;
        when(attributeMapper.findById(attributeId)).thenReturn(mockAttributeEntity);
        when(itemAttributeRelationMapper.findByAttributeId(attributeId, 0, 1)).thenReturn(Collections.emptyList());
        when(attributeMapper.deleteById(attributeId)).thenReturn(0);

        // When & Then
        BizException exception = assertThrows(BizException.class, () -> {
            attributeService.deleteAttribute(attributeId);
        });

        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());
        verify(attributeMapper, times(1)).deleteById(attributeId);
    }

    @Test
    void deleteAttribute_ThrowsBizException_WhenDeleteValuesFailed() {
        // Given
        Long attributeId = 1L;
        int valueCount = 3;
        when(attributeMapper.findById(attributeId)).thenReturn(mockAttributeEntity);
        when(itemAttributeRelationMapper.findByAttributeId(attributeId, 0, 1)).thenReturn(Collections.emptyList());
        when(attributeMapper.deleteById(attributeId)).thenReturn(1);
        when(attributeValueMapper.countValuesByAttributeId(attributeId)).thenReturn(valueCount);
        when(attributeValueMapper.deleteByAttributeId(attributeId)).thenReturn(1); // 删除数量不匹配

        // When & Then
        BizException exception = assertThrows(BizException.class, () -> {
            attributeService.deleteAttribute(attributeId);
        });

        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());
        verify(attributeValueMapper, times(1)).deleteByAttributeId(attributeId);
    }

    @Test
    void deleteAttribute_ThrowsException_WhenIdIsNull() {
        // When & Then
        assertThrows(Exception.class, () -> {
            attributeService.deleteAttribute(null);
        });
    }

    // ==================== getAttributeById方法测试 ====================

    @Test
    void getAttributeById_Success() {
        // Given
        Long attributeId = 1L;
        when(attributeMapper.findById(attributeId)).thenReturn(mockAttributeEntity);

        // When
        Attribute result = attributeService.getAttributeById(attributeId);

        // Then
        assertNotNull(result);
        assertEquals(mockAttributeEntity.getId(), result.getId());
        assertEquals(mockAttributeEntity.getName(), result.getName());
        assertEquals(AttributeType.valueOf(mockAttributeEntity.getAttributeType()), result.getAttributeType());
        assertEquals(AttributeInputType.valueOf(mockAttributeEntity.getInputType()), result.getInputType());
        assertEquals(mockAttributeEntity.getRequired(), result.getRequired());
        assertEquals(mockAttributeEntity.getSearchable(), result.getSearchable());
        assertEquals(mockAttributeEntity.getSortScore(), result.getSortScore());
        assertEquals(mockAttributeEntity.getVisible(), result.getVisible());

        verify(attributeMapper, times(1)).findById(attributeId);
    }

    @Test
    void getAttributeById_ThrowsBizException_WhenAttributeNotFound() {
        // Given
        Long attributeId = 999L;
        when(attributeMapper.findById(attributeId)).thenReturn(null);

        // When & Then
        BizException exception = assertThrows(BizException.class, () -> {
            attributeService.getAttributeById(attributeId);
        });

        assertEquals(ErrorCode.ATTRIBUTE_NOT_FOUND, exception.getErrorCode());
        verify(attributeMapper, times(1)).findById(attributeId);
    }

    @Test
    void getAttributeById_ThrowsException_WhenIdIsNull() {
        // When & Then
        assertThrows(Exception.class, () -> {
            attributeService.getAttributeById(null);
        });
    }

    // ==================== getAttributeByIdWithValues方法测试 ====================

    @Test
    void getAttributeByIdWithValues_Success_WithSelectType() {
        // Given
        Long attributeId = 1L;
        List<AttributeValueEntity> valueEntities = Arrays.asList(mockAttributeValueEntity);
        
        when(attributeMapper.findById(attributeId)).thenReturn(mockAttributeEntity);
        when(attributeValueMapper.findAllAttributeValuesByAttributeId(attributeId)).thenReturn(valueEntities);

        // When
        Attribute result = attributeService.getAttributeByIdWithValues(attributeId);

        // Then
        assertNotNull(result);
        assertEquals(mockAttributeEntity.getId(), result.getId());
        assertNotNull(result.getValues());
        assertEquals(1, result.getValues().size());
        assertEquals(mockAttributeValue.getValue(), result.getValues().get(0).getValue());

        verify(attributeMapper, times(1)).findById(attributeId);
        verify(attributeValueMapper, times(1)).findAllAttributeValuesByAttributeId(attributeId);
    }

    @Test
    void getAttributeByIdWithValues_Success_WithInputType() {
        // Given
        Long attributeId = 1L;
        AttributeEntity inputTypeEntity = new AttributeEntity();
        inputTypeEntity.setId(attributeId);
        inputTypeEntity.setName("描述");
        inputTypeEntity.setInputType("INPUT");
        inputTypeEntity.setAttributeType("OTHER");
        
        when(attributeMapper.findById(attributeId)).thenReturn(inputTypeEntity);

        // When
        Attribute result = attributeService.getAttributeByIdWithValues(attributeId);

        // Then
        assertNotNull(result);
        assertEquals(inputTypeEntity.getId(), result.getId());
        assertNull(result.getValues()); // INPUT类型不会设置值

        verify(attributeMapper, times(1)).findById(attributeId);
        verify(attributeValueMapper, never()).findAllAttributeValuesByAttributeId(attributeId);
    }

    @Test
    void getAttributeByIdWithValues_ThrowsBizException_WhenAttributeNotFound() {
        // Given
        Long attributeId = 999L;
        when(attributeMapper.findById(attributeId)).thenReturn(null);

        // When & Then
        BizException exception = assertThrows(BizException.class, () -> {
            attributeService.getAttributeByIdWithValues(attributeId);
        });

        assertEquals(ErrorCode.ATTRIBUTE_NOT_FOUND, exception.getErrorCode());
        verify(attributeMapper, times(1)).findById(attributeId);
    }

    // ==================== findAllAttributeValuesByAttributeId方法测试 ====================

    @Test
    void findAllAttributeValuesByAttributeId_Success_WithSelectType() {
        // Given
        Long attributeId = 1L;
        List<AttributeValueEntity> valueEntities = Arrays.asList(mockAttributeValueEntity);
        
        when(attributeMapper.findById(attributeId)).thenReturn(mockAttributeEntity);
        when(attributeValueMapper.findAllAttributeValuesByAttributeId(attributeId)).thenReturn(valueEntities);

        // When
        List<AttributeValue> result = attributeService.findAllAttributeValuesByAttributeId(attributeId);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(mockAttributeValueEntity.getId(), result.get(0).getId());
        assertEquals(mockAttributeValueEntity.getAttributeId(), result.get(0).getAttributeId());
        assertEquals(mockAttributeValueEntity.getValue(), result.get(0).getValue());
        assertEquals(mockAttributeValueEntity.getSortScore(), result.get(0).getSortScore());

        verify(attributeMapper, times(1)).findById(attributeId);
        verify(attributeValueMapper, times(1)).findAllAttributeValuesByAttributeId(attributeId);
    }

    @Test
    void findAllAttributeValuesByAttributeId_ReturnsEmptyList_WithInputType() {
        // Given
        Long attributeId = 1L;
        AttributeEntity inputTypeEntity = new AttributeEntity();
        inputTypeEntity.setId(attributeId);
        inputTypeEntity.setInputType("INPUT");
        inputTypeEntity.setAttributeType("OTHER");
        
        when(attributeMapper.findById(attributeId)).thenReturn(inputTypeEntity);

        // When
        List<AttributeValue> result = attributeService.findAllAttributeValuesByAttributeId(attributeId);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(attributeMapper, times(1)).findById(attributeId);
        verify(attributeValueMapper, never()).findAllAttributeValuesByAttributeId(attributeId);
    }

    @Test
    void findAllAttributeValuesByAttributeId_Success_WithMultiSelectType() {
        // Given
        Long attributeId = 1L;
        AttributeEntity multiSelectEntity = new AttributeEntity();
        multiSelectEntity.setId(attributeId);
        multiSelectEntity.setInputType("MULTI_SELECT");
        multiSelectEntity.setAttributeType("SALE");
        
        AttributeValueEntity value1 = new AttributeValueEntity();
        value1.setId(1L);
        value1.setAttributeId(attributeId);
        value1.setValue("红色");
        value1.setSortScore(100);
        
        AttributeValueEntity value2 = new AttributeValueEntity();
        value2.setId(2L);
        value2.setAttributeId(attributeId);
        value2.setValue("蓝色");
        value2.setSortScore(90);
        
        List<AttributeValueEntity> valueEntities = Arrays.asList(value1, value2);
        
        when(attributeMapper.findById(attributeId)).thenReturn(multiSelectEntity);
        when(attributeValueMapper.findAllAttributeValuesByAttributeId(attributeId)).thenReturn(valueEntities);

        // When
        List<AttributeValue> result = attributeService.findAllAttributeValuesByAttributeId(attributeId);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("红色", result.get(0).getValue());
        assertEquals("蓝色", result.get(1).getValue());

        verify(attributeMapper, times(1)).findById(attributeId);
        verify(attributeValueMapper, times(1)).findAllAttributeValuesByAttributeId(attributeId);
    }

    @Test
    void findAllAttributeValuesByAttributeId_ThrowsBizException_WhenAttributeNotFound() {
        // Given
        Long attributeId = 999L;
        when(attributeMapper.findById(attributeId)).thenReturn(null);

        // When & Then
        BizException exception = assertThrows(BizException.class, () -> {
            attributeService.findAllAttributeValuesByAttributeId(attributeId);
        });

        assertEquals(ErrorCode.ATTRIBUTE_NOT_FOUND, exception.getErrorCode());
        verify(attributeMapper, times(1)).findById(attributeId);
    }

    // ==================== getAttributeValueById方法测试 ====================

    @Test
    void getAttributeValueById_Success() {
        // Given
        Long valueId = 1L;
        when(attributeValueMapper.findById(valueId)).thenReturn(mockAttributeValueEntity);

        // When
        AttributeValue result = attributeService.getAttributeValueById(valueId);

        // Then
        assertNotNull(result);
        assertEquals(mockAttributeValueEntity.getId(), result.getId());
        assertEquals(mockAttributeValueEntity.getAttributeId(), result.getAttributeId());
        assertEquals(mockAttributeValueEntity.getValue(), result.getValue());
        assertEquals(mockAttributeValueEntity.getSortScore(), result.getSortScore());

        verify(attributeValueMapper, times(1)).findById(valueId);
    }

    @Test
    void getAttributeValueById_ThrowsBizException_WhenValueNotFound() {
        // Given
        Long valueId = 999L;
        when(attributeValueMapper.findById(valueId)).thenReturn(null);

        // When & Then
        BizException exception = assertThrows(BizException.class, () -> {
            attributeService.getAttributeValueById(valueId);
        });

        assertEquals(ErrorCode.ATTRIBUTE_VALUE_NOT_FOUND, exception.getErrorCode());
        verify(attributeValueMapper, times(1)).findById(valueId);
    }

    @Test
    void getAttributeValueById_ThrowsException_WhenIdIsNull() {
        // When & Then
        assertThrows(Exception.class, () -> {
            attributeService.getAttributeValueById(null);
        });
    }

    // ==================== ensureItemAttributes方法测试 ====================

    @Test
    void ensureItemAttributes_Success_WithNoExistingRelations() {
        // Given
        Long itemId = 100L;
        Long skuId = 200L;
        
        ItemAttributeRequest request1 = new ItemAttributeRequest();
        request1.setAttributeId(1L);
        request1.setAttributeValueId(10L);
        request1.setValue("红色");
        
        ItemAttributeRequest request2 = new ItemAttributeRequest();
        request2.setAttributeId(2L);
        request2.setAttributeValueId(20L);
        request2.setValue("大号");
        
        List<ItemAttributeRequest> attributes = Arrays.asList(request1, request2);
        
        when(itemAttributeRelationMapper.findByItemIdAndSkuId(itemId, skuId)).thenReturn(Collections.emptyList());
        when(itemAttributeRelationMapper.batchInsert(anyList())).thenReturn(2);

        // When
        assertDoesNotThrow(() -> {
            attributeService.ensureItemAttributes(itemId, skuId, attributes);
        });

        // Then
        verify(itemAttributeRelationMapper, times(1)).findByItemIdAndSkuId(itemId, skuId);
        verify(itemAttributeRelationMapper, times(1)).batchInsert(anyList());
        verify(itemAttributeRelationMapper, never()).deleteByItemIdAndAttributeIds(anyLong(), anyList());
    }

    @Test
    void ensureItemAttributes_Success_WithExistingRelations() {
        // Given
        Long itemId = 100L;
        Long skuId = 200L;
        
        // 旧的关系
        ItemAttributeRelationEntity existingRelation = new ItemAttributeRelationEntity();
        existingRelation.setItemId(itemId);
        existingRelation.setSkuId(skuId);
        existingRelation.setAttributeId(1L);
        List<ItemAttributeRelationEntity> existingRelations = Arrays.asList(existingRelation);
        
        // 新的属性
        ItemAttributeRequest request = new ItemAttributeRequest();
        request.setAttributeId(2L); // 不同的属性ID
        request.setAttributeValueId(20L);
        request.setValue("大号");
        List<ItemAttributeRequest> attributes = Arrays.asList(request);
        
        when(itemAttributeRelationMapper.findByItemIdAndSkuId(itemId, skuId)).thenReturn(existingRelations);
        when(itemAttributeRelationMapper.deleteByItemIdAndAttributeIds(eq(itemId), anyList())).thenReturn(1);
        when(itemAttributeRelationMapper.batchInsert(anyList())).thenReturn(1);

        // When
        assertDoesNotThrow(() -> {
            attributeService.ensureItemAttributes(itemId, skuId, attributes);
        });

        // Then
        verify(itemAttributeRelationMapper, times(1)).findByItemIdAndSkuId(itemId, skuId);
        verify(itemAttributeRelationMapper, times(1)).deleteByItemIdAndAttributeIds(eq(itemId), anyList());
        verify(itemAttributeRelationMapper, times(1)).batchInsert(anyList());
    }

    @Test
    void ensureItemAttributes_Success_WithFilteredDuplicateAttributes() {
        // Given
        Long itemId = 100L;
        Long skuId = 200L;
        
        // 旧的关系
        ItemAttributeRelationEntity existingRelation = new ItemAttributeRelationEntity();
        existingRelation.setItemId(itemId);
        existingRelation.setSkuId(skuId);
        existingRelation.setAttributeId(1L);
        List<ItemAttributeRelationEntity> existingRelations = Arrays.asList(existingRelation);
        
        // 新的属性（包含重复的属性ID）
        ItemAttributeRequest request = new ItemAttributeRequest();
        request.setAttributeId(1L); // 相同的属性ID
        request.setAttributeValueId(10L);
        request.setValue("红色");
        List<ItemAttributeRequest> attributes = Arrays.asList(request);
        
        when(itemAttributeRelationMapper.findByItemIdAndSkuId(itemId, skuId)).thenReturn(existingRelations);
        when(itemAttributeRelationMapper.deleteByItemIdAndAttributeIds(eq(itemId), anyList())).thenReturn(1);

        // When
        assertDoesNotThrow(() -> {
            attributeService.ensureItemAttributes(itemId, skuId, attributes);
        });

        // Then
        verify(itemAttributeRelationMapper, times(1)).findByItemIdAndSkuId(itemId, skuId);
        verify(itemAttributeRelationMapper, times(1)).deleteByItemIdAndAttributeIds(eq(itemId), anyList());
        verify(itemAttributeRelationMapper, never()).batchInsert(anyList()); // 没有新关系需要插入
    }

    @Test
    void ensureItemAttributes_ThrowsBizException_WhenDeleteFailed() {
        // Given
        Long itemId = 100L;
        Long skuId = 200L;
        
        ItemAttributeRelationEntity existingRelation = new ItemAttributeRelationEntity();
        existingRelation.setItemId(itemId);
        existingRelation.setSkuId(skuId);
        existingRelation.setAttributeId(1L);
        List<ItemAttributeRelationEntity> existingRelations = Arrays.asList(existingRelation);
        
        ItemAttributeRequest request = new ItemAttributeRequest();
        request.setAttributeId(2L);
        request.setAttributeValueId(20L);
        request.setValue("大号");
        List<ItemAttributeRequest> attributes = Arrays.asList(request);
        
        when(itemAttributeRelationMapper.findByItemIdAndSkuId(itemId, skuId)).thenReturn(existingRelations);
        when(itemAttributeRelationMapper.deleteByItemIdAndAttributeIds(eq(itemId), anyList())).thenReturn(0); // 删除失败

        // When & Then
        BizException exception = assertThrows(BizException.class, () -> {
            attributeService.ensureItemAttributes(itemId, skuId, attributes);
        });

        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());
    }

    @Test
    void ensureItemAttributes_ThrowsBizException_WhenBatchInsertFailed() {
        // Given
        Long itemId = 100L;
        Long skuId = 200L;
        
        ItemAttributeRequest request = new ItemAttributeRequest();
        request.setAttributeId(1L);
        request.setAttributeValueId(10L);
        request.setValue("红色");
        List<ItemAttributeRequest> attributes = Arrays.asList(request);
        
        when(itemAttributeRelationMapper.findByItemIdAndSkuId(itemId, skuId)).thenReturn(Collections.emptyList());
        when(itemAttributeRelationMapper.batchInsert(anyList())).thenReturn(0); // 插入失败

        // When & Then
        BizException exception = assertThrows(BizException.class, () -> {
            attributeService.ensureItemAttributes(itemId, skuId, attributes);
        });

        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());
    }

    @Test
    void ensureItemAttributes_ThrowsException_WhenParametersAreNull() {
        // When & Then
        assertThrows(Exception.class, () -> {
            attributeService.ensureItemAttributes(null, 1L, Arrays.asList());
        });
        
        assertThrows(Exception.class, () -> {
            attributeService.ensureItemAttributes(1L, null, Arrays.asList());
        });
        
        assertThrows(Exception.class, () -> {
            attributeService.ensureItemAttributes(1L, 1L, null);
        });
    }
}