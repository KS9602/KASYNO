package com.example.MakaoService.dto;

import com.example.MakaoService.entities.MatchStatus;

import java.util.List;

public record MatchSnapshotDTO(
        Long matchId,
        String name,
        MatchStatus status,
        int maxPlayers,
        List<PlayerSummaryDTO> players
) {
}
