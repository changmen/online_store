package com.example.onlinestore.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WebUtilsTest {
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
    }

    @Test
    void testClientIpFromXForwardedFor() {
        // 模拟X-Forwarded-For有效的情况
        when(request.getHeader("X-Forwarded-For")).thenReturn("192.168.1.1");
        when(request.getHeader("Proxy-Client-IP")).thenReturn(null);
        when(request.getHeader("WL-Proxy-Client-IP")).thenReturn(null);
        when(request.getHeader("HTTP_CLIENT_IP")).thenReturn(null);
        when(request.getHeader("HTTP_X_FORWARDED_FOR")).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");

        String result = WebUtils.getClientIp(request);
        assertEquals("192.168.1.1", result);
    }

    @Test
    void testClientIpFromProxyClientIP() {
        // X-Forwarded-For无效，Proxy-Client-IP有效
        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        when(request.getHeader("Proxy-Client-IP")).thenReturn("192.168.1.2");
        when(request.getHeader("WL-Proxy-Client-IP")).thenReturn(null);
        when(request.getHeader("HTTP_CLIENT_IP")).thenReturn(null);
        when(request.getHeader("HTTP_X_FORWARDED_FOR")).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");

        String result = WebUtils.getClientIp(request);
        assertEquals("192.168.1.2", result);
    }

    @Test
    void testClientIpFromWLProxyClientIP() {
        // 前两个头无效，WL-Proxy-Client-IP有效
        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        when(request.getHeader("Proxy-Client-IP")).thenReturn(null);
        when(request.getHeader("WL-Proxy-Client-IP")).thenReturn("192.168.1.3");
        when(request.getHeader("HTTP_CLIENT_IP")).thenReturn(null);
        when(request.getHeader("HTTP_X_FORWARDED_FOR")).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");

        String result = WebUtils.getClientIp(request);
        assertEquals("192.168.1.3", result);
    }

    @Test
    void testClientIpFromHTTP_CLIENT_IP() {
        // 前三个头无效，HTTP_CLIENT_IP有效
        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        when(request.getHeader("Proxy-Client-IP")).thenReturn(null);
        when(request.getHeader("WL-Proxy-Client-IP")).thenReturn(null);
        when(request.getHeader("HTTP_CLIENT_IP")).thenReturn("192.168.1.4");
        when(request.getHeader("HTTP_X_FORWARDED_FOR")).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");

        String result = WebUtils.getClientIp(request);
        assertEquals("192.168.1.4", result);
    }

    @Test
    void testClientIpFromHTTP_X_FORWARDED_FOR() {
        // 前四个头无效，HTTP_X_FORWARDED_FOR有效
        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        when(request.getHeader("Proxy-Client-IP")).thenReturn(null);
        when(request.getHeader("WL-Proxy-Client-IP")).thenReturn(null);
        when(request.getHeader("HTTP_CLIENT_IP")).thenReturn(null);
        when(request.getHeader("HTTP_X_FORWARDED_FOR")).thenReturn("192.168.1.5");
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");

        String result = WebUtils.getClientIp(request);
        assertEquals("192.168.1.5", result);
    }

    @Test
    void testClientIpFallbackToRemoteAddr() {
        // 所有头无效，返回getRemoteAddr()
        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        when(request.getHeader("Proxy-Client-IP")).thenReturn(null);
        when(request.getHeader("WL-Proxy-Client-IP")).thenReturn(null);
        when(request.getHeader("HTTP_CLIENT_IP")).thenReturn(null);
        when(request.getHeader("HTTP_X_FORWARDED_FOR")).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");

        String result = WebUtils.getClientIp(request);
        assertEquals("127.0.0.1", result);
    }

    @Test
    void testClientIpWithUnknownHeaderValue() {
        // X-Forwarded-For为"unknown"，继续检查下一个头
        when(request.getHeader("X-Forwarded-For")).thenReturn("unknown");
        when(request.getHeader("Proxy-Client-IP")).thenReturn("192.168.1.6");
        when(request.getHeader("WL-Proxy-Client-IP")).thenReturn(null);
        when(request.getHeader("HTTP_CLIENT_IP")).thenReturn(null);
        when(request.getHeader("HTTP_X_FORWARDED_FOR")).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");

        String result = WebUtils.getClientIp(request);
        assertEquals("192.168.1.6", result);
    }
}
