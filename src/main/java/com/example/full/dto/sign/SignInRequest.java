package com.example.full.dto.sign;

import lombok.*;


// Getter, Setter NoArgsConstructor AllArgsConstructor 로는 안된다.
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SignInRequest {
    private String email;
    private String password;
}
