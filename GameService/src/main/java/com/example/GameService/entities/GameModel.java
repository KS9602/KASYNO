package com.example.GameService.entities;

import com.example.GameService.enums.GameStatus;
import com.example.GameService.enums.TurnPhase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "games")
@Getter
@Setter
@NoArgsConstructor
public class GameModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_id", nullable = false)
    private Long roomId;

    @Column(name = "game_number", nullable = false)
    private Integer gameNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GameStatus status;

    @Column(name = "current_player_id")
    private Long currentPlayerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TurnPhase turnPhase;

    private Long drawnCardId;
}