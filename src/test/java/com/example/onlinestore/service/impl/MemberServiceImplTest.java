package com.example.onlinestore.service.impl;

import com.example.onlinestore.dto.LoginRequest;
import com.example.onlinestore.dto.LoginResponse;
import com.example.onlinestore.entity.MemberEntity;
import com.example.onlinestore.errors.ErrorCode;
import com.example.onlinestore.exceptions.BizException;
import com.example.onlinestore.mapper.MemberMapper;
import com.example.onlinestore.security.CustomUserDetails;
import com.example.onlinestore.security.JwtTokenUtil;
import com.example.onlinestore.service.TokenBlacklistService;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @Mock
    private MemberMapper memberMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtTokenUtil jwtTokenUtil;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private TokenBlacklistService tokenBlacklistService;

    private MemberServiceImpl memberService;
    private Cache<String, MemberEntity> memberByNameCache;
    private Cache<Long, MemberEntity> memberByIdCache;

    private MemberEntity activeMember;

    @BeforeEach
    void setUp() {
        memberByNameCache = Caffeine.newBuilder().build();
        memberByIdCache = Caffeine.newBuilder().build();
        memberService = new MemberServiceImpl(
                memberMapper, passwordEncoder, jwtTokenUtil,
                authenticationManager, memberByNameCache, memberByIdCache, tokenBlacklistService
        );

        activeMember = buildMemberEntity("testUser", "USER", "ACTIVE");
    }

    private MemberEntity buildMemberEntity(String name, String role, String status) {
        MemberEntity entity = new MemberEntity();
        entity.setId(1L);
        entity.setName(name);
        entity.setPassword("encoded-pwd");
        entity.setNickName("Test");
        entity.setPhone("13800138000");
        entity.setGender("MALE");
        entity.setAge(25);
        entity.setRole(role);
        entity.setStatus(status);
        return entity;
    }

    private LoginRequest buildLoginRequest() {
        LoginRequest request = new LoginRequest();
        request.setUsername("testUser");
        request.setPassword("Test@1234");
        return request;
    }

    @Test
    void login_success() {
        CustomUserDetails userDetails = new CustomUserDetails(activeMember);
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        when(authenticationManager.authenticate(any())).thenReturn(auth);
        when(jwtTokenUtil.generateToken(any())).thenReturn("access-token");

        LoginResponse response = memberService.login(buildLoginRequest());

        assertEquals("access-token", response.getToken());
    }

    @Test
    void login_badCredentials_throwsPasswordIncorrect() {
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("bad"));

        BizException ex = assertThrows(BizException.class, () -> memberService.login(buildLoginRequest()));
        assertEquals(ErrorCode.MEMBER_PASSWORD_INCORRECT, ex.getErrorCode());
    }

    @Test
    void login_disabledAccount_throwsMemberDisabled() {
        when(authenticationManager.authenticate(any()))
                .thenThrow(new DisabledException("disabled"));

        BizException ex = assertThrows(BizException.class, () -> memberService.login(buildLoginRequest()));
        assertEquals(ErrorCode.MEMBER_DISABLED, ex.getErrorCode());
    }

    @Test
    void login_lockedAccount_throwsMemberLocked() {
        when(authenticationManager.authenticate(any()))
                .thenThrow(new LockedException("locked"));

        BizException ex = assertThrows(BizException.class, () -> memberService.login(buildLoginRequest()));
        assertEquals(ErrorCode.MEMBER_LOCKED, ex.getErrorCode());
    }

    @Test
    void refreshToken_invalidToken_throwsInvalidRefreshToken() {
        when(jwtTokenUtil.parseToken("bad-token"))
                .thenReturn(JwtTokenUtil.TokenValidationResult.invalid());

        BizException ex = assertThrows(BizException.class, () -> memberService.refreshToken("bad-token"));
        assertEquals(ErrorCode.INVALID_REFRESH_TOKEN, ex.getErrorCode());
    }

    @Test
    void refreshToken_accessTokenUsedAsRefresh_throwsInvalidRefreshToken() {
        Claims claims = new DefaultClaims(Map.of("type", "access"));
        when(jwtTokenUtil.parseToken("access-token"))
                .thenReturn(new JwtTokenUtil.TokenValidationResult(true, "testUser", claims));

        BizException ex = assertThrows(BizException.class, () -> memberService.refreshToken("access-token"));
        assertEquals(ErrorCode.INVALID_REFRESH_TOKEN, ex.getErrorCode());
    }

    @Test
    void refreshToken_noTypeClaim_throwsInvalidRefreshToken() {
        Claims claims = new DefaultClaims(Map.of());
        when(jwtTokenUtil.parseToken("no-type-token"))
                .thenReturn(new JwtTokenUtil.TokenValidationResult(true, "testUser", claims));

        BizException ex = assertThrows(BizException.class, () -> memberService.refreshToken("no-type-token"));
        assertEquals(ErrorCode.INVALID_REFRESH_TOKEN, ex.getErrorCode());
    }

    @Test
    void refreshToken_blacklistedToken_throwsInvalidRefreshToken() {
        Claims claims = new DefaultClaims(Map.of("type", "refresh"));
        when(jwtTokenUtil.parseToken("blacklisted-token"))
                .thenReturn(new JwtTokenUtil.TokenValidationResult(true, "testUser", claims));
        when(tokenBlacklistService.isBlacklisted("blacklisted-token")).thenReturn(true);

        BizException ex = assertThrows(BizException.class, () -> memberService.refreshToken("blacklisted-token"));
        assertEquals(ErrorCode.INVALID_REFRESH_TOKEN, ex.getErrorCode());
    }

    @Test
    void refreshToken_success_rotatesTokenAndBlacklistsOld() {
        Claims claims = new DefaultClaims(Map.of("type", "refresh"));
        when(jwtTokenUtil.parseToken("old-refresh-token"))
                .thenReturn(new JwtTokenUtil.TokenValidationResult(true, "testUser", claims));
        when(tokenBlacklistService.isBlacklisted("old-refresh-token")).thenReturn(false);
        when(jwtTokenUtil.extractMemberId("old-refresh-token")).thenReturn(1L);
        when(jwtTokenUtil.generateToken(any())).thenReturn("new-access-token");
        when(jwtTokenUtil.generateRefreshToken("testUser", 1L)).thenReturn("new-refresh-token");

        memberByNameCache.put("testUser", activeMember);

        LoginResponse response = memberService.refreshToken("old-refresh-token");

        assertEquals("new-access-token", response.getToken());
        assertEquals("new-refresh-token", response.getRefreshToken());
        verify(tokenBlacklistService).addToBlacklist("old-refresh-token");
    }

    @Test
    void refreshToken_userNotFound_throwsMemberNotFound() {
        Claims claims = new DefaultClaims(Map.of("type", "refresh"));
        when(jwtTokenUtil.parseToken("refresh-token"))
                .thenReturn(new JwtTokenUtil.TokenValidationResult(true, "unknownUser", claims));
        when(tokenBlacklistService.isBlacklisted("refresh-token")).thenReturn(false);
        when(jwtTokenUtil.extractMemberId("refresh-token")).thenReturn(1L);
        when(memberMapper.findByName("unknownUser")).thenReturn(null);

        BizException ex = assertThrows(BizException.class, () -> memberService.refreshToken("refresh-token"));
        assertEquals(ErrorCode.MEMBER_NOT_FOUND, ex.getErrorCode());
    }
}
