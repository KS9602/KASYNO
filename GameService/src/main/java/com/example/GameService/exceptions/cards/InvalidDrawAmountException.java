package com.example.GameService.exceptions.cards;

public class InvalidDrawAmountException extends RuntimeException {
    public InvalidDrawAmountException(String message) {
        super(message);
    }
}
