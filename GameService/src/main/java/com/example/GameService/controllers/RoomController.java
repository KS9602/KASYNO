package com.example.GameService.controllers;

import com.example.GameService.dto.game.GameResponse;
import com.example.GameService.dto.room.CreateRoomRequest;
import com.example.GameService.dto.room.JoinRoomRequest;
import com.example.GameService.dto.room.RoomResponse;
import com.example.GameService.config.security.AuthenticatedUser;
import com.example.GameService.services.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
public class RoomController extends BaseController {

    private final RoomService roomService;

    @PostMapping
    public RoomResponse createRoom(
            @RequestBody CreateRoomRequest request,
            Authentication authentication
    ) {

        return roomService.createRoom(request, authPlayer(authentication));
    }

    @GetMapping
    public List<RoomResponse> getRooms() {
        return roomService.getRooms();
    }

    @GetMapping("/{roomId}")
    public RoomResponse getRoom(
            @PathVariable Long roomId
    ) {
        return roomService.getRoom(roomId);
    }

    @PostMapping("/{roomId}/join")
    public RoomResponse joinRoom(
            @PathVariable Long roomId,
            @RequestBody JoinRoomRequest request,
            Authentication authentication
    ) {

        return roomService.joinRoom(
                roomId,
                request,
                authPlayer(authentication)
        );
    }

    @DeleteMapping("/{roomId}/players/me")
    public void leaveRoom(
            @PathVariable Long roomId,
            Authentication authentication
    ) {
        roomService.leaveRoom(
                roomId,
                authPlayer(authentication)

        );
    }

    @PostMapping("/{roomId}/start")
    public GameResponse startGame(
            @PathVariable Long roomId,
            Authentication authentication
    ) {
        AuthenticatedUser user =
                (AuthenticatedUser) authentication.getPrincipal();

        return roomService.startGame(
                roomId,
                user
        );
    }
}