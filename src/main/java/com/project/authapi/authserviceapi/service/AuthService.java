package com.project.authapi.authserviceapi.service;

import com.project.authapi.authserviceapi.dto.LoginRequest;
import com.project.authapi.authserviceapi.dto.RegisterRequest;
import com.project.authapi.authserviceapi.entity.Role;
import com.project.authapi.authserviceapi.entity.User;
import com.project.authapi.authserviceapi.exception.EmptyFieldException;
import com.project.authapi.authserviceapi.exception.InvalidCredentialsException;
import com.project.authapi.authserviceapi.exception.UserNotFoundException;
import com.project.authapi.authserviceapi.mapper.UserMapper;
import com.project.authapi.authserviceapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
    // Register and login logic.
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final UserMapper userMapper;

    public void register(RegisterRequest registerRequest) {
        log.info("Registering User: {}", registerRequest.getUsername());
        User user = userMapper.toEntity(registerRequest);

        userRepository.save(user);
        log.info("User {} registered successfully", user.getUsername());
    }

    public String login(LoginRequest request) {
        log.info("Attempting login for user: {}", request.getUsername());
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> {
                    log.warn("User {} not found", request.getUsername());
                    return new UserNotFoundException("User not found");
                });

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.warn("Invalid password for user {}", request.getUsername());
            throw new InvalidCredentialsException("Invalid credentials");
        }

        log.info("User {} logged in successfully", user.getUsername());
        return jwtService.generateToken(user);


    }
}
