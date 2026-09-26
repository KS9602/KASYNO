package com.example.MakaoService.exceptions;

public class PlayerAlreadyInMatchException extends RuntimeException {
    public PlayerAlreadyInMatchException(String message) {
        super(message);
    }
}
