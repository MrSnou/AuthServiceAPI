package com.project.authapi.authserviceapi.controller;


import com.project.authapi.authserviceapi.exception.EmptyFieldException;
import com.project.authapi.authserviceapi.exception.InvalidCredentialsException;
import com.project.authapi.authserviceapi.exception.UserNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/public")
    public String publicEndpoint() {
        return "✅ Public endpoint - available without login";
    }

    @GetMapping("/private")
    public String privateEndpoint() {
        return "\uD83D\uDD12 Private endpoint - available with login";
    }
    @GetMapping("/user-not-found")
    public void userNotFound() {
        throw new UserNotFoundException("User not found");
    }

    @GetMapping("/invalid-credentials")
    public void invalidCredentials() {
        throw new InvalidCredentialsException("Invalid credentials");
    }

    @GetMapping("/empty-field")
    public void emptyField() {
        throw new EmptyFieldException("Field cannot be empty");
    }

    @GetMapping("/unexpected")
    public void unexpected() {
        throw new RuntimeException("Something went wrong");
    }
}

