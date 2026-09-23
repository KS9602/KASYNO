package com.example.GameService.entities;

import com.example.GameService.enums.CardLocation;
import com.example.GameService.enums.CardSuit;
import com.example.GameService.enums.CardType;
import jakarta.persistence.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cards")
@Getter
@Setter
@NoArgsConstructor
public class CardModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private CardSuit suit;

    @Enumerated(EnumType.STRING)
    private CardType type;

    private Long gameId;

    private Long playerId;

    private CardLocation location;

    @Column(name = "deck_position")
    private Integer deckPosition;
}