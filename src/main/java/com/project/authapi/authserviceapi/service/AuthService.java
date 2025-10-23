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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
    // Register and login logic.
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    public void register(RegisterRequest registerRequest) {
        if (registerRequest.getUsername() == null || registerRequest.getPassword() == null || registerRequest.getEmail() == null ||
        registerRequest.getPassword().isEmpty() || registerRequest.getEmail().isEmpty() || registerRequest.getPassword().isEmpty()
        || registerRequest.getUsername().length() < 6 || registerRequest.getEmail().length() < 6 || registerRequest.getPassword().length() < 6) {
            throw new EmptyFieldException("Username, email address or password is empty");
        }

        User user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.USER)
                .enabled(true).build();

        userRepository.save(user);
    }

    public String login(LoginRequest loginRequest) {
        if (loginRequest.getUsername().isEmpty() || loginRequest.getPassword().isEmpty()) {
            throw new EmptyFieldException("Username or password is empty");
        }

        UserDetails userDetails = customUserDetailsService
                .loadUserByUsername(loginRequest.getUsername());


        String username = userDetails.getUsername();
        String password = userDetails.getPassword();

        if (userRepository.findByUsername(username).isPresent() || userRepository.existsByUsername(username)) {
            if (!passwordEncoder.matches(loginRequest.getPassword(),  password)) {
                throw new InvalidCredentialsException("Password is incorrect");
            } else {

                return jwtService.generateToken(userDetails);
            }

        } else {
            throw new UserNotFoundException("User not found");
        }

    }
}
