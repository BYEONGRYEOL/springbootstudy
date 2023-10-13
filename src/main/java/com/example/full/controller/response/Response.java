package com.example.full.controller.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;


@JsonInclude(JsonInclude.Include.NON_NULL) // result가 null인경우 결과에서 빼기
@AllArgsConstructor(access = AccessLevel.PRIVATE) //Entity처럼 생성하는게 아니라 스태틱 팩터리 메서드 패턴으로 인스턴스를 생성하므로 private
@Getter
public class Response {
    // 컨트롤러 계층에서 응답을 위해 사용될 스태틱 팩토리 메서드
    // 스태틱 팩토리 메서드란? ,
    // 성공 여부와 결과, 등을 응답
    private boolean success;
    private int code;
    private Result result;
    public static Response success(){
        return new Response(true, 0, null);
    }
    public static <T> Response success(T data){
        return new Response(true, 0, new Success<>(data));
    }
    public static Response failure(int code, String msg){
        return new Response(false, code, new Failure(msg));
    }

}
