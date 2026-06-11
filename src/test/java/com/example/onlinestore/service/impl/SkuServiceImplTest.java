package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.Attribute;
import com.example.onlinestore.bean.AttributeValue;
import com.example.onlinestore.bean.Item;
import com.example.onlinestore.bean.Sku;
import com.example.onlinestore.dto.CreateSkuRequest;
import com.example.onlinestore.dto.ItemAttributeRequest;
import com.example.onlinestore.entity.ItemAttributeRelationEntity;
import com.example.onlinestore.entity.ItemEntity;
import com.example.onlinestore.entity.SkuEntity;
import com.example.onlinestore.enums.AttributeInputType;
import com.example.onlinestore.enums.AttributeType;
import com.example.onlinestore.exceptions.BizException;
import com.example.onlinestore.mapper.ItemAttributeRelationMapper;
import com.example.onlinestore.mapper.ItemMapper;
import com.example.onlinestore.mapper.SkuMapper;
import com.example.onlinestore.service.AttributeService;
import com.example.onlinestore.service.ItemDetailCacheService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.mockito.ArgumentCaptor;

@ExtendWith(MockitoExtension.class)
class SkuServiceImplTest {

    @Mock
    private ItemMapper itemMapper;

    @Mock
    private SkuMapper skuMapper;

    @Mock
    private AttributeService attributeService;

    @Mock
    private ItemAttributeRelationMapper itemAttributeRelationMapper;

    @Mock
    private ItemDetailCacheService itemDetailCacheService;

    @InjectMocks
    private SkuServiceImpl skuService;

    private Attribute skuAttr1;
    private Attribute skuAttr2;
    private AttributeValue value1;
    private AttributeValue value2;

    @BeforeEach
    void setUp() {
        skuAttr1 = buildAttribute(1L, "颜色", AttributeInputType.SINGLE_SELECT, AttributeType.SKU);
        skuAttr2 = buildAttribute(2L, "尺寸", AttributeInputType.SINGLE_SELECT, AttributeType.SKU);

        value1 = new AttributeValue();
        value1.setId(10L);
        value1.setAttributeId(1L);
        value1.setValue("红色");
        value1.setSortScore(1);

        value2 = new AttributeValue();
        value2.setId(20L);
        value2.setAttributeId(2L);
        value2.setValue("XL");
        value2.setSortScore(1);
    }

    private Attribute buildAttribute(Long id, String name, AttributeInputType inputType, AttributeType attrType) {
        Attribute attr = new Attribute();
        attr.setId(id);
        attr.setName(name);
        attr.setAttributeType(attrType);
        attr.setInputType(inputType);
        attr.setRequired(1);
        attr.setSearchable(0);
        attr.setSortScore(1);
        attr.setVisible(1);
        return attr;
    }

    private ItemAttributeRequest buildAttrRequest(Long attrId, Long valueId) {
        ItemAttributeRequest req = new ItemAttributeRequest();
        req.setAttributeId(attrId);
        req.setAttributeValueId(valueId);
        return req;
    }

    private CreateSkuRequest buildCreateSkuRequest(List<ItemAttributeRequest> attributes) {
        CreateSkuRequest req = new CreateSkuRequest();
        req.setItemId(1L);
        req.setSkuCode("SKU-001");
        req.setName("红色-XL");
        req.setDescription("红色大号");
        req.setPrice(new BigDecimal("99.9"));
        req.setDefaultSku(1);
        req.setStockQuantity(100);
        req.setWarningQuantity(10);
        req.setImage("http://example.com/sku.jpg");
        req.setAttributes(attributes);
        return req;
    }

    // ========== createSku 批量查询验证 ==========

