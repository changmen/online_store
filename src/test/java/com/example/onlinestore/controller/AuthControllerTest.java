package com.example.onlinestore.controller;

import com.example.onlinestore.bean.User;
import com.example.onlinestore.dto.LoginRequest;
import com.example.onlinestore.dto.LoginResponse;
import com.example.onlinestore.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("认证控制器测试")
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MessageSource messageSource;

    @MockBean
    private UserService userService;

    private LoginRequest request;
    private LoginResponse response;

    @BeforeEach
    void setUp() {
        request = new LoginRequest();
        request.setUsername("test");
        request.setPassword("password");

        response = new LoginResponse();
        response.setToken("test-token");
        response.setExpireTime(LocalDateTime.now().plusHours(2));
    }

    @Nested
    @DisplayName("登录接口测试")
    class LoginTests {
        @Test
        @DisplayName("登录成功")
        void whenLoginSucceeds_thenReturnToken() throws Exception {
            when(userService.login(any(LoginRequest.class))).thenReturn(response);

            mockMvc.perform(post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.token").value(response.getToken()))
                    .andExpect(jsonPath("$.expireTime").exists());
        }

        @Test
        @DisplayName("登录失败 - 英文错误消息")
        void whenLoginFailsInEnglish_thenReturnErrorMessage() throws Exception {
            request.setPassword("wrong_password");
            String errorMessage = messageSource.getMessage(
                "error.invalid.credentials", null, Locale.ENGLISH);

            when(userService.login(any(LoginRequest.class)))
                    .thenThrow(new IllegalArgumentException(errorMessage));

            mockMvc.perform(post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Accept-Language", "en")
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(errorMessage));
        }

        @Test
        @DisplayName("登录失败 - 中文错误消息")
        void whenLoginFailsInChinese_thenReturnErrorMessage() throws Exception {
            request.setPassword("wrong_password");
            String errorMessage = messageSource.getMessage(
                "error.invalid.credentials", null, Locale.SIMPLIFIED_CHINESE);

            when(userService.login(any(LoginRequest.class)))
                    .thenThrow(new IllegalArgumentException(errorMessage));

            mockMvc.perform(post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Accept-Language", "zh-CN")
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(errorMessage));
        }

        @Test
        @DisplayName("系统错误 - 多语言错误消息")
        void whenSystemError_thenReturnLocalizedErrorMessage() throws Exception {
            when(userService.login(any(LoginRequest.class)))
                    .thenThrow(new RuntimeException("Unexpected error"));

            String enErrorMessage = messageSource.getMessage(
                "error.system.internal", null, Locale.ENGLISH);
            mockMvc.perform(post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Accept-Language", "en")
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isInternalServerError())
                    .andExpect(content().string(enErrorMessage));

            String zhErrorMessage = messageSource.getMessage(
                "error.system.internal", null, Locale.SIMPLIFIED_CHINESE);
            mockMvc.perform(post("/api/auth/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Accept-Language", "zh-CN")
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isInternalServerError())
                    .andExpect(content().string(zhErrorMessage));
        }
    }
}
