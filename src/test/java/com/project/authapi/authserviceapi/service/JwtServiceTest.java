package com.project.authapi.authserviceapi.service;


import com.project.authapi.authserviceapi.entity.Role;
import com.project.authapi.authserviceapi.entity.User;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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





}
