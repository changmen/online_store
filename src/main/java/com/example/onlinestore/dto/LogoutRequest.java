package com.example.onlinestore.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LogoutRequest {
    @NotBlank
    private String token;

    private String refreshToken;
}
