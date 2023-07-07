package com.example.full.dto.sign;

import com.example.full.entity.member.Member;
import com.example.full.entity.member.Role;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class SignUpRequest {
    private String email;
    private String password;
    private String username;
    private String nickname;

    public static Member toEntity(SignUpRequest req, Role role, PasswordEncoder encoder){
        return new Member(req.email, encoder.encode(req.password), req.username, req.nickname, List.of(role));
    }
}
