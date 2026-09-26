package com.example.GameService.dto.room;

import com.example.GameService.enums.RoomStatus;

public record RoomResponse(
        Long id,
        String name,
        Long playersCount,
        Integer maxPlayers,
        RoomStatus status
) {}