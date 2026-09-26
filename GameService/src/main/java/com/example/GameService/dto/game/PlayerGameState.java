package com.example.GameService.dto.game;

public record PlayerGameState(
        Long playerId,
        long cardsCount
) {}