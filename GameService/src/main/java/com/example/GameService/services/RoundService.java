package com.example.GameService.services;

import com.example.GameService.dto.StartGameDTO;
import com.example.GameService.entities.GameModel;
import com.example.GameService.entities.RoundModel;
import com.example.GameService.repositories.RoundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoundService {

    private final RoundRepository roundRepository;

    public RoundModel createRound(Long gameId){
        RoundModel roundModel = new RoundModel();
        roundModel.setGameId(gameId);
        roundRepository.saveAndFlush(roundModel);
        return roundModel;
    }
}
