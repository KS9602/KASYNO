package com.example.GameService.dto.game;

import com.example.GameService.enums.GameStatus;

public record GameResponse(
        Long id,
        Long roomId,
        Integer gameNumber,
        GameStatus status,
        Long currentPlayerId
) {
}

