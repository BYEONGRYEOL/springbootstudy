package com.example.full.handler;

import org.junit.jupiter.api.Test;

import java.util.Base64;
import static org.assertj.core.api.Assertions.assertThat;

public class JwtHandlerTest {
    JwtHandler jwtHandler = new JwtHandler();


    // 토큰이 잘 만들어졌는가?
    // 토큰에서 Subject를 추출 가능?
    // 내가 만든 validate함수가 유효한가?
    // 유효기간이 지난것을 확인할수있는가
    @Test
    void createTokenTest() {
        //given
        //when
        String encodedKey = Base64.getEncoder().encodeToString("myKey".getBytes());
        String token = createToken(encodedKey, "testSubject", 1000L);
        //then
        assertThat(token).contains("sbl");
    }

    @Test
    void extractSubjectTest() {
        //given
        String encodedKey = Base64.getEncoder().encodeToString("myKey".getBytes());
        String subject = "testSubject";
        String token = createToken(encodedKey, subject, 60L);
        //when
        String extractedSubject = jwtHandler.extractSubject(encodedKey, token);
        //then
        assertThat(extractedSubject).isEqualTo(subject);
    }

    @Test
    void validateTest() {
        //given
        String encodedKey = Base64.getEncoder().encodeToString("myKey".getBytes());
        String token = createToken(encodedKey,"testSubject", 60L );

        //when
        //then
        assertThat(jwtHandler.validate(encodedKey, token)).isTrue();
    }

    @Test
    void invalidateByInvalidKeyTest() {
        //given
        String encodedKey = Base64.getEncoder().encodeToString("myKey".getBytes());
        String invalidKey = "invalidKey";
        String token = createToken(encodedKey, "testSubject", 60L);
        //when
        //then
        assertThat(jwtHandler.validate(invalidKey, token)).isFalse();
    }

    @Test
    void invalidateByExpiredTest() {
        //given
        String encodedKey = Base64.getEncoder().encodeToString("myKey".getBytes());
        long maxAgeZero = 0L;
        String token = createToken(encodedKey, "testSubject", maxAgeZero);
        //when
        //then
        assertThat(jwtHandler.validate(encodedKey, token)).isFalse();
    }
    private String createToken(String encodedKey, String subject, long maxAgeSeconds){
        return jwtHandler.createToken( // test에 사용할 Token 생성
                encodedKey,
                subject,
                maxAgeSeconds
        );
    }
}
