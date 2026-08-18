package com.example.GameService.services;

import com.example.GameService.dto.StartGameDTO;
import com.example.GameService.entities.PlayerModel;
import com.example.GameService.mappers.PlayerMapper;
import com.example.GameService.repositories.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayersService {

    private final PlayerRepository playerRepository;
    private final PlayerMapper playerMapper;

    public List<PlayerModel> createPlayers(StartGameDTO startGameDTO){
        List<PlayerModel> players = new ArrayList<>();
        for(String player: startGameDTO.getPlayers()){
            PlayerModel playerModel = new PlayerModel();
            playerModel.setNickname(player);
            players.add(playerModel);
        }
        playerRepository.saveAllAndFlush(players);
        return players;
    }
}
