package com.betacom.security;

import com.betacom.service.JwtService;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    private final String username = "jwtUser";

    @Test
    void shouldGenerateTokenSuccessfully() {
        String token = jwtService.generateToken(username);
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void shouldExtractUsernameCorrectly() {
        String token = jwtService.generateToken(username);
        String extractedUsername = jwtService.extractUsername(token);
        assertEquals(username, extractedUsername);
    }

    @Test
    void shouldRecognizeValidToken() {
        String token = jwtService.generateToken(username);
        boolean valid = jwtService.isValid(token, username);
        assertTrue(valid);
    }

    @Test
    void shouldThrowExceptionForInvalidToken() {
        String invalidToken = "invalid_token";
        assertThrows(JwtException.class, () -> jwtService.extractUsername(invalidToken));
    }
}
