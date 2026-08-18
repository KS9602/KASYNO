package com.example.GameService.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.boot.context.properties.bind.DefaultValue;

@Table(name = "round")
@Entity
@Setter
@Getter
@NoArgsConstructor
public class RoundModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "round")
    @ColumnDefault("1")
    private Integer round;

    Long gameId;

    Long deckId;


}
