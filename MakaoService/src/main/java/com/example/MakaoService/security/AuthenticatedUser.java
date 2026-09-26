package com.example.MakaoService.security;

public record AuthenticatedUser(
        Long userId,
        String username
) {
}
