package com.example.GameService.dto;

import java.util.UUID;

public record CardDTO(
        Long id,
        UUID cardId,
        Long roundId,
        Long gameId,
        String color,
        String rank
) {
}
