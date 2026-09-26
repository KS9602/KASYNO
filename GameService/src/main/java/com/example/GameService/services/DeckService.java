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
import java.util.Optional;

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

        Optional<CardModel> deckTop = cardRepository
                .findFirstByGameIdAndLocationOrderByDeckPositionAsc(gameId, CardLocation.DECK);

        if (deckTop.isEmpty()) {
            reshuffleDiscardPile(gameId);
            deckTop = cardRepository
                    .findFirstByGameIdAndLocationOrderByDeckPositionAsc(gameId, CardLocation.DECK);
        }

        CardModel card = deckTop.orElseThrow(() -> new IllegalStateException("No cards left to draw"));

        card.setLocation(location);
        card.setPlayerId(playerId);
        return cardRepository.save(card);
    }

    @Transactional
    public void reshuffleDiscardPile(Long gameId) {

        List<CardModel> discardPile = cardRepository
                .findAllByGameIdAndLocationOrderByDeckPositionAsc(gameId, CardLocation.TABLE);

        if (discardPile.size() <= 1) {
            return;
        }

        List<CardModel> toReshuffle = discardPile.subList(0, discardPile.size() - 1);

        Collections.shuffle(toReshuffle);

        for (int i = 0; i < toReshuffle.size(); i++) {
            CardModel card = toReshuffle.get(i);
            card.setLocation(CardLocation.DECK);
            card.setPlayerId(null);
            card.setDeckPosition(i);
        }

        cardRepository.saveAll(toReshuffle);
    }

    public List<CardModel> getUserCards(Long gameId, Long playerId){
        return cardRepository.findAllByPlayerIdAndGameIdAndLocation(playerId, gameId, CardLocation.HAND);
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