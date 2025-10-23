package com.project.authapi.authserviceapi.service;

import com.project.authapi.authserviceapi.dto.LoginRequest;
import com.project.authapi.authserviceapi.dto.RegisterRequest;
import com.project.authapi.authserviceapi.entity.Role;
import com.project.authapi.authserviceapi.entity.User;
import com.project.authapi.authserviceapi.exception.EmptyFieldException;
import com.project.authapi.authserviceapi.exception.InvalidCredentialsException;
import com.project.authapi.authserviceapi.exception.UserNotFoundException;
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

    public void register(RegisterRequest registerRequest) {
        log.info("Registering User: {}", registerRequest.getUsername());
        User user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.USER)
                .enabled(true).build();

        userRepository.save(user);
        log.info("User {} registered successfully", user.getUsername());
    }

    public String login(LoginRequest loginRequest) {
        log.info("Login User: {}", loginRequest.getUsername());
        UserDetails userDetails = customUserDetailsService
                .loadUserByUsername(loginRequest.getUsername());


        String username = userDetails.getUsername();
        String password = userDetails.getPassword();

        if (userRepository.findByUsername(username).isPresent() || userRepository.existsByUsername(username)) {
            if (!passwordEncoder.matches(loginRequest.getPassword(),  password)) {
                log.warn("User {} password incorrect", loginRequest.getUsername());
                throw new InvalidCredentialsException("Password is incorrect");
            } else {

                log.info("User {} logged in successfully", loginRequest.getUsername());
                return jwtService.generateToken(userDetails);
            }

        } else {
            log.info("User {} does not exist", loginRequest.getUsername());
            throw new UserNotFoundException("User not found");
        }

    }
}
