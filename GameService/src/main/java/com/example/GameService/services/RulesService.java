package com.example.GameService.services;

import com.example.GameService.entities.CardModel;
import com.example.GameService.entities.GameModel;
import com.example.GameService.exceptions.rules.NotThisPlayerTurnException;
import com.example.GameService.repositories.GameRepository;
import com.example.GameService.config.security.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RulesService {

    private final GameRepository gameRepository;

    public void validDraw(GameModel game, AuthenticatedUser player){
        validTurn(game, player);
    }

    public int howManyCards(GameModel game, AuthenticatedUser player){

        // jesli jest atakowany to zsumowac karty
        // jesli nie to tylko jedna

        // sprawdz pierwsza
        // czy moze rzucic
        // jesli tak to zwrotka ze moze uzyc tej karty

        // jesli nie moze rzucic to po prostu dobiera


    }

    private void validTurn(GameModel game, AuthenticatedUser player){
        if(!game.getCurrentPlayerId().equals(player.playerId())){
            throw new NotThisPlayerTurnException("Not your turn");
        }
    }

    public boolean canPlayCard(CardModel card) {
    }
}
