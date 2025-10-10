package com.project.authapi.authserviceapi.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class TestController {
    @GetMapping("/public")
    public String publicEndpoint() {
        return "✅ Public endpoint - available without login";
    }

    @GetMapping("/private")
    public String privateEndpoint() {
        return "\uD83D\uDD12 Private endpoint - available with login";
    }
}
