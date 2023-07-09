package com.example.full.dto.sign;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignInRequest {
    private String email;
    private String password;
}
