package com.project.authapi.authserviceapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {
    @NotEmpty(message = "Username cannot be empty")
    @NotNull(message = "Username cannot be empty")
    @NotBlank(message = "Username cannot be empty")
    @Size(min = 2, max = 20, message = "Username must be at least 2 characters and up to 20")
    private String username;

    @NotEmpty(message = "Password cannot be empty")
    @NotNull(message = "Password cannot be empty")
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 2, max = 20, message = "password must be at least 2 characters and up to 20")
    private String password;
}
