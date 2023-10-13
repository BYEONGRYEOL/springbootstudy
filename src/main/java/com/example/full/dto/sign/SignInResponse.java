package com.example.full.dto.sign;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SignInResponse {
    private String accessToken;
    private String refreshToken;
}
