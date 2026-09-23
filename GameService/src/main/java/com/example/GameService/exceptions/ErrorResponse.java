package com.example.GameService.exceptions;

public record ErrorResponse(
        int status,
        String error,
        String message
) {}