    @Test
    void createSku_shouldUseBatchQueryForAttributes() {
        CreateSkuRequest request = buildCreateSkuRequest(Arrays.asList(
                buildAttrRequest(1L, 10L),
                buildAttrRequest(2L, 20L)
        ));

        when(itemMapper.findById(1L)).thenReturn(new ItemEntity());
        when(skuMapper.findBySkuCode("SKU-001")).thenReturn(null);
        when(attributeService.getAttributesByIds(Arrays.asList(1L, 2L)))
                .thenReturn(Arrays.asList(skuAttr1, skuAttr2));
        when(attributeService.getAttributeValuesByIds(Arrays.asList(10L, 20L)))
                .thenReturn(Map.of(10L, value1, 20L, value2));

        SkuEntity skuEntity = new SkuEntity();
        skuEntity.setId(100L);
        skuEntity.setItemId(1L);
        skuEntity.setSkuCode("SKU-001");
        skuEntity.setName("红色-XL");
        skuEntity.setPrice(new BigDecimal("99.9"));
        skuEntity.setDefaultSku(1);
        skuEntity.setStockQuantity(100);
        skuEntity.setWarningQuantity(10);
        skuEntity.setImage("http://example.com/sku.jpg");

        when(skuMapper.insert(any(SkuEntity.class))).thenAnswer(invocation -> {
            SkuEntity e = invocation.getArgument(0);
            e.setId(100L);
            return 1;
        });

        skuService.createSku(request);

        // 验证使用了批量查询
        verify(attributeService).getAttributesByIds(Arrays.asList(1L, 2L));
        verify(attributeService).getAttributeValuesByIds(Arrays.asList(10L, 20L));
        // 验证没有使用逐个查询
        verify(attributeService, never()).getAttributeById(anyLong());
        verify(attributeService, never()).getAttributeValueById(anyLong());
        // 验证属性记录通过 ensureItemAttributes 保存
        verify(attributeService).ensureItemAttributes(eq(1L), eq(100L), anyList());
    }

    @Test
    void createSku_withNonExistentAttribute_throwsException() {
        CreateSkuRequest request = buildCreateSkuRequest(Arrays.asList(
                buildAttrRequest(1L, 10L),
                buildAttrRequest(999L, 20L)
        ));

        when(itemMapper.findById(1L)).thenReturn(new ItemEntity());
        when(skuMapper.findBySkuCode("SKU-001")).thenReturn(null);
        when(attributeService.getAttributesByIds(Arrays.asList(1L, 999L)))
                .thenReturn(Collections.singletonList(skuAttr1));
        when(attributeService.getAttributeValuesByIds(Arrays.asList(10L, 20L)))
                .thenReturn(Map.of(10L, value1, 20L, value2));

        assertThrows(BizException.class, () -> skuService.createSku(request));

        verify(attributeService, never()).getAttributeById(anyLong());
    }

    @Test
    void createSku_withNonExistentValue_throwsException() {
        CreateSkuRequest request = buildCreateSkuRequest(Arrays.asList(
                buildAttrRequest(1L, 10L),
                buildAttrRequest(2L, 999L)
        ));

        when(itemMapper.findById(1L)).thenReturn(new ItemEntity());
        when(skuMapper.findBySkuCode("SKU-001")).thenReturn(null);
        when(attributeService.getAttributesByIds(Arrays.asList(1L, 2L)))
                .thenReturn(Arrays.asList(skuAttr1, skuAttr2));
        when(attributeService.getAttributeValuesByIds(Arrays.asList(10L, 999L)))
                .thenReturn(Map.of(10L, value1));

        assertThrows(BizException.class, () -> skuService.createSku(request));

        verify(attributeService, never()).getAttributeValueById(anyLong());
    }

    // ========== getSkusByItemId 批量查询验证 ==========

    @Test
    void getSkusByItemId_shouldUseBatchQueryForAttributes() {
        SkuEntity sku1 = buildSkuEntity(1L, 1L, "SKU-001", "红色-XL");
        SkuEntity sku2 = buildSkuEntity(2L, 1L, "SKU-002", "蓝色-M");

        when(skuMapper.findByItemId(1L)).thenReturn(Arrays.asList(sku1, sku2));

        ItemAttributeRelationEntity rel1 = buildRelation(1L, 1L, 1L, 10L);
        ItemAttributeRelationEntity rel2 = buildRelation(1L, 1L, 2L, 20L);
        ItemAttributeRelationEntity rel3 = buildRelation(1L, 2L, 1L, 11L);

        when(itemAttributeRelationMapper.findByItemId(1L))
                .thenReturn(Arrays.asList(rel1, rel2, rel3));

        when(attributeService.getAttributesByIds(anyList()))
                .thenReturn(Arrays.asList(skuAttr1, skuAttr2));
        when(attributeService.getAttributeValuesByAttributeIds(anyList()))
                .thenReturn(Map.of(1L, List.of(value1), 2L, List.of(value2)));
        when(attributeService.getAttributeValuesByIds(anyList()))
                .thenReturn(Map.of(10L, value1, 20L, value2, 11L, value1));

        List<Sku> result = skuService.getSkusByItemId(1L);

        assertEquals(2, result.size());
        verify(attributeService, times(1)).getAttributesByIds(anyList());
        verify(attributeService, times(1)).getAttributeValuesByAttributeIds(anyList());
        verify(attributeService, times(1)).getAttributeValuesByIds(anyList());
        verify(attributeService, never()).getAttributeByIdWithValues(anyLong());
        verify(attributeService, never()).getAttributeValueById(anyLong());
        verify(itemAttributeRelationMapper, never()).findByItemIdAndSkuId(anyLong(), anyLong());
    }

