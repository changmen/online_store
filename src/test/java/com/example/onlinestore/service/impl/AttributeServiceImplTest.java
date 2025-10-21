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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @BeforeEach
    void setUp() {
        // 初始化创建请求
        createRequest = new CreateAttributeRequest();
        createRequest.setName("颜色");
        createRequest.setAttributeType("SKU");
        createRequest.setInputType("SINGLE_SELECT");
        createRequest.setRequired(1);
        createRequest.setSearchable(1);
        createRequest.setSortScore(100);
        createRequest.setVisible(1);

        // 初始化更新请求
        updateRequest = new UpdateAttributeRequest();
        updateRequest.setName("尺寸");
        updateRequest.setAttributeType("SALE");
        updateRequest.setInputType("MULTI_SELECT");
        updateRequest.setRequired(0);
        updateRequest.setSearchable(0);
        updateRequest.setSortScore(90);
        updateRequest.setVisible(0);

        // 初始化实体
        attributeEntity = new AttributeEntity();
        attributeEntity.setId(1L);
        attributeEntity.setName("颜色");
        attributeEntity.setAttributeType("SKU");
        attributeEntity.setInputType("SINGLE_SELECT");
        attributeEntity.setRequired(1);
        attributeEntity.setSearchable(1);
        attributeEntity.setSortScore(100);
        attributeEntity.setVisible(1);
        attributeEntity.setCreatedAt(LocalDateTime.now());
        attributeEntity.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void testCreateAttribute_Success() {
        // Arrange
        when(attributeMapper.findByName("颜色")).thenReturn(null);
        when(attributeMapper.insert(any(AttributeEntity.class))).thenAnswer(invocation -> {
            AttributeEntity entity = invocation.getArgument(0);
            entity.setId(1L);
            return 1;
        });

        // Act
        Attribute result = attributeService.createAttribute(createRequest);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("颜色", result.getName());
        assertEquals(AttributeType.SKU, result.getAttributeType());
        assertEquals(AttributeInputType.SINGLE_SELECT, result.getInputType());
        assertEquals(1, result.getRequired());
        assertEquals(1, result.getSearchable());
        assertEquals(100, result.getSortScore());
        assertEquals(1, result.getVisible());

        verify(attributeMapper).findByName("颜色");
        verify(attributeMapper).insert(any(AttributeEntity.class));
    }

    @Test
    void testCreateAttribute_DuplicateName() {
        // Arrange
        when(attributeMapper.findByName("颜色")).thenReturn(attributeEntity);

        // Act & Assert
        BizException exception = assertThrows(BizException.class, () -> {
            attributeService.createAttribute(createRequest);
        });

        assertEquals(ErrorCode.ATTRIBUTE_NAME_DUPLICATED, exception.getErrorCode());
        verify(attributeMapper).findByName("颜色");
        verify(attributeMapper, never()).insert(any(AttributeEntity.class));
    }

    @Test
    void testCreateAttribute_InsertFailed() {
        // Arrange
        when(attributeMapper.findByName("颜色")).thenReturn(null);
        when(attributeMapper.insert(any(AttributeEntity.class))).thenReturn(0);

        // Act & Assert
        BizException exception = assertThrows(BizException.class, () -> {
            attributeService.createAttribute(createRequest);
        });

        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());
        verify(attributeMapper).findByName("颜色");
        verify(attributeMapper).insert(any(AttributeEntity.class));
    }

    @Test
    void testUpdateAttribute_Success() {
        // Arrange
        when(attributeMapper.findById(1L)).thenReturn(attributeEntity);
        when(attributeMapper.update(any(AttributeEntity.class))).thenReturn(1);

        // Act
        attributeService.updateAttribute(1L, updateRequest);

        // Assert
        ArgumentCaptor<AttributeEntity> captor = ArgumentCaptor.forClass(AttributeEntity.class);
        verify(attributeMapper).findById(1L);
        verify(attributeMapper).update(captor.capture());

        AttributeEntity updatedEntity = captor.getValue();
        assertEquals(1L, updatedEntity.getId());
        assertEquals("尺寸", updatedEntity.getName());
    }

    @Test
    void testUpdateAttribute_NoChanges() {
        // Arrange
        updateRequest.setName("颜色");
        updateRequest.setAttributeType("SKU");
        updateRequest.setInputType("SINGLE_SELECT");
        updateRequest.setRequired(1);
        updateRequest.setSearchable(1);
        updateRequest.setSortScore(100);
        updateRequest.setVisible(1);

        when(attributeMapper.findById(1L)).thenReturn(attributeEntity);

        // Act
        attributeService.updateAttribute(1L, updateRequest);

        // Assert
        verify(attributeMapper).findById(1L);
        verify(attributeMapper, never()).update(any(AttributeEntity.class));
    }

    @Test
    void testUpdateAttribute_NotFound() {
        // Arrange
        when(attributeMapper.findById(1L)).thenReturn(null);

        // Act & Assert
        BizException exception = assertThrows(BizException.class, () -> {
            attributeService.updateAttribute(1L, updateRequest);
        });

        assertEquals(ErrorCode.ATTRIBUTE_NOT_FOUND, exception.getErrorCode());
        verify(attributeMapper).findById(1L);
        verify(attributeMapper, never()).update(any(AttributeEntity.class));
    }

    @Test
    void testUpdateAttribute_UpdateFailed() {
        // Arrange
        when(attributeMapper.findById(1L)).thenReturn(attributeEntity);
        when(attributeMapper.update(any(AttributeEntity.class))).thenReturn(0);

        // Act & Assert
        BizException exception = assertThrows(BizException.class, () -> {
            attributeService.updateAttribute(1L, updateRequest);
        });

        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());
        verify(attributeMapper).findById(1L);
        verify(attributeMapper).update(any(AttributeEntity.class));
    }

    @Test
    void testDeleteAttribute_Success() {
        // Arrange
        when(attributeMapper.findById(1L)).thenReturn(attributeEntity);
        when(itemAttributeRelationMapper.findByAttributeId(1L, 0, 1)).thenReturn(Collections.emptyList());
        when(attributeMapper.deleteById(1L)).thenReturn(1);
        when(attributeValueMapper.countValuesByAttributeId(1L)).thenReturn(2);
        when(attributeValueMapper.deleteByAttributeId(1L)).thenReturn(2);

        // Act
        attributeService.deleteAttribute(1L);

        // Assert
        verify(attributeMapper).findById(1L);
        verify(itemAttributeRelationMapper).findByAttributeId(1L, 0, 1);
        verify(attributeMapper).deleteById(1L);
        verify(attributeValueMapper).countValuesByAttributeId(1L);
        verify(attributeValueMapper).deleteByAttributeId(1L);
    }

    @Test
    void testDeleteAttribute_NotFound() {
        // Arrange
        when(attributeMapper.findById(1L)).thenReturn(null);

        // Act & Assert
        BizException exception = assertThrows(BizException.class, () -> {
            attributeService.deleteAttribute(1L);
        });

        assertEquals(ErrorCode.ATTRIBUTE_NOT_FOUND, exception.getErrorCode());
        verify(attributeMapper).findById(1L);
        verify(attributeMapper, never()).deleteById(anyLong());
    }

    @Test
    void testDeleteAttribute_ReferencedByItem() {
        // Arrange
        ItemAttributeRelationEntity relationEntity = new ItemAttributeRelationEntity();
        relationEntity.setItemId(100L);
        relationEntity.setAttributeId(1L);

        when(attributeMapper.findById(1L)).thenReturn(attributeEntity);
        when(itemAttributeRelationMapper.findByAttributeId(1L, 0, 1))
                .thenReturn(Collections.singletonList(relationEntity));

        // Act & Assert
        BizException exception = assertThrows(BizException.class, () -> {
            attributeService.deleteAttribute(1L);
        });

        assertEquals(ErrorCode.ATTRIBUTE_IS_REFERENCE_BY_ITEM, exception.getErrorCode());
        verify(attributeMapper).findById(1L);
        verify(itemAttributeRelationMapper).findByAttributeId(1L, 0, 1);
        verify(attributeMapper, never()).deleteById(anyLong());
    }

    @Test
    void testDeleteAttribute_DeleteFailed() {
        // Arrange
        when(attributeMapper.findById(1L)).thenReturn(attributeEntity);
        when(itemAttributeRelationMapper.findByAttributeId(1L, 0, 1)).thenReturn(Collections.emptyList());
        when(attributeMapper.deleteById(1L)).thenReturn(0);

        // Act & Assert
        BizException exception = assertThrows(BizException.class, () -> {
            attributeService.deleteAttribute(1L);
        });

        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());
        verify(attributeMapper).deleteById(1L);
    }

    @Test
    void testDeleteAttribute_NoAttributeValues() {
        // Arrange
        when(attributeMapper.findById(1L)).thenReturn(attributeEntity);
        when(itemAttributeRelationMapper.findByAttributeId(1L, 0, 1)).thenReturn(Collections.emptyList());
        when(attributeMapper.deleteById(1L)).thenReturn(1);
        when(attributeValueMapper.countValuesByAttributeId(1L)).thenReturn(0);

        // Act
        attributeService.deleteAttribute(1L);

        // Assert
        verify(attributeMapper).deleteById(1L);
        verify(attributeValueMapper).countValuesByAttributeId(1L);
        verify(attributeValueMapper, never()).deleteByAttributeId(anyLong());
    }

    @Test
    void testDeleteAttribute_DeleteValuesFailed() {
        // Arrange
        when(attributeMapper.findById(1L)).thenReturn(attributeEntity);
        when(itemAttributeRelationMapper.findByAttributeId(1L, 0, 1)).thenReturn(Collections.emptyList());
        when(attributeMapper.deleteById(1L)).thenReturn(1);
        when(attributeValueMapper.countValuesByAttributeId(1L)).thenReturn(2);
        when(attributeValueMapper.deleteByAttributeId(1L)).thenReturn(1);

        // Act & Assert
        BizException exception = assertThrows(BizException.class, () -> {
            attributeService.deleteAttribute(1L);
        });

        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());
    }

    @Test
    void testGetAttributeById_Success() {
        // Arrange
        when(attributeMapper.findById(1L)).thenReturn(attributeEntity);

        // Act
        Attribute result = attributeService.getAttributeById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("颜色", result.getName());
        assertEquals(AttributeType.SKU, result.getAttributeType());
        verify(attributeMapper).findById(1L);
    }

    @Test
    void testGetAttributeById_NotFound() {
        // Arrange
        when(attributeMapper.findById(1L)).thenReturn(null);

        // Act & Assert
        BizException exception = assertThrows(BizException.class, () -> {
            attributeService.getAttributeById(1L);
        });

        assertEquals(ErrorCode.ATTRIBUTE_NOT_FOUND, exception.getErrorCode());
        verify(attributeMapper).findById(1L);
    }

    @Test
    void testGetAttributeByIdWithValues_SingleSelect() {
        // Arrange
        AttributeValueEntity valueEntity1 = new AttributeValueEntity();
        valueEntity1.setId(1L);
        valueEntity1.setAttributeId(1L);
        valueEntity1.setValue("红色");
        valueEntity1.setSortScore(100);

        AttributeValueEntity valueEntity2 = new AttributeValueEntity();
        valueEntity2.setId(2L);
        valueEntity2.setAttributeId(1L);
        valueEntity2.setValue("蓝色");
        valueEntity2.setSortScore(90);

        when(attributeMapper.findById(1L)).thenReturn(attributeEntity);
        when(attributeValueMapper.findAllAttributeValuesByAttributeId(1L))
                .thenReturn(Arrays.asList(valueEntity1, valueEntity2));

        // Act
        Attribute result = attributeService.getAttributeByIdWithValues(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertNotNull(result.getValues());
        assertEquals(2, result.getValues().size());
        assertEquals("红色", result.getValues().get(0).getValue());
        assertEquals("蓝色", result.getValues().get(1).getValue());

        verify(attributeMapper).findById(1L);
        verify(attributeValueMapper).findAllAttributeValuesByAttributeId(1L);
    }

    @Test
    void testGetAttributeByIdWithValues_InputType() {
        // Arrange
        attributeEntity.setInputType("INPUT");
        when(attributeMapper.findById(1L)).thenReturn(attributeEntity);

        // Act
        Attribute result = attributeService.getAttributeByIdWithValues(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertNull(result.getValues());

        verify(attributeMapper).findById(1L);
        verify(attributeValueMapper, never()).findAllAttributeValuesByAttributeId(anyLong());
    }

    @Test
    void testFindAllAttributeValuesByAttributeId_Success() {
        // Arrange
        AttributeValueEntity valueEntity = new AttributeValueEntity();
        valueEntity.setId(1L);
        valueEntity.setAttributeId(1L);
        valueEntity.setValue("红色");
        valueEntity.setSortScore(100);

        when(attributeMapper.findById(1L)).thenReturn(attributeEntity);
        when(attributeValueMapper.findAllAttributeValuesByAttributeId(1L))
                .thenReturn(Collections.singletonList(valueEntity));

        // Act
        List<AttributeValue> result = attributeService.findAllAttributeValuesByAttributeId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("红色", result.get(0).getValue());

        verify(attributeMapper).findById(1L);
        verify(attributeValueMapper).findAllAttributeValuesByAttributeId(1L);
    }

    @Test
    void testFindAllAttributeValuesByAttributeId_InputType() {
        // Arrange
        attributeEntity.setInputType("INPUT");
        when(attributeMapper.findById(1L)).thenReturn(attributeEntity);

        // Act
        List<AttributeValue> result = attributeService.findAllAttributeValuesByAttributeId(1L);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(attributeMapper).findById(1L);
        verify(attributeValueMapper, never()).findAllAttributeValuesByAttributeId(anyLong());
    }

    @Test
    void testGetAttributeValueById_Success() {
        // Arrange
        AttributeValueEntity valueEntity = new AttributeValueEntity();
        valueEntity.setId(1L);
        valueEntity.setAttributeId(1L);
        valueEntity.setValue("红色");
        valueEntity.setSortScore(100);

        when(attributeValueMapper.findById(1L)).thenReturn(valueEntity);

        // Act
        AttributeValue result = attributeService.getAttributeValueById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1L, result.getAttributeId());
        assertEquals("红色", result.getValue());
        assertEquals(100, result.getSortScore());

        verify(attributeValueMapper).findById(1L);
    }

    @Test
    void testGetAttributeValueById_NotFound() {
        // Arrange
        when(attributeValueMapper.findById(1L)).thenReturn(null);

        // Act & Assert
        BizException exception = assertThrows(BizException.class, () -> {
            attributeService.getAttributeValueById(1L);
        });

        assertEquals(ErrorCode.ATTRIBUTE_VALUE_NOT_FOUND, exception.getErrorCode());
        verify(attributeValueMapper).findById(1L);
    }

    @Test
    void testEnsureItemAttributes_NewAttributes() {
        // Arrange
        ItemAttributeRequest request1 = new ItemAttributeRequest();
        request1.setAttributeId(1L);
        request1.setAttributeValueId(10L);
        request1.setValue("红色");

        ItemAttributeRequest request2 = new ItemAttributeRequest();
        request2.setAttributeId(2L);
        request2.setAttributeValueId(20L);
        request2.setValue("大");

        List<ItemAttributeRequest> attributes = Arrays.asList(request1, request2);

        when(itemAttributeRelationMapper.findByItemIdAndSkuId(100L, 200L))
                .thenReturn(Collections.emptyList());
        when(itemAttributeRelationMapper.batchInsert(anyList())).thenReturn(2);

        // Act
        attributeService.ensureItemAttributes(100L, 200L, attributes);

        // Assert
        ArgumentCaptor<List<ItemAttributeRelationEntity>> captor = ArgumentCaptor.forClass(List.class);
        verify(itemAttributeRelationMapper).findByItemIdAndSkuId(100L, 200L);
        verify(itemAttributeRelationMapper).batchInsert(captor.capture());

        List<ItemAttributeRelationEntity> insertedRelations = captor.getValue();
        assertEquals(2, insertedRelations.size());
        assertEquals(1L, insertedRelations.get(0).getAttributeId());
        assertEquals(2L, insertedRelations.get(1).getAttributeId());
    }

    @Test
    void testEnsureItemAttributes_UpdateExisting() {
        // Arrange
        ItemAttributeRelationEntity existingRelation = new ItemAttributeRelationEntity();
        existingRelation.setItemId(100L);
        existingRelation.setSkuId(200L);
        existingRelation.setAttributeId(1L);

        ItemAttributeRequest request = new ItemAttributeRequest();
        request.setAttributeId(2L);
        request.setAttributeValueId(20L);
        request.setValue("新值");

        List<ItemAttributeRequest> attributes = Collections.singletonList(request);

        when(itemAttributeRelationMapper.findByItemIdAndSkuId(100L, 200L))
                .thenReturn(Collections.singletonList(existingRelation));
        when(itemAttributeRelationMapper.deleteByItemIdAndAttributeIds(eq(100L), anyList())).thenReturn(1);
        when(itemAttributeRelationMapper.batchInsert(anyList())).thenReturn(1);

        // Act
        attributeService.ensureItemAttributes(100L, 200L, attributes);

        // Assert
        verify(itemAttributeRelationMapper).findByItemIdAndSkuId(100L, 200L);
        verify(itemAttributeRelationMapper).deleteByItemIdAndAttributeIds(eq(100L), anyList());
        verify(itemAttributeRelationMapper).batchInsert(anyList());
    }

    @Test
    void testEnsureItemAttributes_NoNewRelations() {
        // Arrange
        ItemAttributeRelationEntity existingRelation = new ItemAttributeRelationEntity();
        existingRelation.setItemId(100L);
        existingRelation.setSkuId(200L);
        existingRelation.setAttributeId(1L);

        ItemAttributeRequest request = new ItemAttributeRequest();
        request.setAttributeId(1L);
        request.setAttributeValueId(10L);
        request.setValue("红色");

        List<ItemAttributeRequest> attributes = Collections.singletonList(request);

        when(itemAttributeRelationMapper.findByItemIdAndSkuId(100L, 200L))
                .thenReturn(Collections.singletonList(existingRelation));
        when(itemAttributeRelationMapper.deleteByItemIdAndAttributeIds(eq(100L), anyList())).thenReturn(1);

        // Act
        attributeService.ensureItemAttributes(100L, 200L, attributes);

        // Assert
        verify(itemAttributeRelationMapper).findByItemIdAndSkuId(100L, 200L);
        verify(itemAttributeRelationMapper).deleteByItemIdAndAttributeIds(eq(100L), anyList());
        verify(itemAttributeRelationMapper, never()).batchInsert(anyList());
    }

    @Test
    void testEnsureItemAttributes_DeleteFailed() {
        // Arrange
        ItemAttributeRelationEntity existingRelation1 = new ItemAttributeRelationEntity();
        existingRelation1.setItemId(100L);
        existingRelation1.setAttributeId(1L);

        ItemAttributeRelationEntity existingRelation2 = new ItemAttributeRelationEntity();
        existingRelation2.setItemId(100L);
        existingRelation2.setAttributeId(2L);

        ItemAttributeRequest request = new ItemAttributeRequest();
        request.setAttributeId(3L);
        request.setAttributeValueId(30L);
        request.setValue("新值");

        when(itemAttributeRelationMapper.findByItemIdAndSkuId(100L, 200L))
                .thenReturn(Arrays.asList(existingRelation1, existingRelation2));
        when(itemAttributeRelationMapper.deleteByItemIdAndAttributeIds(eq(100L), anyList())).thenReturn(1);

        // Act & Assert
        BizException exception = assertThrows(BizException.class, () -> {
            attributeService.ensureItemAttributes(100L, 200L, Collections.singletonList(request));
        });

        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());
    }

    @Test
    void testEnsureItemAttributes_InsertFailed() {
        // Arrange
        ItemAttributeRequest request1 = new ItemAttributeRequest();
        request1.setAttributeId(1L);
        request1.setAttributeValueId(10L);
        request1.setValue("红色");

        ItemAttributeRequest request2 = new ItemAttributeRequest();
        request2.setAttributeId(2L);
        request2.setAttributeValueId(20L);
        request2.setValue("大");

        List<ItemAttributeRequest> attributes = Arrays.asList(request1, request2);

        when(itemAttributeRelationMapper.findByItemIdAndSkuId(100L, 200L))
                .thenReturn(Collections.emptyList());
        when(itemAttributeRelationMapper.batchInsert(anyList())).thenReturn(1);

        // Act & Assert
        BizException exception = assertThrows(BizException.class, () -> {
            attributeService.ensureItemAttributes(100L, 200L, attributes);
        });

        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());
    }
}
