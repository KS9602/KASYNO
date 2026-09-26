package com.example.GameService.dto.room;

public record CreateRoomRequest(
        String name,
        String password,
        Integer maxPlayers
) {}