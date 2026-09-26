package com.example.GameService.exceptions.room;

public class InvalidRoomPasswordException extends RuntimeException {
    public InvalidRoomPasswordException(String message) {
        super(message);
    }
}
