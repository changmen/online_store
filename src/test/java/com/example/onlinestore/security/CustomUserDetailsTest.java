package com.example.onlinestore.security;

import com.example.onlinestore.entity.MemberEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomUserDetailsTest {

    private MemberEntity buildMemberEntity(String status) {
        MemberEntity entity = new MemberEntity();
        entity.setId(1L);
        entity.setName("testUser");
        entity.setPassword("encoded-password");
        entity.setNickName("Test");
        entity.setPhone("13800138000");
        entity.setGender("MALE");
        entity.setAge(25);
        entity.setRole("USER");
        entity.setStatus(status);
        return entity;
    }

    @Test
    void activeStatus_isEnabledAndNotLocked() {
        CustomUserDetails userDetails = new CustomUserDetails(buildMemberEntity("ACTIVE"));

        assertTrue(userDetails.isEnabled());
        assertTrue(userDetails.isAccountNonLocked());
    }

    @Test
    void disabledStatus_isNotEnabled() {
        CustomUserDetails userDetails = new CustomUserDetails(buildMemberEntity("DISABLED"));

        assertFalse(userDetails.isEnabled());
        assertTrue(userDetails.isAccountNonLocked());
    }

    @Test
    void lockedStatus_isLocked() {
        CustomUserDetails userDetails = new CustomUserDetails(buildMemberEntity("LOCKED"));

        assertTrue(userDetails.isEnabled());
        assertFalse(userDetails.isAccountNonLocked());
    }

    @Test
    void nullStatus_defaultsToEnabled() {
        CustomUserDetails userDetails = new CustomUserDetails(buildMemberEntity(null));

        assertTrue(userDetails.isEnabled());
        assertTrue(userDetails.isAccountNonLocked());
    }

    @Test
    void authorities_includeRolePrefix() {
        MemberEntity entity = buildMemberEntity("ACTIVE");
        entity.setRole("ADMIN");
        CustomUserDetails userDetails = new CustomUserDetails(entity);

        assertEquals(1, userDetails.getAuthorities().size());
        assertEquals("ROLE_ADMIN", userDetails.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    void nullRole_defaultsToUser() {
        MemberEntity entity = buildMemberEntity("ACTIVE");
        entity.setRole(null);
        CustomUserDetails userDetails = new CustomUserDetails(entity);

        assertEquals("ROLE_USER", userDetails.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    void getMemberId_returnsEntityId() {
        CustomUserDetails userDetails = new CustomUserDetails(buildMemberEntity("ACTIVE"));

        assertEquals(1L, userDetails.getMemberId());
    }

    @Test
    void isAccountNonExpired_alwaysTrue() {
        CustomUserDetails userDetails = new CustomUserDetails(buildMemberEntity("ACTIVE"));
        assertTrue(userDetails.isAccountNonExpired());
    }

    @Test
    void isCredentialsNonExpired_alwaysTrue() {
        CustomUserDetails userDetails = new CustomUserDetails(buildMemberEntity("ACTIVE"));
        assertTrue(userDetails.isCredentialsNonExpired());
    }
}
