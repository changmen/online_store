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

    /**
     * 用户唯一标识ID
     **/
    private Long id;

    /**
     * 用户基础信息，包含姓名、联系方式等详细信息
     */
    private MemberBaseInfoResponse baseInfo;

    @Getter
    @Setter
    @Builder
    @EqualsAndHashCode
    @ToString
    static class MemberBaseInfoResponse implements Serializable {
        @Serial
        private static final long serialVersionUID = 4053414940647847736L;
        /**
         * 用户真实姓名
         */
        private String name;

        /**
         * 用户显示昵称
         */
        private String nickName;

        /**
         * 账户密码
         * 需加密存储，应符合密码强度策略要求（至少包含大小写字母、数字和特殊符号）
         */
        private String password;

        /**
         * 联系电话
         * 需符合E.164国际电话号码格式，如+8613912345678
         */
        private String phone;

        /**
         * 性别枚举值
         * 可选值：MALE(男)/FEMALE(女)
         */
        private GenderType gender;

        /**
         * 用户年龄
         */
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