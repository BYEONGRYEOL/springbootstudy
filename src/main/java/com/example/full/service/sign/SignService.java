package com.example.full.service.sign;

import com.example.full.dto.sign.SignInRequest;
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
import org.springframework.security.core.token.TokenService;
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
    //private final TokenService tokenService;

    @Transactional
    public void signUp(SignUpRequest req){
        validateSignUpInfo(req);

        memberRepository.save(SignUpRequest.toEntity(req,
                roleRepository.findByRoleType(RoleType.ROLE_NORMAL).orElseThrow(RoleNotFoundException::new),
                passwordEncoder));
    }

    private void validateSignUpInfo(SignUpRequest req){
        // 회원가입시 체크해야하는 상황
        if(memberRepository.existsByEmail(req.getEmail()))
            throw new MemberEmailAlreadyExistsException(req.getEmail());
        if(memberRepository.existsByNickname(req.getNickname()))
            throw new MemberNicknameAlreadyExistsException(req.getNickname());
    }

    private void validatePassword(SignInRequest req, Member member){
        if(!passwordEncoder.matches(req.getPassword(), member.getPassword())){
            throw new LoginFailureException();
        }
    }

    private String createSubject(Member member){
        return String.valueOf(member.getId());
    }
}
