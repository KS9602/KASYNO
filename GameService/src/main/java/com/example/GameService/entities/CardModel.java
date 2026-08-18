package com.example.GameService.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.UUID;

@Table(name = "card")
@Entity
@Setter
@Getter
@NoArgsConstructor
public class CardModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "card_id")
    private UUID cardId;

    @Column(name = "round_id")
    private Long roundId;

    @Column(name = "game_id")
    private Long gameId;

    @Column(name = "rank")
    private String rank;

    @Column(name = "color")
    private String color;

    @Column(name = "is_draved")
    @ColumnDefault(value = "false")
    private boolean isDraved;

}
