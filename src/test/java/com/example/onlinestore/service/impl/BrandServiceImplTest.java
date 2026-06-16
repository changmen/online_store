package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.Brand;
import com.example.onlinestore.dto.BrandListQueryOptions;
import com.example.onlinestore.dto.Page;
import com.example.onlinestore.entity.BrandEntity;
import com.example.onlinestore.errors.ErrorCode;
import com.example.onlinestore.exceptions.BizException;
import com.example.onlinestore.mapper.BrandMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
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

    private BrandEntity brandEntity;

    @BeforeEach
    void setUp() {
        brandEntity = new BrandEntity();
        brandEntity.setId(1L);
        brandEntity.setName("NIKE");
        brandEntity.setDescription("运动品牌");
        brandEntity.setLogo("http://example.com/logo.png");
        brandEntity.setStory("品牌故事内容超过16个字符");
        brandEntity.setSortScore(100);
        brandEntity.setVisible(1);
    }

    @Test
    void getBrandById_exists_returnsBrand() {
        when(brandMapper.findById(1L)).thenReturn(brandEntity);

        Brand result = brandService.getBrandById(1L);

        assertNotNull(result);
        assertEquals("NIKE", result.getName());
        assertEquals(1L, result.getId());
        verify(brandMapper).findById(1L);
    }

    @Test
    void getBrandById_notFound_throwsException() {
        when(brandMapper.findById(999L)).thenReturn(null);

        assertThrows(BizException.class, () -> brandService.getBrandById(999L));
    }

    @Test
    void addBrand_success() {
        Brand brand = new Brand("adidas", "运动品牌描述", "http://example.com/logo.png",
                "品牌故事内容超过16个字符", 100, 1);
        when(brandMapper.findByName("ADIDAS")).thenReturn(null);
        when(brandMapper.insert(any(BrandEntity.class))).thenAnswer(invocation -> {
            BrandEntity e = invocation.getArgument(0);
            e.setId(2L);
            return 1;
        });

        Brand result = brandService.addBrand(brand);

        assertNotNull(result);
        assertEquals("ADIDAS", result.getName());
        verify(brandMapper).findByName("ADIDAS");
        verify(brandMapper).insert(any(BrandEntity.class));
    }

    @Test
    void addBrand_duplicateName_throwsException() {
        Brand brand = new Brand("nike", "运动品牌描述", "http://example.com/logo.png",
                "品牌故事内容超过16个字符", 100, 1);
        when(brandMapper.findByName("NIKE")).thenReturn(brandEntity);

        assertThrows(BizException.class, () -> brandService.addBrand(brand));
        verify(brandMapper, never()).insert(any(BrandEntity.class));
    }

    @Test
    void updateBrand_nameChanged_throwsException() {
        Brand brand = new Brand("ADIDAS", "新描述", "http://example.com/logo.png",
                "品牌故事内容超过16个字符", 100, 1);
        when(brandMapper.findById(1L)).thenReturn(brandEntity);

        assertThrows(BizException.class, () -> brandService.updateBrand(1L, brand));
        verify(brandMapper, never()).update(any(BrandEntity.class));
    }

    @Test
    void updateBrand_sameName_updatesFields() {
        Brand brand = new Brand("NIKE", "新描述内容", "http://example.com/new-logo.png",
                "品牌故事内容超过16个字符", 200, 1);
        when(brandMapper.findById(1L)).thenReturn(brandEntity);
        when(brandMapper.update(any(BrandEntity.class))).thenReturn(1);

        brandService.updateBrand(1L, brand);

        verify(brandMapper).update(any(BrandEntity.class));
    }

    @Test
    void deleteBrand_exists_success() {
        when(brandMapper.findById(1L)).thenReturn(brandEntity);
        when(brandMapper.deleteById(1L)).thenReturn(1);

        brandService.deleteBrand(1L);

        verify(brandMapper).deleteById(1L);
    }

    @Test
    void deleteBrand_notFound_throwsException() {
        when(brandMapper.findById(999L)).thenReturn(null);

        assertThrows(BizException.class, () -> brandService.deleteBrand(999L));
    }

    @Test
    void listBrands_returnsPage() {
        BrandListQueryOptions options = new BrandListQueryOptions();
        options.setPageNum(1);
        options.setPageSize(10);
        options.setVisible(1);

        when(brandMapper.findAllBrands(any(BrandListQueryOptions.class)))
                .thenReturn(Collections.singletonList(brandEntity));

        Page<Brand> result = brandService.listBrands(options);

        assertNotNull(result);
        assertEquals(1, result.getItems().size());
        assertEquals("NIKE", result.getItems().get(0).getName());
    }
}
