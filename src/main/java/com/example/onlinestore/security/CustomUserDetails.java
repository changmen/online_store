package com.example.onlinestore.security;

import com.example.onlinestore.bean.Member;
import com.example.onlinestore.entity.MemberEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Member member;
    private final String username;
    private final String password;
    private final List<GrantedAuthority> authorities;
    private final boolean enabled;
    private final boolean accountNonLocked;

    public CustomUserDetails(MemberEntity memberEntity) {
        this.member = memberEntity.toMember();
        this.username = memberEntity.getName();
        this.password = memberEntity.getPassword();
        String role = memberEntity.getRole();
        if (role == null || role.isBlank()) {
            role = "USER";
        }
        this.authorities = List.of(new SimpleGrantedAuthority("ROLE_" + role));

        String status = memberEntity.getStatus();
        this.enabled = !"DISABLED".equals(status);
        this.accountNonLocked = !"LOCKED".equals(status);
    }

    public Member getMember() {
        return this.member;
    }

    public Long getMemberId() {
        return this.member.getId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
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
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
