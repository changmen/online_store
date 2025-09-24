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
import java.util.ArrayList;
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

    private CreateAttributeRequest createAttributeRequest;
    private UpdateAttributeRequest updateAttributeRequest;
    private AttributeEntity attributeEntity;
    private Attribute attribute;
    private AttributeValueEntity attributeValueEntity;
    private AttributeValue attributeValue;
    private ItemAttributeRequest itemAttributeRequest;

    @BeforeEach
    void setUp() {
        // 准备测试数据
        createAttributeRequest = new CreateAttributeRequest();
        createAttributeRequest.setName("测试属性");
        createAttributeRequest.setAttributeType("SKU");
        createAttributeRequest.setInputType("SINGLE_SELECT");
        createAttributeRequest.setRequired(1);
        createAttributeRequest.setSearchable(1);
        createAttributeRequest.setSortScore(100);
        createAttributeRequest.setVisible(1);

        updateAttributeRequest = new UpdateAttributeRequest();
        updateAttributeRequest.setName("更新后的属性名");
        updateAttributeRequest.setAttributeType("SALE");
        updateAttributeRequest.setInputType("MULTI_SELECT");
        updateAttributeRequest.setRequired(0);
        updateAttributeRequest.setSearchable(0);
        updateAttributeRequest.setSortScore(200);
        updateAttributeRequest.setVisible(0);

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

        attribute = new Attribute();
        attribute.setId(1L);
        attribute.setName("测试属性");
        attribute.setAttributeType(AttributeType.SKU);
        attribute.setInputType(AttributeInputType.SINGLE_SELECT);
        attribute.setRequired(1);
        attribute.setSearchable(1);
        attribute.setSortScore(100);
        attribute.setVisible(1);

        attributeValueEntity = new AttributeValueEntity();
        attributeValueEntity.setId(1L);
        attributeValueEntity.setAttributeId(1L);
        attributeValueEntity.setValue("测试值");
        attributeValueEntity.setSortScore(100);

        attributeValue = new AttributeValue();
        attributeValue.setId(1L);
        attributeValue.setAttributeId(1L);
        attributeValue.setValue("测试值");
        attributeValue.setSortScore(100);

        itemAttributeRequest = new ItemAttributeRequest();
        itemAttributeRequest.setAttributeId(1L);
        itemAttributeRequest.setAttributeValueId(1L);
        itemAttributeRequest.setValue("测试值");
    }

    @Test
    void testCreateAttribute_Success() {
        // 准备数据
        when(attributeMapper.findByName(createAttributeRequest.getName())).thenReturn(null);
        when(attributeMapper.insert(any(AttributeEntity.class))).thenReturn(1);

        // 执行测试
        Attribute result = attributeService.createAttribute(createAttributeRequest);

        // 验证结果
        assertNotNull(result);
        assertEquals(createAttributeRequest.getName(), result.getName());
        assertEquals(AttributeType.SKU, result.getAttributeType());
        assertEquals(AttributeInputType.SINGLE_SELECT, result.getInputType());
        assertEquals(createAttributeRequest.getRequired(), result.getRequired());
        assertEquals(createAttributeRequest.getSearchable(), result.getSearchable());
        assertEquals(createAttributeRequest.getSortScore(), result.getSortScore());
        assertEquals(createAttributeRequest.getVisible(), result.getVisible());

        // 验证交互
        verify(attributeMapper).findByName(createAttributeRequest.getName());
        verify(attributeMapper).insert(any(AttributeEntity.class));
    }

    @Test
    void testCreateAttribute_NameDuplicated() {
        // 准备数据
        when(attributeMapper.findByName(createAttributeRequest.getName())).thenReturn(attributeEntity);

        // 执行测试并验证异常
        BizException exception = assertThrows(BizException.class, () -> {
            attributeService.createAttribute(createAttributeRequest);
        });

        assertEquals(ErrorCode.ATTRIBUTE_NAME_DUPLICATED, exception.getErrorCode());
        verify(attributeMapper).findByName(createAttributeRequest.getName());
        verify(attributeMapper, never()).insert(any(AttributeEntity.class));
    }

    @Test
    void testCreateAttribute_InsertFailed() {
        // 准备数据
        when(attributeMapper.findByName(createAttributeRequest.getName())).thenReturn(null);
        when(attributeMapper.insert(any(AttributeEntity.class))).thenReturn(0);

        // 执行测试并验证异常
        BizException exception = assertThrows(BizException.class, () -> {
            attributeService.createAttribute(createAttributeRequest);
        });

        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());
        verify(attributeMapper).findByName(createAttributeRequest.getName());
        verify(attributeMapper).insert(any(AttributeEntity.class));
    }

    @Test
    void testUpdateAttribute_Success() {
        // 准备数据
        Long attributeId = 1L;
        when(attributeMapper.findById(attributeId)).thenReturn(attributeEntity);
        when(attributeMapper.update(any(AttributeEntity.class))).thenReturn(1);

        // 执行测试
        assertDoesNotThrow(() -> {
            attributeService.updateAttribute(attributeId, updateAttributeRequest);
        });

        // 验证交互
        verify(attributeMapper).findById(attributeId);
        verify(attributeMapper).update(any(AttributeEntity.class));
    }

    @Test
    void testUpdateAttribute_AttributeNotFound() {
        // 准备数据
        Long attributeId = 1L;
        when(attributeMapper.findById(attributeId)).thenReturn(null);

        // 执行测试并验证异常
        BizException exception = assertThrows(BizException.class, () -> {
            attributeService.updateAttribute(attributeId, updateAttributeRequest);
        });

        assertEquals(ErrorCode.ATTRIBUTE_NOT_FOUND, exception.getErrorCode());
        verify(attributeMapper).findById(attributeId);
        verify(attributeMapper, never()).update(any(AttributeEntity.class));
    }

    @Test
    void testUpdateAttribute_NoFieldsChanged() {
        // 准备数据
        Long attributeId = 1L;
        UpdateAttributeRequest sameRequest = new UpdateAttributeRequest();
        sameRequest.setName(attributeEntity.getName());
        sameRequest.setAttributeType(attributeEntity.getAttributeType());
        sameRequest.setInputType(attributeEntity.getInputType());
        sameRequest.setRequired(attributeEntity.getRequired());
        sameRequest.setSearchable(attributeEntity.getSearchable());
        sameRequest.setSortScore(attributeEntity.getSortScore());
        sameRequest.setVisible(attributeEntity.getVisible());
        
        when(attributeMapper.findById(attributeId)).thenReturn(attributeEntity);

        // 执行测试
        assertDoesNotThrow(() -> {
            attributeService.updateAttribute(attributeId, sameRequest);
        });

        // 验证交互（不应该调用update）
        verify(attributeMapper).findById(attributeId);
        verify(attributeMapper, never()).update(any(AttributeEntity.class));
    }

    @Test
    void testUpdateAttribute_UpdateFailed() {
        // 准备数据
        Long attributeId = 1L;
        when(attributeMapper.findById(attributeId)).thenReturn(attributeEntity);
        when(attributeMapper.update(any(AttributeEntity.class))).thenReturn(0);

        // 执行测试并验证异常
        BizException exception = assertThrows(BizException.class, () -> {
            attributeService.updateAttribute(attributeId, updateAttributeRequest);
        });

        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());
        verify(attributeMapper).findById(attributeId);
        verify(attributeMapper).update(any(AttributeEntity.class));
    }

    @Test
    void testDeleteAttribute_Success() {
        // 准备数据
        Long attributeId = 1L;
        when(attributeMapper.findById(attributeId)).thenReturn(attributeEntity);
        when(itemAttributeRelationMapper.findByAttributeId(attributeId, 0, 1)).thenReturn(new ArrayList<>());
        when(attributeMapper.deleteById(attributeId)).thenReturn(1);
        when(attributeValueMapper.countValuesByAttributeId(attributeId)).thenReturn(2);
        when(attributeValueMapper.deleteByAttributeId(attributeId)).thenReturn(2);

        // 执行测试
        assertDoesNotThrow(() -> {
            attributeService.deleteAttribute(attributeId);
        });

        // 验证交互
        verify(attributeMapper).findById(attributeId);
        verify(itemAttributeRelationMapper).findByAttributeId(attributeId, 0, 1);
        verify(attributeMapper).deleteById(attributeId);
        verify(attributeValueMapper).countValuesByAttributeId(attributeId);
        verify(attributeValueMapper).deleteByAttributeId(attributeId);
    }

    @Test
    void testDeleteAttribute_AttributeNotFound() {
        // 准备数据
        Long attributeId = 1L;
        when(attributeMapper.findById(attributeId)).thenReturn(null);

        // 执行测试并验证异常
        BizException exception = assertThrows(BizException.class, () -> {
            attributeService.deleteAttribute(attributeId);
        });

        assertEquals(ErrorCode.ATTRIBUTE_NOT_FOUND, exception.getErrorCode());
        verify(attributeMapper).findById(attributeId);
        verify(itemAttributeRelationMapper, never()).findByAttributeId(anyLong(), anyInt(), anyInt());
        verify(attributeMapper, never()).deleteById(anyLong());
    }

    @Test
    void testDeleteAttribute_ReferencedByItem() {
        // 准备数据
        Long attributeId = 1L;
        ItemAttributeRelationEntity relationEntity = new ItemAttributeRelationEntity();
        relationEntity.setItemId(100L);
        relationEntity.setAttributeId(attributeId);
        List<ItemAttributeRelationEntity> relationEntities = List.of(relationEntity);
        
        when(attributeMapper.findById(attributeId)).thenReturn(attributeEntity);
        when(itemAttributeRelationMapper.findByAttributeId(attributeId, 0, 1)).thenReturn(relationEntities);

        // 执行测试并验证异常
        BizException exception = assertThrows(BizException.class, () -> {
            attributeService.deleteAttribute(attributeId);
        });

        assertEquals(ErrorCode.ATTRIBUTE_IS_REFERENCE_BY_ITEM, exception.getErrorCode());
        verify(attributeMapper).findById(attributeId);
        verify(itemAttributeRelationMapper).findByAttributeId(attributeId, 0, 1);
        verify(attributeMapper, never()).deleteById(anyLong());
    }

    @Test
    void testDeleteAttribute_DeleteFailed() {
        // 准备数据
        Long attributeId = 1L;
        when(attributeMapper.findById(attributeId)).thenReturn(attributeEntity);
        when(itemAttributeRelationMapper.findByAttributeId(attributeId, 0, 1)).thenReturn(new ArrayList<>());
        when(attributeMapper.deleteById(attributeId)).thenReturn(0);

        // 执行测试并验证异常
        BizException exception = assertThrows(BizException.class, () -> {
            attributeService.deleteAttribute(attributeId);
        });

        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());
        verify(attributeMapper).findById(attributeId);
        verify(itemAttributeRelationMapper).findByAttributeId(attributeId, 0, 1);
        verify(attributeMapper).deleteById(attributeId);
        verify(attributeValueMapper, never()).countValuesByAttributeId(anyLong());
    }

    @Test
    void testDeleteAttribute_DeleteValuesFailed() {
        // 准备数据
        Long attributeId = 1L;
        when(attributeMapper.findById(attributeId)).thenReturn(attributeEntity);
        when(itemAttributeRelationMapper.findByAttributeId(attributeId, 0, 1)).thenReturn(new ArrayList<>());
        when(attributeMapper.deleteById(attributeId)).thenReturn(1);
        when(attributeValueMapper.countValuesByAttributeId(attributeId)).thenReturn(2);
        when(attributeValueMapper.deleteByAttributeId(attributeId)).thenReturn(1); // 删除失败

        // 执行测试并验证异常
        BizException exception = assertThrows(BizException.class, () -> {
            attributeService.deleteAttribute(attributeId);
        });

        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());
        verify(attributeMapper).findById(attributeId);
        verify(itemAttributeRelationMapper).findByAttributeId(attributeId, 0, 1);
        verify(attributeMapper).deleteById(attributeId);
        verify(attributeValueMapper).countValuesByAttributeId(attributeId);
        verify(attributeValueMapper).deleteByAttributeId(attributeId);
    }

    @Test
    void testGetAttributeById_Success() {
        // 准备数据
        Long attributeId = 1L;
        when(attributeMapper.findById(attributeId)).thenReturn(attributeEntity);

        // 执行测试
        Attribute result = attributeService.getAttributeById(attributeId);

        // 验证结果
        assertNotNull(result);
        assertEquals(attributeEntity.getId(), result.getId());
        assertEquals(attributeEntity.getName(), result.getName());
        assertEquals(AttributeType.valueOf(attributeEntity.getAttributeType()), result.getAttributeType());
        assertEquals(AttributeInputType.valueOf(attributeEntity.getInputType()), result.getInputType());
        assertEquals(attributeEntity.getRequired(), result.getRequired());
        assertEquals(attributeEntity.getSearchable(), result.getSearchable());
        assertEquals(attributeEntity.getSortScore(), result.getSortScore());
        assertEquals(attributeEntity.getVisible(), result.getVisible());

        // 验证交互
        verify(attributeMapper).findById(attributeId);
    }

    @Test
    void testGetAttributeById_NotFound() {
        // 准备数据
        Long attributeId = 1L;
        when(attributeMapper.findById(attributeId)).thenReturn(null);

        // 执行测试并验证异常
        BizException exception = assertThrows(BizException.class, () -> {
            attributeService.getAttributeById(attributeId);
        });

        assertEquals(ErrorCode.ATTRIBUTE_NOT_FOUND, exception.getErrorCode());
        verify(attributeMapper).findById(attributeId);
    }

    @Test
    void testGetAttributeByIdWithValues_SingleSelect() {
        // 准备数据 - 单选类型
        Long attributeId = 1L;
        attributeEntity.setInputType("SINGLE_SELECT");
        List<AttributeValueEntity> valueEntities = List.of(attributeValueEntity);
        
        when(attributeMapper.findById(attributeId)).thenReturn(attributeEntity);
        when(attributeValueMapper.findAllAttributeValuesByAttributeId(attributeId)).thenReturn(valueEntities);

        // 执行测试
        Attribute result = attributeService.getAttributeByIdWithValues(attributeId);

        // 验证结果
        assertNotNull(result);
        assertNotNull(result.getValues());
        assertEquals(1, result.getValues().size());
        assertEquals(attributeValueEntity.getValue(), result.getValues().get(0).getValue());

        // 验证交互
        verify(attributeMapper).findById(attributeId);
        verify(attributeValueMapper).findAllAttributeValuesByAttributeId(attributeId);
    }

    @Test
    void testGetAttributeByIdWithValues_MultiSelect() {
        // 准备数据 - 多选类型
        Long attributeId = 1L;
        attributeEntity.setInputType("MULTI_SELECT");
        List<AttributeValueEntity> valueEntities = List.of(attributeValueEntity);
        
        when(attributeMapper.findById(attributeId)).thenReturn(attributeEntity);
        when(attributeValueMapper.findAllAttributeValuesByAttributeId(attributeId)).thenReturn(valueEntities);

        // 执行测试
        Attribute result = attributeService.getAttributeByIdWithValues(attributeId);

        // 验证结果
        assertNotNull(result);
        assertNotNull(result.getValues());
        assertEquals(1, result.getValues().size());

        // 验证交互
        verify(attributeMapper).findById(attributeId);
        verify(attributeValueMapper).findAllAttributeValuesByAttributeId(attributeId);
    }

    @Test
    void testGetAttributeByIdWithValues_InputType() {
        // 准备数据 - 输入类型（不需要属性值）
        Long attributeId = 1L;
        attributeEntity.setInputType("INPUT");
        
        when(attributeMapper.findById(attributeId)).thenReturn(attributeEntity);

        // 执行测试
        Attribute result = attributeService.getAttributeByIdWithValues(attributeId);

        // 验证结果
        assertNotNull(result);
        assertNull(result.getValues()); // 输入类型不设置属性值

        // 验证交互
        verify(attributeMapper).findById(attributeId);
        verify(attributeValueMapper, never()).findAllAttributeValuesByAttributeId(anyLong());
    }

    @Test
    void testFindAllAttributeValuesByAttributeId_SingleSelect() {
        // 准备数据
        Long attributeId = 1L;
        attributeEntity.setInputType("SINGLE_SELECT");
        List<AttributeValueEntity> valueEntities = List.of(attributeValueEntity);
        
        when(attributeMapper.findById(attributeId)).thenReturn(attributeEntity);
        when(attributeValueMapper.findAllAttributeValuesByAttributeId(attributeId)).thenReturn(valueEntities);

        // 执行测试
        List<AttributeValue> result = attributeService.findAllAttributeValuesByAttributeId(attributeId);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(attributeValueEntity.getId(), result.get(0).getId());
        assertEquals(attributeValueEntity.getAttributeId(), result.get(0).getAttributeId());
        assertEquals(attributeValueEntity.getValue(), result.get(0).getValue());
        assertEquals(attributeValueEntity.getSortScore(), result.get(0).getSortScore());

        // 验证交互
        verify(attributeMapper).findById(attributeId);
        verify(attributeValueMapper).findAllAttributeValuesByAttributeId(attributeId);
    }

    @Test
    void testFindAllAttributeValuesByAttributeId_MultiSelect() {
        // 准备数据
        Long attributeId = 1L;
        attributeEntity.setInputType("MULTI_SELECT");
        List<AttributeValueEntity> valueEntities = List.of(attributeValueEntity);
        
        when(attributeMapper.findById(attributeId)).thenReturn(attributeEntity);
        when(attributeValueMapper.findAllAttributeValuesByAttributeId(attributeId)).thenReturn(valueEntities);

        // 执行测试
        List<AttributeValue> result = attributeService.findAllAttributeValuesByAttributeId(attributeId);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());

        // 验证交互
        verify(attributeMapper).findById(attributeId);
        verify(attributeValueMapper).findAllAttributeValuesByAttributeId(attributeId);
    }

    @Test
    void testFindAllAttributeValuesByAttributeId_InputType() {
        // 准备数据 - 输入类型返回空列表
        Long attributeId = 1L;
        attributeEntity.setInputType("INPUT");
        
        when(attributeMapper.findById(attributeId)).thenReturn(attributeEntity);

        // 执行测试
        List<AttributeValue> result = attributeService.findAllAttributeValuesByAttributeId(attributeId);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isEmpty());

        // 验证交互
        verify(attributeMapper).findById(attributeId);
        verify(attributeValueMapper, never()).findAllAttributeValuesByAttributeId(anyLong());
    }

    @Test
    void testGetAttributeValueById_Success() {
        // 准备数据
        Long valueId = 1L;
        when(attributeValueMapper.findById(valueId)).thenReturn(attributeValueEntity);

        // 执行测试
        AttributeValue result = attributeService.getAttributeValueById(valueId);

        // 验证结果
        assertNotNull(result);
        assertEquals(attributeValueEntity.getId(), result.getId());
        assertEquals(attributeValueEntity.getAttributeId(), result.getAttributeId());
        assertEquals(attributeValueEntity.getValue(), result.getValue());
        assertEquals(attributeValueEntity.getSortScore(), result.getSortScore());

        // 验证交互
        verify(attributeValueMapper).findById(valueId);
    }

    @Test
    void testGetAttributeValueById_NotFound() {
        // 准备数据
        Long valueId = 1L;
        when(attributeValueMapper.findById(valueId)).thenReturn(null);

        // 执行测试并验证异常
        BizException exception = assertThrows(BizException.class, () -> {
            attributeService.getAttributeValueById(valueId);
        });

        assertEquals(ErrorCode.ATTRIBUTE_VALUE_NOT_FOUND, exception.getErrorCode());
        verify(attributeValueMapper).findById(valueId);
    }

    @Test
    void testEnsureItemAttributes_NewRelations() {
        // 准备数据 - 新增场景（之前没有关系）
        Long itemId = 100L;
        Long skuId = 200L;
        List<ItemAttributeRequest> attributes = List.of(itemAttributeRequest);
        
        when(itemAttributeRelationMapper.findByItemIdAndSkuId(itemId, skuId)).thenReturn(new ArrayList<>());
        when(itemAttributeRelationMapper.batchInsert(anyList())).thenReturn(1);

        // 执行测试
        assertDoesNotThrow(() -> {
            attributeService.ensureItemAttributes(itemId, skuId, attributes);
        });

        // 验证交互
        verify(itemAttributeRelationMapper).findByItemIdAndSkuId(itemId, skuId);
        verify(itemAttributeRelationMapper).batchInsert(anyList());
        verify(itemAttributeRelationMapper, never()).deleteByItemIdAndAttributeIds(anyLong(), anyList());
    }

    @Test
    void testEnsureItemAttributes_UpdateExistingRelations() {
        // 准备数据 - 更新场景（已存在关系）
        Long itemId = 100L;
        Long skuId = 200L;
        Long existingAttributeId = 1L;
        Long newAttributeId = 2L;
        
        // 现有关系
        ItemAttributeRelationEntity existingRelation = new ItemAttributeRelationEntity();
        existingRelation.setItemId(itemId);
        existingRelation.setSkuId(skuId);
        existingRelation.setAttributeId(existingAttributeId);
        List<ItemAttributeRelationEntity> existingRelations = List.of(existingRelation);
        
        // 新的属性请求
        ItemAttributeRequest newAttributeRequest = new ItemAttributeRequest();
        newAttributeRequest.setAttributeId(newAttributeId);
        newAttributeRequest.setAttributeValueId(2L);
        newAttributeRequest.setValue("新测试值");
        List<ItemAttributeRequest> attributes = List.of(newAttributeRequest);
        
        when(itemAttributeRelationMapper.findByItemIdAndSkuId(itemId, skuId)).thenReturn(existingRelations);
        when(itemAttributeRelationMapper.deleteByItemIdAndAttributeIds(eq(itemId), anyList())).thenReturn(1);
        when(itemAttributeRelationMapper.batchInsert(anyList())).thenReturn(1);

        // 执行测试
        assertDoesNotThrow(() -> {
            attributeService.ensureItemAttributes(itemId, skuId, attributes);
        });

        // 验证交互
        verify(itemAttributeRelationMapper).findByItemIdAndSkuId(itemId, skuId);
        verify(itemAttributeRelationMapper).deleteByItemIdAndAttributeIds(eq(itemId), anyList());
        verify(itemAttributeRelationMapper).batchInsert(anyList());
    }

    @Test
    void testEnsureItemAttributes_NoNewRelationsToInsert() {
        // 准备数据 - 没有新的关系需要插入
        Long itemId = 100L;
        Long skuId = 200L;
        Long existingAttributeId = 1L;
        
        // 现有关系
        ItemAttributeRelationEntity existingRelation = new ItemAttributeRelationEntity();
        existingRelation.setItemId(itemId);
        existingRelation.setSkuId(skuId);
        existingRelation.setAttributeId(existingAttributeId);
        List<ItemAttributeRelationEntity> existingRelations = List.of(existingRelation);
        
        // 相同的属性请求（已存在）
        ItemAttributeRequest sameAttributeRequest = new ItemAttributeRequest();
        sameAttributeRequest.setAttributeId(existingAttributeId);
        sameAttributeRequest.setAttributeValueId(1L);
        sameAttributeRequest.setValue("测试值");
        List<ItemAttributeRequest> attributes = List.of(sameAttributeRequest);
        
        when(itemAttributeRelationMapper.findByItemIdAndSkuId(itemId, skuId)).thenReturn(existingRelations);
        when(itemAttributeRelationMapper.deleteByItemIdAndAttributeIds(eq(itemId), anyList())).thenReturn(1);

        // 执行测试
        assertDoesNotThrow(() -> {
            attributeService.ensureItemAttributes(itemId, skuId, attributes);
        });

        // 验证交互
        verify(itemAttributeRelationMapper).findByItemIdAndSkuId(itemId, skuId);
        verify(itemAttributeRelationMapper).deleteByItemIdAndAttributeIds(eq(itemId), anyList());
        verify(itemAttributeRelationMapper, never()).batchInsert(anyList()); // 不应该调用插入
    }

    @Test
    void testEnsureItemAttributes_DeleteFailed() {
        // 准备数据 - 删除失败场景
        Long itemId = 100L;
        Long skuId = 200L;
        Long existingAttributeId = 1L;
        
        ItemAttributeRelationEntity existingRelation = new ItemAttributeRelationEntity();
        existingRelation.setItemId(itemId);
        existingRelation.setSkuId(skuId);
        existingRelation.setAttributeId(existingAttributeId);
        List<ItemAttributeRelationEntity> existingRelations = List.of(existingRelation);
        
        ItemAttributeRequest newAttributeRequest = new ItemAttributeRequest();
        newAttributeRequest.setAttributeId(2L);
        newAttributeRequest.setAttributeValueId(2L);
        newAttributeRequest.setValue("新测试值");
        List<ItemAttributeRequest> attributes = List.of(newAttributeRequest);
        
        when(itemAttributeRelationMapper.findByItemIdAndSkuId(itemId, skuId)).thenReturn(existingRelations);
        when(itemAttributeRelationMapper.deleteByItemIdAndAttributeIds(eq(itemId), anyList())).thenReturn(0); // 删除失败

        // 执行测试并验证异常
        BizException exception = assertThrows(BizException.class, () -> {
            attributeService.ensureItemAttributes(itemId, skuId, attributes);
        });

        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());
        verify(itemAttributeRelationMapper).findByItemIdAndSkuId(itemId, skuId);
        verify(itemAttributeRelationMapper).deleteByItemIdAndAttributeIds(eq(itemId), anyList());
        verify(itemAttributeRelationMapper, never()).batchInsert(anyList());
    }

    @Test
    void testEnsureItemAttributes_BatchInsertFailed() {
        // 准备数据 - 批量插入失败场景
        Long itemId = 100L;
        Long skuId = 200L;
        List<ItemAttributeRequest> attributes = List.of(itemAttributeRequest);
        
        when(itemAttributeRelationMapper.findByItemIdAndSkuId(itemId, skuId)).thenReturn(new ArrayList<>());
        when(itemAttributeRelationMapper.batchInsert(anyList())).thenReturn(0); // 插入失败

        // 执行测试并验证异常
        BizException exception = assertThrows(BizException.class, () -> {
            attributeService.ensureItemAttributes(itemId, skuId, attributes);
        });

        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());
        verify(itemAttributeRelationMapper).findByItemIdAndSkuId(itemId, skuId);
        verify(itemAttributeRelationMapper).batchInsert(anyList());
    }
}