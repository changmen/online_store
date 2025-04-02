package com.example.onlinestore.entity;

import com.example.onlinestore.bean.Member;
import com.example.onlinestore.bean.MemberBaseInfo;
import com.example.onlinestore.enums.GenderType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

// 用户表
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class MemberEntity implements Serializable  {
    @Serial
    private static final long serialVersionUID = -5135051549283930313L;
    
    // 主键ID
    private Long id;

    // 用户姓名
    private String name;

    // 用户昵称
    private String nickName;

    // 用户密码
    private String password;

    // 用户手机号
    private String phone;

    // 用户性别
    private String gender;

    // 用户年龄
    private int age;

    // 创建时间
    private LocalDateTime createdAt;

    // 更新时间
    private LocalDateTime updatedAt;

    public Member toMember() {
        Member member = new Member();
        member.setId(this.id);

        MemberBaseInfo baseInfo = new MemberBaseInfo();
        baseInfo.setName(this.name);
        baseInfo.setNickName(this.nickName);
        baseInfo.setPassword(this.password);
        baseInfo.setPhone(this.phone);
        baseInfo.setGender(GenderType.valueOf(this.gender));
        baseInfo.setAge(this.age);

        member.setBaseInfo(baseInfo);
        return member;
    }
}