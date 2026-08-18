package com.example.AuthService.DTO;

import jakarta.validation.constraints.NotBlank;

public record RegisterRequestDTO(

        @NotBlank(message = "username cant be null")
        String username,

        @NotBlank(message = "password cant be null")
        String password,

        @NotBlank(message = "email cant be null")
        String email
) {
}
