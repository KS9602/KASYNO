package com.example.GameService.services;

import com.example.GameService.dto.game.PlayerGameState;
import com.example.GameService.entities.CardModel;
import com.example.GameService.entities.RoomPlayerModel;
import com.example.GameService.enums.CardLocation;
import com.example.GameService.enums.CardSuit;
import com.example.GameService.enums.CardType;
import com.example.GameService.repositories.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DeckService {

    private final CardRepository cardRepository;

    @Transactional
    public void createDeck(Long gameId) {

        List<CardModel> cards = new ArrayList<>();

        for (CardSuit suit : CardSuit.values()) {
            for (CardType type : CardType.values()) {

                CardModel card = new CardModel();

                card.setGameId(gameId);
                card.setSuit(suit);
                card.setType(type);
                card.setLocation(CardLocation.DECK);

                cards.add(card);
            }
        }

        cardRepository.saveAll(cards);
    }

    @Transactional
    public void shuffle(Long gameId) {

        List<CardModel> cards =
                cardRepository.findAllByGameId(gameId);

        Collections.shuffle(cards);

        for (int i = 0; i < cards.size(); i++) {
            cards.get(i).setDeckPosition(i);
        }

        cardRepository.saveAll(cards);
    }

    @Transactional
    public CardModel draw(Long gameId, Long playerId, CardLocation location) {

        CardModel card = cardRepository
                .findFirstByGameIdAndLocationOrderByDeckPositionAsc(
                        gameId,
                        CardLocation.DECK
                )
                .orElseThrow();

        card.setLocation(location);
        card.setPlayerId(playerId);
        return cardRepository.save(card);
    }

    public List<CardModel> getUserCards(Long gameId, Long playerId){
        return cardRepository.findAllByPlayerIdAndGameId(playerId, gameId);
    }

    public List<CardModel> getAllGameCards(Long gameId, Long playerId){
        return cardRepository.findAllByGameId(gameId);
    }
    public long getCountByGameIdAndPlayerId(Long gameId, Long playerId, CardLocation location) {
        return cardRepository.countByGameIdAndPlayerIdAndLocation(gameId, playerId, location);
    }
    public long getCountByGameIdAndLocation(Long gameId, CardLocation location) {
        return cardRepository.countByGameIdAndLocation(gameId, location);
    }

    public List<CardModel> getPlayedCards(Long gameId, CardLocation location) {
        return cardRepository
                .findAllByGameIdAndLocationOrderByDeckPositionAsc(
                        gameId,
                        location
                );
    }

}