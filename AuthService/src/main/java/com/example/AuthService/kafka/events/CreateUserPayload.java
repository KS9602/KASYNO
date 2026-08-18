package com.example.AuthService.kafka.events;

public record CreateUserPayload(
    Long userId,
    String username,
    String email
) {
}
