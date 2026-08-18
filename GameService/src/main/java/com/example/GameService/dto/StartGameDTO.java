package com.example.GameService.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StartGameDTO{
        private List<String> players;
        private Long gameId;

}
