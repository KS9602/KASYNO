package com.example.MakaoService.dto;

import com.example.MakaoService.entities.MatchStatus;

public record MatchListItemDTO(
        Long matchId,
        String name,
        MatchStatus status,
        int playerCount,
        int maxPlayers
) {
}
