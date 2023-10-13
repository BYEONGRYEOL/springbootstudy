package com.example.full.controller.sign;

import com.example.full.dto.sign.SignInRequest;
import com.example.full.dto.sign.SignInResponse;
import com.example.full.dto.sign.SignUpRequest;
import com.example.full.service.sign.SignService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class) // 테스트 클래스를 만들면 첫째로 해야할것
    // 컨트롤러 계층으 ㅣ테스트, 각 url에 요청이 제대로 전달되는지, 의도한 응답을 받을 수 잇는지 검증,
class SignControllerTest {

    @InjectMocks SignController signController; //SignService를 주입받아서 테스트 
    @Mock SignService signService; // SignController가 주입받아야 할 의존성
    MockMvc mockMvc; // 가짜 요청 생성기
    ObjectMapper objectMapper = new ObjectMapper(); // Java Object를 Json으로 직렬화, 역질렬화 수행

    @BeforeEach
    void beforeEach(){
        mockMvc = MockMvcBuilders.standaloneSetup(signController).build();
    }
    @Test
    void signUpTest() throws Exception {
        // 요청이 들어옴
        //given
        // 들어올 요청 객체 준비
        SignUpRequest req = new SignUpRequest("email@email.com", "123gdsfae!@#" , "username", "nickname");
        //when then
        mockMvc.perform( // 사용자가 api 실행한것처럼
                post("/api/sign-up") // 이 url로 post 실행
                        .contentType(MediaType.APPLICATION_JSON) // json으로 
                        .content(objectMapper.writeValueAsString(req))) // java 객체를 json으로 변환
                .andExpect(status().isCreated()); // 만들어졌으면 통과
        verify(signService).signUp(req); // signService의 signUp 메소드가 실행되었는지 검사
    }

    @Test
    void signInTest() throws Exception{
        //given
        // 들어올 요청 객체 준비
        SignInRequest req = new SignInRequest("email@email.com", "asdf123@!#");
        // signService의 signIn 함수가 위에서 만든 req 객체를 매개변수로 실행되면, 로그인 성공 응답을 주도록함
        given(signService.signIn(req)).willReturn(new SignInResponse("access", "refresh"));
        //when //then
        mockMvc.perform(
                post("/api/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result.data.accessToken").value("access"))
                .andExpect(jsonPath("$.result.data.refreshToken").value("refresh"));
        verify(signService).signIn(req);
    }
}