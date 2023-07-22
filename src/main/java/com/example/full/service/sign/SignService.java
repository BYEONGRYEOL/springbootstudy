package com.example.full.service.sign;

import com.example.full.dto.sign.SignInRequest;
import com.example.full.dto.sign.SignInResponse;
import com.example.full.dto.sign.SignUpRequest;
import com.example.full.entity.member.Member;
import com.example.full.entity.member.RoleType;
import com.example.full.exception.LoginFailureException;
import com.example.full.exception.MemberEmailAlreadyExistsException;
import com.example.full.exception.MemberNicknameAlreadyExistsException;
import com.example.full.exception.RoleNotFoundException;
import com.example.full.repository.member.MemberRepository;
import com.example.full.repository.member.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) //
public class SignService {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;


    @Transactional // save 사용되므로
    public void signUp(SignUpRequest req){
        // 전달받은 회원가입 정보가 유효한지 검사
        validateSignUpInfo(req);

        memberRepository.save(SignUpRequest.toEntity(req,
                roleRepository.findByRoleType(RoleType.ROLE_NORMAL).orElseThrow(RoleNotFoundException::new),
                passwordEncoder));
    }

    // 조회만 하므로 Transactional 필요 x
    public SignInResponse signIn(SignInRequest req){
        // 로그인 요청받은 멤버를 DB에서 조회
        Member member = memberRepository.findByEmail(req.getEmail()).orElseThrow(LoginFailureException::new);
        // 입력받은 암호화된 password가 db에 저장된 password와 같은지 검사 (유효성 검사)
        validatePassword(req, member);
        // 토큰의 subject
        String subject = createSubject(member);
        String accessToken = tokenService.createAccessToken(subject);
        String refreshToken = tokenService.createRefreshToken(subject);
        return new SignInResponse(accessToken, refreshToken);

    }



    private void validateSignUpInfo(SignUpRequest req){
        // 회원가입시 체크해야하는 상황
        // 이메일 중복 x, 닉네임 중복 x
        if(memberRepository.existsByEmail(req.getEmail()))
            throw new MemberEmailAlreadyExistsException(req.getEmail());
        if(memberRepository.existsByNickname(req.getNickname()))
            throw new MemberNicknameAlreadyExistsException(req.getNickname());
    }

    // 입력된 비밀번호 유효성 검증
    private void validatePassword(SignInRequest req, Member member){
        if(!passwordEncoder.matches(req.getPassword(), member.getPassword())){
            throw new LoginFailureException();
        }
    }

    private String createSubject(Member member){
        return String.valueOf(member.getId());
    }
}
