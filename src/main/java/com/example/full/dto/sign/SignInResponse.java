package com.example.full.dto.sign;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class SignInResponse {
    private String accessToken;
    private String refreshToken;
}
