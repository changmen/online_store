package com.example.onlinestore.controller;

import com.example.onlinestore.bean.Attribute;
import com.example.onlinestore.dto.AttributeResponse;
import com.example.onlinestore.dto.CreateAttributeRequest;
import com.example.onlinestore.dto.Response;
import com.example.onlinestore.dto.UpdateAttributeRequest;
import com.example.onlinestore.enums.AttributeInputType;
import com.example.onlinestore.enums.AttributeType;
import com.example.onlinestore.service.AttributeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * AttributeController 单元测试类
 * 测试属性控制器的各个接口功能
 */
@ExtendWith(MockitoExtension.class)
class AttributeControllerTest {

    @Mock
    private AttributeService attributeService;

    @InjectMocks
    private AttributeController attributeController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(attributeController).build();
        objectMapper = new ObjectMapper();
    }

    /**
     * 创建测试用的属性对象
     */
    private Attribute createTestAttribute() {
        Attribute attribute = new Attribute();
        attribute.setId(1L);
        attribute.setName("颜色");
        attribute.setInputType(AttributeInputType.SINGLE_SELECT);
        attribute.setRequired(1);
        attribute.setSearchable(1);
        attribute.setSortScore(100);
        attribute.setVisible(1);
        attribute.setAttributeType(AttributeType.SKU);
        return attribute;
    }

    /**
     * 创建测试用的创建属性请求对象
     */
    private CreateAttributeRequest createTestCreateRequest() {
        CreateAttributeRequest request = new CreateAttributeRequest();
        request.setName("颜色");
        request.setInputType("SINGLE_SELECT");
        request.setRequired(1);
        request.setSearchable(1);
        request.setSortScore(100);
        request.setVisible(1);
        request.setAttributeType("SKU");
        return request;
    }

    /**
     * 创建测试用的更新属性请求对象
     */
    private UpdateAttributeRequest createTestUpdateRequest() {
        UpdateAttributeRequest request = new UpdateAttributeRequest();
        request.setName("尺寸");
        request.setSortScore(200);
        request.setVisible(1);
        request.setAttributeType("SALE");
        request.setInputType("MULTI_SELECT");
        request.setRequired(0);
        request.setSearchable(1);
        return request;
    }

    /**
     * 测试成功创建属性的场景
     */
    @Test
    void testAddAttribute_Success() throws Exception {
        // Given
        CreateAttributeRequest request = createTestCreateRequest();
        Attribute mockAttribute = createTestAttribute();
        
        when(attributeService.createAttribute(any(CreateAttributeRequest.class)))
                .thenReturn(mockAttribute);

        // When & Then
        mockMvc.perform(post("/api/v1/attributes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("操作成功"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("颜色"))
                .andExpect(jsonPath("$.data.inputType").value("SINGLE_SELECT"))
                .andExpect(jsonPath("$.data.required").value(1))
                .andExpect(jsonPath("$.data.searchable").value(1))
                .andExpect(jsonPath("$.data.sortScore").value(100))
                .andExpect(jsonPath("$.data.visible").value(1))
                .andExpect(jsonPath("$.data.attributeType").value("SKU"));

        // Verify
        verify(attributeService, times(1)).createAttribute(any(CreateAttributeRequest.class));
    }

    /**
     * 测试创建属性时参数验证失败的场景
     */
    @Test
    void testAddAttribute_ValidationFailure() throws Exception {
        // Given - 创建一个无效的请求对象（name为null）
        CreateAttributeRequest invalidRequest = new CreateAttributeRequest();
        invalidRequest.setName(null); // 违反@NotNull约束
        invalidRequest.setInputType("SINGLE_SELECT");
        invalidRequest.setRequired(1);
        invalidRequest.setSearchable(1);
        invalidRequest.setSortScore(100);
        invalidRequest.setVisible(1);
        invalidRequest.setAttributeType("SKU");

        // When & Then
        mockMvc.perform(post("/api/v1/attributes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        // Verify - 由于验证失败，服务方法不应被调用
        verify(attributeService, never()).createAttribute(any(CreateAttributeRequest.class));
    }

    /**
     * 测试输入类型格式验证失败的场景
     */
    @Test
    void testAddAttribute_InvalidInputType() throws Exception {
        // Given - 创建一个输入类型无效的请求对象
        CreateAttributeRequest invalidRequest = createTestCreateRequest();
        invalidRequest.setInputType("INVALID_TYPE"); // 违反@Pattern约束

        // When & Then
        mockMvc.perform(post("/api/v1/attributes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        // Verify
        verify(attributeService, never()).createAttribute(any(CreateAttributeRequest.class));
    }

    /**
     * 测试根据ID成功获取属性的场景
     */
    @Test
    void testGetAttribute_Success() throws Exception {
        // Given
        Long attributeId = 1L;
        Attribute mockAttribute = createTestAttribute();
        
        when(attributeService.getAttributeById(attributeId))
                .thenReturn(mockAttribute);

        // When & Then
        mockMvc.perform(get("/api/v1/attributes/{attributeId}", attributeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("操作成功"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("颜色"))
                .andExpect(jsonPath("$.data.inputType").value("SINGLE_SELECT"))
                .andExpect(jsonPath("$.data.required").value(1))
                .andExpect(jsonPath("$.data.searchable").value(1))
                .andExpect(jsonPath("$.data.sortScore").value(100))
                .andExpect(jsonPath("$.data.visible").value(1))
                .andExpect(jsonPath("$.data.attributeType").value("SKU"));

        // Verify
        verify(attributeService, times(1)).getAttributeById(attributeId);
    }

    /**
     * 测试获取不存在ID的属性场景
     */
    @Test
    void testGetAttribute_NotFound() throws Exception {
        // Given
        Long nonExistentId = 999L;
        
        when(attributeService.getAttributeById(nonExistentId))
                .thenThrow(new RuntimeException("属性不存在"));

        // When & Then
        mockMvc.perform(get("/api/v1/attributes/{attributeId}", nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

        // Verify
        verify(attributeService, times(1)).getAttributeById(nonExistentId);
    }

    /**
     * 测试成功更新属性的场景
     */
    @Test
    void testUpdateAttribute_Success() throws Exception {
        // Given
        Long attributeId = 1L;
        UpdateAttributeRequest request = createTestUpdateRequest();
        
        doNothing().when(attributeService).updateAttribute(eq(attributeId), any(UpdateAttributeRequest.class));

        // When & Then
        mockMvc.perform(put("/api/v1/attributes/{attributeId}", attributeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("操作成功"))
                .andExpect(jsonPath("$.data").doesNotExist());

        // Verify
        verify(attributeService, times(1)).updateAttribute(eq(attributeId), any(UpdateAttributeRequest.class));
    }

    /**
     * 测试更新不存在ID的属性场景
     */
    @Test
    void testUpdateAttribute_NotFound() throws Exception {
        // Given
        Long nonExistentId = 999L;
        UpdateAttributeRequest request = createTestUpdateRequest();
        
        doThrow(new RuntimeException("属性不存在"))
                .when(attributeService).updateAttribute(eq(nonExistentId), any(UpdateAttributeRequest.class));

        // When & Then
        mockMvc.perform(put("/api/v1/attributes/{attributeId}", nonExistentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isInternalServerError());

        // Verify
        verify(attributeService, times(1)).updateAttribute(eq(nonExistentId), any(UpdateAttributeRequest.class));
    }

    /**
     * 测试更新属性时使用无效的请求体
     */
    @Test
    void testUpdateAttribute_InvalidRequest() throws Exception {
        // Given
        Long attributeId = 1L;
        String invalidJson = "{ \"invalidField\": \"invalid\" }";

        // When & Then
        mockMvc.perform(put("/api/v1/attributes/{attributeId}", attributeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isOk()); // 由于UpdateAttributeRequest中的字段都是可选的，所以不会导致验证失败

        // Verify
        verify(attributeService, times(1)).updateAttribute(eq(attributeId), any(UpdateAttributeRequest.class));
    }

}