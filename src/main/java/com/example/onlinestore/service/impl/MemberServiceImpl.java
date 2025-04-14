package com.example.onlinestore.service.impl;

import com.example.onlinestore.bean.Member;
import com.example.onlinestore.dto.LoginRequest;
import com.example.onlinestore.dto.LoginResponse;
import com.example.onlinestore.dto.MemberRegistryRequest;
import com.example.onlinestore.entity.MemberEntity;
import com.example.onlinestore.errors.ErrorCode;
import com.example.onlinestore.exceptions.BizException;
import com.example.onlinestore.mapper.MemberMapper;
import com.example.onlinestore.security.JwtTokenUtil;
import com.example.onlinestore.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
public class MemberServiceImpl implements MemberService{

    private static final Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class);

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    @Transactional
    public LoginResponse login(LoginRequest request) {
        MemberEntity user = memberMapper.findByName(request.getUsername());
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            logger.error("login failed. because username or password is invalid. username:{}, requestPassword:{}", request.getUsername(), request.getPassword());
            throw new BizException(ErrorCode.MEMBER_PASSWORD_INCORRECT);
        }

        String token =jwtTokenUtil.generateToken(new User(user.getName(), user.getPassword(), new ArrayList<>()));
        return new LoginResponse(token);
    }

    @Override
    public Member registry(MemberRegistryRequest request) {
        // 判断用户名是否重复
        if (memberMapper.findByName(request.getName()) != null) {
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
        memberEntity.setCreatedAt(now);
        memberEntity.setUpdatedAt(now);

        int effectRows = memberMapper.insertMember(memberEntity);
        if (effectRows != 1) {
            logger.error("insert member failed. because effect rows is 0. memberName:{}", request.getName());
            throw new BizException(ErrorCode.INTERNAL_ERROR);
        }

        return memberEntity.toMember();
    }

    @Override
    public Member getMemberById(Long id) {
        MemberEntity memberEntity = memberMapper.findById(id);
        if (memberEntity != null) {
            return memberEntity.toMember();
        }
        throw new BizException(ErrorCode.MEMBER_NOT_FOUND);
    }

}