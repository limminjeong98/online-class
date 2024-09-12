package com.example.userservice.domain.user.service;

import com.example.userservice.domain.user.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JWTServiceTest {

    private final String secretKey = "1ad35d74dc65cc0ee9a3a409f7f87e332aa6aa89d9cc64c66cdf9a1ffdc534ee";

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private JWTService jwtService;

    @Test
    void generateToken_ValidCredentials_ReturnsToken() {
        // given
        User user = new User();
        user.setEmail("user@example.com");
        user.setPasswordHash("hashedPassword");

        // when
        when(passwordEncoder.matches("validPassword", user.getPasswordHash())).thenReturn(true);
        ReflectionTestUtils.setField(jwtService, "secretKey", secretKey);
        String token = jwtService.generateToken(user, "validPassword");

        // then
        assertNotNull(token);
        // argument matcher 인자 테스트
        verify(passwordEncoder).matches(anyString(), anyString());
        // raw 인자 테스트
        verify(passwordEncoder).matches("validPassword", "hashedPassword");
        Claims claims = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(token)
                .getPayload();
        assertEquals(user.getEmail(), claims.getSubject());
    }

    @Test
    void generateToken_InvalidCredentials_ThrowsException() {
        // given
        User user = new User();
        user.setEmail("user@example.com");
        user.setPasswordHash("hashedPassword");

        // when
        when(passwordEncoder.matches("invalidPassword", user.getPasswordHash())).thenReturn(false);

        // then
        assertThrows(IllegalArgumentException.class, () -> jwtService.generateToken(user, "invalidPassword"));
    }

    @Test
    void validateToken_ValidToken_ReturnsTrue() {
        // given & when
        String subject = "user@example.com";
        long currentTimeMillis = System.currentTimeMillis();

        ReflectionTestUtils.setField(jwtService, "secretKey", secretKey);

        String token = Jwts.builder()
                .subject(subject)
                .issuedAt(new Date(currentTimeMillis))
                .expiration(new Date(currentTimeMillis + 3600000))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                .compact();

        // then
        assertTrue(jwtService.validateToken(token));
    }

    @Test
    void validateToken_InvalidToken_ReturnsFalse() {
        // given & when
        String token = "invalidToken";

        // then
        assertFalse(jwtService.validateToken(token));
    }
}
