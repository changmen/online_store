package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.Attribute;
import com.example.onlinestore.bean.Item;
import com.example.onlinestore.dto.*;
import com.example.onlinestore.entity.ItemEntity;
import com.example.onlinestore.enums.AttributeInputType;
import com.example.onlinestore.enums.AttributeType;
import com.example.onlinestore.enums.ItemStatus;
import com.example.onlinestore.errors.ErrorCode;
import com.example.onlinestore.exceptions.BizException;
import com.example.onlinestore.mapper.ItemMapper;
import com.example.onlinestore.service.*;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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

    private CreateItemRequest createItemRequest;
    private UpdateItemRequest updateItemRequest;
    private ItemEntity itemEntity;
    private Attribute attribute;
    private ItemAttributeRequest itemAttributeRequest;
    private ItemListQueryRequest queryRequest;

    @BeforeEach
    void setUp() {
        // 设置配置参数
        ReflectionTestUtils.setField(itemService, "forbiddenWords", "刀,枪");
        ReflectionTestUtils.setField(itemService, "uploadDescriptionToOSS", false);
        ReflectionTestUtils.setField(itemService, "defaultItemSortScore", 1);

        // 创建商品请求
        createItemRequest = new CreateItemRequest();
        createItemRequest.setBrandId(1L);
        createItemRequest.setCategoryId(1L);
        createItemRequest.setName("测试商品");
        createItemRequest.setDescription("测试商品描述");
        createItemRequest.setMainImageUrl("http://example.com/main.jpg");
        createItemRequest.setSubImageUrls(Arrays.asList("http://example.com/sub1.jpg", "http://example.com/sub2.jpg"));
        createItemRequest.setSortScore(100);

        // 商品属性请求
        itemAttributeRequest = new ItemAttributeRequest();
        itemAttributeRequest.setAttributeId(1L);
        itemAttributeRequest.setAttributeValueId(1L);
        itemAttributeRequest.setValue("测试值");
        createItemRequest.setAttributes(Arrays.asList(itemAttributeRequest));

        // 更新商品请求
        updateItemRequest = new UpdateItemRequest();
        updateItemRequest.setName("更新后的商品");
        updateItemRequest.setDescription("更新后的描述");
        updateItemRequest.setMainImageUrl("http://example.com/new_main.jpg");
        updateItemRequest.setSubImageUrls(Arrays.asList("http://example.com/new_sub1.jpg"));
        updateItemRequest.setAttributes(Arrays.asList(itemAttributeRequest));

        // 属性对象
        attribute = new Attribute();
        attribute.setId(1L);
        attribute.setName("测试属性");
        attribute.setAttributeType(AttributeType.SKU);
        attribute.setInputType(AttributeInputType.SINGLE_SELECT);
        attribute.setRequired(1);

        // 商品实体
        itemEntity = new ItemEntity();
        itemEntity.setId(1L);
        itemEntity.setBrandId(1L);
        itemEntity.setCategoryId(1L);
        itemEntity.setName("测试商品");
        itemEntity.setDescription("测试商品描述");
        itemEntity.setMainImageURL("http://example.com/main.jpg");
        itemEntity.setSubImageURLs("[\"http://example.com/sub1.jpg\",\"http://example.com/sub2.jpg\"]");
        itemEntity.setStatus("DRAFT");
        itemEntity.setSortScore(100);
        itemEntity.setCreatedAt(LocalDateTime.now());
        itemEntity.setUpdatedAt(LocalDateTime.now());

        // 查询请求
        queryRequest = new ItemListQueryRequest();
        queryRequest.setPageNum(1);
        queryRequest.setPageSize(10);
    }

    @Test
    void testCreateItem_Success() {
        // Given
        when(attributeService.getAttributeById(1L)).thenReturn(attribute);
        when(categoryService.getCategoryById(1L)).thenReturn(null); // void method
        when(brandService.getBrandById(1L)).thenReturn(null); // void method
        when(itemMapper.insert(any(ItemEntity.class))).thenReturn(1);
        doNothing().when(attributeService).ensureItemAttributes(anyLong(), eq(0L), anyList());

        // When
        Item result = itemService.createItem(createItemRequest);

        // Then
        assertNotNull(result);
        assertEquals("测试商品", result.getName());
        assertEquals("测试商品描述", result.getDescription());
        assertEquals(1L, result.getBrandId());
        assertEquals(1L, result.getCategoryId());
        assertEquals(ItemStatus.DRAFT, result.getStatus());
        assertEquals(100, result.getSortScore());

        verify(attributeService).getAttributeById(1L);
        verify(categoryService).getCategoryById(1L);
        verify(brandService).getBrandById(1L);
        verify(itemMapper).insert(any(ItemEntity.class));
        verify(attributeService).ensureItemAttributes(anyLong(), eq(0L), anyList());
    }

    @Test
    void testCreateItem_NameContainsForbiddenWords() {
        // Given
        createItemRequest.setName("测试刀具");

        // When & Then
        BizException exception = assertThrows(BizException.class, () -> {
            itemService.createItem(createItemRequest);
        });
        assertEquals(ErrorCode.ITEM_NAME_CONTAINS_FORBIDDEN_WORDS, exception.getErrorCode());

        verify(itemMapper, never()).insert(any(ItemEntity.class));
    }

    @Test
    void testCreateItem_DescriptionContainsForbiddenWords() {
        // Given
        createItemRequest.setDescription("包含刀的描述");

        // When & Then
        BizException exception = assertThrows(BizException.class, () -> {
            itemService.createItem(createItemRequest);
        });
        assertEquals(ErrorCode.ITEM_DESCRIPTION_CONTAINS_FORBIDDEN_WORDS, exception.getErrorCode());

        verify(itemMapper, never()).insert(any(ItemEntity.class));
    }

    @Test
    void testCreateItem_AttributeValueEmpty_SelectType() {
        // Given
        itemAttributeRequest.setAttributeValueId(null);
        when(attributeService.getAttributeById(1L)).thenReturn(attribute);

        // When & Then
        BizException exception = assertThrows(BizException.class, () -> {
            itemService.createItem(createItemRequest);
        });
        assertEquals(ErrorCode.ITEM_ATTRIBUTE_VALUE_IS_EMPTY, exception.getErrorCode());

        verify(attributeService).getAttributeById(1L);
        verify(itemMapper, never()).insert(any(ItemEntity.class));
    }

    @Test
    void testCreateItem_AttributeValueEmpty_InputType() {
        // Given
        attribute.setInputType(AttributeInputType.INPUT);
        itemAttributeRequest.setValue("");
        when(attributeService.getAttributeById(1L)).thenReturn(attribute);

        // When & Then
        BizException exception = assertThrows(BizException.class, () -> {
            itemService.createItem(createItemRequest);
        });
        assertEquals(ErrorCode.ITEM_ATTRIBUTE_VALUE_IS_EMPTY, exception.getErrorCode());

        verify(attributeService).getAttributeById(1L);
        verify(itemMapper, never()).insert(any(ItemEntity.class));
    }

    @Test
    void testCreateItem_InsertFailed() {
        // Given
        when(attributeService.getAttributeById(1L)).thenReturn(attribute);
        when(categoryService.getCategoryById(1L)).thenReturn(null);
        when(brandService.getBrandById(1L)).thenReturn(null);
        when(itemMapper.insert(any(ItemEntity.class))).thenReturn(0);

        // When & Then
        BizException exception = assertThrows(BizException.class, () -> {
            itemService.createItem(createItemRequest);
        });
        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());

        verify(itemMapper).insert(any(ItemEntity.class));
        verify(attributeService, never()).ensureItemAttributes(anyLong(), anyLong(), anyList());
    }

    @Test
    void testUpdateItem_Success() {
        // Given
        when(itemMapper.findById(1L)).thenReturn(itemEntity);
        when(attributeService.getAttributeById(1L)).thenReturn(attribute);
        when(itemMapper.update(any(ItemEntity.class))).thenReturn(1);
        doNothing().when(attributeService).ensureItemAttributes(eq(1L), eq(0L), anyList());

        // When
        itemService.updateItem(1L, updateItemRequest);

        // Then
        verify(itemMapper).findById(1L);
        verify(attributeService).getAttributeById(1L);
        verify(itemMapper).update(any(ItemEntity.class));
        verify(attributeService).ensureItemAttributes(eq(1L), eq(0L), anyList());
    }

    @Test
    void testUpdateItem_NameContainsForbiddenWords() {
        // Given
        updateItemRequest.setName("更新后的刀具");
        when(itemMapper.findById(1L)).thenReturn(itemEntity);

        // When & Then
        BizException exception = assertThrows(BizException.class, () -> {
            itemService.updateItem(1L, updateItemRequest);
        });
        assertEquals(ErrorCode.ITEM_NAME_CONTAINS_FORBIDDEN_WORDS, exception.getErrorCode());

        verify(itemMapper).findById(1L);
        verify(itemMapper, never()).update(any(ItemEntity.class));
    }

    @Test
    void testUpdateItem_UpdateFailed() {
        // Given
        when(itemMapper.findById(1L)).thenReturn(itemEntity);
        when(attributeService.getAttributeById(1L)).thenReturn(attribute);
        when(itemMapper.update(any(ItemEntity.class))).thenReturn(0);

        // When & Then
        BizException exception = assertThrows(BizException.class, () -> {
            itemService.updateItem(1L, updateItemRequest);
        });
        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());

        verify(itemMapper).findById(1L);
        verify(attributeService).getAttributeById(1L);
        verify(itemMapper).update(any(ItemEntity.class));
        verify(attributeService, never()).ensureItemAttributes(anyLong(), anyLong(), anyList());
    }

    @Test
    void testGetItemById_Success() {
        // Given
        when(itemMapper.findById(1L)).thenReturn(itemEntity);

        // When
        Item result = itemService.getItemById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("测试商品", result.getName());
        assertEquals("测试商品描述", result.getDescription());
        assertEquals(1L, result.getBrandId());
        assertEquals(1L, result.getCategoryId());
        assertEquals(ItemStatus.DRAFT, result.getStatus());
        assertEquals(100, result.getSortScore());
        assertNotNull(result.getSubImageURLs());
        assertEquals(2, result.getSubImageURLs().size());

        verify(itemMapper).findById(1L);
    }

    @Test
    void testGetItemById_NotFound() {
        // Given
        when(itemMapper.findById(1L)).thenReturn(null);

        // When & Then
        BizException exception = assertThrows(BizException.class, () -> {
            itemService.getItemById(1L);
        });
        assertEquals(ErrorCode.ITEM_NOT_FOUND, exception.getErrorCode());

        verify(itemMapper).findById(1L);
    }

    @Test
    void testListItems_Success() {
        // Given
        List<ItemEntity> itemEntities = Arrays.asList(itemEntity);
        when(itemMapper.queryItemsByOptions(queryRequest)).thenReturn(itemEntities);

        // When
        Page<Item> result = itemService.listItems(queryRequest);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getData().size());
        assertEquals(1, result.getPageNum());
        assertEquals(10, result.getPageSize());
        
        Item item = result.getData().get(0);
        assertEquals("测试商品", item.getName());
        assertEquals(ItemStatus.DRAFT, item.getStatus());

        verify(itemMapper).queryItemsByOptions(queryRequest);
    }

    @Test
    void testCreateItem_WithOSSUpload() {
        // Given
        ReflectionTestUtils.setField(itemService, "uploadDescriptionToOSS", true);
        when(attributeService.getAttributeById(1L)).thenReturn(attribute);
        when(categoryService.getCategoryById(1L)).thenReturn(null);
        when(brandService.getBrandById(1L)).thenReturn(null);
        when(ossService.uploadItemDescription("测试商品描述")).thenReturn("http://oss.example.com/desc.html");
        when(itemMapper.insert(any(ItemEntity.class))).thenReturn(1);
        doNothing().when(attributeService).ensureItemAttributes(anyLong(), eq(0L), anyList());

        // When
        Item result = itemService.createItem(createItemRequest);

        // Then
        assertNotNull(result);
        assertEquals("测试商品", result.getName());

        verify(ossService).uploadItemDescription("测试商品描述");
        verify(itemMapper).insert(any(ItemEntity.class));
    }

    @Test
    void testGetItemDescription_WithOSS() {
        // Given
        itemEntity.setDescriptionURL("http://oss.example.com/desc.html");
        itemEntity.setDescription(null);
        when(itemMapper.findById(1L)).thenReturn(itemEntity);
        when(ossService.getItemDescription("http://oss.example.com/desc.html")).thenReturn("从 OSS 获取的描述");

        // When
        Item result = itemService.getItemById(1L);

        // Then
        assertNotNull(result);
        assertEquals("从 OSS 获取的描述", result.getDescription());

        verify(ossService).getItemDescription("http://oss.example.com/desc.html");
    }
}