package com.example.GameService.exceptions.rules;

public class NotThisPlayerTurnException extends RuntimeException {
    public NotThisPlayerTurnException(String message) {
        super(message);
    }
}
