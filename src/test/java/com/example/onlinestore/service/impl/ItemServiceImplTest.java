package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.Brand;
import com.example.onlinestore.bean.Category;
import com.example.onlinestore.bean.Item;
import com.example.onlinestore.dto.CreateItemRequest;
import com.example.onlinestore.dto.ItemAttributeRequest;
import com.example.onlinestore.dto.UpdateItemRequest;
import com.example.onlinestore.entity.ItemEntity;
import com.example.onlinestore.errors.ErrorCode;
import com.example.onlinestore.exceptions.BizException;
import com.example.onlinestore.mapper.ItemMapper;
import com.example.onlinestore.service.AttributeService;
import com.example.onlinestore.service.BrandService;
import com.example.onlinestore.service.CategoryService;
import com.example.onlinestore.service.ContentValidationService;
import com.example.onlinestore.service.ItemDetailCacheService;
import com.example.onlinestore.service.OssService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

    @Mock
    private ItemDetailCacheService itemDetailCacheService;

    @Mock
    private ContentValidationService contentValidationService;

    private ItemServiceImpl itemService;

    @BeforeEach
    void setUp() {
        itemService = new ItemServiceImpl(false, 1,
                attributeService, ossService, itemMapper, brandService, categoryService, itemDetailCacheService, contentValidationService);
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
    void createItem_shouldDelegateAttributeValidation() {
        CreateItemRequest request = buildCreateRequest(Arrays.asList(
                buildAttributeRequest(1L, 10L, null),
                buildAttributeRequest(2L, 20L, null)
        ));

        when(categoryService.getCategoryById(1L)).thenReturn(new Category());
        when(brandService.getBrandById(1L)).thenReturn(new Brand());
        when(itemMapper.insert(any(ItemEntity.class))).thenReturn(1);

        itemService.createItem(request);

        verify(attributeService).validateItemAttributes(request.getAttributes());
        verify(attributeService).ensureItemAttributes(nullable(Long.class), eq(0L), anyList());
    }

    @Test
    void createItem_withInvalidAttribute_throwsException() {
        CreateItemRequest request = buildCreateRequest(Arrays.asList(
                buildAttributeRequest(1L, 10L, null),
                buildAttributeRequest(999L, 20L, null)
        ));

        doThrow(new BizException(ErrorCode.ATTRIBUTE_NOT_FOUND, 999L))
                .when(attributeService).validateItemAttributes(anyList());

        assertThrows(BizException.class, () -> itemService.createItem(request));

        verify(itemMapper, never()).insert(any());
    }

    @Test
    void createItem_withValidAttributes_succeeds() {
        CreateItemRequest request = buildCreateRequest(Arrays.asList(
                buildAttributeRequest(1L, null, "一些备注文字")
        ));

        when(categoryService.getCategoryById(1L)).thenReturn(new Category());
        when(brandService.getBrandById(1L)).thenReturn(new Brand());
        when(itemMapper.insert(any(ItemEntity.class))).thenReturn(1);

        Item result = itemService.createItem(request);

        assertNotNull(result);
        verify(attributeService).validateItemAttributes(request.getAttributes());
    }

    @Test
    void createItem_withMissingValue_throwsException() {
        CreateItemRequest request = buildCreateRequest(Arrays.asList(
                buildAttributeRequest(1L, null, null)
        ));

        doThrow(new BizException(ErrorCode.ITEM_ATTRIBUTE_VALUE_IS_EMPTY, 1L))
                .when(attributeService).validateItemAttributes(anyList());

        assertThrows(BizException.class, () -> itemService.createItem(request));
    }

    @Test
    void createItem_withMultipleAttributes_delegatesValidationOnce() {
        CreateItemRequest request = buildCreateRequest(Arrays.asList(
                buildAttributeRequest(1L, 10L, null),
                buildAttributeRequest(2L, 20L, null),
                buildAttributeRequest(3L, 30L, null)
        ));

        when(categoryService.getCategoryById(1L)).thenReturn(new Category());
        when(brandService.getBrandById(1L)).thenReturn(new Brand());
        when(itemMapper.insert(any(ItemEntity.class))).thenReturn(1);

        itemService.createItem(request);

        verify(attributeService, times(1)).validateItemAttributes(anyList());
    }

    @Test
    void updateItem_shouldSetIdOnEntity() {
        ItemEntity existingItem = new ItemEntity();
        existingItem.setId(42L);
        existingItem.setName("旧名称");
        existingItem.setStatus("DRAFT");
        when(itemMapper.findByIdBasic(42L)).thenReturn(existingItem);
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
        verify(attributeService).validateItemAttributes(request.getAttributes());
    }
}
