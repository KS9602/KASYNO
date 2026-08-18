package com.example.AuthService.DTO;

public record AccessTokenDTO(
    String username,
    String refreshToken
){ }
