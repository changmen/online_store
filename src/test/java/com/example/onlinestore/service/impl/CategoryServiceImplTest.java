package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.Category;
import com.example.onlinestore.constants.Constants;
import com.example.onlinestore.entity.CategoryEntity;
import com.example.onlinestore.mapper.CategoryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private CategoryEntity rootCategoryEntity;
    private CategoryEntity childCategoryEntity;
    private List<CategoryEntity> categoryEntities;

    @BeforeEach
    void setUp() {
        // 根类目实体
        rootCategoryEntity = new CategoryEntity();
        rootCategoryEntity.setId(1L);
        rootCategoryEntity.setName("电子产品");
        rootCategoryEntity.setParentId(Constants.ROOT_CATEGORY_PARENT_ID);
        rootCategoryEntity.setSort(100);
        rootCategoryEntity.setStatus("ACTIVE");
        rootCategoryEntity.setCreatedAt(LocalDateTime.now());
        rootCategoryEntity.setUpdatedAt(LocalDateTime.now());

        // 子类目实体
        childCategoryEntity = new CategoryEntity();
        childCategoryEntity.setId(2L);
        childCategoryEntity.setName("智能手机");
        childCategoryEntity.setParentId(1L);
        childCategoryEntity.setSort(200);
        childCategoryEntity.setStatus("ACTIVE");
        childCategoryEntity.setCreatedAt(LocalDateTime.now());
        childCategoryEntity.setUpdatedAt(LocalDateTime.now());

        categoryEntities = Arrays.asList(rootCategoryEntity, childCategoryEntity);

        // 模拟加载类目数据
        when(categoryMapper.findAllCategories(0, 1000)).thenReturn(categoryEntities);
        
        // 手动触发loadCategory方法来初始化内部状态
        try {
            categoryService.afterPropertiesSet();
            // 等待一段时间让定时任务执行
            Thread.sleep(100);
        } catch (Exception e) {
            // 忽略异常
        }
    }

    @Test
    void testIsRootCategory_True() {
        // Given
        setupCategoryMap();

        // When
        boolean result = categoryService.isRootCategory(1L);

        // Then
        assertTrue(result);
    }

    @Test
    void testIsRootCategory_False() {
        // Given
        setupCategoryMap();

        // When
        boolean result = categoryService.isRootCategory(2L);

        // Then
        assertFalse(result);
    }

    @Test
    void testIsRootCategory_CategoryNotExist() {
        // Given
        setupCategoryMap();

        // When
        boolean result = categoryService.isRootCategory(999L);

        // Then
        assertFalse(result);
    }

    @Test
    void testGetRootCategories_Success() {
        // Given
        setupCategoryMap();
        setupRootCategories();

        // When
        List<Category> result = categoryService.getRootCategories();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("电子产品", result.get(0).getName());
        assertEquals(Constants.ROOT_CATEGORY_PARENT_ID, result.get(0).getParentId());
    }

    @Test
    void testGetRootCategories_Empty() {
        // Given - 不设置根类目

        // When
        List<Category> result = categoryService.getRootCategories();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetCategoryById_Success() {
        // Given
        setupCategoryMap();

        // When
        Category result = categoryService.getCategoryById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("电子产品", result.getName());
        assertEquals(Constants.ROOT_CATEGORY_PARENT_ID, result.getParentId());
    }

    @Test
    void testGetCategoryById_NotFound() {
        // Given
        setupCategoryMap();

        // When
        Category result = categoryService.getCategoryById(999L);

        // Then
        assertNull(result);
    }

    @Test
    void testGetAllCategories() {
        // When
        List<Category> result = categoryService.getAllCategories();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty()); // 当前实现返回空列表
    }

    @Test
    void testGetChildCategories() {
        // When
        List<Category> result = categoryService.getChildCategories(1L);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty()); // 当前实现返回空列表
    }

    @Test
    void testLoadCategoryWithException() {
        // Given
        when(categoryMapper.findAllCategories(0, 1000))
            .thenThrow(new RuntimeException("Database error"));

        // When
        try {
            categoryService.afterPropertiesSet();
            Thread.sleep(100); // 等待定时任务执行
        } catch (Exception e) {
            // 忽略异常
        }

        // Then - 应该捕获异常并继续运行，不会影响服务的正常功能
        assertDoesNotThrow(() -> categoryService.isRootCategory(1L));
    }

    @Test
    void testDestroy() {
        // When & Then
        assertDoesNotThrow(() -> categoryService.destroy());
    }

    // 辅助方法：设置类目映射
    private void setupCategoryMap() {
        Map<Long, Category> categoryMap = new HashMap<>();
        
        Category rootCategory = new Category();
        rootCategory.setId(1L);
        rootCategory.setName("电子产品");
        rootCategory.setParentId(Constants.ROOT_CATEGORY_PARENT_ID);
        rootCategory.setSort(100);
        rootCategory.setStatus("ACTIVE");
        rootCategory.setChildren(Set.of(2L));
        
        Category childCategory = new Category();
        childCategory.setId(2L);
        childCategory.setName("智能手机");
        childCategory.setParentId(1L);
        childCategory.setSort(200);
        childCategory.setStatus("ACTIVE");
        childCategory.setChildren(new HashSet<>());
        
        categoryMap.put(1L, rootCategory);
        categoryMap.put(2L, childCategory);
        
        ReflectionTestUtils.setField(categoryService, "categoryMap", categoryMap);
    }

    // 辅助方法：设置根类目集合
    private void setupRootCategories() {
        Set<Long> rootCategories = new HashSet<>();
        rootCategories.add(1L);
        ReflectionTestUtils.setField(categoryService, "rootCategories", rootCategories);
    }
}