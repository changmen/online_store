package com.example.onlinestore.aspect;

import com.example.onlinestore.bean.User;
import com.example.onlinestore.context.UserContext;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("管理员权限验证切面测试")
public class AdminAuthAspectTest {

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private AdminAuthAspect adminAuthAspect;

    private static final String ADMIN_USERNAME = "admin";
    private static final String ERROR_ACCESS_DENIED = "error.access.denied";
    private static final String ACCESS_DENIED_MESSAGE = "访问被拒绝";

    @BeforeEach
    void setUp() {
        adminAuthAspect.adminUsername = ADMIN_USERNAME;
        lenient().when(messageSource.getMessage(eq(ERROR_ACCESS_DENIED), any(), eq(LocaleContextHolder.getLocale())))
            .thenReturn(ACCESS_DENIED_MESSAGE);
    }

    @AfterEach
    void tearDown() {
        UserContext.clear();
    }

    @Nested
    @DisplayName("权限验证测试")
    class AuthorizationTests {
        @Test
        @DisplayName("管理员访问成功")
        void whenUserIsAdmin_thenAllowAccess() {
            // 准备测试数据
            User adminUser = new User();
            adminUser.setUsername(ADMIN_USERNAME);
            UserContext.setCurrentUser(adminUser);

            // 执行测试
            assertDoesNotThrow(() -> adminAuthAspect.checkAdminAuth());
        }

        @Test
        @DisplayName("非管理员访问失败")
        void whenUserIsNotAdmin_thenThrowException() {
            // 准备测试数据
            User normalUser = new User();
            normalUser.setUsername("user");
            UserContext.setCurrentUser(normalUser);

            // 执行测试
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> adminAuthAspect.checkAdminAuth()
            );

            // 验证结果
            assertEquals(ACCESS_DENIED_MESSAGE, exception.getMessage());
            verify(messageSource).getMessage(
                eq(ERROR_ACCESS_DENIED), 
                any(), 
                eq(LocaleContextHolder.getLocale()));
        }

        @Test
        @DisplayName("未登录用户访问失败")
        void whenUserIsNull_thenThrowException() {
            // 准备测试数据
            UserContext.setCurrentUser(null);

            // 执行测试
            IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> adminAuthAspect.checkAdminAuth()
            );

            // 验证结果
            assertEquals(ACCESS_DENIED_MESSAGE, exception.getMessage());
            verify(messageSource).getMessage(
                eq(ERROR_ACCESS_DENIED), 
                any(), 
                eq(LocaleContextHolder.getLocale()));
        }
    }
} 