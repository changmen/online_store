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
import com.example.onlinestore.utils.CommonUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * AttributeServiceImpl 单元测试类
 * 
 * 测试目标：
 * - 验证业务逻辑正确性
 * - 验证异常处理机制
 * - 验证依赖组件交互
 * - 验证边界条件处理
 * 
 * 技术栈：
 * - JUnit 5 + Mockito
 * - AssertJ 断言库
 * - MockedStatic 静态方法模拟
 */
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

    private TestDataFactory testDataFactory;

    @BeforeEach
    void setUp() {
        testDataFactory = new TestDataFactory();
    }

    // ==================== createAttribute 方法测试 ====================

    @Test
    void testCreateAttribute_Success() {
        // TODO: 实现创建属性成功的测试用例
    }

    @Test
    void testCreateAttribute_NameDuplicated() {
        // TODO: 实现属性名称重复异常测试
    }

    @Test
    void testCreateAttribute_InsertFailed() {
        // TODO: 实现数据库插入失败测试
    }

    // ==================== updateAttribute 方法测试 ====================

    @Test
    void testUpdateAttribute_Success() {
        // TODO: 实现更新属性成功测试
    }

    @Test
    void testUpdateAttribute_NoChange() {
        // TODO: 实现无字段变更测试
    }

    @Test
    void testUpdateAttribute_NotFound() {
        // TODO: 实现属性不存在异常测试
    }

    // ==================== deleteAttribute 方法测试 ====================

    @Test
    void testDeleteAttribute_Success() {
        // TODO: 实现删除属性成功测试
    }

    @Test
    void testDeleteAttribute_Referenced() {
        // TODO: 实现属性被引用异常测试
    }

    @Test
    void testDeleteAttribute_NotFound() {
        // TODO: 实现属性不存在异常测试
    }

    // ==================== 查询方法测试 ====================

    @Test
    void testGetAttributeById_Success() {
        // TODO: 实现根据ID查询属性成功测试
    }

    @Test
    void testGetAttributeById_NotFound() {
        // TODO: 实现根据ID查询属性不存在测试
    }

    @Test
    void testGetAttributeByIdWithValues_Success() {
        // TODO: 实现查询属性包含属性值测试
    }

    @Test
    void testFindAllAttributeValuesByAttributeId_SelectType() {
        // Arrange
        Long attributeId = TestDataFactory.TEST_ATTRIBUTE_ID;
        AttributeEntity attributeEntity = testDataFactory.buildAttributeEntity();
        List<AttributeValueEntity> valueEntities = testDataFactory.buildAttributeValueEntityList();
        
        when(attributeMapper.findById(attributeId)).thenReturn(attributeEntity);
        when(attributeValueMapper.findAllAttributeValuesByAttributeId(attributeId)).thenReturn(valueEntities);
        
        // Act
        List<AttributeValue> result = attributeService.findAllAttributeValuesByAttributeId(attributeId);
        
        // Assert
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(TestDataFactory.TEST_ATTRIBUTE_VALUE_ID);
        assertThat(result.get(0).getValue()).isEqualTo(TestDataFactory.TEST_ATTRIBUTE_VALUE);
        
        verify(attributeMapper).findById(attributeId);
        verify(attributeValueMapper).findAllAttributeValuesByAttributeId(attributeId);
    }

    @Test
    void testFindAllAttributeValuesByAttributeId_InputType() {
        // Arrange
        Long attributeId = TestDataFactory.TEST_ATTRIBUTE_ID;
        AttributeEntity attributeEntity = testDataFactory.buildInputTypeAttributeEntity();
        
        when(attributeMapper.findById(attributeId)).thenReturn(attributeEntity);
        
        // Act
        List<AttributeValue> result = attributeService.findAllAttributeValuesByAttributeId(attributeId);
        
        // Assert
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        
        verify(attributeMapper).findById(attributeId);
        verify(attributeValueMapper, never()).findAllAttributeValuesByAttributeId(attributeId);
    }

    @Test
    void testGetAttributeValueById_Success() {
        // Arrange
        Long valueId = TestDataFactory.TEST_ATTRIBUTE_VALUE_ID;
        AttributeValueEntity valueEntity = testDataFactory.buildAttributeValueEntity();
        
        when(attributeValueMapper.findById(valueId)).thenReturn(valueEntity);
        
        // Act
        AttributeValue result = attributeService.getAttributeValueById(valueId);
        
        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(valueEntity.getId());
        assertThat(result.getAttributeId()).isEqualTo(valueEntity.getAttributeId());
        assertThat(result.getValue()).isEqualTo(valueEntity.getValue());
        assertThat(result.getSortScore()).isEqualTo(valueEntity.getSortScore());
        
        verify(attributeValueMapper).findById(valueId);
    }

    @Test
    void testGetAttributeValueById_NotFound() {
        // Arrange
        Long valueId = TestDataFactory.TEST_ATTRIBUTE_VALUE_ID;
        
        when(attributeValueMapper.findById(valueId)).thenReturn(null);
        
        // Act & Assert
        assertThatThrownBy(() -> attributeService.getAttributeValueById(valueId))
                .isInstanceOf(BizException.class)
                .hasMessageContaining(ErrorCode.ATTRIBUTE_VALUE_NOT_FOUND.getCode());
        
        verify(attributeValueMapper).findById(valueId);
    }

    // ==================== ensureItemAttributes 方法测试 ====================

    @Test
    void testEnsureItemAttributes_NewRelations() {
        // Arrange
        Long itemId = TestDataFactory.TEST_ITEM_ID;
        Long skuId = TestDataFactory.TEST_SKU_ID;
        List<ItemAttributeRequest> attributes = testDataFactory.buildItemAttributeRequestList();
        
        when(itemAttributeRelationMapper.findByItemIdAndSkuId(itemId, skuId))
                .thenReturn(Collections.emptyList());
        when(itemAttributeRelationMapper.batchInsert(anyList())).thenReturn(2);
        
        // Act
        attributeService.ensureItemAttributes(itemId, skuId, attributes);
        
        // Assert
        verify(itemAttributeRelationMapper).findByItemIdAndSkuId(itemId, skuId);
        verify(itemAttributeRelationMapper).batchInsert(argThat(list -> list.size() == 2));
        verify(itemAttributeRelationMapper, never()).deleteByItemIdAndAttributeIds(any(), any());
    }

    @Test
    void testEnsureItemAttributes_UpdateRelations() {
        // Arrange
        Long itemId = TestDataFactory.TEST_ITEM_ID;
        Long skuId = TestDataFactory.TEST_SKU_ID;
        List<ItemAttributeRequest> attributes = testDataFactory.buildItemAttributeRequestList();
        List<ItemAttributeRelationEntity> existingRelations = testDataFactory.buildItemAttributeRelationEntityList();
        
        when(itemAttributeRelationMapper.findByItemIdAndSkuId(itemId, skuId))
                .thenReturn(existingRelations);
        when(itemAttributeRelationMapper.deleteByItemIdAndAttributeIds(eq(itemId), anyList()))
                .thenReturn(2);
        when(itemAttributeRelationMapper.batchInsert(anyList())).thenReturn(0); // 无新增关系
        
        // Act
        attributeService.ensureItemAttributes(itemId, skuId, attributes);
        
        // Assert
        verify(itemAttributeRelationMapper).findByItemIdAndSkuId(itemId, skuId);
        verify(itemAttributeRelationMapper).deleteByItemIdAndAttributeIds(eq(itemId), 
                argThat(list -> list.size() == 2));
        // 由于新请求的attributeId与已存在的相同，所以不会有新增关系
        verify(itemAttributeRelationMapper, never()).batchInsert(anyList());
    }

    @Test
    void testEnsureItemAttributes_NoNewRelations() {
        // Arrange
        Long itemId = TestDataFactory.TEST_ITEM_ID;
        Long skuId = TestDataFactory.TEST_SKU_ID;
        List<ItemAttributeRequest> attributes = Collections.emptyList();
        
        when(itemAttributeRelationMapper.findByItemIdAndSkuId(itemId, skuId))
                .thenReturn(Collections.emptyList());
        
        // Act
        attributeService.ensureItemAttributes(itemId, skuId, attributes);
        
        // Assert
        verify(itemAttributeRelationMapper).findByItemIdAndSkuId(itemId, skuId);
        verify(itemAttributeRelationMapper, never()).batchInsert(anyList());
        verify(itemAttributeRelationMapper, never()).deleteByItemIdAndAttributeIds(any(), any());
    }

    @Test
    void testEnsureItemAttributes_DeleteFailed() {
        // Arrange
        Long itemId = TestDataFactory.TEST_ITEM_ID;
        Long skuId = TestDataFactory.TEST_SKU_ID;
        List<ItemAttributeRequest> attributes = testDataFactory.buildItemAttributeRequestList();
        List<ItemAttributeRelationEntity> existingRelations = testDataFactory.buildItemAttributeRelationEntityList();
        
        when(itemAttributeRelationMapper.findByItemIdAndSkuId(itemId, skuId))
                .thenReturn(existingRelations);
        when(itemAttributeRelationMapper.deleteByItemIdAndAttributeIds(eq(itemId), anyList()))
                .thenReturn(1); // 期望删除2条，实际1条
        
        // Act & Assert
        assertThatThrownBy(() -> attributeService.ensureItemAttributes(itemId, skuId, attributes))
                .isInstanceOf(BizException.class)
                .hasMessageContaining(ErrorCode.INTERNAL_SERVER_ERROR.getCode());
        
        verify(itemAttributeRelationMapper).findByItemIdAndSkuId(itemId, skuId);
        verify(itemAttributeRelationMapper).deleteByItemIdAndAttributeIds(eq(itemId), anyList());
        verify(itemAttributeRelationMapper, never()).batchInsert(anyList());
    }

    @Test
    void testEnsureItemAttributes_InsertFailed() {
        // Arrange
        Long itemId = TestDataFactory.TEST_ITEM_ID;
        Long skuId = TestDataFactory.TEST_SKU_ID;
        List<ItemAttributeRequest> attributes = testDataFactory.buildItemAttributeRequestList();
        
        when(itemAttributeRelationMapper.findByItemIdAndSkuId(itemId, skuId))
                .thenReturn(Collections.emptyList());
        when(itemAttributeRelationMapper.batchInsert(anyList())).thenReturn(1); // 期望插入2条，实际1条
        
        // Act & Assert
        assertThatThrownBy(() -> attributeService.ensureItemAttributes(itemId, skuId, attributes))
                .isInstanceOf(BizException.class)
                .hasMessageContaining(ErrorCode.INTERNAL_SERVER_ERROR.getCode());
        
        verify(itemAttributeRelationMapper).findByItemIdAndSkuId(itemId, skuId);
        verify(itemAttributeRelationMapper).batchInsert(anyList());
    }

    @Test
    void testEnsureItemAttributes_MixedRelations() {
        // Arrange - 测试部分更新部分新增的场景
        Long itemId = TestDataFactory.TEST_ITEM_ID;
        Long skuId = TestDataFactory.TEST_SKU_ID;
        
        // 创建包含不同属性ID的请求
        ItemAttributeRequest newRequest1 = new ItemAttributeRequest();
        newRequest1.setAttributeId(1L);
        newRequest1.setAttributeValueId(10L);
        newRequest1.setValue("新值1");
        
        ItemAttributeRequest newRequest2 = new ItemAttributeRequest();
        newRequest2.setAttributeId(3L); // 新的属性ID
        newRequest2.setAttributeValueId(30L);
        newRequest2.setValue("新值2");
        
        List<ItemAttributeRequest> attributes = Arrays.asList(newRequest1, newRequest2);
        
        // 已存在的关系包含属性ID=1和2
        ItemAttributeRelationEntity existing1 = new ItemAttributeRelationEntity();
        existing1.setAttributeId(1L);
        ItemAttributeRelationEntity existing2 = new ItemAttributeRelationEntity();
        existing2.setAttributeId(2L);
        List<ItemAttributeRelationEntity> existingRelations = Arrays.asList(existing1, existing2);
        
        when(itemAttributeRelationMapper.findByItemIdAndSkuId(itemId, skuId))
                .thenReturn(existingRelations);
        when(itemAttributeRelationMapper.deleteByItemIdAndAttributeIds(eq(itemId), 
                argThat(list -> list.contains(1L) && list.contains(2L))))
                .thenReturn(2);
        when(itemAttributeRelationMapper.batchInsert(argThat(list -> 
                list.size() == 1 && 
                list.stream().anyMatch(rel -> rel.getAttributeId().equals(3L)))))
                .thenReturn(1);
        
        // Act
        attributeService.ensureItemAttributes(itemId, skuId, attributes);
        
        // Assert
        verify(itemAttributeRelationMapper).findByItemIdAndSkuId(itemId, skuId);
        verify(itemAttributeRelationMapper).deleteByItemIdAndAttributeIds(eq(itemId), anyList());
        verify(itemAttributeRelationMapper).batchInsert(argThat(list -> list.size() == 1));
    }
}