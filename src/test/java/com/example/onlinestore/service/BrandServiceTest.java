package com.example.onlinestore.service;

import com.example.onlinestore.bean.Brand;
import com.example.onlinestore.dto.BrandListQueryOptions;
import com.example.onlinestore.dto.Page;
import com.example.onlinestore.entity.BrandEntity;
import com.example.onlinestore.errors.ErrorCode;
import com.example.onlinestore.exceptions.BizException;
import com.example.onlinestore.mapper.BrandMapper;
import com.example.onlinestore.service.impl.BrandServiceImpl;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import com.github.pagehelper.PageHelper;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * BrandService单元测试
 */
@ExtendWith(MockitoExtension.class)
class BrandServiceTest {

    @Mock
    private BrandMapper brandMapper;

    @InjectMocks
    private BrandServiceImpl brandService;

    private BrandEntity testBrandEntity;
    private Brand testBrand;
    private BrandListQueryOptions testQueryOptions;

    @BeforeEach
    void setUp() {
        // 设置测试品牌实体
        testBrandEntity = new BrandEntity();
        testBrandEntity.setId(1L);
        testBrandEntity.setName("NIKE");
        testBrandEntity.setDescription("Just Do It");
        testBrandEntity.setLogo("https://example.com/nike-logo.png");
        testBrandEntity.setStory("Nike is a global athletic brand story");
        testBrandEntity.setSortScore(100);
        testBrandEntity.setVisible(1);
        testBrandEntity.setCreatedAt(LocalDateTime.now());
        testBrandEntity.setUpdatedAt(LocalDateTime.now());

        // 设置测试品牌对象
        testBrand = new Brand();
        testBrand.setId(1L);
        testBrand.setName("ADIDAS");
        testBrand.setDescription("Impossible is Nothing");
        testBrand.setLogo("https://example.com/adidas-logo.png");
        testBrand.setStory("Adidas is a global sports brand with three stripes story");
        testBrand.setSortScore(90);
        testBrand.setVisible(1);

        // 设置查询选项
        testQueryOptions = new BrandListQueryOptions();
        testQueryOptions.setPageNum(1);
        testQueryOptions.setPageSize(10);
        testQueryOptions.setVisible(1);
    }

    @Test
    void testGetBrandByIdSuccess() {
        // Given
        when(brandMapper.findById(1L)).thenReturn(testBrandEntity);

        // When
        Brand result = brandService.getBrandById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("NIKE", result.getName());
        assertEquals("Just Do It", result.getDescription());
        assertEquals("https://example.com/nike-logo.png", result.getLogo());
        assertEquals("Nike is a global athletic brand story", result.getStory());
        assertEquals(100, result.getSortScore());
        assertEquals(1, result.getVisible());
        
        verify(brandMapper).findById(1L);
    }

    @Test
    void testGetBrandByIdNotFound() {
        // Given
        when(brandMapper.findById(1L)).thenReturn(null);

        // When & Then
        BizException exception = assertThrows(BizException.class, 
            () -> brandService.getBrandById(1L));
        
        assertEquals(ErrorCode.BRAND_NOT_FOUND, exception.getErrorCode());
        verify(brandMapper).findById(1L);
    }

    @Test
    void testListBrands() {
        // Given
        List<BrandEntity> brandEntities = Arrays.asList(testBrandEntity);
        
        try (MockedStatic<PageHelper> pageHelperMock = mockStatic(PageHelper.class)) {
            when(brandMapper.findAllBrands(testQueryOptions)).thenReturn(brandEntities);
            
            // When
            Page<Brand> result = brandService.listBrands(testQueryOptions);

            // Then
            assertNotNull(result);
            assertEquals(1, result.getData().size());
            assertEquals("NIKE", result.getData().get(0).getName());
            assertEquals(1, result.getPageNum());
            assertEquals(10, result.getPageSize());
            
            pageHelperMock.verify(() -> PageHelper.startPage(1, 10, "sort_score DESC"));
            verify(brandMapper).findAllBrands(testQueryOptions);
        }
    }

    @Test
    void testListBrandsWithCustomOrder() {
        // Given
        testQueryOptions.setOrderBy("name ASC");
        List<BrandEntity> brandEntities = Arrays.asList(testBrandEntity);
        
        try (MockedStatic<PageHelper> pageHelperMock = mockStatic(PageHelper.class)) {
            when(brandMapper.findAllBrands(testQueryOptions)).thenReturn(brandEntities);
            
            // When
            Page<Brand> result = brandService.listBrands(testQueryOptions);

            // Then
            assertNotNull(result);
            pageHelperMock.verify(() -> PageHelper.startPage(1, 10, "name ASC"));
            verify(brandMapper).findAllBrands(testQueryOptions);
        }
    }

    @Test
    void testTianJiaPingPaiSuccess() {
        // Given
        when(brandMapper.findByName("ADIDAS")).thenReturn(null);
        when(brandMapper.insert(any(BrandEntity.class))).thenAnswer(invocation -> {
            BrandEntity entity = invocation.getArgument(0);
            entity.setId(2L);
            return 1;
        });

        // When
        Brand result = brandService.tianJiaPingPai(testBrand);

        // Then
        assertNotNull(result);
        assertEquals("ADIDAS", result.getName());
        assertEquals("Impossible is Nothing", result.getDescription());
        assertEquals(90, result.getSortScore());
        assertEquals(1, result.getVisible());
        
        verify(brandMapper).findByName("ADIDAS");
        verify(brandMapper).insert(any(BrandEntity.class));
    }

