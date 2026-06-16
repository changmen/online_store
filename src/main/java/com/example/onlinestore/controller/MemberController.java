package com.example.onlinestore.controller;

import com.example.onlinestore.bean.Member;
import com.example.onlinestore.dto.*;
import com.example.onlinestore.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户管理控制器
 */
@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {
    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    private final MemberService memberService;


    @PostMapping("/registry")
    public Response<MemberResponse> registry(@Valid @RequestBody MemberRegistryRequest request) {
        logger.info("registry member: {}", request.getName());
        Member member =  memberService.registry(request);
        return Response.success(MemberResponse.of(member));
    }

    @PostMapping("/login")
    public Response<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return Response.success(memberService.login(request));
    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public Response<String> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            memberService.logout(token);
        }
        return Response.success("登出成功");
    }

    @PostMapping("/refresh")
    public Response<LoginResponse> refresh(@Valid @RequestBody RefreshTokenRequest request) {
        LoginResponse response = memberService.refreshToken(request.getRefreshToken());
        return Response.success(response);
    }
} 