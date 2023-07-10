package com.example.full.service.sign;


import com.example.full.handler.JwtHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {
    // 테스트하고 싶은 클래스는 TokenService 서비스 로직으로 JwtHandler는 따로 테스트가 완료 된 상태,
    // AOP 관점에서 TokenService 테스트에 초점을 맞추기 위하여 Mockito 라이브러리 활용
    @InjectMocks TokenService tokenService;
    @Mock JwtHandler jwtHandler;



    @BeforeEach //가짜로 생성한 객체니까 실제 서비스중에는 알아서 값이 적용되었을 필드들이지만 Mock객체니까 넣어줘야한다.
    void beforeEach(){
        ReflectionTestUtils.setField(tokenService, "accessTokenMaxAgeSeconds", 10L);
        ReflectionTestUtils.setField(tokenService, "refreshTokenMaxAgeSeconds", 10L);
        ReflectionTestUtils.setField(tokenService, "accessKey", "accessKey");
        ReflectionTestUtils.setField(tokenService, "refreshKey", "refreshKey");
    }

    @Test
    void createAccessTokenTest(){
        //given
        //when
        //then
    }
    @Test
    void createRefreshTokenTest(){
        //given
        
        //when

        //then
        
    }
}
