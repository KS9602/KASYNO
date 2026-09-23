package com.example.GameService.entities;

import com.example.GameService.enums.GameActionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "game_actions")
@Getter
@Setter
public class GameActionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "game_id", nullable = false)
    private Long gameId;

    @Column(name = "player_id", nullable = false)
    private Long playerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GameActionType type;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}