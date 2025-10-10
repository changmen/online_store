package com.example.onlinestore.controller;

import com.example.onlinestore.bean.Brand;
import com.example.onlinestore.dto.BrandListQueryOptions;
import com.example.onlinestore.dto.Page;
import com.example.onlinestore.service.BrandService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * BrandController单元测试
 */
@WebMvcTest(BrandController.class)
class BrandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BrandService brandService;

    @Autowired
    private ObjectMapper objectMapper;

    private Brand testBrand;
    private Page<Brand> testPage;

    @BeforeEach
    void setUp() {
        // 设置测试品牌
        testBrand = new Brand();
        testBrand.setId(1L);
        testBrand.setName("NIKE");
        testBrand.setDescription("Just Do It - Global Athletic Brand");
        testBrand.setLogo("https://example.com/nike-logo.png");
        testBrand.setStory("Nike is a multinational corporation that designs, develops athletic footwear and apparel");
        testBrand.setSortScore(100);
        testBrand.setVisible(1);

        // 设置测试分页数据
        List<Brand> brands = Arrays.asList(testBrand);
        testPage = new Page<>(brands, 1L, 1, 10);
    }

    @Test
    void testListBrandsDefault() throws Exception {
        // Given
        when(brandService.listBrands(any(BrandListQueryOptions.class))).thenReturn(testPage);

        // When & Then
        mockMvc.perform(get("/api/v1/brands"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.data").isArray())
                .andExpect(jsonPath("$.data.data[0].id").value(1))
                .andExpect(jsonPath("$.data.data[0].name").value("NIKE"))
                .andExpect(jsonPath("$.data.pageNum").value(1))
                .andExpect(jsonPath("$.data.pageSize").value(10))
                .andExpect(jsonPath("$.data.total").value(1));

        verify(brandService).listBrands(any(BrandListQueryOptions.class));
    }

    @Test
    void testListBrandsWithParameters() throws Exception {
        // Given
        when(brandService.listBrands(any(BrandListQueryOptions.class))).thenReturn(testPage);

        // When & Then
        mockMvc.perform(get("/api/v1/brands")
                .param("pageNum", "2")
                .param("pageSize", "20")
                .param("visible", "0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(brandService).listBrands(any(BrandListQueryOptions.class));
    }

    @Test
    void testGetBrandByIdSuccess() throws Exception {
        // Given
        when(brandService.getBrandById(1L)).thenReturn(testBrand);

        // When & Then
        mockMvc.perform(get("/api/v1/brands/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("NIKE"))
                .andExpect(jsonPath("$.data.description").value("Just Do It - Global Athletic Brand"))
                .andExpect(jsonPath("$.data.logo").value("https://example.com/nike-logo.png"))
                .andExpect(jsonPath("$.data.sortScore").value(100))
                .andExpect(jsonPath("$.data.visible").value(1));

        verify(brandService).getBrandById(1L);
    }

    @Test
    void testGetBrandByIdInvalidPath() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/v1/brands/invalid"))
                .andExpect(status().isBadRequest());

        verify(brandService, never()).getBrandById(any());
    }

    @Test
    void testAddBrandSuccess() throws Exception {
        // Given
        Brand newBrand = new Brand();
        newBrand.setName("ADIDAS");
        newBrand.setDescription("Impossible is Nothing");
        newBrand.setLogo("https://example.com/adidas-logo.png");
        newBrand.setStory("Adidas is a German multinational corporation that designs and manufactures sports shoes");
        newBrand.setSortScore(90);
        newBrand.setVisible(1);

        Brand createdBrand = new Brand();
        createdBrand.setId(2L);
        createdBrand.setName("ADIDAS");
        createdBrand.setDescription("Impossible is Nothing");
        createdBrand.setLogo("https://example.com/adidas-logo.png");
        createdBrand.setStory("Adidas is a German multinational corporation that designs and manufactures sports shoes");
        createdBrand.setSortScore(90);
        createdBrand.setVisible(1);

        when(brandService.tianJiaPingPai(any(Brand.class))).thenReturn(createdBrand);

        // When & Then
        mockMvc.perform(post("/api/v1/brands")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newBrand)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(2))
                .andExpect(jsonPath("$.data.name").value("ADIDAS"));

        verify(brandService).tianJiaPingPai(any(Brand.class));
    }

    @Test
    void testAddBrandInvalidData() throws Exception {
        // Given - 创建无效的品牌数据
        Brand invalidBrand = new Brand();
        invalidBrand.setName(""); // 空名称
        invalidBrand.setDescription(""); // 空描述

        // When & Then
        mockMvc.perform(post("/api/v1/brands")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidBrand)))
                .andExpect(status().isBadRequest());

        verify(brandService, never()).tianJiaPingPai(any());
    }

    @Test
    void testAddBrandMissingFields() throws Exception {
        // Given - 缺少必填字段
        Brand incompleteBrand = new Brand();
        incompleteBrand.setName("TEST_BRAND");
        // 缺少其他必填字段

        // When & Then
        mockMvc.perform(post("/api/v1/brands")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(incompleteBrand)))
                .andExpect(status().isBadRequest());

        verify(brandService, never()).tianJiaPingPai(any());
    }

    @Test
    void testUpdateBrandSuccess() throws Exception {
        // Given
        Brand updatedBrand = new Brand();
        updatedBrand.setName("NIKE");
        updatedBrand.setDescription("Updated Description");
        updatedBrand.setLogo("https://example.com/updated-logo.png");
        updatedBrand.setStory("Updated story for Nike brand with more details about its history");
        updatedBrand.setSortScore(95);
        updatedBrand.setVisible(1);

        doNothing().when(brandService).updateBrand(eq(1L), any(Brand.class));

        // When & Then
        mockMvc.perform(put("/api/v1/brands/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedBrand)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(brandService).updateBrand(eq(1L), any(Brand.class));
    }

    @Test
    void testUpdateBrandInvalidId() throws Exception {
        // Given
        Brand updatedBrand = new Brand();
        updatedBrand.setName("NIKE");
        updatedBrand.setDescription("Updated Description");
        updatedBrand.setLogo("https://example.com/updated-logo.png");
        updatedBrand.setStory("Updated story for Nike brand with more details about its history");
        updatedBrand.setSortScore(95);
        updatedBrand.setVisible(1);

        // When & Then
        mockMvc.perform(put("/api/v1/brands/invalid")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedBrand)))
                .andExpect(status().isBadRequest());

        verify(brandService, never()).updateBrand(any(), any());
    }

    @Test
    void testUpdateBrandInvalidData() throws Exception {
        // Given - 无效的品牌数据
        Brand invalidBrand = new Brand();
        invalidBrand.setName(""); // 空名称
        invalidBrand.setVisible(2); // 无效的可见性值

        // When & Then
        mockMvc.perform(put("/api/v1/brands/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidBrand)))
                .andExpect(status().isBadRequest());

        verify(brandService, never()).updateBrand(any(), any());
    }

    @Test
    void testDeleteBrandSuccess() throws Exception {
        // Given
        doNothing().when(brandService).delteBrand(1L);

        // When & Then
        mockMvc.perform(delete("/api/v1/brands/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").value("Success"));

        verify(brandService).delteBrand(1L);
    }

    @Test
    void testDeleteBrandInvalidId() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/v1/brands/invalid"))
                .andExpect(status().isBadRequest());

        verify(brandService, never()).delteBrand(any());
    }

    @Test
    void testAddBrandWithInvalidUrl() throws Exception {
        // Given - Logo URL无效
        Brand brandWithInvalidUrl = new Brand();
        brandWithInvalidUrl.setName("TEST_BRAND");
        brandWithInvalidUrl.setDescription("Test Description for validation");
        brandWithInvalidUrl.setLogo("invalid-url"); // 无效的URL
        brandWithInvalidUrl.setStory("Test story for brand validation testing");
        brandWithInvalidUrl.setSortScore(100);
        brandWithInvalidUrl.setVisible(1);

        // When & Then
        mockMvc.perform(post("/api/v1/brands")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(brandWithInvalidUrl)))
                .andExpect(status().isBadRequest());

        verify(brandService, never()).tianJiaPingPai(any());
    }

    @Test
    void testAddBrandWithInvalidSortScore() throws Exception {
        // Given - 无效的排序分数
        Brand brandWithInvalidScore = new Brand();
        brandWithInvalidScore.setName("TEST_BRAND");
        brandWithInvalidScore.setDescription("Test Description for validation");
        brandWithInvalidScore.setLogo("https://example.com/logo.png");
        brandWithInvalidScore.setStory("Test story for brand validation testing");
        brandWithInvalidScore.setSortScore(null); // null值
        brandWithInvalidScore.setVisible(1);

        // When & Then
        mockMvc.perform(post("/api/v1/brands")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(brandWithInvalidScore)))
                .andExpect(status().isBadRequest());

        verify(brandService, never()).tianJiaPingPai(any());
    }
}