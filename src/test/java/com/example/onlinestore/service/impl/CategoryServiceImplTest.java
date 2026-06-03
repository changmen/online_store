package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.Category;
import com.example.onlinestore.mapper.CategoryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @BeforeEach
    void setUp() {
        Category root = new Category();
        root.setId(1L);
        root.setName("服装");
        root.setParentId(0L);
        root.setChildren(Set.of(2L));

        Category child = new Category();
        child.setId(2L);
        child.setName("男装");
        child.setParentId(1L);
        child.setChildren(Set.of());

        Map<Long, Category> categoryMap = new ConcurrentHashMap<>();
        categoryMap.put(1L, root);
        categoryMap.put(2L, child);

        ReflectionTestUtils.setField(categoryService, "categoryMap", categoryMap);
        ReflectionTestUtils.setField(categoryService, "rootCategories", Set.of(1L));
    }

    @Test
    void getCategoryById_exists_returnsCategory() {
        Category result = categoryService.getCategoryById(1L);

        assertNotNull(result);
        assertEquals("服装", result.getName());
    }

    @Test
    void getCategoryById_notFound_returnsNull() {
        Category result = categoryService.getCategoryById(999L);

        assertNull(result);
    }

    @Test
    void isRootCategory_root_returnsTrue() {
        assertTrue(categoryService.isRootCategory(1L));
    }

    @Test
    void isRootCategory_child_returnsFalse() {
        assertFalse(categoryService.isRootCategory(2L));
    }

    @Test
    void isRootCategory_notFound_returnsFalse() {
        assertFalse(categoryService.isRootCategory(999L));
    }

    @Test
    void getRootCategories_returnsRoots() {
        List<Category> roots = categoryService.getRootCategories();

        assertFalse(roots.isEmpty());
        assertEquals(1, roots.size());
        assertEquals("服装", roots.get(0).getName());
    }
}
