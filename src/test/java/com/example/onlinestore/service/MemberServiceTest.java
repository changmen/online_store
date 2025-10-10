package com.example.onlinestore.service;

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
import com.example.onlinestore.service.impl.MemberServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
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

/**
 * MemberService单元测试
 */
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

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

    private MemberEntity testMemberEntity;
    private LoginRequest testLoginRequest;
    private MemberRegistryRequest testRegistryRequest;

    @BeforeEach
    void setUp() {
        // 设置测试数据
        testMemberEntity = new MemberEntity();
        testMemberEntity.setId(1L);
        testMemberEntity.setName("testuser");
        testMemberEntity.setPassword("encoded_password");
        testMemberEntity.setNickName("Test User");
        testMemberEntity.setPhone("13800138000");
        testMemberEntity.setGender("MALE");
        testMemberEntity.setAge(25);
        testMemberEntity.setCreatedAt(LocalDateTime.now());
        testMemberEntity.setUpdatedAt(LocalDateTime.now());

        testLoginRequest = new LoginRequest();
        testLoginRequest.setUsername("testuser");
        testLoginRequest.setPassword("password123");

        testRegistryRequest = new MemberRegistryRequest();
        testRegistryRequest.setName("newuser");
        testRegistryRequest.setPassword("password123");
        testRegistryRequest.setNickName("New User");
        testRegistryRequest.setPhone("13900139000");
        testRegistryRequest.setGender(GenderType.FEMALE);
        testRegistryRequest.setAge(22);
    }

    @Test
    void testLoginSuccess() {
        // Given
        when(memberMapper.findByName("testuser")).thenReturn(testMemberEntity);
        when(passwordEncoder.matches("password123", "encoded_password")).thenReturn(true);
        when(jwtTokenUtil.generateToken(any(User.class))).thenReturn("jwt_token_123");

        // When
        LoginResponse response = memberService.login(testLoginRequest);

        // Then
        assertNotNull(response);
        assertEquals("jwt_token_123", response.getToken());
        
        verify(memberMapper).findByName("testuser");
        verify(passwordEncoder).matches("password123", "encoded_password");
        verify(jwtTokenUtil).generateToken(any(User.class));
    }

    @Test
    void testLoginFailure_UserNotFound() {
        // Given
        when(memberMapper.findByName("testuser")).thenReturn(null);

        // When & Then
        BizException exception = assertThrows(BizException.class, 
            () -> memberService.login(testLoginRequest));
        
        assertEquals(ErrorCode.MEMBER_PASSWORD_INCORRECT, exception.getErrorCode());
        verify(memberMapper).findByName("testuser");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtTokenUtil, never()).generateToken(any());
    }

    @Test
    void testLoginFailure_WrongPassword() {
        // Given
        when(memberMapper.findByName("testuser")).thenReturn(testMemberEntity);
        when(passwordEncoder.matches("password123", "encoded_password")).thenReturn(false);

        // When & Then
        BizException exception = assertThrows(BizException.class, 
            () -> memberService.login(testLoginRequest));
        
        assertEquals(ErrorCode.MEMBER_PASSWORD_INCORRECT, exception.getErrorCode());
        verify(memberMapper).findByName("testuser");
        verify(passwordEncoder).matches("password123", "encoded_password");
        verify(jwtTokenUtil, never()).generateToken(any());
    }

    @Test
    void testRegistrySuccess() {
        // Given
        when(memberMapper.findByName("newuser")).thenReturn(null);
        when(passwordEncoder.encode("password123")).thenReturn("encoded_password");
        when(memberMapper.insertMember(any(MemberEntity.class))).thenReturn(1);

        // When
        Member result = memberService.registry(testRegistryRequest);

        // Then
        assertNotNull(result);
        assertEquals("newuser", result.getBaseInfo().getName());
        assertEquals("New User", result.getBaseInfo().getNickName());
        assertEquals("13900139000", result.getBaseInfo().getPhone());
        assertEquals(GenderType.FEMALE, result.getBaseInfo().getGender());
        assertEquals(22, result.getBaseInfo().getAge());
        
        verify(memberMapper).findByName("newuser");
        verify(passwordEncoder).encode("password123");
        verify(memberMapper).insertMember(any(MemberEntity.class));
    }

    @Test
    void testRegistryFailure_UserExists() {
        // Given
        when(memberMapper.findByName("newuser")).thenReturn(testMemberEntity);

        // When & Then
        BizException exception = assertThrows(BizException.class, 
            () -> memberService.registry(testRegistryRequest));
        
        assertEquals(ErrorCode.MEMBER_EXISTED, exception.getErrorCode());
        verify(memberMapper).findByName("newuser");
        verify(passwordEncoder, never()).encode(anyString());
        verify(memberMapper, never()).insertMember(any());
    }

    @Test
    void testRegistryFailure_DatabaseError() {
        // Given
        when(memberMapper.findByName("newuser")).thenReturn(null);
        when(passwordEncoder.encode("password123")).thenReturn("encoded_password");
        when(memberMapper.insertMember(any(MemberEntity.class))).thenReturn(0);

        // When & Then
        BizException exception = assertThrows(BizException.class, 
            () -> memberService.registry(testRegistryRequest));
        
        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, exception.getErrorCode());
        verify(memberMapper).insertMember(any(MemberEntity.class));
    }

    @Test
    void testGetMemberByIdSuccess() {
        // Given
        when(memberMapper.findById(1L)).thenReturn(testMemberEntity);

        // When
        Member result = memberService.getMemberById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("testuser", result.getBaseInfo().getName());
        verify(memberMapper).findById(1L);
    }

    @Test
    void testGetMemberByIdNotFound() {
        // Given
        when(memberMapper.findById(1L)).thenReturn(null);

        // When & Then
        BizException exception = assertThrows(BizException.class, 
            () -> memberService.getMemberById(1L));
        
        assertEquals(ErrorCode.MEMBER_NOT_FOUND, exception.getErrorCode());
        verify(memberMapper).findById(1L);
    }

    @Test
    void testGetMemberByNameSuccess() {
        // Given
        when(memberMapper.findByName("testuser")).thenReturn(testMemberEntity);

        // When
        Member result = memberService.getMemberByName("testuser");

        // Then
        assertNotNull(result);
        assertEquals("testuser", result.getBaseInfo().getName());
        verify(memberMapper).findByName("testuser");
    }

    @Test
    void testGetMemberByNameNotFound() {
        // Given
        when(memberMapper.findByName("testuser")).thenReturn(null);

        // When
        Member result = memberService.getMemberByName("testuser");

        // Then
        assertNull(result);
        verify(memberMapper).findByName("testuser");
    }

    @Test
    void testGetLoginMemberSuccess() {
        // Given
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");
        when(memberMapper.findByName("testuser")).thenReturn(testMemberEntity);

        // When
        Member result = memberService.getLoginMember();

        // Then
        assertNotNull(result);
        assertEquals("testuser", result.getBaseInfo().getName());
        verify(memberMapper).findByName("testuser");
    }

    @Test
    void testGetLoginMemberNotLogin() {
        // Given
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(null);

        // When & Then
        BizException exception = assertThrows(BizException.class, 
            () -> memberService.getLoginMember());
        
        assertEquals(ErrorCode.MEMBER_NOT_LOGIN, exception.getErrorCode());
    }
}