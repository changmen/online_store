package com.example.onlinestore.security;

import com.example.onlinestore.constants.Constants;
import com.example.onlinestore.dto.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class AuthRateLimitFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(AuthRateLimitFilter.class);

    private final Cache<String, AtomicInteger> requestCounts;
    private final int maxRequests;
    private final ObjectMapper objectMapper;

    public AuthRateLimitFilter(
            @Value("${auth.rate-limit.max-requests:10}") int maxRequests,
            @Value("${auth.rate-limit.window-seconds:60}") int windowSeconds,
            ObjectMapper objectMapper) {
        this.maxRequests = maxRequests;
        this.objectMapper = objectMapper;
        this.requestCounts = Caffeine.newBuilder()
                .expireAfterWrite(windowSeconds, TimeUnit.SECONDS)
                .build();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return !Constants.PUBLIC_API_PATHS.contains(path);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String clientIp = getClientIp(request);
        String key = request.getServletPath() + ":" + clientIp;

        AtomicInteger count = requestCounts.get(key, k -> new AtomicInteger(0));
        if (count.incrementAndGet() > maxRequests) {
            logger.warn("Rate limit exceeded for {} on {}", clientIp, request.getServletPath());
            writeTooManyRequests(response);
            return;
        }

        chain.doFilter(request, response);
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isBlank()) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isBlank()) {
            ip = request.getRemoteAddr();
        }
        if (ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    private void writeTooManyRequests(HttpServletResponse response) throws IOException {
        response.setStatus(429);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(
                Response.fail("请求过于频繁，请稍后再试")
        ));
    }
}
