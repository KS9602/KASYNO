package com.example.MakaoService.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(MatchNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMatchNotFound(MatchNotFoundException ex) {
        return notFound("MATCH_NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(InvalidMatchPasswordException.class)
    public ResponseEntity<ErrorResponse> handleInvalidMatchPassword(InvalidMatchPasswordException ex) {
        return badRequest("INVALID_MATCH_PASSWORD", ex.getMessage());
    }

    @ExceptionHandler(PlayerAlreadyInMatchException.class)
    public ResponseEntity<ErrorResponse> handlePlayerAlreadyInMatch(PlayerAlreadyInMatchException ex) {
        return badRequest("PLAYER_ALREADY_IN_MATCH", ex.getMessage());
    }

    @ExceptionHandler(IllegalActionException.class)
    public ResponseEntity<ErrorResponse> handleIllegalAction(IllegalActionException ex) {
        return badRequest("ILLEGAL_ACTION", ex.getMessage());
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ErrorResponse> handleOptimisticLock(ObjectOptimisticLockingFailureException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(
                        HttpStatus.CONFLICT.value(),
                        "CONCURRENT_UPDATE",
                        "Stan gry zmienił się w międzyczasie, spróbuj ponownie"
                ));
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
