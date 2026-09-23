package com.example.GameService.services;


import com.example.GameService.dto.game.GameResponse;
import com.example.GameService.entities.CardModel;
import com.example.GameService.entities.GameModel;
import com.example.GameService.enums.CardLocation;
import com.example.GameService.exceptions.game.GameNotFoundException;
import com.example.GameService.repositories.GameRepository;
import com.example.GameService.config.security.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GameActionService {

    private final DeckService deckService;
    private final GameRepository gameRepository;
    private final RulesService rulesService;

    @Transactional
    public GameResponse draw(
            Long gameId,
            AuthenticatedUser player
    ) {
        GameModel game = gameRepository.findById(gameId).orElseThrow(() -> new GameNotFoundException("Game not found"));

        rulesService.validDraw(game, player);

        // sprawdzenie co za karta, czy moze rzucac
        // draw()
        CardModel card = deckService.draw(gameId, player.playerId(), CardLocation.HAND);

        // rulesService.canPlay()
        boolean canPlay = rulesService.canPlayCard(card);

        // return karta z zwrotka can play



        // nie moze zagrac, musi dobrac

        int cardsNumber = rulesService.howManyCards(game, player);

        for (int i = 0; i < cardsNumber; i++) {
            deckService.draw(gameId, player.playerId(), CardLocation.HAND);
        }

        // TODO: zmiana stanu gry / kolejki
        // TODO: zapis GameAction

        return resultService.toResponse(game);
    }
}