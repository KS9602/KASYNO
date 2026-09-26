package com.example.GameService.exceptions.room;

public class NotEnoughPlayersException extends RuntimeException {
    public NotEnoughPlayersException(String message) {
        super(message);
    }
}
