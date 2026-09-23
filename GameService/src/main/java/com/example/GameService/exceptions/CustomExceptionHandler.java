package com.example.GameService.exceptions;

import com.example.GameService.exceptions.game.GameNotFoundException;
import com.example.GameService.exceptions.room.RoomNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(RoomNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMatchNotFound(RoomNotFoundException ex) {
        return notFound("MATCH_NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(GameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleGameNotFound(GameNotFoundException ex) {
        return notFound("GAME_NOT_FOUND", ex.getMessage());
    }

    private ResponseEntity<ErrorResponse> notFound(String code, String message) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(), code, message));
    }

    private ResponseEntity<ErrorResponse> badRequest(String code, String message) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), code, message));
    }
}
