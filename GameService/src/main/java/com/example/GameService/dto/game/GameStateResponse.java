package com.example.GameService.dto.game;

import com.example.GameService.dto.CardResponse;
import com.example.GameService.enums.AttackType;
import com.example.GameService.enums.CardSuit;
import com.example.GameService.enums.CardType;
import com.example.GameService.enums.GameStatus;

import java.util.List;

public record GameStateResponse(
        List<CardResponse> myCards,
        int myCardsCount,
        List<PlayerGameState> opponents,
        List<CardResponse> table,
        long tableCardsCount,
        long deckCardsCount,
        GameStatus status,
        Long currentPlayerId,
        AttackType attackType,
        Integer attackAmount,
        CardType requestedRank,
        CardSuit requestedSuit,
        Long drawnCardId
) {}