package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.Attribute;
import com.example.onlinestore.bean.Brand;
import com.example.onlinestore.bean.Category;
import com.example.onlinestore.bean.Item;
import com.example.onlinestore.dto.CreateItemRequest;
import com.example.onlinestore.dto.ItemAttributeRequest;
import com.example.onlinestore.dto.UpdateItemRequest;
import com.example.onlinestore.entity.ItemEntity;
import com.example.onlinestore.enums.AttributeInputType;
import com.example.onlinestore.enums.AttributeType;
import com.example.onlinestore.exceptions.BizException;
import com.example.onlinestore.mapper.ItemMapper;
import com.example.onlinestore.service.AttributeService;
import com.example.onlinestore.service.BrandService;
import com.example.onlinestore.service.CategoryService;
import com.example.onlinestore.service.OssService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.mockito.ArgumentCaptor;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    private AttributeService attributeService;

    @Mock
    private OssService ossService;

    @Mock
    private ItemMapper itemMapper;

    @Mock
    private BrandService brandService;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private ItemServiceImpl itemService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(itemService, "forbiddenWords", "刀");
        ReflectionTestUtils.setField(itemService, "uploadDescriptionToOSS", false);
        ReflectionTestUtils.setField(itemService, "defaultItemSortScore", 1);
    }

    private Attribute buildAttribute(Long id, String name, AttributeInputType inputType) {
        Attribute attr = new Attribute();
        attr.setId(id);
        attr.setName(name);
        attr.setAttributeType(AttributeType.OTHER);
        attr.setInputType(inputType);
        attr.setRequired(1);
        attr.setSearchable(0);
        attr.setSortScore(1);
        attr.setVisible(1);
        return attr;
    }

    private ItemAttributeRequest buildAttributeRequest(Long attributeId, Long valueId, String value) {
        ItemAttributeRequest req = new ItemAttributeRequest();
        req.setAttributeId(attributeId);
        req.setAttributeValueId(valueId);
        req.setValue(value);
        return req;
    }

    private CreateItemRequest buildCreateRequest(List<ItemAttributeRequest> attributes) {
        CreateItemRequest request = new CreateItemRequest();
        request.setName("测试商品");
        request.setDescription("测试描述");
        request.setBrandId(1L);
        request.setCategoryId(1L);
        request.setMainImageUrl("http://example.com/img.jpg");
        request.setSubImageUrls(Collections.emptyList());
        request.setAttributes(attributes);
        request.setSortScore(1);
        return request;
    }

    @Test
    void createItem_shouldUseBatchQueryForAttributes() {
        Attribute attr1 = buildAttribute(1L, "颜色", AttributeInputType.SINGLE_SELECT);
        Attribute attr2 = buildAttribute(2L, "尺寸", AttributeInputType.SINGLE_SELECT);

        CreateItemRequest request = buildCreateRequest(Arrays.asList(
                buildAttributeRequest(1L, 10L, null),
                buildAttributeRequest(2L, 20L, null)
        ));

        when(attributeService.getAttributesByIds(Arrays.asList(1L, 2L)))
                .thenReturn(Arrays.asList(attr1, attr2));
        when(categoryService.getCategoryById(1L)).thenReturn(new Category());
        when(brandService.getBrandById(1L)).thenReturn(new Brand());

        when(itemMapper.insert(any(ItemEntity.class))).thenReturn(1);

        itemService.createItem(request);

        // 验证使用了批量查询，而非逐个查询
        verify(attributeService).getAttributesByIds(Arrays.asList(1L, 2L));
        verify(attributeService, never()).getAttributeById(anyLong());
    }

    @Test
    void createItem_withNonExistentAttribute_throwsException() {
        CreateItemRequest request = buildCreateRequest(Arrays.asList(
                buildAttributeRequest(1L, 10L, null),
                buildAttributeRequest(999L, 20L, null)
        ));

        Attribute attr1 = buildAttribute(1L, "颜色", AttributeInputType.SINGLE_SELECT);
        when(attributeService.getAttributesByIds(Arrays.asList(1L, 999L)))
                .thenReturn(Collections.singletonList(attr1));

        assertThrows(BizException.class, () -> itemService.createItem(request));

        verify(attributeService).getAttributesByIds(anyList());
        verify(attributeService, never()).getAttributeById(anyLong());
    }

    @Test
    void createItem_withInputTypeAttribute_validatesTextInput() {
        Attribute attr = buildAttribute(1L, "备注", AttributeInputType.INPUT);

        CreateItemRequest request = buildCreateRequest(Arrays.asList(
                buildAttributeRequest(1L, null, "一些备注文字")
        ));

        when(attributeService.getAttributesByIds(Collections.singletonList(1L)))
                .thenReturn(Collections.singletonList(attr));
        when(categoryService.getCategoryById(1L)).thenReturn(new Category());
        when(brandService.getBrandById(1L)).thenReturn(new Brand());
        when(itemMapper.insert(any(ItemEntity.class))).thenReturn(1);

        Item result = itemService.createItem(request);

        assertNotNull(result);
        verify(attributeService).getAttributesByIds(Collections.singletonList(1L));
    }

    @Test
    void createItem_withInputTypeAttribute_missingValue_throwsException() {
        Attribute attr = buildAttribute(1L, "备注", AttributeInputType.INPUT);

        CreateItemRequest request = buildCreateRequest(Arrays.asList(
                buildAttributeRequest(1L, null, null)
        ));

        when(attributeService.getAttributesByIds(Collections.singletonList(1L)))
                .thenReturn(Collections.singletonList(attr));

        assertThrows(BizException.class, () -> itemService.createItem(request));
    }

    @Test
    void createItem_withMultipleAttributes_singleBatchQuery() {
        Attribute attr1 = buildAttribute(1L, "颜色", AttributeInputType.SINGLE_SELECT);
        Attribute attr2 = buildAttribute(2L, "尺寸", AttributeInputType.SINGLE_SELECT);
        Attribute attr3 = buildAttribute(3L, "材质", AttributeInputType.SINGLE_SELECT);

        CreateItemRequest request = buildCreateRequest(Arrays.asList(
                buildAttributeRequest(1L, 10L, null),
                buildAttributeRequest(2L, 20L, null),
                buildAttributeRequest(3L, 30L, null)
        ));

        when(attributeService.getAttributesByIds(Arrays.asList(1L, 2L, 3L)))
                .thenReturn(Arrays.asList(attr1, attr2, attr3));
        when(categoryService.getCategoryById(1L)).thenReturn(new Category());
        when(brandService.getBrandById(1L)).thenReturn(new Brand());
        when(itemMapper.insert(any(ItemEntity.class))).thenReturn(1);

        itemService.createItem(request);

        // 3个属性只调用1次批量查询，而非3次单独查询
        verify(attributeService, times(1)).getAttributesByIds(anyList());
        verify(attributeService, never()).getAttributeById(anyLong());
    }

    @Test
    void updateItem_shouldSetIdOnEntity() {
        ItemEntity existingItem = new ItemEntity();
        existingItem.setId(42L);
        existingItem.setName("旧名称");
        existingItem.setStatus("DRAFT");
        when(itemMapper.findById(42L)).thenReturn(existingItem);

        Attribute attr = buildAttribute(1L, "颜色", AttributeInputType.SINGLE_SELECT);
        when(attributeService.getAttributesByIds(Collections.singletonList(1L)))
                .thenReturn(Collections.singletonList(attr));
        when(itemMapper.update(any(ItemEntity.class))).thenReturn(1);

        UpdateItemRequest request = new UpdateItemRequest();
        request.setName("新名称");
        request.setDescription("新描述");
        request.setMainImageUrl("http://example.com/new.jpg");
        request.setSubImageUrls(Collections.emptyList());
        request.setAttributes(Collections.singletonList(buildAttributeRequest(1L, 10L, null)));

        itemService.updateItem(42L, request);

        ArgumentCaptor<ItemEntity> captor = ArgumentCaptor.forClass(ItemEntity.class);
        verify(itemMapper).update(captor.capture());
        assertEquals(42L, captor.getValue().getId());
        assertEquals("新名称", captor.getValue().getName());
    }
}
