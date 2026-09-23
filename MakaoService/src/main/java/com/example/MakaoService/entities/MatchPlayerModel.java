package com.example.MakaoService.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Table(name = "match_player")
@Entity
@Setter
@Getter
@NoArgsConstructor
public class MatchPlayerModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "match_id")
    private Long matchId;

    @Column(name = "player_id")
    private Long playerId;

    @Column(name = "seat_no")
    private Integer seatNo;

    @Column(name = "joined_at")
    private Instant joinedAt;
}
