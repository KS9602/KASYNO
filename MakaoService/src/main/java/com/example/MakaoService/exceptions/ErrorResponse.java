package com.example.MakaoService.exceptions;

public record ErrorResponse(
        int status,
        String error,
        String message
) {}
