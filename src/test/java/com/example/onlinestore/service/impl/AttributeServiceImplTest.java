packagecom.example.onlinestore.service.impl;

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
importorg.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
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

    private CreateAttributeRequest createAttributeRequest;
    private UpdateAttributeRequest updateAttributeRequest;
    private AttributeEntity attributeEntity;
    private Attribute attribute;
    private AttributeValueEntity attributeValueEntity;
   private AttributeValue attributeValue;
    private ItemAttributeRequest itemAttributeRequest;

    @BeforeEach
    void setUp() {
        // 创建测试数据
        createAttributeRequest = new CreateAttributeRequest();
        createAttributeRequest.setName("test-attribute");
        createAttributeRequest.setAttributeType("SKU");
        createAttributeRequest.setInputType("INPUT");
        createAttributeRequest.setRequired(1);
        createAttributeRequest.setSearchable(1);
        createAttributeRequest.setSortScore(100);
        createAttributeRequest.setVisible(1);

        updateAttributeRequest = new UpdateAttributeRequest();
        updateAttributeRequest.setName("updated-attribute");
        updateAttributeRequest.setAttributeType("SALE");
        updateAttributeRequest.setInputType("SINGLE_SELECT");
        updateAttributeRequest.setRequired(0);
        updateAttributeRequest.setSearchable(0);
        updateAttributeRequest.setSortScore(200);
        updateAttributeRequest.setVisible(0);

        attributeEntity = new AttributeEntity();
attributeEntity.setId(1L);
        attributeEntity.setName("test-attribute");
        attributeEntity.setAttributeType("SKU");
        attributeEntity.setInputType("INPUT");
        attributeEntity.setRequired(1);
        attributeEntity.setSearchable(1);
        attributeEntity.setSortScore(100);
        attributeEntity.setVisible(1);
        attributeEntity.setCreatedAt(LocalDateTime.now());
        attributeEntity.setUpdatedAt(LocalDateTime.now());

        attribute = new Attribute();
        attribute.setId(1L);
        attribute.setName("test-attribute");
        attribute.setAttributeType(AttributeType.SKU);
        attribute.setInputType(AttributeInputType.INPUT);
        attribute.setRequired(1);
        attribute.setSearchable(1);
        attribute.setSortScore(100);
        attribute.setVisible(1);

        attributeValueEntity = new AttributeValueEntity();
        attributeValueEntity.setId(1L);
        attributeValueEntity.setAttributeId(1L);
        attributeValueEntity.setValue("test-value");
       attributeValueEntity.setSortScore(10);

        attributeValue = new AttributeValue();
        attributeValue.setId(1L);
        attributeValue.setAttributeId(1L);
        attributeValue.setValue("test-value");
        attributeValue.setSortScore(10);

        itemAttributeRequest = new ItemAttributeRequest();
        itemAttributeRequest.setAttributeId(1L);
        itemAttributeRequest.setAttributeValueId(1L);
        itemAttributeRequest.setValue("test-value");
    }

    // ==================== createAttribute 测试用例 ====================

    @Test
    void testCreateAttribute_Success() {
        // Given
        when(attributeMapper.findByName("test-attribute")).thenReturn(null);
        when(attributeMapper.insert(any(AttributeEntity.class))).thenReturn(1);

        try (MockedStatic<LocalDateTime> mockedLocalDateTime = mockStatic(LocalDateTime.class)) {
            LocalDateTime now = LocalDateTime.now();
            mockedLocalDateTime.when(LocalDateTime::now).thenReturn(now);

            //When
            Attribute result = attributeService.createAttribute(createAttributeRequest);

            // Then
            assertNotNull(result);
            assertEquals("test-attribute", result.getName());
            assertEquals(AttributeType.SKU, result.getAttributeType());
            assertEquals(AttributeInputType.INPUT, result.getInputType());
            assertEquals(1, result.getRequired());
            assertEquals(1, result.getSearchable());
            assertEquals(100, result.getSortScore());
            assertEquals(1, result.getVisible());

            verify(attributeMapper).findByName("test-attribute");
            verify(attributeMapper).insert(any(AttributeEntity.class));
        }
    }

    // ==================== updateAttribute 测试用例 ====================

    @Test
    void testUpdateAttribute_Success() {
        // Given
        when(attributeMapper.findById(1L)).thenReturn(attributeEntity);
        when(attributeMapper.update(any(AttributeEntity.class))).thenReturn(1);

        try (MockedStatic<CommonUtils> mockedCommonUtils = mockStatic(CommonUtils.class)) {
            mockedCommonUtils.when(() -> CommonUtils.updateFieldIfChanged(
                eq("updated-attribute"), eq("test-attribute"), any()))
                .thenReturn(true);
            mockedCommonUtils.when(() -> CommonUtils.updateFieldIfChanged(
                eq("SALE"), eq("SKU"), any()))
                .thenReturn(true);
            // 其他字段返回false
            mockedCommonUtils.when(() -> CommonUtils.updateFieldIfChanged(
                any(), any(), any()))
                .thenReturn(false);

            // When
            attributeService.updateAttribute(1L, updateAttributeRequest);

            // Then
            verify(attributeMapper).findById(1L);
            verify(attributeMapper).update(any(AttributeEntity.class));
        }
    }

    @Test
    void testUpdateAttribute_AttributeNotFound() {
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
    void testUpdateAttribute_NoFieldChanged() {
        // Given
        when(attributeMapper.findById(1L)).thenReturn(attributeEntity);

        try (MockedStatic<CommonUtils> mockedCommonUtils = mockStatic(CommonUtils.class)) {
            // 所有字段都返回false，表示无更新
            mockedCommonUtils.when(() ->CommonUtils.updateFieldIfChanged(
                any(), any(), any()))
                .thenReturn(false);

            // When
            attributeService.updateAttribute(1L, updateAttributeRequest);

            // Then
            verify(attributeMapper).findById(1L);
            verify(attributeMapper, never()).update(any(AttributeEntity.class));
        }
}

    @Test
    void testUpdateAttribute_UpdateFailed() {
        // Given
        when(attributeMapper.findById(1L)).thenReturn(attributeEntity);
        when(attributeMapper.update(any(AttributeEntity.class))).thenReturn(0);

        try (MockedStatic<CommonUtils> mockedCommonUtils = mockStatic(CommonUtils.class)) {
            mockedCommonUtils.when(() -> CommonUtils.updateFieldIfChanged(
                eq("updated-attribute"), eq("test-attribute"), any()))
                .thenReturn(true);
            mockedCommonUtils.when(() -> CommonUtils.updateFieldIfChanged(
                any(), any(), any()))
                .thenReturn(false);

            // When & Then
            BizException exception = assertThrows(BizException.class, 
                () -> attributeService.updateAttribute(1L, updateAttributeRequest));
            
            assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());
            verify(attributeMapper).findById(1L);
            verify(attributeMapper).update(any(AttributeEntity.class));
        }
    }

    // ==================== deleteAttribute 测试用例 ====================

    @Test
    void testDeleteAttribute_Success() {
        // Given
        when(attributeMapper.findById(1L)).thenReturn(attributeEntity);
        when(itemAttributeRelationMapper.findByAttributeId(1L, 0, 1))
            .thenReturn(Collections.emptyList());
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
    void testDeleteAttribute_AttributeNotFound() {
        // Given
        when(attributeMapper.findById(1L)).thenReturn(null);

        // When & Then
        BizExceptionexception = assertThrows(BizException.class, 
            () -> attributeService.deleteAttribute(1L));
        
        assertEquals(ErrorCode.ATTRIBUTE_NOT_FOUND, exception.getErrorCode());
        verify(attributeMapper).findById(1L);
        verify(itemAttributeRelationMapper, never()).findByAttributeId(anyLong(), anyInt(), anyInt());
        verify(attributeMapper, never()).deleteById(anyLong());
    }

   @Test
    void testDeleteAttribute_AttributeIsReferenced() {
        // Given
        ItemAttributeRelationEntity relationEntity = new ItemAttributeRelationEntity();
        relationEntity.setItemId(100L);
        relationEntity.setAttributeId(1L);
        
        when(attributeMapper.findById(1L)).thenReturn(attributeEntity);
        when(itemAttributeRelationMapper.findByAttributeId(1L, 0, 1))
            .thenReturn(Arrays.asList(relationEntity));

        // When & Then
        BizException exception = assertThrows(BizException.class, 
            () -> attributeService.deleteAttribute(1L));
        
        assertEquals(ErrorCode.ATTRIBUTE_IS_REFERENCE_BY_ITEM, exception.getErrorCode());
        verify(attributeMapper).findById(1L);
        verify(itemAttributeRelationMapper).findByAttributeId(1L, 0, 1);
        verify(attributeMapper, never()).deleteById(anyLong());
    }

    @Test
    void testDeleteAttribute_DeleteFailed() {
// Given
        when(attributeMapper.findById(1L)).thenReturn(attributeEntity);
        when(itemAttributeRelationMapper.findByAttributeId(1L, 0, 1))
            .thenReturn(Collections.emptyList());
        when(attributeMapper.deleteById(1L)).thenReturn(0);

        // When & Then
        BizException exception= assertThrows(BizException.class, 
            () -> attributeService.deleteAttribute(1L));
        
        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());
        verify(attributeMapper).findById(1L);
        verify(itemAttributeRelationMapper).findByAttributeId(1L, 0, 1);
        verify(attributeMapper).deleteById(1L);
        verify(attributeValueMapper,never()).countValuesByAttributeId(anyLong());
    }

    @Test
    void testDeleteAttribute_DeleteValuesFailed() {
        // Given
        when(attributeMapper.findById(1L)).thenReturn(attributeEntity);
        when(itemAttributeRelationMapper.findByAttributeId(1L, 0, 1))
            .thenReturn(Collections.emptyList());
        when(attributeMapper.deleteById(1L)).thenReturn(1);
        when(attributeValueMapper.countValuesByAttributeId(1L)).thenReturn(2);
        when(attributeValueMapper.deleteByAttributeId(1L)).thenReturn(1); // 删除数量不匹配

        // When & Then
        BizException exception = assertThrows(BizException.class, 
            () -> attributeService.deleteAttribute(1L));
        
        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());
        verify(attributeMapper).findById(1L);
        verify(itemAttributeRelationMapper).findByAttributeId(1L, 0, 1);
        verify(attributeMapper).deleteById(1L);
        verify(attributeValueMapper).countValuesByAttributeId(1L);
        verify(attributeValueMapper).deleteByAttributeId(1L);
    }

    //==================== 查询方法测试用例 ====================

    @Test
    void testGetAttributeById_Success() {
        // Given
        when(attributeMapper.findById(1L)).thenReturn(attributeEntity);

        // When
        Attribute result = attributeService.getAttributeById(1L);

        // Then
        assertNotNull(result);
       assertEquals(1L, result.getId());
        assertEquals("test-attribute", result.getName());
        assertEquals(AttributeType.SKU, result.getAttributeType());
        assertEquals(AttributeInputType.INPUT, result.getInputType());
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
       BizException exception = assertThrows(BizException.class, 
            () -> attributeService.getAttributeById(1L));
        
        assertEquals(ErrorCode.ATTRIBUTE_NOT_FOUND, exception.getErrorCode());
        verify(attributeMapper).findById(1L);
    }

    @Test
    void testGetAttributeByIdWithValues_WithSelectType() {
        // Given
        AttributeEntity selectAttributeEntity = new AttributeEntity();
       selectAttributeEntity.setId(1L);
        selectAttributeEntity.setName("test-select-attribute");
        selectAttributeEntity.setAttributeType("SKU");
        selectAttributeEntity.setInputType("SINGLE_SELECT");
        selectAttributeEntity.setRequired(1);
        selectAttributeEntity.setSearchable(1);
        selectAttributeEntity.setSortScore(100);
        selectAttributeEntity.setVisible(1);
        
        when(attributeMapper.findById(1L)).thenReturn(selectAttributeEntity);
        when(attributeValueMapper.findAllAttributeValuesByAttributeId(1L))
            .thenReturn(Arrays.asList(attributeValueEntity));

        // When
        Attribute result = attributeService.getAttributeByIdWithValues(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(AttributeInputType.SINGLE_SELECT, result.getInputType());
        assertNotNull(result.getValues());
        assertEquals(1, result.getValues().size());
        assertEquals("test-value", result.getValues().get(0).getValue());
        
        verify(attributeMapper).findById(1L);
        verify(attributeValueMapper).findAllAttributeValuesByAttributeId(1L);
    }

    @Test
    void testGetAttributeByIdWithValues_WithInputType() {
        // Given
        when(attributeMapper.findById(1L)).thenReturn(attributeEntity);

//When
        Attribute result = attributeService.getAttributeByIdWithValues(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(AttributeInputType.INPUT, result.getInputType());
        assertNull(result.getValues()); // INPUT类型不加载属性值
        
        verify(attributeMapper).findById(1L);
        verify(attributeValueMapper, never()).findAllAttributeValuesByAttributeId(anyLong());
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
        assertEquals("test-value", result.getValue());
        assertEquals(10, result.getSortScore());
        
        verify(attributeValueMapper).findById(1L);
    }

    @Test
    void testGetAttributeValueById_NotFound() {
        // Given
        when(attributeValueMapper.findById(1L)).thenReturn(null);

        // When & Then
        BizException exception = assertThrows(BizException.class, 
            () -> attributeService.getAttributeValueById(1L));
        
        assertEquals(ErrorCode.ATTRIBUTE_VALUE_NOT_FOUND, exception.getErrorCode());
        verify(attributeValueMapper).findById(1L);
    }

    // ==================== findAllAttributeValuesByAttributeId 测试用例 ====================

    @Test
    void testFindAllAttributeValuesByAttributeId_WithSelectType() {
        // Given
        AttributeEntity selectAttributeEntity = newAttributeEntity();
        selectAttributeEntity.setId(1L);
        selectAttributeEntity.setName("test-select-attribute");
        selectAttributeEntity.setAttributeType("SKU");
        selectAttributeEntity.setInputType("SINGLE_SELECT");
        selectAttributeEntity.setRequired(1);
        selectAttributeEntity.setSearchable(1);
        selectAttributeEntity.setSortScore(100);
        selectAttributeEntity.setVisible(1);
        
        when(attributeMapper.findById(1L)).thenReturn(selectAttributeEntity);
        when(attributeValueMapper.findAllAttributeValuesByAttributeId(1L))
            .thenReturn(Arrays.asList(attributeValueEntity));

        // When
        List<AttributeValue> result = attributeService.findAllAttributeValuesByAttributeId(1L);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("test-value", result.get(0).getValue());
        assertEquals(1L, result.get(0).getAttributeId());
        
        verify(attributeMapper).findById(1L);
        verify(attributeValueMapper).findAllAttributeValuesByAttributeId(1L);
    }

    @Test
    void testFindAllAttributeValuesByAttributeId_WithMultiSelectType() {
        // Given
        AttributeEntity multiSelectAttributeEntity = new AttributeEntity();
        multiSelectAttributeEntity.setId(1L);
        multiSelectAttributeEntity.setName("test-multi-select-attribute");
        multiSelectAttributeEntity.setAttributeType("SKU");
        multiSelectAttributeEntity.setInputType("MULTI_SELECT");
        multiSelectAttributeEntity.setRequired(1);
        multiSelectAttributeEntity.setSearchable(1);
        multiSelectAttributeEntity.setSortScore(100);
        multiSelectAttributeEntity.setVisible(1);
        
        AttributeValueEntity value1 = new AttributeValueEntity();
        value1.setId(1L);
        value1.setAttributeId(1L);
        value1.setValue("value1");
        value1.setSortScore(10);
        
        AttributeValueEntity value2 = new AttributeValueEntity();
        value2.setId(2L);
        value2.setAttributeId(1L);
        value2.setValue("value2");
        value2.setSortScore(20);
        
        when(attributeMapper.findById(1L)).thenReturn(multiSelectAttributeEntity);
        when(attributeValueMapper.findAllAttributeValuesByAttributeId(1L))
            .thenReturn(Arrays.asList(value1, value2));

        // When
        List<AttributeValue> result = attributeService.findAllAttributeValuesByAttributeId(1L);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("value1", result.get(0).getValue());
        assertEquals("value2", result.get(1).getValue());
        
        verify(attributeMapper).findById(1L);
        verify(attributeValueMapper).findAllAttributeValuesByAttributeId(1L);
    }

    @Test
    void testFindAllAttributeValuesByAttributeId_WithInputType() {
        // Given
        when(attributeMapper.findById(1L)).thenReturn(attributeEntity); // INPUT类型

        // When
        List<AttributeValue> result = attributeService.findAllAttributeValuesByAttributeId(1L);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty()); // INPUT类型返回空列表
        
        verify(attributeMapper).findById(1L);
        verify(attributeValueMapper, never()).findAllAttributeValuesByAttributeId(anyLong());
    }

    @Test
    void testFindAllAttributeValuesByAttributeId_AttributeNotFound() {
        // Given
        when(attributeMapper.findById(1L)).thenReturn(null);

        // When & Then
        BizException exception = assertThrows(BizException.class, 
            () -> attributeService.findAllAttributeValuesByAttributeId(1L));
        
        assertEquals(ErrorCode.ATTRIBUTE_NOT_FOUND, exception.getErrorCode());
        verify(attributeMapper).findById(1L);
        verify(attributeValueMapper, never()).findAllAttributeValuesByAttributeId(anyLong());
    }

    @Test
    void testCreateAttribute_NameDuplicated(){
        // Given
        when(attributeMapper.findByName("test-attribute")).thenReturn(attributeEntity);

        // When & Then
        BizException exception = assertThrows(BizException.class, 
            () -> attributeService.createAttribute(createAttributeRequest));
        
        assertEquals(ErrorCode.ATTRIBUTE_NAME_DUPLICATED, exception.getErrorCode());
        verify(attributeMapper).findByName("test-attribute");
        verify(attributeMapper, never()).insert(any(AttributeEntity.class));
    }

    @Test
    void testCreateAttribute_InsertFailed() {
        // Given
        when(attributeMapper.findByName("test-attribute")).thenReturn(null);
        when(attributeMapper.insert(any(AttributeEntity.class))).thenReturn(0);

        try (MockedStatic<LocalDateTime> mockedLocalDateTime = mockStatic(LocalDateTime.class)) {
            LocalDateTime now = LocalDateTime.now();
            mockedLocalDateTime.when(LocalDateTime::now).thenReturn(now);

            // When & Then
            BizException exception = assertThrows(BizException.class, 
                () -> attributeService.createAttribute(createAttributeRequest));
            
            assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());
            verify(attributeMapper).findByName("test-attribute");
            verify(attributeMapper).insert(any(AttributeEntity.class));
        }
    }

