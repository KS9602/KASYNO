package com.example.GameService.dto.game;

import com.example.GameService.enums.CardSuit;
import com.example.GameService.enums.CardType;

import java.util.List;

public record PlayCardRequest(
        List<Long> cardIds,
        CardType requestedRank,
        CardSuit requestedSuit
) {}
