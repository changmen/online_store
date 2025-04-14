package com.example.onlinestore.dto;

import com.example.onlinestore.bean.Member;
import com.example.onlinestore.enums.GenderType;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
@Builder
@EqualsAndHashCode
@ToString
public class MemberResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 4907636112900361030L;

    // 会员ID
    private Long id;

    // 会员基本信息
    private MemberBaseInfoResponse baseInfo;


    // 会员基本信息
    @Getter
    @Setter
    @Builder
    @EqualsAndHashCode
    @ToString
    static class MemberBaseInfoResponse implements Serializable {
        @Serial
        private static final long serialVersionUID = 4053414940647847736L;
        private String name;
        private String nickName;
        private String password;
        private String phone;
        private GenderType gender;
        private int age;
    }

    public static MemberResponse of(Member member) {
        return MemberResponse.builder()
                .id(member.getId())
                .baseInfo(MemberBaseInfoResponse.builder()
                        .name(member.getBaseInfo().getName())
                        .nickName(member.getBaseInfo().getNickName())
                        .password(member.getBaseInfo().getPassword())
                        .phone(member.getBaseInfo().getPhone())
                        .gender(member.getBaseInfo().getGender())
                        .age(member.getBaseInfo().getAge()).build()
                ).build();
    }


}