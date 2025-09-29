package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.Brand;
import com.example.onlinestore.dto.BrandListQueryOptions;
import com.example.onlinestore.dto.Page;
import com.example.onlinestore.entity.BrandEntity;
import com.example.onlinestore.errors.ErrorCode;
import com.example.onlinestore.exceptions.BizException;
import com.example.onlinestore.mapper.BrandMapper;
import com.github.pagehelper.PageInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BrandServiceImplTest {

    @Mock
    private BrandMapper brandMapper;

    @InjectMocks
    private BrandServiceImpl brandService;

    private Brand brand;
    private BrandEntity brandEntity;
    private BrandListQueryOptions queryOptions;

    @BeforeEach
    void setUp() {
        // 品牌对象
        brand = new Brand();
        brand.setId(1L);
        brand.setName("NIKE");
        brand.setDescription("Just Do It");
        brand.setLogo("http://example.com/nike-logo.png");
        brand.setStory("Nike品牌故事");
        brand.setSortScore(100);
        brand.setVisible(1);

        // 品牌实体
        brandEntity = new BrandEntity();
        brandEntity.setId(1L);
        brandEntity.setName("NIKE");
        brandEntity.setDescription("Just Do It");
        brandEntity.setLogo("http://example.com/nike-logo.png");
        brandEntity.setStory("Nike品牌故事");
        brandEntity.setSortScore(100);
        brandEntity.setVisible(1);
        brandEntity.setCreatedAt(LocalDateTime.now());
        brandEntity.setUpdatedAt(LocalDateTime.now());

        // 查询选项
        queryOptions = new BrandListQueryOptions();
        queryOptions.setPageNum(1);
        queryOptions.setPageSize(10);
    }

    @Test
    void testGetBrandById_Success() {
        // Given
        when(brandMapper.findById(1L)).thenReturn(brandEntity);

        // When
        Brand result = brandService.getBrandById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("NIKE", result.getName());
        assertEquals("Just Do It", result.getDescription());
        assertEquals("http://example.com/nike-logo.png", result.getLogo());
        assertEquals("Nike品牌故事", result.getStory());
        assertEquals(100, result.getSortScore());
        assertEquals(1, result.getVisible());

        verify(brandMapper).findById(1L);
    }

    @Test
    void testGetBrandById_NotFound() {
        // Given
        when(brandMapper.findById(1L)).thenReturn(null);

        // When & Then
        BizException exception = assertThrows(BizException.class, () -> {
            brandService.getBrandById(1L);
        });
        assertEquals(ErrorCode.BRAND_NOT_FOUND, exception.getErrorCode());

        verify(brandMapper).findById(1L);
    }

    @Test
    void testUpdateBrand_Success() {
        // Given
        Brand updateBrand = new Brand();
        updateBrand.setName("NIKE"); // 保持名称不变
        updateBrand.setDescription("Updated Description");
        updateBrand.setLogo("http://example.com/new-logo.png");
        updateBrand.setStory("新的品牌故事");
        updateBrand.setSortScore(200);
        updateBrand.setVisible(0);

        when(brandMapper.findById(1L)).thenReturn(brandEntity);
        when(brandMapper.update(any(BrandEntity.class))).thenReturn(1);

        // When
        brandService.updateBrand(1L, updateBrand);

        // Then
        verify(brandMapper).findById(1L);
        verify(brandMapper).update(any(BrandEntity.class));
    }

    @Test
    void testUpdateBrand_NameModifyForbidden() {
        // Given
        Brand updateBrand = new Brand();
        updateBrand.setName("ADIDAS"); // 尝试修改名称

        when(brandMapper.findById(1L)).thenReturn(brandEntity);

        // When & Then
        BizException exception = assertThrows(BizException.class, () -> {
            brandService.updateBrand(1L, updateBrand);
        });
        assertEquals(ErrorCode.BRAND_NAME_MODIFY_FORBIDDEN, exception.getErrorCode());

        verify(brandMapper).findById(1L);
        verify(brandMapper, never()).update(any(BrandEntity.class));
    }

    @Test
    void testUpdateBrand_NoChangeNeeded() {
        // Given
        Brand updateBrand = new Brand();
        updateBrand.setName("NIKE");
        updateBrand.setDescription("Just Do It"); // 相同的描述
        updateBrand.setLogo("http://example.com/nike-logo.png"); // 相同的Logo
        updateBrand.setStory("Nike品牌故事"); // 相同的故事
        updateBrand.setSortScore(100); // 相同的排序分数
        updateBrand.setVisible(1); // 相同的可见性

        when(brandMapper.findById(1L)).thenReturn(brandEntity);

        // When
        brandService.updateBrand(1L, updateBrand);

        // Then
        verify(brandMapper).findById(1L);
        verify(brandMapper, never()).update(any(BrandEntity.class));
    }

    @Test
    void testUpdateBrand_UpdateFailed() {
        // Given
        Brand updateBrand = new Brand();
        updateBrand.setName("NIKE");
        updateBrand.setDescription("Updated Description");

        when(brandMapper.findById(1L)).thenReturn(brandEntity);
        when(brandMapper.update(any(BrandEntity.class))).thenReturn(0);

        // When & Then
        BizException exception = assertThrows(BizException.class, () -> {
            brandService.updateBrand(1L, updateBrand);
        });
        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());

        verify(brandMapper).update(any(BrandEntity.class));
    }

    @Test
    void testListBrands_Success() {
        // Given
        List<BrandEntity> brandEntities = Arrays.asList(brandEntity);
        when(brandMapper.findAllBrands(queryOptions)).thenReturn(brandEntities);

        // When
        Page<Brand> result = brandService.listBrands(queryOptions);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getData().size());
        assertEquals(1, result.getPageNum());
        assertEquals(10, result.getPageSize());
        
        Brand resultBrand = result.getData().get(0);
        assertEquals("NIKE", resultBrand.getName());

        verify(brandMapper).findAllBrands(queryOptions);
    }

    @Test
    void testTianJiaPingPai_Success() {
        // Given
        Brand newBrand = new Brand();
        newBrand.setName("nike"); // 小写名称，会被转换为大写
        newBrand.setDescription("New Brand");
        newBrand.setLogo("http://example.com/logo.png");

        when(brandMapper.findByName("NIKE")).thenReturn(null);
        when(brandMapper.insert(any(BrandEntity.class))).thenReturn(1);

        // When
        Brand result = brandService.tianJiaPingPai(newBrand);

        // Then
        assertNotNull(result);
        assertEquals("NIKE", result.getName());
        assertEquals("New Brand", result.getDescription());
        assertEquals(100, result.getSortScore()); // 默认值
        assertEquals(1, result.getVisible()); // 默认值

        verify(brandMapper).findByName("NIKE");
        verify(brandMapper).insert(any(BrandEntity.class));
    }

    @Test
    void testTianJiaPingPai_NameContainsSpecialCharacter() {
        // Given
        Brand newBrand = new Brand();
        newBrand.setName("假货品牌");

        // When & Then
        BizException exception = assertThrows(BizException.class, () -> {
            brandService.tianJiaPingPai(newBrand);
        });
        assertEquals(ErrorCode.BRAND_NAME_CONTAIN_SPECIAL_CHARACTER, exception.getErrorCode());

        verify(brandMapper, never()).findByName(anyString());
        verify(brandMapper, never()).insert(any(BrandEntity.class));
    }

    @Test
    void testTianJiaPingPai_NameDuplicated() {
        // Given
        Brand newBrand = new Brand();
        newBrand.setName("NIKE");

        when(brandMapper.findByName("NIKE")).thenReturn(brandEntity);

        // When & Then
        BizException exception = assertThrows(BizException.class, () -> {
            brandService.tianJiaPingPai(newBrand);
        });
        assertEquals(ErrorCode.BRAND_NAME_DUPLICATED, exception.getErrorCode());

        verify(brandMapper).findByName("NIKE");
        verify(brandMapper, never()).insert(any(BrandEntity.class));
    }

    @Test
    void testTianJiaPingPai_InsertFailed() {
        // Given
        Brand newBrand = new Brand();
        newBrand.setName("NIKE");

        when(brandMapper.findByName("NIKE")).thenReturn(null);
        when(brandMapper.insert(any(BrandEntity.class))).thenReturn(0);

        // When & Then
        BizException exception = assertThrows(BizException.class, () -> {
            brandService.tianJiaPingPai(newBrand);
        });
        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());

        verify(brandMapper).insert(any(BrandEntity.class));
    }

    @Test
    void testDelteBrand_Success() {
        // Given
        when(brandMapper.findById(1L)).thenReturn(brandEntity);
        when(brandMapper.deleteById(1L)).thenReturn(1);

        // When
        brandService.delteBrand(1L);

        // Then
        verify(brandMapper).findById(1L);
        verify(brandMapper).deleteById(1L);
    }

    @Test
    void testDelteBrand_BrandNotFound() {
        // Given
        when(brandMapper.findById(1L)).thenReturn(null);

        // When & Then
        BizException exception = assertThrows(BizException.class, () -> {
            brandService.delteBrand(1L);
        });
        assertEquals(ErrorCode.BRAND_NOT_FOUND, exception.getErrorCode());

        verify(brandMapper).findById(1L);
        verify(brandMapper, never()).deleteById(1L);
    }

    @Test
    void testDelteBrand_DeleteFailed() {
        // Given
        when(brandMapper.findById(1L)).thenReturn(brandEntity);
        when(brandMapper.deleteById(1L)).thenReturn(0);

        // When & Then
        BizException exception = assertThrows(BizException.class, () -> {
            brandService.delteBrand(1L);
        });
        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());

        verify(brandMapper).deleteById(1L);
    }
}