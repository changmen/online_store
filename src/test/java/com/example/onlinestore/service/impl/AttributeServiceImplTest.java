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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
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
@DisplayName("AttributeServiceImpl 单元测试")
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
    private AttributeValueEntity attributeValueEntity;
    private Attribute attribute;
    private AttributeValue attributeValue;

    @BeforeEach
    void setUp() {
        // 初始化测试数据
        createAttributeRequest = new CreateAttributeRequest();
        createAttributeRequest.setName("颜色");
        createAttributeRequest.setAttributeType("SKU");
        createAttributeRequest.setInputType("SINGLE_SELECT");
        createAttributeRequest.setRequired(1);
        createAttributeRequest.setSearchable(1);
        createAttributeRequest.setSortScore(100);
        createAttributeRequest.setVisible(1);

        updateAttributeRequest = new UpdateAttributeRequest();
        updateAttributeRequest.setName("尺寸");
        updateAttributeRequest.setAttributeType("SALE");
        updateAttributeRequest.setInputType("MULTI_SELECT");
        updateAttributeRequest.setRequired(0);
        updateAttributeRequest.setSearchable(0);
        updateAttributeRequest.setSortScore(200);
        updateAttributeRequest.setVisible(0);

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

        attributeValueEntity = new AttributeValueEntity();
        attributeValueEntity.setId(1L);
        attributeValueEntity.setAttributeId(1L);
        attributeValueEntity.setValue("红色");
        attributeValueEntity.setSortScore(100);

        attribute = new Attribute();
        attribute.setId(1L);
        attribute.setName("颜色");
        attribute.setAttributeType(AttributeType.SKU);
        attribute.setInputType(AttributeInputType.SINGLE_SELECT);
        attribute.setRequired(1);
        attribute.setSearchable(1);
        attribute.setSortScore(100);
        attribute.setVisible(1);

        attributeValue = new AttributeValue();
        attributeValue.setId(1L);
        attributeValue.setAttributeId(1L);
        attributeValue.setValue("红色");
        attributeValue.setSortScore(100);
    }

    @Nested
    @DisplayName("createAttribute 方法测试")
    class CreateAttributeTest {

        @Test
        @DisplayName("成功创建属性")
        void createAttribute_Success() {
            // Given
            when(attributeMapper.findByName("颜色")).thenReturn(null);
            when(attributeMapper.insert(any(AttributeEntity.class))).thenReturn(1);

            // When
            Attribute result = attributeService.createAttribute(createAttributeRequest);

            // Then
            assertNotNull(result);
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
        @DisplayName("属性名称重复时抛出异常")
        void createAttribute_DuplicateName_ThrowsException() {
            // Given
            when(attributeMapper.findByName("颜色")).thenReturn(attributeEntity);

            // When & Then
            BizException exception = assertThrows(BizException.class, 
                () -> attributeService.createAttribute(createAttributeRequest));
            assertEquals(ErrorCode.ATTRIBUTE_NAME_DUPLICATED, exception.getErrorCode());

            verify(attributeMapper).findByName("颜色");
            verify(attributeMapper, never()).insert(any(AttributeEntity.class));
        }

        @Test
        @DisplayName("插入数据库失败时抛出异常")
        void createAttribute_InsertFailed_ThrowsException() {
            // Given
            when(attributeMapper.findByName("颜色")).thenReturn(null);
            when(attributeMapper.insert(any(AttributeEntity.class))).thenReturn(0);

            // When & Then
            BizException exception = assertThrows(BizException.class, 
                () -> attributeService.createAttribute(createAttributeRequest));
            assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());

            verify(attributeMapper).findByName("颜色");
            verify(attributeMapper).insert(any(AttributeEntity.class));
        }
    }

    @Nested
    @DisplayName("updateAttribute 方法测试")
    class UpdateAttributeTest {

        @Test
        @DisplayName("成功更新属性")
        void updateAttribute_Success() {
            // Given
            when(attributeMapper.findById(1L)).thenReturn(attributeEntity);
            when(attributeMapper.update(any(AttributeEntity.class))).thenReturn(1);

            try (MockedStatic<CommonUtils> mockedCommonUtils = Mockito.mockStatic(CommonUtils.class)) {
                mockedCommonUtils.when(() -> CommonUtils.updateFieldIfChanged(any(), any(), any()))
                    .thenReturn(true);

                // When
                attributeService.updateAttribute(1L, updateAttributeRequest);

                // Then
                verify(attributeMapper).findById(1L);
                verify(attributeMapper).update(any(AttributeEntity.class));
            }
        }

        @Test
        @DisplayName("属性不存在时抛出异常")
        void updateAttribute_AttributeNotFound_ThrowsException() {
            // Given
            when(attributeMapper.findById(1L)).thenReturn(null);

            // When & Then
            BizException exception = assertThrows(BizException.class, 
                () -> attributeService.updateAttribute(1L, updateAttributeRequest));
            assertEquals(ErrorCode.ATTRIBUTE_NOT_FOUND, exception.getErrorCode());

            verify(attributeMapper).findById(1L);
            verify(attributeMapper, never()).update(any(AttributeEntity.class));
        }

        @Test
        @DisplayName("没有字段变更时不执行更新")
        void updateAttribute_NoFieldChanged_NoUpdate() {
            // Given
            when(attributeMapper.findById(1L)).thenReturn(attributeEntity);

            try (MockedStatic<CommonUtils> mockedCommonUtils = Mockito.mockStatic(CommonUtils.class)) {
                mockedCommonUtils.when(() -> CommonUtils.updateFieldIfChanged(any(), any(), any()))
                    .thenReturn(false);

                // When
                attributeService.updateAttribute(1L, updateAttributeRequest);

                // Then
                verify(attributeMapper).findById(1L);
                verify(attributeMapper, never()).update(any(AttributeEntity.class));
            }
        }

        @Test
        @DisplayName("更新数据库失败时抛出异常")
        void updateAttribute_UpdateFailed_ThrowsException() {
            // Given
            when(attributeMapper.findById(1L)).thenReturn(attributeEntity);
            when(attributeMapper.update(any(AttributeEntity.class))).thenReturn(0);

            try (MockedStatic<CommonUtils> mockedCommonUtils = Mockito.mockStatic(CommonUtils.class)) {
                mockedCommonUtils.when(() -> CommonUtils.updateFieldIfChanged(any(), any(), any()))
                    .thenReturn(true);

                // When & Then
                BizException exception = assertThrows(BizException.class, 
                    () -> attributeService.updateAttribute(1L, updateAttributeRequest));
                assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());

                verify(attributeMapper).findById(1L);
                verify(attributeMapper).update(any(AttributeEntity.class));
            }
        }
    }

    @Nested
    @DisplayName("deleteAttribute 方法测试")
    class DeleteAttributeTest {

        @Test
        @DisplayName("成功删除属性")
        void deleteAttribute_Success() {
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
        @DisplayName("属性不存在时抛出异常")
        void deleteAttribute_AttributeNotFound_ThrowsException() {
            // Given
            when(attributeMapper.findById(1L)).thenReturn(null);

            // When & Then
            BizException exception = assertThrows(BizException.class, 
                () -> attributeService.deleteAttribute(1L));
            assertEquals(ErrorCode.ATTRIBUTE_NOT_FOUND, exception.getErrorCode());

            verify(attributeMapper).findById(1L);
            verify(itemAttributeRelationMapper, never()).findByAttributeId(anyLong(), anyInt(), anyInt());
        }

        @Test
        @DisplayName("属性被商品引用时抛出异常")
        void deleteAttribute_AttributeIsReferenced_ThrowsException() {
            // Given
            ItemAttributeRelationEntity relationEntity = new ItemAttributeRelationEntity();
            relationEntity.setItemId(100L);
            relationEntity.setAttributeId(1L);
            List<ItemAttributeRelationEntity> relationEntities = Arrays.asList(relationEntity);

            when(attributeMapper.findById(1L)).thenReturn(attributeEntity);
            when(itemAttributeRelationMapper.findByAttributeId(1L, 0, 1)).thenReturn(relationEntities);

            // When & Then
            BizException exception = assertThrows(BizException.class, 
                () -> attributeService.deleteAttribute(1L));
            assertEquals(ErrorCode.ATTRIBUTE_IS_REFERENCE_BY_ITEM, exception.getErrorCode());

            verify(attributeMapper).findById(1L);
            verify(itemAttributeRelationMapper).findByAttributeId(1L, 0, 1);
            verify(attributeMapper, never()).deleteById(anyLong());
        }

        @Test
        @DisplayName("删除属性失败时抛出异常")
        void deleteAttribute_DeleteFailed_ThrowsException() {
            // Given
            when(attributeMapper.findById(1L)).thenReturn(attributeEntity);
            when(itemAttributeRelationMapper.findByAttributeId(1L, 0, 1)).thenReturn(Collections.emptyList());
            when(attributeMapper.deleteById(1L)).thenReturn(0);

            // When & Then
            BizException exception = assertThrows(BizException.class, 
                () -> attributeService.deleteAttribute(1L));
            assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());

            verify(attributeMapper).deleteById(1L);
        }

        @Test
        @DisplayName("删除属性值失败时抛出异常")
        void deleteAttribute_DeleteValuesFailed_ThrowsException() {
            // Given
            when(attributeMapper.findById(1L)).thenReturn(attributeEntity);
            when(itemAttributeRelationMapper.findByAttributeId(1L, 0, 1)).thenReturn(Collections.emptyList());
            when(attributeMapper.deleteById(1L)).thenReturn(1);
            when(attributeValueMapper.countValuesByAttributeId(1L)).thenReturn(2);
            when(attributeValueMapper.deleteByAttributeId(1L)).thenReturn(1); // 实际删除数量与预期不符

            // When & Then
            BizException exception = assertThrows(BizException.class, 
                () -> attributeService.deleteAttribute(1L));
            assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());

            verify(attributeValueMapper).deleteByAttributeId(1L);
        }
    }

    @Nested
    @DisplayName("getAttributeById 方法测试")
    class GetAttributeByIdTest {

        @Test
        @DisplayName("成功获取属性")
        void getAttributeById_Success() {
            // Given
            when(attributeMapper.findById(1L)).thenReturn(attributeEntity);

            // When
            Attribute result = attributeService.getAttributeById(1L);

            // Then
            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals("颜色", result.getName());
            assertEquals(AttributeType.SKU, result.getAttributeType());
            assertEquals(AttributeInputType.SINGLE_SELECT, result.getInputType());

            verify(attributeMapper).findById(1L);
        }

        @Test
        @DisplayName("属性不存在时抛出异常")
        void getAttributeById_NotFound_ThrowsException() {
            // Given
            when(attributeMapper.findById(1L)).thenReturn(null);

            // When & Then
            BizException exception = assertThrows(BizException.class, 
                () -> attributeService.getAttributeById(1L));
            assertEquals(ErrorCode.ATTRIBUTE_NOT_FOUND, exception.getErrorCode());

            verify(attributeMapper).findById(1L);
        }
    }

    @Nested
    @DisplayName("getAttributeByIdWithValues 方法测试")
    class GetAttributeByIdWithValuesTest {

        @Test
        @DisplayName("获取单选属性及其值")
        void getAttributeByIdWithValues_SingleSelect_Success() {
            // Given
            attributeEntity.setInputType("SINGLE_SELECT");
            List<AttributeValueEntity> valueEntities = Arrays.asList(attributeValueEntity);

            when(attributeMapper.findById(1L)).thenReturn(attributeEntity);
            when(attributeValueMapper.findAllAttributeValuesByAttributeId(1L)).thenReturn(valueEntities);

            // When
            Attribute result = attributeService.getAttributeByIdWithValues(1L);

            // Then
            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals(AttributeInputType.SINGLE_SELECT, result.getInputType());
            assertNotNull(result.getValues());
            assertEquals(1, result.getValues().size());
            assertEquals("红色", result.getValues().get(0).getValue());

            verify(attributeMapper).findById(1L);
            verify(attributeValueMapper).findAllAttributeValuesByAttributeId(1L);
        }

        @Test
        @DisplayName("获取多选属性及其值")
        void getAttributeByIdWithValues_MultiSelect_Success() {
            // Given
            attributeEntity.setInputType("MULTI_SELECT");
            List<AttributeValueEntity> valueEntities = Arrays.asList(attributeValueEntity);

            when(attributeMapper.findById(1L)).thenReturn(attributeEntity);
            when(attributeValueMapper.findAllAttributeValuesByAttributeId(1L)).thenReturn(valueEntities);

            // When
            Attribute result = attributeService.getAttributeByIdWithValues(1L);

            // Then
            assertNotNull(result);
            assertEquals(AttributeInputType.MULTI_SELECT, result.getInputType());
            assertNotNull(result.getValues());
            assertEquals(1, result.getValues().size());

            verify(attributeValueMapper).findAllAttributeValuesByAttributeId(1L);
        }

        @Test
        @DisplayName("获取输入型属性不包含值")
        void getAttributeByIdWithValues_InputType_NoValues() {
            // Given
            attributeEntity.setInputType("INPUT");

            when(attributeMapper.findById(1L)).thenReturn(attributeEntity);

            // When
            Attribute result = attributeService.getAttributeByIdWithValues(1L);

            // Then
            assertNotNull(result);
            assertEquals(AttributeInputType.INPUT, result.getInputType());
            assertNull(result.getValues());

            verify(attributeMapper).findById(1L);
            verify(attributeValueMapper, never()).findAllAttributeValuesByAttributeId(anyLong());
        }
    }

    @Nested
    @DisplayName("findAllAttributeValuesByAttributeId 方法测试")
    class FindAllAttributeValuesByAttributeIdTest {

        @Test
        @DisplayName("获取选择型属性的属性值列表")
        void findAllAttributeValuesByAttributeId_SelectType_Success() {
            // Given
            List<AttributeValueEntity> valueEntities = Arrays.asList(attributeValueEntity);

            when(attributeMapper.findById(1L)).thenReturn(attributeEntity);
            when(attributeValueMapper.findAllAttributeValuesByAttributeId(1L)).thenReturn(valueEntities);

            // When
            List<AttributeValue> result = attributeService.findAllAttributeValuesByAttributeId(1L);

            // Then
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("红色", result.get(0).getValue());

            verify(attributeMapper).findById(1L);
            verify(attributeValueMapper).findAllAttributeValuesByAttributeId(1L);
        }

        @Test
        @DisplayName("输入型属性返回空列表")
        void findAllAttributeValuesByAttributeId_InputType_EmptyList() {
            // Given
            attributeEntity.setInputType("INPUT");

            when(attributeMapper.findById(1L)).thenReturn(attributeEntity);

            // When
            List<AttributeValue> result = attributeService.findAllAttributeValuesByAttributeId(1L);

            // Then
            assertNotNull(result);
            assertTrue(result.isEmpty());

            verify(attributeMapper).findById(1L);
            verify(attributeValueMapper, never()).findAllAttributeValuesByAttributeId(anyLong());
        }
    }

    @Nested
    @DisplayName("getAttributeValueById 方法测试")
    class GetAttributeValueByIdTest {

        @Test
        @DisplayName("成功获取属性值")
        void getAttributeValueById_Success() {
            // Given
            when(attributeValueMapper.findById(1L)).thenReturn(attributeValueEntity);

            // When
            AttributeValue result = attributeService.getAttributeValueById(1L);

            // Then
            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals(1L, result.getAttributeId());
            assertEquals("红色", result.getValue());
            assertEquals(100, result.getSortScore());

            verify(attributeValueMapper).findById(1L);
        }

        @Test
        @DisplayName("属性值不存在时抛出异常")
        void getAttributeValueById_NotFound_ThrowsException() {
            // Given
            when(attributeValueMapper.findById(1L)).thenReturn(null);

            // When & Then
            BizException exception = assertThrows(BizException.class, 
                () -> attributeService.getAttributeValueById(1L));
            assertEquals(ErrorCode.ATTRIBUTE_VALUE_NOT_FOUND, exception.getErrorCode());

            verify(attributeValueMapper).findById(1L);
        }
    }

    @Nested
    @DisplayName("ensureItemAttributes 方法测试")
    class EnsureItemAttributesTest {

        private ItemAttributeRequest itemAttributeRequest;
        private List<ItemAttributeRequest> attributes;

        @BeforeEach
        void setUp() {
            itemAttributeRequest = new ItemAttributeRequest();
            itemAttributeRequest.setAttributeId(1L);
            itemAttributeRequest.setAttributeValueId(1L);
            itemAttributeRequest.setValue("红色");

            attributes = Arrays.asList(itemAttributeRequest);
        }

        @Test
        @DisplayName("首次添加商品属性成功")
        void ensureItemAttributes_FirstTime_Success() {
            // Given
            Long itemId = 100L;
            Long skuId = 200L;

            when(itemAttributeRelationMapper.findByItemIdAndSkuId(itemId, skuId)).thenReturn(Collections.emptyList());
            when(itemAttributeRelationMapper.batchInsert(anyList())).thenReturn(1);

            // When
            attributeService.ensureItemAttributes(itemId, skuId, attributes);

            // Then
            verify(itemAttributeRelationMapper).findByItemIdAndSkuId(itemId, skuId);
            verify(itemAttributeRelationMapper).batchInsert(anyList());
            verify(itemAttributeRelationMapper, never()).deleteByItemIdAndAttributeIds(anyLong(), anyList());
        }

        @Test
        @DisplayName("更新现有商品属性成功")
        void ensureItemAttributes_Update_Success() {
            // Given
            Long itemId = 100L;
            Long skuId = 200L;

            ItemAttributeRelationEntity existingRelation = new ItemAttributeRelationEntity();
            existingRelation.setItemId(itemId);
            existingRelation.setSkuId(skuId);
            existingRelation.setAttributeId(2L); // 不同的属性ID
            List<ItemAttributeRelationEntity> existingRelations = Arrays.asList(existingRelation);

            when(itemAttributeRelationMapper.findByItemIdAndSkuId(itemId, skuId)).thenReturn(existingRelations);
            when(itemAttributeRelationMapper.deleteByItemIdAndAttributeIds(eq(itemId), anyList())).thenReturn(1);
            when(itemAttributeRelationMapper.batchInsert(anyList())).thenReturn(1);

            // When
            attributeService.ensureItemAttributes(itemId, skuId, attributes);

            // Then
            verify(itemAttributeRelationMapper).findByItemIdAndSkuId(itemId, skuId);
            verify(itemAttributeRelationMapper).deleteByItemIdAndAttributeIds(eq(itemId), anyList());
            verify(itemAttributeRelationMapper).batchInsert(anyList());
        }

        @Test
        @DisplayName("删除现有关系失败时抛出异常")
        void ensureItemAttributes_DeleteFailed_ThrowsException() {
            // Given
            Long itemId = 100L;
            Long skuId = 200L;

            ItemAttributeRelationEntity existingRelation = new ItemAttributeRelationEntity();
            existingRelation.setAttributeId(2L);
            List<ItemAttributeRelationEntity> existingRelations = Arrays.asList(existingRelation);

            when(itemAttributeRelationMapper.findByItemIdAndSkuId(itemId, skuId)).thenReturn(existingRelations);
            when(itemAttributeRelationMapper.deleteByItemIdAndAttributeIds(eq(itemId), anyList())).thenReturn(0);

            // When & Then
            BizException exception = assertThrows(BizException.class, 
                () -> attributeService.ensureItemAttributes(itemId, skuId, attributes));
            assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());

            verify(itemAttributeRelationMapper).deleteByItemIdAndAttributeIds(eq(itemId), anyList());
            verify(itemAttributeRelationMapper, never()).batchInsert(anyList());
        }

        @Test
        @DisplayName("插入新关系失败时抛出异常")
        void ensureItemAttributes_InsertFailed_ThrowsException() {
            // Given
            Long itemId = 100L;
            Long skuId = 200L;

            when(itemAttributeRelationMapper.findByItemIdAndSkuId(itemId, skuId)).thenReturn(Collections.emptyList());
            when(itemAttributeRelationMapper.batchInsert(anyList())).thenReturn(0);

            // When & Then
            BizException exception = assertThrows(BizException.class, 
                () -> attributeService.ensureItemAttributes(itemId, skuId, attributes));
            assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());

            verify(itemAttributeRelationMapper).batchInsert(anyList());
        }

        @Test
        @DisplayName("没有新关系需要插入时正常返回")
        void ensureItemAttributes_NoNewRelations_Return() {
            // Given
            Long itemId = 100L;
            Long skuId = 200L;

            ItemAttributeRelationEntity existingRelation = new ItemAttributeRelationEntity();
            existingRelation.setAttributeId(1L); // 相同的属性ID
            List<ItemAttributeRelationEntity> existingRelations = Arrays.asList(existingRelation);

            when(itemAttributeRelationMapper.findByItemIdAndSkuId(itemId, skuId)).thenReturn(existingRelations);
            when(itemAttributeRelationMapper.deleteByItemIdAndAttributeIds(eq(itemId), anyList())).thenReturn(1);

            // When
            attributeService.ensureItemAttributes(itemId, skuId, attributes);

            // Then
            verify(itemAttributeRelationMapper).findByItemIdAndSkuId(itemId, skuId);
            verify(itemAttributeRelationMapper).deleteByItemIdAndAttributeIds(eq(itemId), anyList());
            verify(itemAttributeRelationMapper, never()).batchInsert(anyList());
        }
    }
}