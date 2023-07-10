package com.example.full.service.sign;

import com.example.full.handler.JwtHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor // 생성자에 final or NonNull 선언된 필드들에 자동으로 스프링 빈 주입
public class TokenService {
    private final JwtHandler jwtHandler;

}
