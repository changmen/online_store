package com.example.onlinestore.controller;

import com.example.onlinestore.dto.PageResponse;
import com.example.onlinestore.dto.UserVO;
import com.example.onlinestore.service.UserService;
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

import java.util.Arrays;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("用户控制器测试")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private MessageSource messageSource;

    private PageResponse<UserVO> mockResponse;

    @BeforeEach
    void setUp() {
        // 准备测试数据
        UserVO user1 = new UserVO();
        user1.setId(1L);
        user1.setUsername("user1");

        UserVO user2 = new UserVO();
        user2.setId(2L);
        user2.setUsername("user2");

        mockResponse = new PageResponse<>();
        mockResponse.setItems(Arrays.asList(user1, user2));
        mockResponse.setTotal(2);
        mockResponse.setPageNum(1);
        mockResponse.setPageSize(10);
    }

    @Nested
    @DisplayName("获取用户列表接口测试")
    class ListUsersTests {
        @Test
        @DisplayName("成功获取用户列表")
        void whenListUsers_thenReturnSuccess() throws Exception {
            // 设置 mock 行为
            when(userService.listUsers(any())).thenReturn(mockResponse);

            // 执行测试
            mockMvc.perform(get("/api/users")
                    .param("pageNum", "1")
                    .param("pageSize", "10")
                    .header("X-Token", "test-token")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.records").isArray())
                    .andExpect(jsonPath("$.records.length()").value(2))
                    .andExpect(jsonPath("$.records[0].id").value(1))
                    .andExpect(jsonPath("$.records[0].username").value("user1"))
                    .andExpect(jsonPath("$.records[1].id").value(2))
                    .andExpect(jsonPath("$.records[1].username").value("user2"))
                    .andExpect(jsonPath("$.total").value(2))
                    .andExpect(jsonPath("$.pageNum").value(1))
                    .andExpect(jsonPath("$.pageSize").value(10));
        }

        @Test
        @DisplayName("页大小超过限制")
        void whenPageSizeExceedsLimit_thenReturnBadRequest() throws Exception {
            mockMvc.perform(get("/api/users")
                    .param("pageNum", "1")
                    .param("pageSize", "101")
                    .header("X-Token", "test-token")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("页码无效")
        void whenPageNumberIsInvalid_thenReturnBadRequest() throws Exception {
            mockMvc.perform(get("/api/users")
                    .param("pageNum", "0")
                    .param("pageSize", "10")
                    .header("X-Token", "test-token")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("数据库错误")
        void whenDatabaseError_thenReturnInternalError() throws Exception {
            // 设置模拟行为
            when(userService.listUsers(any()))
                .thenThrow(new RuntimeException("Database error"));
            when(messageSource.getMessage(eq("error.system.internal"), eq(null), any(Locale.class)))
                .thenReturn("系统内部错误");

            // 执行请求并验证
            mockMvc.perform(get("/api/users")
                    .param("pageSize", "10")
                    .param("pageNum", "1"))
                    .andExpect(status().isInternalServerError())
                    .andExpect(jsonPath("$.message").value("系统内部错误"));
        }
    }
} 