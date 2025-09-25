package com.example.onlinestore.handler;

import com.example.onlinestore.dto.Response;
import com.example.onlinestore.errors.ErrorCode;
import com.example.onlinestore.exceptions.BizException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private MethodArgumentNotValidException methodArgumentNotValidException;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        // 测试前的准备工作
    }

    @Test
    void testHandleException_RuntimeException() {
        // 测试处理运行时异常
        RuntimeException exception = new RuntimeException("Test runtime exception");
        
        Response<String> response = globalExceptionHandler.handleException(exception);
        
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("INTERNAL_SERVER_ERROR", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void testHandleException_GeneralException() {
        // 测试处理一般异常
        Exception exception = new Exception("Test general exception");
        
        Response<String> response = globalExceptionHandler.handleException(exception);
        
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("INTERNAL_SERVER_ERROR", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void testHandleException_MethodArgumentNotValidException_WithFieldError() {
        // 测试处理方法参数验证异常（有字段错误）
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.hasErrors()).thenReturn(true);
        
        FieldError fieldError = new FieldError("user", "username", "用户名不能为空");
        List<ObjectError> errors = Collections.singletonList(fieldError);
        when(bindingResult.getAllErrors()).thenReturn(errors);
        
        Response<String> response = globalExceptionHandler.handleException(methodArgumentNotValidException);
        
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("Parameter:username, error:用户名不能为空", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void testHandleException_MethodArgumentNotValidException_NoErrors() {
        // 测试处理方法参数验证异常（无错误）
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.hasErrors()).thenReturn(false);
        
        Response<String> response = globalExceptionHandler.handleException(methodArgumentNotValidException);
        
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("Invalid request", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void testHandleException_MethodArgumentNotValidException_EmptyErrorList() {
        // 测试处理方法参数验证异常（错误列表为空）
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getAllErrors()).thenReturn(Collections.emptyList());
        
        Response<String> response = globalExceptionHandler.handleException(methodArgumentNotValidException);
        
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("Invalid request", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void testHandleException_BizException_WithErrorCode() {
        // 测试处理业务异常（有错误码）
        ErrorCode errorCode = ErrorCode.MEMBER_NOT_FOUND;
        BizException bizException = new BizException(errorCode, "testUser");
        
        when(messageSource.getMessage(eq(errorCode.getCode()), isNull(), any(Locale.class)))
                .thenReturn("会员:{0}不存在");
        
        Response<String> response = globalExceptionHandler.handleException(bizException);
        
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("会员:testUser不存在", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void testHandleException_BizException_NoSuchMessageException() {
        // 测试处理业务异常（消息源中没有对应消息）
        ErrorCode errorCode = ErrorCode.MEMBER_NOT_FOUND;
        BizException bizException = new BizException(errorCode, "testUser");
        
        when(messageSource.getMessage(eq(errorCode.getCode()), isNull(), any(Locale.class)))
                .thenThrow(new NoSuchMessageException("No message found"));
        
        Response<String> response = globalExceptionHandler.handleException(bizException);
        
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("会员:testUser不存在", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void testHandleException_BizException_BlankMessage() {
        // 测试处理业务异常（空白消息）
        ErrorCode errorCode = ErrorCode.MEMBER_NOT_FOUND;
        BizException bizException = new BizException(errorCode, "testUser");
        
        when(messageSource.getMessage(eq(errorCode.getCode()), isNull(), any(Locale.class)))
                .thenReturn("");
        
        Response<String> response = globalExceptionHandler.handleException(bizException);
        
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("会员:testUser不存在", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void testHandleException_BizException_NullErrorCode() {
        // 测试处理业务异常（错误码为空）
        BizException bizException = spy(new BizException(ErrorCode.MEMBER_NOT_FOUND));
        when(bizException.getErrorCode()).thenReturn(null);
        
        Response<String> response = globalExceptionHandler.handleException(bizException);
        
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("INTERNAL ERROR", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void testHandleException_BizException_NoParams() {
        // 测试处理业务异常（无参数）
        ErrorCode errorCode = ErrorCode.MEMBER_PASSWORD_INCORRECT;
        BizException bizException = new BizException(errorCode);
        
        when(messageSource.getMessage(eq(errorCode.getCode()), isNull(), any(Locale.class)))
                .thenReturn("密码不正确");
        
        Response<String> response = globalExceptionHandler.handleException(bizException);
        
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("密码不正确", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void testHandleException_ConstraintViolationException() {
        // 测试处理约束违反异常
        Set<ConstraintViolation<?>> violations = new HashSet<>();
        
        ConstraintViolation<?> violation1 = mock(ConstraintViolation.class);
        Path path1 = mock(Path.class);
        when(path1.toString()).thenReturn("username");
        when(violation1.getPropertyPath()).thenReturn(path1);
        when(violation1.getMessage()).thenReturn("不能为空");
        violations.add(violation1);
        
        ConstraintViolation<?> violation2 = mock(ConstraintViolation.class);
        Path path2 = mock(Path.class);
        when(path2.toString()).thenReturn("email");
        when(violation2.getPropertyPath()).thenReturn(path2);
        when(violation2.getMessage()).thenReturn("邮箱格式不正确");
        violations.add(violation2);
        
        ConstraintViolationException exception = new ConstraintViolationException(violations);
        
        Response<String> response = globalExceptionHandler.handleException(exception);
        
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertTrue(response.getMessage().startsWith("参数验证失败: "));
        assertTrue(response.getMessage().contains("username: 不能为空"));
        assertTrue(response.getMessage().contains("email: 邮箱格式不正确"));
        assertNull(response.getData());
    }

    @Test
    void testHandleException_ConstraintViolationException_EmptyViolations() {
        // 测试处理约束违反异常（空违反集合）
        Set<ConstraintViolation<?>> violations = new HashSet<>();
        ConstraintViolationException exception = new ConstraintViolationException(violations);
        
        Response<String> response = globalExceptionHandler.handleException(exception);
        
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("参数验证失败: ", response.getMessage());
        assertNull(response.getData());
    }
}