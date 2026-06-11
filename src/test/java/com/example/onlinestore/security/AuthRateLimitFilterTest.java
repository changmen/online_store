package com.example.onlinestore.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthRateLimitFilterTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private AuthRateLimitFilter filter;

    @BeforeEach
    void setUp() {
        filter = new AuthRateLimitFilter(3, 60, new ObjectMapper());
    }

    @Test
    void underLimit_passesThrough() throws Exception {
        when(request.getServletPath()).thenReturn("/api/v1/members/login");
        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        when(request.getHeader("X-Real-IP")).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn("192.168.1.1");

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(response, never()).setStatus(429);
    }

    @Test
    void exceedingLimit_returns429() throws Exception {
        StringWriter responseWriter = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(responseWriter));
        when(request.getServletPath()).thenReturn("/api/v1/members/login");
        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        when(request.getHeader("X-Real-IP")).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn("10.0.0.1");

        for (int i = 0; i < 3; i++) {
            filter.doFilterInternal(request, response, filterChain);
        }
        verify(filterChain, times(3)).doFilter(request, response);

        filter.doFilterInternal(request, response, filterChain);

        verify(response).setStatus(429);
        verify(filterChain, times(3)).doFilter(request, response);
        assertTrue(responseWriter.toString().contains("请求过于频繁"));
    }

    @Test
    void differentIps_haveSeparateCounters() throws Exception {
        when(request.getServletPath()).thenReturn("/api/v1/members/login");
        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        when(request.getHeader("X-Real-IP")).thenReturn(null);

        when(request.getRemoteAddr()).thenReturn("10.0.0.1");
        for (int i = 0; i < 3; i++) {
            filter.doFilterInternal(request, response, filterChain);
        }

        when(request.getRemoteAddr()).thenReturn("10.0.0.2");
        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(4)).doFilter(request, response);
        verify(response, never()).setStatus(429);
    }

    @Test
    void differentPaths_haveSeparateCounters() throws Exception {
        when(request.getHeader("X-Forwarded-For")).thenReturn(null);
        when(request.getHeader("X-Real-IP")).thenReturn(null);
        when(request.getRemoteAddr()).thenReturn("10.0.0.1");

        when(request.getServletPath()).thenReturn("/api/v1/members/login");
        for (int i = 0; i < 3; i++) {
            filter.doFilterInternal(request, response, filterChain);
        }

        when(request.getServletPath()).thenReturn("/api/v1/members/registry");
        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain, times(4)).doFilter(request, response);
    }

    @Test
    void xForwardedFor_usesFirstIp() throws Exception {
        when(request.getServletPath()).thenReturn("/api/v1/members/login");
        when(request.getHeader("X-Forwarded-For")).thenReturn("1.2.3.4, 5.6.7.8");

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }
}
