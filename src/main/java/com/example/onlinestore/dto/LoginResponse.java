package com.example.onlinestore.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class LoginResponse {
    private String token;
}