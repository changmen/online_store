package com.example.onlinestore.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WebUtilsTest {

    @Test
    @DisplayName("X-Forwarded-For 优先级最高")
    void returnsIpFromXForwardedFor() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("X-Forwarded-For")).thenReturn("1.2.3.4");

        assertEquals("1.2.3.4", WebUtils.getClientIp(request));
    }

    @Test
    @DisplayName("X-Forwarded-For 为 unknown 时回退到 Proxy-Client-IP")
    void fallsBackToProxyClientIp() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("X-Forwarded-For")).thenReturn("unknown");
        when(request.getHeader("Proxy-Client-IP")).thenReturn("10.0.0.1");

        assertEquals("10.0.0.1", WebUtils.getClientIp(request));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "unknown", "UNKNOWN", "Unknown"})
    @DisplayName("空字符串和 unknown（不区分大小写）均视为无效")
    void treatsEmptyAndUnknownAsInvalid(String invalidValue) {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("X-Forwarded-For")).thenReturn(invalidValue);
        when(request.getHeader("Proxy-Client-IP")).thenReturn("192.168.1.1");

        assertEquals("192.168.1.1", WebUtils.getClientIp(request));
    }

    @Test
    @DisplayName("所有 header 均为 null 时回退到 remoteAddr")
    void fallsBackToRemoteAddrWhenAllHeadersNull() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRemoteAddr()).thenReturn("127.0.0.1");

        assertEquals("127.0.0.1", WebUtils.getClientIp(request));
    }

    @Test
    @DisplayName("依次跳过 null header 直到 WL-Proxy-Client-IP")
    void fallsBackThroughMultipleHeaders() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        when(request.getHeader("Proxy-Client-IP")).thenReturn("");
        when(request.getHeader("WL-Proxy-Client-IP")).thenReturn("172.16.0.1");

        assertEquals("172.16.0.1", WebUtils.getClientIp(request));
    }

    @Test
    @DisplayName("HTTP_CLIENT_IP 可正确返回")
    void returnsIpFromHttpClientIp() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        when(request.getHeader("Proxy-Client-IP")).thenReturn(null);
        when(request.getHeader("WL-Proxy-Client-IP")).thenReturn("unknown");
        when(request.getHeader("HTTP_CLIENT_IP")).thenReturn("203.0.113.5");

        assertEquals("203.0.113.5", WebUtils.getClientIp(request));
    }

    @Test
    @DisplayName("HTTP_X_FORWARDED_FOR 可正确返回")
    void returnsIpFromHttpXForwardedFor() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        when(request.getHeader("Proxy-Client-IP")).thenReturn(null);
        when(request.getHeader("WL-Proxy-Client-IP")).thenReturn(null);
        when(request.getHeader("HTTP_CLIENT_IP")).thenReturn("");
        when(request.getHeader("HTTP_X_FORWARDED_FOR")).thenReturn("198.51.100.7");

        assertEquals("198.51.100.7", WebUtils.getClientIp(request));
    }
     @Test
    @DisplayName("HTTP_X_FORWARDED_FOR 可正确返回 2")
    void returnsIpFromHttpXForwardedFor2() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        when(request.getHeader("Proxy-Client-IP")).thenReturn(null);
        when(request.getHeader("WL-Proxy-Client-IP")).thenReturn(null);
        when(request.getHeader("HTTP_CLIENT_IP")).thenReturn("");
        when(request.getHeader("HTTP_X_FORWARDED_FOR")).thenReturn("198.51.100.8");

        assertEquals("198.51.100.8", WebUtils.getClientIp(request));
    }
}
