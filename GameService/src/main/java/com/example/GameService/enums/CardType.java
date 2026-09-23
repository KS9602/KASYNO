package com.example.GameService.enums;

public enum CardType {

    TWO(false),
    THREE(false),
    FOUR(false),
    FIVE(false),
    SIX(false),
    SEVEN(false),
    EIGHT(false),
    NINE(false),
    TEN(false),
    JACK(true),
    QUEEN(true),
    KING(true),
    ACE(true);

    private final boolean functional;

    CardType(boolean functional) {
        this.functional = functional;
    }

    public boolean isFunctional() {
        return functional;
    }
}