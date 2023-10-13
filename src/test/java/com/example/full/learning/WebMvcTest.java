package com.example.full.learning;

import com.example.full.controller.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.stereotype.Controller;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class WebMvcTest {
    @InjectMocks TestController testController;
    MockMvc mockMvc; // controller에 요청을 보내기 위함

    @Controller
    public static class TestController{
        @GetMapping("/test/ignore-null-value")
        public Response ignoreNullValueTest(){
            return Response.success(); // 아까 정의한 null value를 포함한 Resoponse 반환
        }
    }
    @BeforeEach
    void beforeEach(){
        mockMvc = MockMvcBuilders.standaloneSetup(testController).build();
    }

    @Test
    void ignoreNullValueJsonResponseTest() throws Exception{
        mockMvc.perform(
                get("/test/ignore-null-value"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").doesNotExist());
    }
}
