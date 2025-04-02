package com.example.onlinestore.dto;

import com.example.onlinestore.constants.Constants;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@EqualsAndHashCode
@ToString
public class LoginRequest {
    @NotNull
    @Pattern(regexp = Constants.MEMBER_NAME_PATTERN)
    private String username;

    @NotNull
    @Pattern(regexp = Constants.MEMBER_PASSWORD_PATTERN)
    private String password;

}