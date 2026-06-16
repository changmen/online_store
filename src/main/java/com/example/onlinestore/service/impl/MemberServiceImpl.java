package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.Member;
import com.example.onlinestore.dto.LoginRequest;
import com.example.onlinestore.dto.LoginResponse;
import com.example.onlinestore.dto.MemberRegistryRequest;
import com.example.onlinestore.entity.MemberEntity;
import com.example.onlinestore.errors.ErrorCode;
import com.example.onlinestore.exceptions.BizException;
import com.example.onlinestore.mapper.MemberMapper;
import com.example.onlinestore.security.CustomUserDetails;
import com.example.onlinestore.security.JwtTokenUtil;
import com.example.onlinestore.service.MemberService;
import com.example.onlinestore.service.TokenBlacklistService;
import com.github.benmanes.caffeine.cache.Cache;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private static final Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class);

    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final org.springframework.security.authentication.AuthenticationManager authenticationManager;
    private final Cache<String, MemberEntity> memberByNameCache;
    private final Cache<Long, MemberEntity> memberByIdCache;
    private final TokenBlacklistService tokenBlacklistService;

    @Override
    @Transactional(readOnly = true)
    public LoginResponse login(@Valid LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String token = jwtTokenUtil.generateToken(userDetails);
            String refreshToken = jwtTokenUtil.generateRefreshToken(userDetails.getUsername(), userDetails.getMemberId());
            return new LoginResponse(token, refreshToken);
        } catch (BadCredentialsException e) {
            logger.warn("login failed. because username or password is invalid. username:{}", request.getUsername());
            throw new BizException(ErrorCode.MEMBER_PASSWORD_INCORRECT);
        } catch (org.springframework.security.authentication.DisabledException e) {
            logger.warn("login failed. account is disabled. username:{}", request.getUsername());
            throw new BizException(ErrorCode.MEMBER_DISABLED);
        } catch (org.springframework.security.authentication.LockedException e) {
            logger.warn("login failed. account is locked. username:{}", request.getUsername());
            throw new BizException(ErrorCode.MEMBER_LOCKED);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Member registry(@Valid MemberRegistryRequest request) {
        if (findMemberByNameCached(request.getName()) != null) {
            throw new BizException(ErrorCode.MEMBER_EXISTED, request.getName());
        }

        LocalDateTime now = LocalDateTime.now();
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setName(request.getName());
        memberEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        memberEntity.setNickName(request.getNickName());
        memberEntity.setPhone(request.getPhone());
        memberEntity.setGender(request.getGender().name());
        memberEntity.setAge(request.getAge());
        memberEntity.setRole("USER");
        memberEntity.setCreatedAt(now);
        memberEntity.setUpdatedAt(now);

        int effectRows = memberMapper.insertMember(memberEntity);
        if (effectRows != 1) {
            logger.error("insert member failed. because effect rows is 0. memberName:{}", request.getName());
            throw new BizException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        memberByNameCache.put(memberEntity.getName(), memberEntity);
        memberByIdCache.put(memberEntity.getId(), memberEntity);
        return memberEntity.toMember();
    }

    @Override
    @Transactional(readOnly = true)
    public Member getMemberById(@NotNull Long id) {
        MemberEntity memberEntity = memberByIdCache.get(id, memberMapper::findById);
        if (memberEntity != null) {
            memberByNameCache.put(memberEntity.getName(), memberEntity);
            return memberEntity.toMember();
        }
        throw new BizException(ErrorCode.MEMBER_NOT_FOUND, id);
    }

    @Override
    @Transactional(readOnly = true)
    public Member getMemberByName(@NotNull String name) {
        MemberEntity memberEntity = findMemberByNameCached(StringUtils.trim(name));
        if (memberEntity != null) {
            return memberEntity.toMember();
        }
        logger.info("member not found. memberName:{}", name);
        return null;
    }

    MemberEntity findMemberByNameCached(String name) {
        MemberEntity entity = memberByNameCache.get(name, memberMapper::findByName);
        if (entity != null) {
            memberByIdCache.put(entity.getId(), entity);
        }
        return entity;
    }

    @Override
    public Member getLoginMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            throw new BizException(ErrorCode.MEMBER_NOT_LOGIN);
        }

        if (authentication.getPrincipal() instanceof CustomUserDetails customUserDetails) {
            return customUserDetails.getMember();
        }

        throw new BizException(ErrorCode.MEMBER_NOT_LOGIN);
    }

    @Override
    @Transactional(readOnly = true)
    public LoginResponse refreshToken(String refreshToken) {
        try {
            JwtTokenUtil.TokenValidationResult result = jwtTokenUtil.parseToken(refreshToken);
            if (!result.valid()) {
                logger.warn("refreshToken is invalid or expired");
                throw new BizException(ErrorCode.INVALID_REFRESH_TOKEN);
            }

            if (!"refresh".equals(result.claims().get("type", String.class))) {
                logger.warn("Token is not a refresh token");
                throw new BizException(ErrorCode.INVALID_REFRESH_TOKEN);
            }

            if (tokenBlacklistService.isBlacklisted(refreshToken)) {
                logger.warn("refreshToken is blacklisted");
                throw new BizException(ErrorCode.INVALID_REFRESH_TOKEN);
            }

            String username = result.username();
            Long memberId = jwtTokenUtil.extractMemberId(refreshToken);

            MemberEntity memberEntity = findMemberByNameCached(username);
            if (memberEntity == null) {
                logger.warn("User not found for refreshToken: username={}", username);
                throw new BizException(ErrorCode.MEMBER_NOT_FOUND, username);
            }

            CustomUserDetails userDetails = new CustomUserDetails(memberEntity);
            String newAccessToken = jwtTokenUtil.generateToken(userDetails);
            String newRefreshToken = jwtTokenUtil.generateRefreshToken(username, memberId);

            tokenBlacklistService.addToBlacklist(refreshToken);

            return new LoginResponse(newAccessToken, newRefreshToken);
        } catch (BizException e) {
            throw e;
        } catch (Exception e) {
            logger.error("Failed to refresh token: {}", e.getMessage(), e);
            throw new BizException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
    }

    @Override
    public void logout(String token) {
        if (token != null && !token.isBlank()) {
            tokenBlacklistService.addToBlacklist(token);
        }
    }
}