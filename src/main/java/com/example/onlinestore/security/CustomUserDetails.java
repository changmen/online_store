package com.example.onlinestore.security;

import com.example.onlinestore.bean.Member;
import com.example.onlinestore.entity.MemberEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Member member;
    private final String username;
    private final String password;

    public CustomUserDetails(MemberEntity memberEntity) {
        this.member = memberEntity.toMember();
        this.username = memberEntity.getName();
        this.password = memberEntity.getPassword();
    }

    public Member getMember() {
        return this.member;
    }

    public Long getMemberId() {
        return this.member.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
