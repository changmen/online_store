package com.example.onlinestore.controller;

import com.example.onlinestore.bean.Member;
import com.example.onlinestore.bean.MemberBaseInfo;
import com.example.onlinestore.dto.*;
import com.example.onlinestore.enums.GenderType;
import com.example.onlinestore.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * MemberController单元测试
 */
@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @Autowired
    private ObjectMapper objectMapper;

    private LoginRequest loginRequest;
    private MemberRegistryRequest registryRequest;
    private Member testMember;
    private LoginResponse loginResponse;

    @BeforeEach
    void setUp() {
        // 设置登录请求
        loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser123");
        loginRequest.setPassword("Password123!");

        // 设置注册请求
        registryRequest = new MemberRegistryRequest();
        registryRequest.setName("newuser123");
        registryRequest.setPassword("Password123!");
        registryRequest.setNickName("New User");
        registryRequest.setPhone("13800138000");
        registryRequest.setGender(GenderType.MALE);
        registryRequest.setAge(25);

        // 设置测试会员
        testMember = new Member();
        testMember.setId(1L);
        MemberBaseInfo baseInfo = new MemberBaseInfo();
        baseInfo.setName("newuser123");
        baseInfo.setNickName("New User");
        baseInfo.setPhone("13800138000");
        baseInfo.setGender(GenderType.MALE);
        baseInfo.setAge(25);
        testMember.setBaseInfo(baseInfo);

        // 设置登录响应
        loginResponse = new LoginResponse("jwt_token_123");
    }

    @Test
    void testRegistrySuccess() throws Exception {
        // Given
        when(memberService.registry(any(MemberRegistryRequest.class))).thenReturn(testMember);

        // When & Then
        mockMvc.perform(post("/api/v1/members/registry")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registryRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.baseInfo.name").value("newuser123"))
                .andExpect(jsonPath("$.data.baseInfo.nickName").value("New User"))
                .andExpect(jsonPath("$.data.baseInfo.phone").value("13800138000"))
                .andExpect(jsonPath("$.data.baseInfo.gender").value("MALE"))
                .andExpect(jsonPath("$.data.baseInfo.age").value(25));
    }

    @Test
    void testRegistryWithInvalidName() throws Exception {
        // Given - 设置无效的用户名
        registryRequest.setName("a"); // 太短

        // When & Then
        mockMvc.perform(post("/api/v1/members/registry")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registryRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegistryWithInvalidPassword() throws Exception {
        // Given - 设置无效的密码
        registryRequest.setPassword("123"); // 太短且不符合复杂度要求

        // When & Then
        mockMvc.perform(post("/api/v1/members/registry")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registryRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegistryWithInvalidPhone() throws Exception {
        // Given - 设置无效的手机号
        registryRequest.setPhone("1234567890"); // 不是有效的手机号格式

        // When & Then
        mockMvc.perform(post("/api/v1/members/registry")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registryRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegistryWithInvalidAge() throws Exception {
        // Given - 设置无效的年龄
        registryRequest.setAge(16); // 小于最小年龄

        // When & Then
        mockMvc.perform(post("/api/v1/members/registry")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registryRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegistryWithMissingRequiredFields() throws Exception {
        // Given - 设置缺少必填字段
        registryRequest.setName(null);
        registryRequest.setGender(null);

        // When & Then
        mockMvc.perform(post("/api/v1/members/registry")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registryRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLoginSuccess() throws Exception {
        // Given
        when(memberService.login(any(LoginRequest.class))).thenReturn(loginResponse);

        // When & Then
        mockMvc.perform(post("/api/v1/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.token").value("jwt_token_123"));
    }

    @Test
    void testLoginWithInvalidUsername() throws Exception {
        // Given - 设置无效的用户名
        loginRequest.setUsername("a"); // 太短

        // When & Then
        mockMvc.perform(post("/api/v1/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLoginWithInvalidPassword() throws Exception {
        // Given - 设置无效的密码
        loginRequest.setPassword("123"); // 不符合密码格式要求

        // When & Then
        mockMvc.perform(post("/api/v1/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLoginWithMissingCredentials() throws Exception {
        // Given - 设置缺少登录凭证
        loginRequest.setUsername(null);
        loginRequest.setPassword(null);

        // When & Then
        mockMvc.perform(post("/api/v1/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLoginWithEmptyRequestBody() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/v1/members/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testRegistryWithEmptyRequestBody() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/v1/members/registry")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }
}