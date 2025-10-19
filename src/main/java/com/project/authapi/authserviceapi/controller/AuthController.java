package com.project.authapi.authserviceapi.controller;

import com.project.authapi.authserviceapi.dto.LoginRequest;
import com.project.authapi.authserviceapi.dto.RegisterRequest;
import com.project.authapi.authserviceapi.service.AuthService;
import com.project.authapi.authserviceapi.service.CustomUserDetailsService;
import com.project.authapi.authserviceapi.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

// REST API
// endpoints: /login, /register, /refresh itd.
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {
    private final AuthService authService;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        authService.register(registerRequest);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        String token = authService.login(loginRequest);

        ResponseCookie jwtCookie = ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .path("/")
                .maxAge(24 * 60 * 60)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(Map.of("message", "User logged in successfully"));
    }

    @GetMapping("/secure")
    public ResponseEntity<String> secureEndpoint(Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok("Zalogowano jako: " + username);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        return ResponseEntity.ok(authentication.getPrincipal());
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@CookieValue(value = "jwt", required = false) String jwtCookie,
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        String token = null;

        if (jwtCookie != null && !jwtCookie.isEmpty()) {
            token = jwtCookie;
        } else if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        } else {
            return ResponseEntity.badRequest().body("Missing token");
        }

        String username = jwtService.extractUsername(token);
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        if (!jwtService.isTokenValid(token, userDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }

        String newToken = jwtService.generateToken(userDetails);

        ResponseCookie newJwtCookie = ResponseCookie.from("jwt", newToken)
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(24 * 60 * 60)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, newJwtCookie.toString())
                .body(Map.of("message", "Token refreshed successfully"));
    }


    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> adminOnly() {
        return ResponseEntity.ok("Welcome, admin!");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        ResponseCookie deleteCookie = ResponseCookie.from("jwt", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .body(Map.of("message", "Logged out successfully"));
    }


}
