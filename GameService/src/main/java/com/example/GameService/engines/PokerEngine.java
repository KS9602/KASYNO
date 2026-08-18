package com.example.GameService.engines;

import com.example.GameService.dto.CardDTO;
import com.example.GameService.dto.DrawDTO;
import com.example.GameService.dto.StartGameDTO;
import com.example.GameService.dto.StartRoundDTO;
import com.example.GameService.entities.CardModel;
import com.example.GameService.entities.GameModel;
import com.example.GameService.entities.RoundModel;
import com.example.GameService.exceptions.DeckNotFoundException;
import com.example.GameService.mappers.CardMapper;
import com.example.GameService.services.CardService;
import com.example.GameService.services.GameService;
import com.example.GameService.services.RoundService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class PokerEngine  {

    private final CardService cardService;
    private final GameService gameService;
    private final RoundService roundService;
    private final CardMapper cardMapper;

    private final Random random = new Random();

    public Long startGame(StartGameDTO startGameDTO) {
        Long gameId = gameService.createGame(startGameDTO);
        return gameId;
    }

    public StartRoundDTO startRound(StartRoundDTO startRoundDTO){
        GameModel gameModel = gameService.getGame(startRoundDTO.getGameId());
        RoundModel roundModel = roundService.createRound(gameModel.getId());
        cardService.buildDeck(gameModel.getId(), roundModel.getId());
        startRoundDTO.setRoundId(roundModel.getId());
        return startRoundDTO;
    }
    public List<CardDTO> getCards(DrawDTO drawDTO) throws DeckNotFoundException {
        List<CardModel> cards = cardService.getDeck(drawDTO);
        List<CardDTO> pack = new ArrayList<>();
        Collections.shuffle(cards);
        int pos;
        for (int i = 0; i < drawDTO.getAmount(); i++) {
            pos = random.nextInt(cards.size());
            pack.add(cardMapper.toDTO(cards.get(pos)));
            cardService.setCardIsDraved(cards.get(pos));
            cards.remove(pos);
        }
        return pack;
    }


    public void endGame() {

    }
}
