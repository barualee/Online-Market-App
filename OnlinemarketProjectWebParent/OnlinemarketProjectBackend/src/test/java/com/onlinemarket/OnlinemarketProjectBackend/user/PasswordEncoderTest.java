package com.onlinemarket.OnlinemarketProjectBackend.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderTest {
    @Test
    public void testEncodePassword() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String rawPass = "lee7991";
        String encoded = passwordEncoder.encode(rawPass);
        System.out.println(encoded);

        boolean matches = passwordEncoder.matches(rawPass, encoded);
        assertThat(matches).isTrue();
    }
}
