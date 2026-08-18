package com.example.GameService.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DrawDTO{
        private Long gameId;
        private Long roundId;
        private Long playerId;
        private Integer amount;

}
