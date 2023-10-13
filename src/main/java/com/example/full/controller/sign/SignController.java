package com.example.full.controller.sign;

import com.example.full.controller.response.Response;
import com.example.full.dto.sign.SignInRequest;
import com.example.full.dto.sign.SignUpRequest;
import com.example.full.service.sign.SignService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static com.example.full.controller.response.Response.success;

@RestController // json 응답을 위해 Controller 대신 RestController
@RequiredArgsConstructor
public class SignController {
    private final SignService signService;


    //어떤 요청에 대해서 어떤 데이터를 응답해야하는가에 대한 3가지 선택지
//    생성 요청에 대한 응답 결과가 굳이 필요하지 않다면, 처리 결과에 대한 상태 코드 정도만 응답해주고,
//
//    응답 결과가 필요하다면, 상황에 맞게 생성과 조회를 같이 수행하거나 꼭 필요한 데이터만 내려주는 방법, 또는 그냥 새롭게 다시 요청하는 방법이 있겠습니다. 캐시를 이용하면서 다시 요청하더라도 전달되는 데이터를 최소화하는 방법도 있겠고요.
//
//    결국 각자의 선택인 것 같습니다.
    @PostMapping("/api/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    // @ RequestBody 요청으로 전달받는 JSON을 객체로 변환하기 위해
    //@Valid는 @Request객체의 필드값을 검증하기 위해
    public Response signUp(@Valid @RequestBody SignUpRequest req){ // 회원가입시 201
        signService.signUp(req); // 회원 가입 처리
        return success(); // 성공 응답
    }

    @PostMapping("/api/sign-in")
    @ResponseStatus(HttpStatus.OK)
    public Response signIn(@Valid @RequestBody SignInRequest req){ //로그인 시 토큰을 응답
        return success(signService.signIn(req)); // 데이터와 함께 성공 응답(JWT토큰)
    }


}
