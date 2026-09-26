package com.example.GameService.config.security;

public record AuthenticatedUser(
        Long playerId,
        String username
) {
}
