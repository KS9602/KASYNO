package com.example.GameService.services;

import com.example.GameService.dto.DrawDTO;
import com.example.GameService.entities.CardModel;
import com.example.GameService.exceptions.DeckNotFoundException;
import com.example.GameService.mappers.CardMapper;
import com.example.GameService.repositories.CardRepositrory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepositrory cardRepositrory;
    private final CardMapper cardMapper;


    public final List<String> CARDS_RANK = List.of(
            "2",
            "3",
            "4",
            "5",
            "6",
            "7",
            "8",
            "9",
            "10",
            "J",
            "Q",
            "K",
            "A"
    );
    public final List<String> CARDS_COLOR = List.of(
            "S",
            "C",
            "H",
            "D"
    );

    public CardModel createCard(Long gameId, Long roundId, String rank, String color){
        CardModel cardModel = new CardModel();
        cardModel.setRoundId(roundId);
        cardModel.setRank(rank);
        cardModel.setColor(color);
        UUID uuid = UUID.randomUUID();
        cardModel.setCardId(uuid);
        cardModel.setGameId(gameId);
        return cardRepositrory.saveAndFlush(cardModel);
    }


    public List<CardModel> buildDeck(Long gameId, Long roundId) {

        List<CardModel> decksCards = new ArrayList<>();
        for(String rank : CARDS_RANK){
            for(String color : CARDS_COLOR){
                decksCards.add(createCard(gameId, roundId, rank, color));
            }
        }
        return decksCards;
    }


    public List<CardModel> getDeck(DrawDTO drawDTO) throws DeckNotFoundException {
        List<CardModel> deck = cardRepositrory.findAllByGameIdAndRoundId(drawDTO.getGameId(), drawDTO.getRoundId());
        if(deck.isEmpty()){throw new DeckNotFoundException("Deck not found");}

        return deck;
    }

    public void setCardIsDraved(CardModel cardModel){
        cardModel.setDraved(true);
        cardRepositrory.save(cardModel);
    }
}
