package com.example.GameService.exceptions;

import com.example.GameService.exceptions.cards.InvalidDrawAmountException;
import com.example.GameService.exceptions.game.GameNotFoundException;
import com.example.GameService.exceptions.room.AlreadyInRoomException;
import com.example.GameService.exceptions.room.InvalidRoomPasswordException;
import com.example.GameService.exceptions.room.NotEnoughPlayersException;
import com.example.GameService.exceptions.room.NotOwnerException;
import com.example.GameService.exceptions.room.RoomFullException;
import com.example.GameService.exceptions.room.RoomNotFoundException;
import com.example.GameService.exceptions.room.RoomNotJoinableException;
import com.example.GameService.exceptions.rules.InvalidMakaoCallException;
import com.example.GameService.exceptions.rules.InvalidMoveException;
import com.example.GameService.exceptions.rules.NotThisPlayerTurnException;
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

    @ExceptionHandler(RoomNotJoinableException.class)
    public ResponseEntity<ErrorResponse> handleRoomNotJoinable(RoomNotJoinableException ex) {
        return badRequest("ROOM_NOT_JOINABLE", ex.getMessage());
    }

    @ExceptionHandler(RoomFullException.class)
    public ResponseEntity<ErrorResponse> handleRoomFull(RoomFullException ex) {
        return badRequest("ROOM_FULL", ex.getMessage());
    }

    @ExceptionHandler(InvalidRoomPasswordException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRoomPassword(InvalidRoomPasswordException ex) {
        return badRequest("INVALID_ROOM_PASSWORD", ex.getMessage());
    }

    @ExceptionHandler(AlreadyInRoomException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyInRoom(AlreadyInRoomException ex) {
        return badRequest("ALREADY_IN_ROOM", ex.getMessage());
    }

    @ExceptionHandler(NotOwnerException.class)
    public ResponseEntity<ErrorResponse> handleNotOwner(NotOwnerException ex) {
        return badRequest("NOT_OWNER", ex.getMessage());
    }

    @ExceptionHandler(NotEnoughPlayersException.class)
    public ResponseEntity<ErrorResponse> handleNotEnoughPlayers(NotEnoughPlayersException ex) {
        return badRequest("NOT_ENOUGH_PLAYERS", ex.getMessage());
    }

    @ExceptionHandler(NotThisPlayerTurnException.class)
    public ResponseEntity<ErrorResponse> handleNotThisPlayerTurn(NotThisPlayerTurnException ex) {
        return badRequest("NOT_YOUR_TURN", ex.getMessage());
    }

    @ExceptionHandler(InvalidDrawAmountException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDrawAmount(InvalidDrawAmountException ex) {
        return badRequest("INVALID_DRAW_AMOUNT", ex.getMessage());
    }

    @ExceptionHandler(InvalidMoveException.class)
    public ResponseEntity<ErrorResponse> handleInvalidMove(InvalidMoveException ex) {
        return badRequest("INVALID_MOVE", ex.getMessage());
    }

    @ExceptionHandler(InvalidMakaoCallException.class)
    public ResponseEntity<ErrorResponse> handleInvalidMakaoCall(InvalidMakaoCallException ex) {
        return badRequest("INVALID_MAKAO_CALL", ex.getMessage());
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
