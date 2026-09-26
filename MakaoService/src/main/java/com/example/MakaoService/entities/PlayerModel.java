package com.example.MakaoService.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "player")
@Entity
@Setter
@Getter
@NoArgsConstructor
public class PlayerModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "global_user_id")
    private Long globalUserId;

    @Column(name = "username")
    private String username;
}
