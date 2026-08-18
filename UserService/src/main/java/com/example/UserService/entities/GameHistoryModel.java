package com.example.UserService.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "game_history")
@Getter
@Setter
@NoArgsConstructor
public class GameHistoryModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    private String gameId;

    private String gameType;

    private BigDecimal betAmount;

    private BigDecimal winAmount;

    private String result;

    private String currency;

    private String roundId;

    private LocalDateTime createdAt;
}