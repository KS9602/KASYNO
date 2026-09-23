package com.example.MakaoService.dto;

public record CreateMatchRequest(
        String name,
        String password,
        Integer maxPlayers
) {
}
