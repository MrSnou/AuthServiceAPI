package com.project.authapi.authserviceapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.authapi.authserviceapi.dto.LoginRequest;
import com.project.authapi.authserviceapi.dto.RegisterRequest;
import com.project.authapi.authserviceapi.exception.EmptyFieldException;
import com.project.authapi.authserviceapi.service.AuthService;
import com.project.authapi.authserviceapi.service.CustomUserDetailsService;
import com.project.authapi.authserviceapi.service.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private AuthService authService;
    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;
    @MockitoBean
    private JwtService jwtService;



    @Test
    void register_shouldReturn200AndMessage() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("TestUser");
        request.setEmail("test@mail.com");
        request.setPassword("TestPassword");

        doNothing().when(authService).register(any(RegisterRequest.class));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully"));

        verify(authService, times(1)).register(any(RegisterRequest.class));
    }

    @Test
    void registerWithEmptyData_shouldReturnException() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("");
        request.setEmail("");
        request.setPassword("");

        doThrow(new EmptyFieldException("Username, email address or password is empty"))
                .when(authService).register(any(RegisterRequest.class));

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Username, email address or password is empty"));

        verify(authService, times(1)).register(any(RegisterRequest.class));
    }


    @Test
    void login_shouldReturn200AndCookieAndMessage() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("TestUser");
        request.setPassword("TestPassword");

        when(authService.login(any(LoginRequest.class))).thenReturn("jwt-token-value");

        mockMvc.perform(post("/api/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(header().exists("Set-Cookie"))
                .andExpect(jsonPath("$.message").value("User logged in successfully"));


    }

    @Test
    void login_shouldReturn401ForInvalidCredentials() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("WrongUser");
        request.setPassword("BadPassword");

        when(authService.login(any(LoginRequest.class)))
                .thenThrow(new com.project.authapi.authserviceapi.exception.InvalidCredentialsException("Password is incorrect"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Password is incorrect"));
    }

    @Test
    @WithMockUser(username = "TestUser")
    void logout_shouldReturn200AndDeleteCookie() throws Exception {
        
        mockMvc.perform(post("/api/auth/logout"))
                .andExpect(status().isOk())
                .andExpect(header().string("Set-Cookie", org.hamcrest.Matchers.containsString("Max-Age=0")))
                .andExpect(jsonPath("$.message").value("Logged out successfully"));
    }



}

