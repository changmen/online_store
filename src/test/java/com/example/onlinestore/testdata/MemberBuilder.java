package com.example.onlinestore.testdata;

import com.example.onlinestore.bean.Member;
import com.example.onlinestore.entity.MemberEntity;
import com.example.onlinestore.enums.GenderType;
import java.time.LocalDateTime;

/**
 * Member和MemberEntity测试数据构建器
 * 提供标准的用户实体测试数据构建功能
 */
public class MemberBuilder {
    
    private Long id;
    private String name;
    private String nickName;
    private String password;
    private String phone;
    private GenderType gender;
    private Integer age;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    private MemberBuilder() {
        // 设置默认值
        this.name = "testuser";
        this.nickName = "测试用户";
        this.password = "encodedPassword123";
        this.phone = "13800138000";
        this.gender = GenderType.MALE;
        this.age = 25;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public static MemberBuilder builder() {
        return new MemberBuilder();
    }
    
    public MemberBuilder id(Long id) {
        this.id = id;
        return this;
    }
    
    public MemberBuilder name(String name) {
        this.name = name;
        return this;
    }
    
    public MemberBuilder nickName(String nickName) {
        this.nickName = nickName;
        return this;
    }
    
    public MemberBuilder password(String password) {
        this.password = password;
        return this;
    }
    
    public MemberBuilder phone(String phone) {
        this.phone = phone;
        return this;
    }
    
    public MemberBuilder gender(GenderType gender) {
        this.gender = gender;
        return this;
    }
    
    public MemberBuilder age(Integer age) {
        this.age = age;
        return this;
    }
    
    public MemberBuilder createdAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }
    
    public MemberBuilder updatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
    
    /**
     * 构建标准管理员用户
     */
    public MemberBuilder asAdmin() {
        return this.name("admin")
                  .nickName("管理员")
                  .phone("13900000001");
    }
    
    /**
     * 构建标准普通用户
     */
    public MemberBuilder asRegularUser() {
        return this.name("user001")
                  .nickName("普通用户")
                  .phone("13900000002");
    }
    
    /**
     * 构建用于测试的用户
     */
    public MemberBuilder asTestUser(Long userId) {
        return this.id(userId)
                  .name("testuser" + userId)
                  .nickName("测试用户" + userId)
                  .phone("1390000" + String.format("%04d", userId));
    }
    
    /**
     * 构建女性用户
     */
    public MemberBuilder asFemale() {
        return this.gender(GenderType.FEMALE)
                  .nickName("女性用户");
    }
    
    public Member buildMember() {
        Member member = new Member();
        member.setId(this.id);
        member.setName(this.name);
        member.setNickName(this.nickName);
        member.setPhone(this.phone);
        member.setGender(this.gender);
        member.setAge(this.age);
        return member;
    }
    
    public MemberEntity buildMemberEntity() {
        MemberEntity entity = new MemberEntity();
        entity.setId(this.id);
        entity.setName(this.name);
        entity.setNickName(this.nickName);
        entity.setPassword(this.password);
        entity.setPhone(this.phone);
        entity.setGender(this.gender != null ? this.gender.name() : null);
        entity.setAge(this.age);
        entity.setCreatedAt(this.createdAt);
        entity.setUpdatedAt(this.updatedAt);
        return entity;
    }
}