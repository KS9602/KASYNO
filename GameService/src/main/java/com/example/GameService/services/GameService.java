package com.example.GameService.services;

import com.example.GameService.dto.StartGameDTO;
import com.example.GameService.entities.GameModel;
import com.example.GameService.entities.PlayerModel;
import com.example.GameService.exceptions.GameNotFoundException;
import com.example.GameService.repositories.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameService {

    private final PlayersService playersService;
    private final GameRepository gameRepository;


    public Long createGame(StartGameDTO startGameDTO){
        List<PlayerModel> players = playersService.createPlayers(startGameDTO);
        GameModel gameModel = new GameModel();
        gameModel.setPlayers(players);
        return gameRepository.saveAndFlush(gameModel).getId();
    }

    public GameModel getGame(Long gameId){
        return gameRepository.findById(gameId).orElseThrow(() -> new GameNotFoundException("Game not found"));
    }
}
