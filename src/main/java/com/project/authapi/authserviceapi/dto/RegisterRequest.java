package com.project.authapi.authserviceapi.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {

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

    @Email(message = "Email must be in correct format \"example@mail.com\"")
    @NotEmpty(message = "Password cannot be empty")
    @NotNull(message = "Password cannot be empty")
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 5, max = 30, message = "Email must be at least 5 characters long")
    private String email;

}
