package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.Attribute;
import com.example.onlinestore.bean.AttributeValue;
import com.example.onlinestore.cache.AttributeCacheManager;
import com.example.onlinestore.dto.converter.AttributeConverter;
import com.example.onlinestore.dto.ItemAttributeRequest;
import com.example.onlinestore.entity.AttributeEntity;
import com.example.onlinestore.entity.AttributeValueEntity;
import com.example.onlinestore.entity.ItemAttributeRelationEntity;
import com.example.onlinestore.enums.AttributeInputType;
import com.example.onlinestore.enums.AttributeType;
import com.example.onlinestore.mapper.AttributeMapper;
import com.example.onlinestore.mapper.AttributeValueMapper;
import com.example.onlinestore.mapper.ItemAttributeRelationMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.mockito.ArgumentCaptor;

@ExtendWith(MockitoExtension.class)
class AttributeServiceImplTest {

    @Mock
    private AttributeMapper attributeMapper;

    @Mock
    private AttributeValueMapper attributeValueMapper;

    @Mock
    private ItemAttributeRelationMapper itemAttributeRelationMapper;

    private AttributeServiceImpl attributeService;

    private AttributeEntity attributeEntity1;
    private AttributeEntity attributeEntity2;
    private AttributeValueEntity valueEntity1;
    private AttributeValueEntity valueEntity2;
    private AttributeValueEntity valueEntity3;

    @BeforeEach
    void setUp() {
        attributeService = new AttributeServiceImpl(
                attributeMapper,
                attributeValueMapper,
                itemAttributeRelationMapper,
                new AttributeCacheManager(),
                new AttributeConverter()
        );

        attributeEntity1 = new AttributeEntity();
        attributeEntity1.setId(1L);
        attributeEntity1.setName("颜色");
        attributeEntity1.setAttributeType("SKU");
        attributeEntity1.setInputType("SINGLE_SELECT");
        attributeEntity1.setRequired(1);
        attributeEntity1.setSearchable(1);
        attributeEntity1.setSortScore(1);
        attributeEntity1.setVisible(1);

        attributeEntity2 = new AttributeEntity();
        attributeEntity2.setId(2L);
        attributeEntity2.setName("尺寸");
        attributeEntity2.setAttributeType("SKU");
        attributeEntity2.setInputType("SINGLE_SELECT");
        attributeEntity2.setRequired(1);
        attributeEntity2.setSearchable(0);
        attributeEntity2.setSortScore(2);
        attributeEntity2.setVisible(1);

        valueEntity1 = new AttributeValueEntity();
        valueEntity1.setId(10L);
        valueEntity1.setAttributeId(1L);
        valueEntity1.setValue("红色");
        valueEntity1.setSortScore(1);

        valueEntity2 = new AttributeValueEntity();
        valueEntity2.setId(11L);
        valueEntity2.setAttributeId(1L);
        valueEntity2.setValue("蓝色");
        valueEntity2.setSortScore(2);

        valueEntity3 = new AttributeValueEntity();
        valueEntity3.setId(20L);
        valueEntity3.setAttributeId(2L);
        valueEntity3.setValue("XL");
        valueEntity3.setSortScore(1);
    }

    // ========== getAttributesByIds ==========

    @Test
    void getAttributesByIds_withValidIds_returnsAttributes() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(attributeMapper.findByIds(ids)).thenReturn(Arrays.asList(attributeEntity1, attributeEntity2));

        List<Attribute> result = attributeService.getAttributesByIds(ids);

