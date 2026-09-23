package com.example.GameService.dto.game;

import com.example.GameService.dto.CardResponse;

import java.util.List;

public record GameStateResponse(
        List<CardResponse> myCards,
        int myCardsCount,
        List<PlayerGameState> opponents,
        List<CardResponse> table,
        long tableCardsCount,
        long deckCardsCount
) {}