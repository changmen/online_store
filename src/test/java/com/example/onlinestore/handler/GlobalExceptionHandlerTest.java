package com.example.onlinestore.handler;

import com.example.onlinestore.exception.BusinessException;
import com.example.onlinestore.exception.OrderException;
import com.example.onlinestore.dto.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.StaticMessageSource;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private MockHttpServletRequest request;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler(new StaticMessageSource());
        request = new MockHttpServletRequest();
        request.setRequestURI("/api/test");
    }

    @Test
    void handleBusinessException_shouldReturnFailResponse() {
        OrderException ex = new OrderException("订单不存在");

        Response<String> response = handler.handleBusinessException(ex, request);

        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals("订单不存在", response.getMessage());
    }

    @Test
    void handleBusinessException_shouldPreserveErrorCode() {
        BusinessException ex = new BusinessException("CUSTOM_CODE", "自定义错误");

        Response<String> response = handler.handleBusinessException(ex, request);

        assertFalse(response.isSuccess());
        assertEquals("自定义错误", response.getMessage());
        assertEquals("CUSTOM_CODE", ex.getErrorCode());
    }
}
