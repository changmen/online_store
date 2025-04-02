package com.example.onlinestore.bean;

import jakarta.validation.Valid;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
@EqualsAndHashCode
@ToString
public class Member implements Serializable {
    @Serial
    private static final long serialVersionUID = 1099483498189107702L;

    private Long id;
    // 会员基本信息
    @Valid
    private MemberBaseInfo baseInfo;

}
