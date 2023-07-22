package com.example.full.handler;

import io.jsonwebtoken.*;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class JwtHandler {

    private String type = "sbl";
    public String createToken(String encodedKey, String subject, long maxAgeSeconds){
        Date now = new Date();
        return type + Jwts.builder() // jwt 빌드
                .setSubject(subject) // sub 토큰 제목, unique 한 값을 사용해야하며 사용자의 이메일을 주로 사용한다.
                .setIssuedAt(now) // iat 토큰 발급 시간
                .setExpiration(new Date(now.getTime() + maxAgeSeconds * 1000L)) // exp 토큰 만료시간
                .signWith(SignatureAlgorithm.HS256, encodedKey) // SHA-256 알고리즘으로 key를 암호화하여 서명
                .compact(); // 토큰 생성
    }

    public String extractSubject(String encodedKey, String token){
        return parse(encodedKey, token) // token 파싱
                .getBody() // body 에서
                .getSubject(); //Subject get
    }
    private Jws<Claims> parse(String key, String token){
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(untype(token)); // type을 제거한 token의 내용만 남은걸로
        //parseclaimsJws 토큰을 jws로 파싱한다.
    }

    private String untype(String token){
        return token.substring(type.length()); // token에서 일전에 붙여놨던 type 문자열 제거
    }

    public boolean validate(String encodedKey, String token){
        try{
            parse(encodedKey, token); // parse 안에서
            return true;
        } catch(JwtException e){
            System.out.println(e.toString());
            return false;
        }
    }
}