    @Test
    void testTianJiaPingPaiWithDefaults() {
        // Given
        testBrand.setSortScore(null);
        testBrand.setVisible(null);
        when(brandMapper.findByName("ADIDAS")).thenReturn(null);
        when(brandMapper.insert(any(BrandEntity.class))).thenAnswer(invocation -> {
            BrandEntity entity = invocation.getArgument(0);
            entity.setId(2L);
            return 1;
        });

        // When
        Brand result = brandService.tianJiaPingPai(testBrand);

        // Then
        assertNotNull(result);
        assertEquals(100, result.getSortScore()); // 默认值
        assertEquals(1, result.getVisible()); // 默认值
        
        verify(brandMapper).insert(any(BrandEntity.class));
    }

    @Test
    void testTianJiaPingPaiDuplicateName() {
        // Given
        when(brandMapper.findByName("ADIDAS")).thenReturn(testBrandEntity);

        // When & Then
        BizException exception = assertThrows(BizException.class, 
            () -> brandService.tianJiaPingPai(testBrand));
        
        assertEquals(ErrorCode.BRAND_NAME_DUPLICATED, exception.getErrorCode());
        verify(brandMapper).findByName("ADIDAS");
        verify(brandMapper, never()).insert(any());
    }

    @Test
    void testTianJiaPingPaiContainsSpecialCharacter() {
        // Given
        testBrand.setName("假货品牌");

        // When & Then
        BizException exception = assertThrows(BizException.class, 
            () -> brandService.tianJiaPingPai(testBrand));
        
        assertEquals(ErrorCode.BRAND_NAME_CONTAIN_SPECIAL_CHARACTER, exception.getErrorCode());
        verify(brandMapper, never()).findByName(anyString());
        verify(brandMapper, never()).insert(any());
    }

    @Test
    void testTianJiaPingPaiDatabaseError() {
        // Given
        when(brandMapper.findByName("ADIDAS")).thenReturn(null);
        when(brandMapper.insert(any(BrandEntity.class))).thenReturn(0);

        // When & Then
        BizException exception = assertThrows(BizException.class, 
            () -> brandService.tianJiaPingPai(testBrand));
        
        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());
        verify(brandMapper).insert(any(BrandEntity.class));
    }

    @Test
    void testUpdateBrandSuccess() {
        // Given
        Brand existingBrand = new Brand();
        existingBrand.setId(1L);
        existingBrand.setName("NIKE");
        existingBrand.setDescription("Old Description");
        existingBrand.setLogo("https://example.com/old-logo.png");
        existingBrand.setStory("Old story");
        existingBrand.setSortScore(80);
        existingBrand.setVisible(0);

        Brand updatedBrand = new Brand();
        updatedBrand.setName("NIKE"); // 相同名称
        updatedBrand.setDescription("New Description");
        updatedBrand.setLogo("https://example.com/new-logo.png");
        updatedBrand.setStory("New story");
        updatedBrand.setSortScore(90);
        updatedBrand.setVisible(1);

        when(brandMapper.findById(1L)).thenReturn(testBrandEntity);
        when(brandMapper.update(any(BrandEntity.class))).thenReturn(1);

        // When
        brandService.updateBrand(1L, updatedBrand);

        // Then
        verify(brandMapper).findById(1L);
        verify(brandMapper).update(any(BrandEntity.class));
    }

    @Test
    void testUpdateBrandNameModifyForbidden() {
        // Given
        Brand updatedBrand = new Brand();
        updatedBrand.setName("DIFFERENT_NAME");
        updatedBrand.setDescription("New Description");

        when(brandMapper.findById(1L)).thenReturn(testBrandEntity);

        // When & Then
        BizException exception = assertThrows(BizException.class, 
            () -> brandService.updateBrand(1L, updatedBrand));
        
        assertEquals(ErrorCode.BRAND_NAME_MODIFY_FORBIDDEN, exception.getErrorCode());
        verify(brandMapper).findById(1L);
        verify(brandMapper, never()).update(any());
    }

    @Test
    void testDelteBrandSuccess() {
        // Given
        when(brandMapper.findById(1L)).thenReturn(testBrandEntity);
        when(brandMapper.deleteById(1L)).thenReturn(1);

        // When
        brandService.delteBrand(1L);

        // Then
        verify(brandMapper).findById(1L);
        verify(brandMapper).deleteById(1L);
    }

    @Test
    void testDelteBrandNotFound() {
        // Given
        when(brandMapper.findById(1L)).thenReturn(null);

        // When & Then
        BizException exception = assertThrows(BizException.class, 
            () -> brandService.delteBrand(1L));
        
        assertEquals(ErrorCode.BRAND_NOT_FOUND, exception.getErrorCode());
        verify(brandMapper).findById(1L);
        verify(brandMapper, never()).deleteById(anyLong());
    }

    @Test
    void testDelteBrandDatabaseError() {
        // Given
        when(brandMapper.findById(1L)).thenReturn(testBrandEntity);
        when(brandMapper.deleteById(1L)).thenReturn(0);

        // When & Then
        BizException exception = assertThrows(BizException.class, 
            () -> brandService.delteBrand(1L));
        
        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());
        verify(brandMapper).deleteById(1L);
    }
}