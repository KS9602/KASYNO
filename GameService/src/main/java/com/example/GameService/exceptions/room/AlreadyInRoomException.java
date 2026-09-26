package com.example.GameService.exceptions.room;

public class AlreadyInRoomException extends RuntimeException {
    public AlreadyInRoomException(String message) {
        super(message);
    }
}
