package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.Member;
import com.example.onlinestore.dto.LoginRequest;
import com.example.onlinestore.dto.LoginResponse;
import com.example.onlinestore.dto.MemberRegistryRequest;
import com.example.onlinestore.entity.MemberEntity;
import com.example.onlinestore.enums.GenderType;
import com.example.onlinestore.errors.ErrorCode;
import com.example.onlinestore.exceptions.BizException;
import com.example.onlinestore.mapper.MemberMapper;
import com.example.onlinestore.security.JwtTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @Mock
    private MemberMapper memberMapper;

    @Mock
    private MessageSource messageSource;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private MemberServiceImpl memberService;

    private LoginRequest loginRequest;
    private MemberRegistryRequest registryRequest;
    private MemberEntity memberEntity;

    @BeforeEach
    void setUp() {
        // 登录请求
        loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");

        // 注册请求
        registryRequest = new MemberRegistryRequest();
        registryRequest.setName("testuser");
        registryRequest.setPassword("password123");
        registryRequest.setNickName("测试用户");
        registryRequest.setGender(GenderType.MALE);
        registryRequest.setAge(25);
        registryRequest.setPhone("13800138000");

        // 会员实体
        memberEntity = new MemberEntity();
        memberEntity.setId(1L);
        memberEntity.setName("testuser");
        memberEntity.setPassword("encodedPassword");
        memberEntity.setNickName("测试用户");
        memberEntity.setGender("MALE");
        memberEntity.setAge(25);
        memberEntity.setPhone("13800138000");
        memberEntity.setCreatedAt(LocalDateTime.now());
        memberEntity.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void testLogin_Success() {
        // Given
        when(memberMapper.findByName("testuser")).thenReturn(memberEntity);
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
        when(jwtTokenUtil.generateToken(any(User.class))).thenReturn("jwt-token");

        // When
        LoginResponse result = memberService.login(loginRequest);

        // Then
        assertNotNull(result);
        assertEquals("jwt-token", result.getToken());

        verify(memberMapper).findByName("testuser");
        verify(passwordEncoder).matches("password123", "encodedPassword");
        verify(jwtTokenUtil).generateToken(any(User.class));
    }

    @Test
    void testLogin_UserNotFound() {
        // Given
        when(memberMapper.findByName("testuser")).thenReturn(null);

        // When & Then
        BizException exception = assertThrows(BizException.class, () -> {
            memberService.login(loginRequest);
        });
        assertEquals(ErrorCode.MEMBER_PASSWORD_INCORRECT, exception.getErrorCode());

        verify(memberMapper).findByName("testuser");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtTokenUtil, never()).generateToken(any(User.class));
    }

    @Test
    void testLogin_PasswordIncorrect() {
        // Given
        when(memberMapper.findByName("testuser")).thenReturn(memberEntity);
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(false);

        // When & Then
        BizException exception = assertThrows(BizException.class, () -> {
            memberService.login(loginRequest);
        });
        assertEquals(ErrorCode.MEMBER_PASSWORD_INCORRECT, exception.getErrorCode());

        verify(memberMapper).findByName("testuser");
        verify(passwordEncoder).matches("password123", "encodedPassword");
        verify(jwtTokenUtil, never()).generateToken(any(User.class));
    }

    @Test
    void testRegistry_Success() {
        // Given
        when(memberMapper.findByName("testuser")).thenReturn(null);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(memberMapper.insertMember(any(MemberEntity.class))).thenReturn(1);

        // When
        Member result = memberService.registry(registryRequest);

        // Then
        assertNotNull(result);
        
        verify(memberMapper).findByName("testuser");
        verify(passwordEncoder).encode("password123");
        verify(memberMapper).insertMember(any(MemberEntity.class));
    }

    @Test
    void testRegistry_MemberExists() {
        // Given
        when(memberMapper.findByName("testuser")).thenReturn(memberEntity);

        // When & Then
        BizException exception = assertThrows(BizException.class, () -> {
            memberService.registry(registryRequest);
        });
        assertEquals(ErrorCode.MEMBER_EXISTED, exception.getErrorCode());

        verify(memberMapper).findByName("testuser");
        verify(memberMapper, never()).insertMember(any(MemberEntity.class));
    }

    @Test
    void testRegistry_InsertFailed() {
        // Given
        when(memberMapper.findByName("testuser")).thenReturn(null);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(memberMapper.insertMember(any(MemberEntity.class))).thenReturn(0);

        // When & Then
        BizException exception = assertThrows(BizException.class, () -> {
            memberService.registry(registryRequest);
        });
        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());

        verify(memberMapper).insertMember(any(MemberEntity.class));
    }

    @Test
    void testGetMemberById_Success() {
        // Given
        when(memberMapper.findById(1L)).thenReturn(memberEntity);

        // When
        Member result = memberService.getMemberById(1L);

        // Then
        assertNotNull(result);
        
        verify(memberMapper).findById(1L);
    }

    @Test
    void testGetMemberById_NotFound() {
        // Given
        when(memberMapper.findById(1L)).thenReturn(null);

        // When & Then
        BizException exception = assertThrows(BizException.class, () -> {
            memberService.getMemberById(1L);
        });
        assertEquals(ErrorCode.MEMBER_NOT_FOUND, exception.getErrorCode());

        verify(memberMapper).findById(1L);
    }

    @Test
    void testGetMemberByName_Success() {
        // Given
        when(memberMapper.findByName("testuser")).thenReturn(memberEntity);

        // When
        Member result = memberService.getMemberByName("testuser");

        // Then
        assertNotNull(result);
        
        verify(memberMapper).findByName("testuser");
    }

    @Test
    void testGetMemberByName_NotFound() {
        // Given
        when(memberMapper.findByName("testuser")).thenReturn(null);

        // When
        Member result = memberService.getMemberByName("testuser");

        // Then
        assertNull(result);
        
        verify(memberMapper).findByName("testuser");
    }

    @Test
    void testGetLoginMember_Success() {
        // Given
        Authentication auth = new UsernamePasswordAuthenticationToken("testuser", "password");
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
        when(memberMapper.findByName("testuser")).thenReturn(memberEntity);

        // When
        Member result = memberService.getLoginMember();

        // Then
        assertNotNull(result);
        
        verify(memberMapper).findByName("testuser");
    }

    @Test
    void testGetLoginMember_AnonymousUser() {
        // Given
        Authentication auth = mock(AnonymousAuthenticationToken.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        // When & Then
        BizException exception = assertThrows(BizException.class, () -> {
            memberService.getLoginMember();
        });
        assertEquals(ErrorCode.MEMBER_NOT_LOGIN, exception.getErrorCode());
    }

    @Test
    void testGetLoginMember_BlankUsername() {
        // Given
        Authentication auth = new UsernamePasswordAuthenticationToken("", "password");
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);

        // When & Then
        BizException exception = assertThrows(BizException.class, () -> {
            memberService.getLoginMember();
        });
        assertEquals(ErrorCode.MEMBER_NOT_LOGIN, exception.getErrorCode());
    }

    @Test
    void testGetLoginMember_MemberNotFound() {
        // Given
        Authentication auth = new UsernamePasswordAuthenticationToken("testuser", "password");
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
        when(memberMapper.findByName("testuser")).thenReturn(null);

        // When & Then
        BizException exception = assertThrows(BizException.class, () -> {
            memberService.getLoginMember();
        });
        assertEquals(ErrorCode.MEMBER_NOT_LOGIN, exception.getErrorCode());

        verify(memberMapper).findByName("testuser");
    }
}