        assertEquals(2, result.size());
        assertEquals("颜色", result.get(0).getName());
        assertEquals(AttributeType.SKU, result.get(0).getAttributeType());
        assertEquals(AttributeInputType.SINGLE_SELECT, result.get(0).getInputType());
        assertEquals("尺寸", result.get(1).getName());
        verify(attributeMapper).findByIds(ids);
    }

    @Test
    void getAttributesByIds_withEmptyList_returnsEmptyList() {
        List<Attribute> result = attributeService.getAttributesByIds(Collections.emptyList());

        assertTrue(result.isEmpty());
        verifyNoInteractions(attributeMapper);
    }

    @Test
    void getAttributesByIds_withNull_returnsEmptyList() {
        List<Attribute> result = attributeService.getAttributesByIds(null);

        assertTrue(result.isEmpty());
        verifyNoInteractions(attributeMapper);
    }

    @Test
    void getAttributesByIds_withPartialMatch_returnsFoundOnly() {
        List<Long> ids = Arrays.asList(1L, 999L);
        when(attributeMapper.findByIds(ids)).thenReturn(Collections.singletonList(attributeEntity1));

        List<Attribute> result = attributeService.getAttributesByIds(ids);

        assertEquals(1, result.size());
        assertEquals("颜色", result.get(0).getName());
    }

    // ========== getAttributeValuesByAttributeIds ==========

    @Test
    void getAttributeValuesByAttributeIds_withValidIds_returnsGroupedMap() {
        List<Long> attributeIds = Arrays.asList(1L, 2L);
        when(attributeValueMapper.findByAttributeIds(attributeIds))
                .thenReturn(Arrays.asList(valueEntity1, valueEntity2, valueEntity3));

        Map<Long, List<AttributeValue>> result = attributeService.getAttributeValuesByAttributeIds(attributeIds);

        assertEquals(2, result.size());
        assertEquals(2, result.get(1L).size());
        assertEquals("红色", result.get(1L).get(0).getValue());
        assertEquals("蓝色", result.get(1L).get(1).getValue());
        assertEquals(1, result.get(2L).size());
        assertEquals("XL", result.get(2L).get(0).getValue());
        verify(attributeValueMapper).findByAttributeIds(attributeIds);
    }

    @Test
    void getAttributeValuesByAttributeIds_withEmptyList_returnsEmptyMap() {
        Map<Long, List<AttributeValue>> result = attributeService.getAttributeValuesByAttributeIds(Collections.emptyList());

        assertTrue(result.isEmpty());
        verifyNoInteractions(attributeValueMapper);
    }

    @Test
    void getAttributeValuesByAttributeIds_withNull_returnsEmptyMap() {
        Map<Long, List<AttributeValue>> result = attributeService.getAttributeValuesByAttributeIds(null);

        assertTrue(result.isEmpty());
        verifyNoInteractions(attributeValueMapper);
    }

    // ========== getAttributeValuesByIds ==========

    @Test
    void getAttributeValuesByIds_withValidIds_returnsMapById() {
        List<Long> ids = Arrays.asList(10L, 11L, 20L);
        when(attributeValueMapper.findByIds(ids))
                .thenReturn(Arrays.asList(valueEntity1, valueEntity2, valueEntity3));

        Map<Long, AttributeValue> result = attributeService.getAttributeValuesByIds(ids);

        assertEquals(3, result.size());
        assertEquals("红色", result.get(10L).getValue());
        assertEquals(1L, result.get(10L).getAttributeId());
        assertEquals("蓝色", result.get(11L).getValue());
        assertEquals("XL", result.get(20L).getValue());
        verify(attributeValueMapper).findByIds(ids);
    }

    @Test
    void getAttributeValuesByIds_withEmptyList_returnsEmptyMap() {
        Map<Long, AttributeValue> result = attributeService.getAttributeValuesByIds(Collections.emptyList());

        assertTrue(result.isEmpty());
        verifyNoInteractions(attributeValueMapper);
    }

    @Test
    void getAttributeValuesByIds_withNull_returnsEmptyMap() {
        Map<Long, AttributeValue> result = attributeService.getAttributeValuesByIds(null);

        assertTrue(result.isEmpty());
        verifyNoInteractions(attributeValueMapper);
    }

    // ========== ensureItemAttributes ==========

    private ItemAttributeRequest buildAttrRequest(Long attrId, Long valueId, String value) {
        ItemAttributeRequest req = new ItemAttributeRequest();
        req.setAttributeId(attrId);
        req.setAttributeValueId(valueId);
        req.setValue(value);
        return req;
    }

    private ItemAttributeRelationEntity buildRelation(Long itemId, Long skuId, Long attrId) {
        ItemAttributeRelationEntity e = new ItemAttributeRelationEntity();
        e.setItemId(itemId);
        e.setSkuId(skuId);
        e.setAttributeId(attrId);
        return e;
    }

    @Test
    void ensureItemAttributes_updateShouldInsertAllAttributes() {
        Long itemId = 1L;
        Long skuId = 0L;

        ItemAttributeRelationEntity existingA = buildRelation(itemId, skuId, 10L);
        ItemAttributeRelationEntity existingB = buildRelation(itemId, skuId, 20L);
        when(itemAttributeRelationMapper.findByItemIdAndSkuId(itemId, skuId))
                .thenReturn(Arrays.asList(existingA, existingB));
        when(itemAttributeRelationMapper.deleteByItemIdAndAttributeIds(itemId, Arrays.asList(10L, 20L)))
                .thenReturn(2);

        List<ItemAttributeRequest> newAttrs = Arrays.asList(
                buildAttrRequest(10L, 100L, null),
                buildAttrRequest(30L, 300L, null)
        );
        when(itemAttributeRelationMapper.batchInsert(anyList())).thenReturn(2);

        attributeService.ensureItemAttributes(itemId, skuId, newAttrs);

        ArgumentCaptor<List<ItemAttributeRelationEntity>> captor = ArgumentCaptor.forClass(List.class);
        verify(itemAttributeRelationMapper).batchInsert(captor.capture());

        List<ItemAttributeRelationEntity> inserted = captor.getValue();
        assertEquals(2, inserted.size());
        assertEquals(10L, inserted.get(0).getAttributeId());
        assertEquals(30L, inserted.get(1).getAttributeId());
    }

    @Test
    void ensureItemAttributes_emptyExisting_insertsAll() {
        Long itemId = 1L;
        Long skuId = 0L;

        when(itemAttributeRelationMapper.findByItemIdAndSkuId(itemId, skuId))
                .thenReturn(Collections.emptyList());

        List<ItemAttributeRequest> newAttrs = Arrays.asList(
                buildAttrRequest(10L, 100L, null),
                buildAttrRequest(20L, 200L, null)
        );
        when(itemAttributeRelationMapper.batchInsert(anyList())).thenReturn(2);

        attributeService.ensureItemAttributes(itemId, skuId, newAttrs);

        ArgumentCaptor<List<ItemAttributeRelationEntity>> captor = ArgumentCaptor.forClass(List.class);
        verify(itemAttributeRelationMapper).batchInsert(captor.capture());
        assertEquals(2, captor.getValue().size());
        verify(itemAttributeRelationMapper, never()).deleteByItemIdAndAttributeIds(anyLong(), anyList());
    }
}
