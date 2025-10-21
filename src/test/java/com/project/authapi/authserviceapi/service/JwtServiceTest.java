package com.project.authapi.authserviceapi.service;


import com.project.authapi.authserviceapi.entity.Role;
import com.project.authapi.authserviceapi.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = JwtService.class, properties = {
        "jwt.secret=test-secret-key-which-is-long-enaugh",
        "jwt.expiration-ms=3600000"})

public class JwtServiceTest {
    @Autowired
    private JwtService jwtService;
    private User testUser;

    @BeforeEach // Building|reseting testUser for tests.
    public void setup() {
        testUser = User.builder()
                .username("TestUser")
                .password("TestPassword")
                .email("TestMail@mail.com")
                .role(Role.USER)
                .enabled(true)
                .build();
    }

    @Test
    void generateToken_shouldReturnToken() {
        String token = jwtService.generateToken(testUser);
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void extractUsername_shouldReturnUsername() {
        String token = jwtService.generateToken(testUser);
        String username = jwtService.extractUsername(token);
        assertNotNull(username);
        assertEquals("TestUser", username);
    }

    @Test
    void isTokenValid_shouldReturnValidToken() {
        String token = jwtService.generateToken(testUser);
        String fakeToken = jwtService.generateToken(testUser) + "fakeToken";
        boolean valid = jwtService.isTokenValid(token, testUser);
        assertTrue(valid);
        boolean invalid = jwtService.isTokenValid(fakeToken, testUser);
        assertFalse(invalid);
    }

    @Test
    void expiredToken_shouldReturnTrue() throws InterruptedException {
        jwtService = new JwtService();
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        ReflectionTestUtils.setField(jwtService, "secretKey", key);
        ReflectionTestUtils.setField(jwtService, "expirationMs", 10L);

        String token = jwtService.generateToken(testUser);
        Thread.sleep(15);
        assertTrue(jwtService.isTokenExpired(token));
    }





}
