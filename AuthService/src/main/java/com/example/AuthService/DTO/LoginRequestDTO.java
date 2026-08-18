package com.example.AuthService.DTO;

import jakarta.validation.constraints.NotBlank;



public record LoginRequestDTO(

        @NotBlank(message = "Username cant be null")
        String username,
        @NotBlank(message = "Password cant be null")
        String password
) {
}
