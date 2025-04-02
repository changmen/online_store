package com.example.onlinestore.dto;

import com.example.onlinestore.constants.Constants;
import com.example.onlinestore.enums.GenderType;
import jakarta.validation.constraints.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class MemberRegistryRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = -7490516187847553182L;

    @NotNull
    @Size(min = 2, max = 16)
    @Pattern(regexp = Constants.MEMBER_NAME_PATTERN)
    private String name;

    @NotNull
    @Pattern(regexp = Constants.MEMBER_PASSWORD_PATTERN)
    @Size(min = 8, max = 16)
    private String password;

    @Size(max = 16)
    private String nickName;

    @NotNull
    private GenderType gender;

    @Min(18)
    private int age;

    @NotNull
    @Pattern(regexp = Constants.PHONE_PATTERN)
    private String phone;

}
