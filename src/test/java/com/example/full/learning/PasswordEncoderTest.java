package com.example.full.learning;


import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import static org.assertj.core.api.Assertions.*;

public class PasswordEncoderTest {

    // 1
    PasswordEncoder passwordEncoder = Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8();


    @Test
    void encodeWithMD5Test() { // 2
        // given
        String password = "password123";

        // when
        String encodedPassword = passwordEncoder.encode(password);

        // then
        assertThat(encodedPassword.contains("password")).isFalse();
    }

    @Test
    void matchTest() { // 3
        // given
        String password = "password";
        String encodedPassword = passwordEncoder.encode(password);

        // when
        boolean isMatch = passwordEncoder.matches(password, encodedPassword);

        // then
        assertThat(isMatch).isTrue();
    }
}
