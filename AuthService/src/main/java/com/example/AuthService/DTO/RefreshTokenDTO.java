package com.example.AuthService.DTO;

public record RefreshTokenDTO(
    String username,
    String refreshToken
){ }
