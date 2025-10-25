package com.project.authapi.authserviceapi.service;

import com.project.authapi.authserviceapi.dto.LoginRequest;
import com.project.authapi.authserviceapi.entity.Role;
import com.project.authapi.authserviceapi.entity.User;
import com.project.authapi.authserviceapi.exception.UserNotFoundException;
import com.project.authapi.authserviceapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AuthServiceTest {
    @Autowired
    private AuthService authService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void registerTest_shouldReturnCorrectUser() {
        // Test User DATA
        String TestUsername = "TestUsername";
        String testEmail = "TestEmail@mail.com";
        String testPassword = "TestPassword";

        User testUser = User.builder()
                .username(TestUsername)
                .email(testEmail)
                .password(testPassword)
                .role(Role.USER)
                .enabled(true).build();
        userRepository.save(testUser);

        User retrievedUser = userRepository.findByUsername(TestUsername).get();

        assertEquals(retrievedUser.getUsername(), testUser.getUsername());
        assertEquals(retrievedUser.getEmail(), testUser.getEmail());
        assertEquals(retrievedUser.getPassword(), testUser.getPassword());
        assertEquals(retrievedUser.getRole(), testUser.getRole());
        assertNotEquals(retrievedUser.getUsername(), "RandomName");
        assertNotEquals(retrievedUser.getEmail(), "RandomEmail");
        assertNotEquals(retrievedUser.getPassword(), "RandomPassword");
        assertNotEquals(retrievedUser.getRole(), Role.ADMIN);
    }

    @Test
    void loginTest_shouldReturnCorrectUserDetails() {
        String TestUsername = "TestUsername";
        String testEmail = "TestEmail@mail.com";
        String testPassword = "TestPassword";

        User testUser = User.builder()
                .username(TestUsername)
                .email(testEmail)
                .password(passwordEncoder.encode(testPassword))
                .role(Role.USER)
                .enabled(true).build();
        userRepository.save(testUser);

        LoginRequest testLoginRequest = new LoginRequest();
        testLoginRequest.setUsername(TestUsername);
        testLoginRequest.setPassword(testPassword);

        String testReceivedToken = authService.login(testLoginRequest);
        assertNotNull(testReceivedToken);

        User user = userRepository.findByUsername("TestUsername").get();
        assertTrue(jwtService.isTokenValid(testReceivedToken, user));

        LoginRequest empty = new LoginRequest();
        assertThrows(UserNotFoundException.class, () -> authService.login(empty));

        LoginRequest wrong = new LoginRequest();
        wrong.setUsername("WrongUsername");
        wrong.setPassword("WrongPassword");
        assertThrows(UserNotFoundException.class, () -> authService.login(wrong));
    }
}
