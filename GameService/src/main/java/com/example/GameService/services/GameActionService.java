package com.example.GameService.services;


import com.example.GameService.config.security.AuthenticatedUser;
import com.example.GameService.config.websocket.GameWebSocketService;
import com.example.GameService.dto.game.GameStateResponse;
import com.example.GameService.entities.CardModel;
import com.example.GameService.entities.GameActionModel;
import com.example.GameService.entities.GameModel;
import com.example.GameService.entities.RoomPlayerModel;
import com.example.GameService.enums.CardLocation;
import com.example.GameService.enums.CardSuit;
import com.example.GameService.enums.CardType;
import com.example.GameService.enums.GameActionType;
import com.example.GameService.enums.GameStatus;
import com.example.GameService.enums.TurnPhase;
import com.example.GameService.exceptions.game.GameNotFoundException;
import com.example.GameService.exceptions.rules.InvalidMakaoCallException;
import com.example.GameService.exceptions.rules.InvalidMoveException;
import com.example.GameService.repositories.CardRepository;
import com.example.GameService.repositories.GameActionRepository;
import com.example.GameService.repositories.GameRepository;
import com.example.GameService.repositories.RoomPlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameActionService {

    private final DeckService deckService;
    private final GameRepository gameRepository;
    private final GameActionRepository gameActionRepository;
    private final CardRepository cardRepository;
    private final RoomPlayerRepository roomPlayerRepository;
    private final RulesService rulesService;
    private final GameService gameService;
    private final GameWebSocketService gameWebSocketService;

    @Transactional
    public GameStateResponse playCards(
            Long gameId,
            List<Long> cardIds,
            CardType requestedRank,
            CardSuit requestedSuit,
            AuthenticatedUser player
    ) {
        GameModel game = gameRepository.findById(gameId).orElseThrow(() -> new GameNotFoundException("Game not found"));
        rulesService.checkTurn(game, player);

        List<CardModel> cards = cardIds.stream().map(id -> ownedHandCard(gameId, player.playerId(), id)).toList();

        rulesService.playSequence(game, cards, game.getRoomId(), player.playerId(), requestedRank, requestedSuit);

        for (CardModel card : cards) {
            card.setLocation(CardLocation.TABLE);
            card.setPlayerId(player.playerId());
            card.setDeckPosition(nextTablePosition(gameId));
            cardRepository.save(card);
        }

        game.setTurnPhase(TurnPhase.PLAY);

        long remainingCards = deckService.getCountByGameIdAndPlayerId(gameId, player.playerId(), CardLocation.HAND);
        if (remainingCards == 0) {
            game.setStatus(GameStatus.FINISHED);
        }

        gameRepository.saveAndFlush(game);
        resetMakaoIfNeeded(game.getRoomId(), gameId, player.playerId());
        logAction(gameId, player.playerId(), GameActionType.PLAY_CARD);

        gameWebSocketService.sendGameUpdate(game.getRoomId(), gameId);
        return gameService.getGameStateForPlayer(game.getRoomId(), gameId, player.playerId());
    }

    @Transactional
    public GameStateResponse draw(
            Long gameId,
            AuthenticatedUser player
    ) {
        GameModel game = gameRepository.findById(gameId).orElseThrow(() -> new GameNotFoundException("Game not found"));
        rulesService.checkTurn(game, player);

        if (game.getDrawnCardId() != null) {
            throw new InvalidMoveException("You already drew this turn - play the drawn card or pass");
        }

        int amount = rulesService.amountToDraw(game);
        CardModel firstDrawn = null;
        for (int i = 0; i < amount; i++) {
            CardModel drawn = deckService.draw(gameId, player.playerId(), CardLocation.HAND);
            if (i == 0) {
                firstDrawn = drawn;
            }
        }

        if (firstDrawn != null && rulesService.isPlayable(game, firstDrawn)) {
            game.setDrawnCardId(firstDrawn.getId());
            game.setTurnPhase(TurnPhase.PLAY_DRAWN_CARD);
        } else {
            rulesService.resolveAfterDraw(game, game.getRoomId(), player.playerId());
            game.setTurnPhase(TurnPhase.PLAY);
        }

        gameRepository.saveAndFlush(game);
        resetMakaoIfNeeded(game.getRoomId(), gameId, player.playerId());
        logAction(gameId, player.playerId(), GameActionType.DRAW_CARD);

        gameWebSocketService.sendGameUpdate(game.getRoomId(), gameId);
        return gameService.getGameStateForPlayer(game.getRoomId(), gameId, player.playerId());
    }

    @Transactional
    public GameStateResponse pass(
            Long gameId,
            AuthenticatedUser player
    ) {
        GameModel game = gameRepository.findById(gameId).orElseThrow(() -> new GameNotFoundException("Game not found"));
        rulesService.checkTurn(game, player);

        if (game.getDrawnCardId() == null) {
            throw new InvalidMoveException("Nothing to pass - draw a card first");
        }

        rulesService.resolveAfterDraw(game, game.getRoomId(), player.playerId());
        game.setTurnPhase(TurnPhase.PLAY);
        gameRepository.saveAndFlush(game);

        gameWebSocketService.sendGameUpdate(game.getRoomId(), gameId);
        return gameService.getGameStateForPlayer(game.getRoomId(), gameId, player.playerId());
    }

    @Transactional
    public GameStateResponse callMakao(Long gameId, AuthenticatedUser player) {
        GameModel game = gameRepository.findById(gameId).orElseThrow(() -> new GameNotFoundException("Game not found"));

        long handSize = deckService.getCountByGameIdAndPlayerId(gameId, player.playerId(), CardLocation.HAND);
        if (handSize != 1) {
            throw new InvalidMakaoCallException("You can only call makao when you have exactly one card left");
        }

        RoomPlayerModel roomPlayer = findRoomPlayer(game.getRoomId(), player.playerId());
        roomPlayer.setCalledMakao(true);
        roomPlayerRepository.save(roomPlayer);

        return gameService.getGameStateForPlayer(game.getRoomId(), gameId, player.playerId());
    }

    @Transactional
    public GameStateResponse stopMakao(Long gameId, Long targetPlayerId, AuthenticatedUser player) {
        GameModel game = gameRepository.findById(gameId).orElseThrow(() -> new GameNotFoundException("Game not found"));

        long targetHandSize = deckService.getCountByGameIdAndPlayerId(gameId, targetPlayerId, CardLocation.HAND);
        RoomPlayerModel target = findRoomPlayer(game.getRoomId(), targetPlayerId);

        boolean caughtRedHanded = targetHandSize == 1 && !target.isCalledMakao();
        Long penalizedPlayerId = caughtRedHanded ? targetPlayerId : player.playerId();

        for (int i = 0; i < 5; i++) {
            deckService.draw(gameId, penalizedPlayerId, CardLocation.HAND);
        }

        RoomPlayerModel penalized = findRoomPlayer(game.getRoomId(), penalizedPlayerId);
        penalized.setCalledMakao(false);
        roomPlayerRepository.save(penalized);

        gameWebSocketService.sendGameUpdate(game.getRoomId(), gameId);
        return gameService.getGameStateForPlayer(game.getRoomId(), gameId, player.playerId());
    }

    private CardModel ownedHandCard(Long gameId, Long playerId, Long cardId) {
        CardModel card = cardRepository.findById(cardId)
                .orElseThrow(() -> new InvalidMoveException("Card not found: " + cardId));
        if (!card.getGameId().equals(gameId)
                || !playerId.equals(card.getPlayerId())
                || card.getLocation() != CardLocation.HAND) {
            throw new InvalidMoveException("Card is not in your hand: " + cardId);
        }
        return card;
    }

    private int nextTablePosition(Long gameId) {
        return cardRepository.findFirstByGameIdAndLocationOrderByDeckPositionDesc(gameId, CardLocation.TABLE)
                .map(c -> c.getDeckPosition() + 1)
                .orElse(0);
    }

    private RoomPlayerModel findRoomPlayer(Long roomId, Long playerId) {
        return roomPlayerRepository.findByPlayerIdAndRoomId(playerId, roomId)
                .orElseThrow(() -> new InvalidMakaoCallException("Player is not in this room"));
    }

    private void resetMakaoIfNeeded(Long roomId, Long gameId, Long playerId) {
        long handSize = deckService.getCountByGameIdAndPlayerId(gameId, playerId, CardLocation.HAND);
        if (handSize == 1) {
            return;
        }
        roomPlayerRepository.findByPlayerIdAndRoomId(playerId, roomId).ifPresent(rp -> {
            if (rp.isCalledMakao()) {
                rp.setCalledMakao(false);
                roomPlayerRepository.save(rp);
            }
        });
    }

    private void logAction(Long gameId, Long playerId, GameActionType type) {
        GameActionModel action = new GameActionModel();
        action.setGameId(gameId);
        action.setPlayerId(playerId);
        action.setType(type);
        action.setCreatedAt(LocalDateTime.now());
        gameActionRepository.save(action);
    }
}
