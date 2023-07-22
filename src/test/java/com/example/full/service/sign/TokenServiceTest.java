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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {
    // 테스트하고 싶은 클래스는 TokenService 서비스 로직으로 JwtHandler는 따로 테스트가 완료 된 상태,
    // 그러나 TokenService는 JwtHandler에 의존성을 갖고있다.
    // AOP 관점에서 TokenService 테스트에 초점을 맞추기 위하여 Mockito 라이브러리 활용
    @InjectMocks TokenService tokenService; // 테스트하고 싶은 관점의 주인공, 의존성 주입이 필요함에 따라 @InjectMocks 어노테이션 활용
    @Mock JwtHandler jwtHandler;



    // 목업 테스트 시작전 준비
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
        // 테스트 도중 이 메서드가 사용되면 이 리턴값이 나오실게요~
        given(jwtHandler.createToken(anyString(), anyString(), anyLong())).willReturn("access");
        String subject = "testSubject";
        //테스트하고싶은 토큰서비스 목객체의 메서드는 createAccessToken이다.
        //토큰서비스를 테스트하고싶은거니까 jwtHandler에 영향을받으면 안된다.
        //-> tokenService.createAccessToken 메서드를 호출하고 나서
        // 호출될 주입된 목객체 jwtHandler의 메서드 (**현재는 jwtHandler.createToken**)
        //의 반환값을 미리 지정해준다.

        //when
        String token = tokenService.createAccessToken(subject);
        //then
        assertThat(token).isEqualTo("access");
        //테스트 동안 jwtHandler 객체의 createToken 메서드가 사용되었는지 체크할게
        verify(jwtHandler).createToken(anyString(), anyString(), anyLong());
    }
    @Test
    void createRefreshTokenTest(){
        //given
        given(jwtHandler.createToken(anyString(), anyString(), anyLong())).willReturn("refresh");

        //when
        String token = tokenService.createRefreshToken("subject");
        //then

        assertThat(token).isEqualTo("refresh");
        verify(jwtHandler).createToken(anyString(), anyString(), anyLong());

    }
}