    @Test
    void getSkusByItemId_withNoSkus_returnsEmptyList() {
        when(skuMapper.findByItemId(1L)).thenReturn(Collections.emptyList());

        List<Sku> result = skuService.getSkusByItemId(1L);

        assertTrue(result.isEmpty());
        verifyNoInteractions(attributeService);
    }

    // ========== getSkuById 验证 ==========

    @Test
    void getSkuById_shouldUseBatchQueryForAttributes() {
        SkuEntity skuEntity = buildSkuEntity(1L, 1L, "SKU-001", "红色-XL");
        when(skuMapper.findById(1L)).thenReturn(skuEntity);

        ItemAttributeRelationEntity rel = buildRelation(1L, 1L, 1L, 10L);
        when(itemAttributeRelationMapper.findByItemIdAndSkuId(1L, 1L))
                .thenReturn(Collections.singletonList(rel));

        when(attributeService.getAttributesByIds(Collections.singletonList(1L)))
                .thenReturn(Collections.singletonList(skuAttr1));
        when(attributeService.getAttributeValuesByIds(Collections.singletonList(10L)))
                .thenReturn(Map.of(10L, value1));

        Sku result = skuService.getSkuById(1L);

        assertNotNull(result);
        assertEquals("SKU-001", result.getSkuCode());
        assertEquals(1, result.getAttributes().size());
        // 验证使用了批量查询而非逐个查询
        verify(attributeService).getAttributesByIds(anyList());
        verify(attributeService, never()).getAttributeByIdWithValues(anyLong());
        verify(attributeService, never()).getAttributeValueById(anyLong());
    }

    // ========== 辅助方法 ==========

    private SkuEntity buildSkuEntity(Long id, Long itemId, String skuCode, String name) {
        SkuEntity entity = new SkuEntity();
        entity.setId(id);
        entity.setItemId(itemId);
        entity.setSkuCode(skuCode);
        entity.setName(name);
        entity.setPrice(new BigDecimal("99.9"));
        entity.setDefaultSku(1);
        entity.setStockQuantity(100);
        entity.setWarningQuantity(10);
        entity.setImage("http://example.com/sku.jpg");
        return entity;
    }

    private ItemAttributeRelationEntity buildRelation(Long itemId, Long skuId, Long attrId, Long valueId) {
        ItemAttributeRelationEntity entity = new ItemAttributeRelationEntity();
        entity.setId(1L);
        entity.setItemId(itemId);
        entity.setSkuId(skuId);
        entity.setAttributeId(attrId);
        entity.setValueId(valueId);
        return entity;
    }

    @Test
    void createSku_firstTimeAttributes_shouldSetSkuId() {
        CreateSkuRequest request = buildCreateSkuRequest(Arrays.asList(
                buildAttrRequest(1L, 10L),
                buildAttrRequest(2L, 20L)
        ));

        when(itemMapper.findById(1L)).thenReturn(new ItemEntity());
        when(skuMapper.findBySkuCode("SKU-001")).thenReturn(null);
        when(attributeService.getAttributesByIds(Arrays.asList(1L, 2L)))
                .thenReturn(Arrays.asList(skuAttr1, skuAttr2));
        when(attributeService.getAttributeValuesByIds(Arrays.asList(10L, 20L)))
                .thenReturn(Map.of(10L, value1, 20L, value2));

        when(skuMapper.insert(any(SkuEntity.class))).thenAnswer(invocation -> {
            SkuEntity e = invocation.getArgument(0);
            e.setId(100L);
            return 1;
        });

        skuService.createSku(request);

        // 验证 ensureItemAttributes 被调用，且 skuId 正确设置为新生成的 100L
        verify(attributeService).ensureItemAttributes(eq(1L), eq(100L), anyList());
    }
}
