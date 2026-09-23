package com.example.MakaoService.exceptions;

public class InvalidMatchPasswordException extends RuntimeException {
    public InvalidMatchPasswordException(String message) {
        super(message);
    }
}
