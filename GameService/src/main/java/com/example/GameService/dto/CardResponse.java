package com.example.GameService.dto;

import com.example.GameService.enums.CardSuit;
import com.example.GameService.enums.CardType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public record CardResponse(
        Long id,
        CardSuit suit,
        CardType type
) {
}