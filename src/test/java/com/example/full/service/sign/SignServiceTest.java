package com.example.full.service.sign;

import com.example.full.dto.sign.SignInRequest;
import com.example.full.dto.sign.SignInResponse;
import com.example.full.dto.sign.SignUpRequest;
import com.example.full.entity.member.Member;
import com.example.full.entity.member.Role;
import com.example.full.entity.member.RoleType;
import com.example.full.exception.RoleNotFoundException;
import com.example.full.repository.member.MemberRepository;
import com.example.full.repository.member.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.*;


// Mockito와 JUnit5를 함꼐 사용할 수 있도록 함
@ExtendWith(MockitoExtension.class) 
class SignServiceTest {

    @InjectMocks SignService signService;
    @Mock MemberRepository memberRepository;
    @Mock RoleRepository roleRepository;
    @Mock PasswordEncoder passwordEncoder;
    @Mock TokenService tokenService;
    @Test
    void signUpTest() {
        // 회원가입 요청이 잘 진행되는지 확인한다.
        // 요청 온 SignUpRequest가 유효한지 먼저 검증하기위해
        // validateSignUpInfo함수가 사용된다.
        // SignUpRequest DTO객체에서 Member Entity로 변환하는 과정에서
        // 1. roleRepository.findByRoleType 메서드가 실행된다. ->현재 db에서 Role을 가져올수 없음 -> return 결정
        // 2. encoder.encode(req.password) 메서드가 실행된다. -> 실행 여부 결정
        // 3. memberRepositiory.save 메서드가 실행된다. -> 실행 여부 검증

        //given
        SignUpRequest req = new SignUpRequest("email", "password", "username", "nickname");
        given(roleRepository.findByRoleType(RoleType.ROLE_NORMAL)).willReturn(Optional.of(new Role(RoleType.ROLE_NORMAL)));

        //when
        signService.signUp(req);
        //then
        verify(passwordEncoder).encode(req.getPassword());
        verify(memberRepository).save(any());
    }

    @Test
    void signInTest() {
        // memberRepository.findByEmail(req.getEmail()) -> 리턴값 결정
        // validatePassword 내부 로직 passwordEncoder.matches(요청password, db password)-> 리턴 true
        // tokenService.createAccessToken(subject) -> 리턴값 결정
        // tokenService.createRefreshToken(subject) -> 리턴값 결정
        // String subject = createSubject(member); -> ???

        //given
        SignInRequest req = new SignInRequest("email", "password");
        given(memberRepository.findByEmail(req.getEmail())).willReturn(Optional.of(new Member("email", "password", "username", "nickname", emptyList())));
        given(passwordEncoder.matches(anyString(),anyString())).willReturn(true);
        given(tokenService.createAccessToken(anyString())).willReturn("access");
        given(tokenService.createRefreshToken(anyString())).willReturn("refresh");

        //when
        SignInResponse res = signService.signIn(req);

        //then

        assertThat(res.getAccessToken()).isEqualTo("access");
        assertThat(res.getRefreshToken()).isEqualTo("refresh");

    }


}