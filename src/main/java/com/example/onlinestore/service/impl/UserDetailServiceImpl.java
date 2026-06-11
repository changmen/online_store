package com.example.onlinestore.service.impl;

import com.example.onlinestore.entity.MemberEntity;
import com.example.onlinestore.mapper.MemberMapper;
import com.example.onlinestore.security.CustomUserDetails;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final MemberMapper memberMapper;
    private final Cache<String, MemberEntity> memberByNameCache;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MemberEntity memberEntity = memberByNameCache.get(username, memberMapper::findByName);
        if (memberEntity == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        return new CustomUserDetails(memberEntity);
    }
}
