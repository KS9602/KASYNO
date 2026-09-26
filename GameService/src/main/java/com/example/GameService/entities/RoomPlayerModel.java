package com.example.GameService.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "room_players",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"room_id", "player_id"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
public class RoomPlayerModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_id", nullable = false)
    private Long roomId;

    @Column(name = "player_id", nullable = false)
    private Long playerId;

    @Column(name = "joined_at", nullable = false)
    private LocalDateTime joinedAt;

    @Column(name = "called_makao", nullable = false)
    private boolean calledMakao = false;
}