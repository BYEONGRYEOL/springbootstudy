package com.example.full.service.sign;

import com.example.full.handler.JwtHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor // 생성자에 final or NonNull 선언된 필드들에 자동으로 스프링 빈 주입
public class TokenService {
    private final JwtHandler jwtHandler;

    @Value("${jwt.max-age.access}")
    private long accessTokenMaxAgeSeconds;

    @Value("${jwt.max-age.refresh}")
    private long refreshTokenMaxAgeSeconds;

    @Value("${jwt.key.access}")
    private String accessKey;

    @Value("${jwt.key.refresh}")
    private String refreshKey;


    public String createAccessToken(String subject){
        return jwtHandler.createToken(accessKey, subject, accessTokenMaxAgeSeconds);
    }
    public String createRefreshToken(String subject){
        return jwtHandler.createToken(refreshKey, subject, refreshTokenMaxAgeSeconds);
    }
}
