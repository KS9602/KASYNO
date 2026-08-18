package com.example.GameService.exceptions;

public class DeckNotFoundException extends Exception{
    public DeckNotFoundException(String message){
        super(message);
    }
}
