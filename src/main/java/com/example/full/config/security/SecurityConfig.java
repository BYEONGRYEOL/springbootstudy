package com.example.full.config.security;

import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/예외처리url");
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request -> request
                // 웹보안 서블릿? 이 다른곳으로 요청을 보낼거라서 Forward인가?
                        .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                // 로그인 페이지든 어디든 인가되지않았다고해서 로그인 화면에 있는 사진을 불러오지못하는건 말이 안된다.
                        .requestMatchers("/resources").permitAll()
                //모르겠음
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        //로그인 성공시 도착 url
                        .defaultSuccessUrl("/src/main/reactfront/public/index.html", true)
                        .permitAll()
                )
                .logout(Customizer.withDefaults());

        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        // PasswordEncoder의 구현체는 Pbkdf2 deprecated되지 않은것으로 골랐다
        PasswordEncoder passwordEncoder = Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8();

        return passwordEncoder;
    }

}