// ==================== ensureItemAttributes 测试用例 ====================

    @Test
    void testEnsureItemAttributes_NewRelations() {
       // Given
        Long itemId = 100L;
        Long skuId = 200L;
       List<ItemAttributeRequest> attributes = Arrays.asList(itemAttributeRequest);
        
        when(itemAttributeRelationMapper.findByItemIdAndSkuId(itemId, skuId))
            .thenReturn(Collections.emptyList());
        when(itemAttributeRelationMapper.batchInsert(anyList())).thenReturn(1);

        try (MockedStatic<LocalDateTime> mockedLocalDateTime= mockStatic(LocalDateTime.class)) {
            LocalDateTime now = LocalDateTime.now();
            mockedLocalDateTime.when(LocalDateTime::now).thenReturn(now);

            // When
            attributeService.ensureItemAttributes(itemId, skuId, attributes);

            // Then
            verify(itemAttributeRelationMapper).findByItemIdAndSkuId(itemId, skuId);
            verify(itemAttributeRelationMapper).batchInsert(anyList());
            verify(itemAttributeRelationMapper, never()).deleteByItemIdAndAttributeIds(anyLong(), anyList());
        }
    }

    @Test
    void testEnsureItemAttributes_UpdateExistingRelations() {
        // Given
        Long itemId = 100L;
        Long skuId = 200L;
        
        ItemAttributeRelationEntity existingRelation = new ItemAttributeRelationEntity();
        existingRelation.setItemId(itemId);
        existingRelation.setSkuId(skuId);
        existingRelation.setAttributeId(2L); // 与新属性不同
        existingRelation.setValueId(2L);
        
        List<ItemAttributeRequest> attributes = Arrays.asList(itemAttributeRequest);
        
        when(itemAttributeRelationMapper.findByItemIdAndSkuId(itemId, skuId))
            .thenReturn(Arrays.asList(existingRelation));
        when(itemAttributeRelationMapper.deleteByItemIdAndAttributeIds(eq(itemId), anyList()))
            .thenReturn(1);
        when(itemAttributeRelationMapper.batchInsert(anyList())).thenReturn(1);

        try (MockedStatic<LocalDateTime> mockedLocalDateTime = mockStatic(LocalDateTime.class)) {
           LocalDateTime now = LocalDateTime.now();
            mockedLocalDateTime.when(LocalDateTime::now).thenReturn(now);

            // When
            attributeService.ensureItemAttributes(itemId, skuId, attributes);

            // Then
            verify(itemAttributeRelationMapper).findByItemIdAndSkuId(itemId, skuId);
            verify(itemAttributeRelationMapper).deleteByItemIdAndAttributeIds(eq(itemId), anyList());
            verify(itemAttributeRelationMapper).batchInsert(anyList());
        }
    }

    @Test
    void testEnsureItemAttributes_DeleteFailed() {
        // Given
        Long itemId = 100L;
        Long skuId = 200L;
        
ItemAttributeRelationEntity existingRelation1= new ItemAttributeRelationEntity();
        existingRelation1.setItemId(itemId);
        existingRelation1.setSkuId(skuId);
        existingRelation1.setAttributeId(2L);
        
        ItemAttributeRelationEntity existingRelation2 = new ItemAttributeRelationEntity();
        existingRelation2.setItemId(itemId);
        existingRelation2.setSkuId(skuId);
        existingRelation2.setAttributeId(3L);
        
        List<ItemAttributeRequest> attributes = Arrays.asList(itemAttributeRequest);
        
        when(itemAttributeRelationMapper.findByItemIdAndSkuId(itemId, skuId))
            .thenReturn(Arrays.asList(existingRelation1, existingRelation2));
       when(itemAttributeRelationMapper.deleteByItemIdAndAttributeIds(eq(itemId), anyList()))
            .thenReturn(1); // 应该删除2个但只删除1个

        // When & Then
        BizException exception = assertThrows(BizException.class, 
            () -> attributeService.ensureItemAttributes(itemId,skuId, attributes));
        
        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());
        verify(itemAttributeRelationMapper).findByItemIdAndSkuId(itemId, skuId);
        verify(itemAttributeRelationMapper).deleteByItemIdAndAttributeIds(eq(itemId), anyList());
        verify(itemAttributeRelationMapper, never()).batchInsert(anyList());
    }

    @Test
    void testEnsureItemAttributes_InsertFailed() {
        // Given
        Long itemId = 100L;
        Long skuId = 200L;
        List<ItemAttributeRequest> attributes = Arrays.asList(itemAttributeRequest);
        
        when(itemAttributeRelationMapper.findByItemIdAndSkuId(itemId, skuId))
            .thenReturn(Collections.emptyList());
        when(itemAttributeRelationMapper.batchInsert(anyList())).thenReturn(0); // 插入失败

        try (MockedStatic<LocalDateTime> mockedLocalDateTime = mockStatic(LocalDateTime.class)) {
            LocalDateTime now = LocalDateTime.now();
            mockedLocalDateTime.when(LocalDateTime::now).thenReturn(now);

            // When & Then
            BizException exception = assertThrows(BizException.class, 
                () -> attributeService.ensureItemAttributes(itemId, skuId, attributes));
            
            assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());
            verify(itemAttributeRelationMapper).findByItemIdAndSkuId(itemId, skuId);
            verify(itemAttributeRelationMapper).batchInsert(anyList());
        }
    }

    @Test
    void testEnsureItemAttributes_NoNewRelationsToInsert() {
        // Given
        Long itemId = 100L;
        Long skuId = 200L;
        
        ItemAttributeRelationEntity existingRelation = new ItemAttributeRelationEntity();
        existingRelation.setItemId(itemId);
        existingRelation.setSkuId(skuId);
       existingRelation.setAttributeId(1L); // 与请求中的属性ID相同
        existingRelation.setValueId(1L);
        
        List<ItemAttributeRequest> attributes = Arrays.asList(itemAttributeRequest);
        
        when(itemAttributeRelationMapper.findByItemIdAndSkuId(itemId, skuId))
            .thenReturn(Arrays.asList(existingRelation));
        when(itemAttributeRelationMapper.deleteByItemIdAndAttributeIds(eq(itemId), anyList()))
            .thenReturn(1);

try (MockedStatic<LocalDateTime> mockedLocalDateTime = mockStatic(LocalDateTime.class)) {
            LocalDateTime now = LocalDateTime.now();
            mockedLocalDateTime.when(LocalDateTime::now).thenReturn(now);

            // When
            attributeService.ensureItemAttributes(itemId, skuId, attributes);

            // Then
            verify(itemAttributeRelationMapper).findByItemIdAndSkuId(itemId, skuId);
            verify(itemAttributeRelationMapper).deleteByItemIdAndAttributeIds(eq(itemId), anyList());
            verify(itemAttributeRelationMapper,never()).batchInsert(anyList()); // 没有新关系需要插入
        }
    }

}