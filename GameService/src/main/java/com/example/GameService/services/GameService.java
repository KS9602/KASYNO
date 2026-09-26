package com.example.GameService.services;

import com.example.GameService.dto.CardResponse;
import com.example.GameService.dto.game.GameResponse;
import com.example.GameService.dto.game.GameStateResponse;
import com.example.GameService.dto.game.PlayerGameState;
import com.example.GameService.entities.CardModel;
import com.example.GameService.entities.GameModel;
import com.example.GameService.entities.RoomModel;
import com.example.GameService.entities.RoomPlayerModel;
import com.example.GameService.enums.CardLocation;
import com.example.GameService.enums.GameStatus;
import com.example.GameService.enums.TurnPhase;
import com.example.GameService.exceptions.game.GameNotFoundException;
import com.example.GameService.mappers.GameMapper;
import com.example.GameService.repositories.GameRepository;
import com.example.GameService.repositories.RoomPlayerRepository;
import com.example.GameService.config.security.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class GameService {

    private final RoomPlayerRepository roomPlayerRepository;
    private final GameRepository gameRepository;
    private final GameMapper gameMapper;
    private final DeckService deckService;
    private final Random random = new Random();


    public GameModel startGame(Long roomId) {
        GameModel gameModel = createGame(roomId);
        buildDeck(gameModel.getId());
        drawInitialCardsPlayers(roomId, gameModel.getId());
        drawInitialCardsTable(gameModel);
        resetMakaoFlags(roomId);
        gameModel.setStatus(GameStatus.IN_PROGRESS);
        gameModel.setTurnPhase(TurnPhase.PLAY);
        gameRepository.saveAndFlush(gameModel);
        return gameModel;
    }

    public GameModel createGame(Long roomId) {
        GameModel gameModel = new GameModel();
        gameModel.setRoomId(roomId);
        gameModel.setGameNumber(1);
        gameModel.setStatus(GameStatus.WAITING);
        gameModel.setTurnPhase(TurnPhase.STARTING);
        gameModel.setCurrentPlayerId(randomPlayerId(roomId));
        gameRepository.saveAndFlush(gameModel);
        return gameModel;
    }

    private void resetMakaoFlags(Long roomId) {
        List<RoomPlayerModel> roomPlayers = roomPlayerRepository.findAllByRoomId(roomId);
        roomPlayers.forEach(rp -> rp.setCalledMakao(false));
        roomPlayerRepository.saveAll(roomPlayers);
    }

    public GameResponse getGame(Long gameId, AuthenticatedUser player) {
        return gameMapper.toResponse(findAuthorizedGame(gameId, player));
    }

    public GameStateResponse getGameStateForPlayer(Long gameId, AuthenticatedUser player) {
        GameModel game = findAuthorizedGame(gameId, player);
        return getGameStateForPlayer(game.getRoomId(), gameId, player.playerId());
    }

    private GameModel findAuthorizedGame(Long gameId, AuthenticatedUser player) {
        GameModel game = gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException("Game not found: " + gameId));

        if (!roomPlayerRepository.existsByRoomIdAndPlayerId(game.getRoomId(), player.playerId())) {
            throw new GameNotFoundException("Game not found: " + gameId);
        }

        return game;
    }

    private Long randomPlayerId(Long roomId) {
        List<RoomPlayerModel> players = roomPlayerRepository.findAllByRoomId(roomId);
        if (players.isEmpty()) {
            throw new IllegalStateException("Room has no players");
        }
        return  players.get(random.nextInt(players.size())).getPlayerId();
    }


    public void buildDeck(Long gameId) {
        deckService.createDeck(gameId);
        deckService.shuffle(gameId);
    }


    public GameStateResponse getGameStateForPlayer(Long roomId, Long gameId, Long playerId) {
        if (!roomPlayerRepository.existsByRoomIdAndPlayerId(roomId, playerId)) {
            throw new GameNotFoundException("Player not found");
        }
        GameModel game = gameRepository.findById(gameId)
                .orElseThrow(() -> new GameNotFoundException("Game not found: " + gameId));

        List<CardResponse> cards = deckService
                .getUserCards(gameId, playerId)
                .stream()
                .map(c -> new CardResponse(c.getId(),c.getSuit(),c.getType()))
                .toList();

        List<CardResponse> playedCards = deckService
                .getPlayedCards(gameId, CardLocation.TABLE)
                .stream()
                .map(c -> new CardResponse(c.getId(),c.getSuit(),c.getType()))
                .toList();

        return new GameStateResponse(
                cards,
                cards.size(),
                getOponentsCardsAmount(roomId, gameId, playerId),
                playedCards,
                playedCards.size(),
                deckService.getCountByGameIdAndLocation(gameId, CardLocation.DECK),
                game.getStatus(),
                game.getCurrentPlayerId(),
                game.getAttackType(),
                game.getAttackAmount(),
                game.getRequestedRank(),
                game.getRequestedSuit(),
                playerId.equals(game.getCurrentPlayerId()) ? game.getDrawnCardId() : null
        );
    }

    private List<PlayerGameState>  getOponentsCardsAmount(Long roomId, Long gameId, Long playerId) {
        List<RoomPlayerModel> players = roomPlayerRepository.findAllByRoomId(roomId);
        List<PlayerGameState> playerGameStates = new ArrayList<>();
        for(RoomPlayerModel player: players) {
            if (player.getPlayerId().equals(playerId)) {
                continue;
            }

            long cardsAmount = deckService.getCountByGameIdAndPlayerId(gameId, player.getPlayerId(), CardLocation.HAND);
            playerGameStates.add(new PlayerGameState(player.getPlayerId(), cardsAmount));
        }
        return playerGameStates;
    }

    public void drawInitialCardsPlayers(Long roomId, Long gameId) {
        List<RoomPlayerModel> playerModels = roomPlayerRepository.findAllByRoomId(roomId);
        for (RoomPlayerModel playerModel : playerModels) {
            for(int i = 0; i < 5; i++){
                deckService.draw(gameId, playerModel.getPlayerId(), CardLocation.HAND);
            }
        }
    }
    public void drawInitialCardsTable(GameModel gameModel) {
        long count = deckService.getCountByGameIdAndLocation(gameModel.getId(), CardLocation.DECK);
        CardModel lastCard = null;
        for(int i = 0; i < count; i++){
            CardModel card = deckService.draw(gameModel.getId(), null, CardLocation.TABLE);
            lastCard = card;
            if(!card.getType().isFunctional()){
                break;
            }
        }
        if (lastCard != null) {
            gameModel.setLastRank(lastCard.getType());
            gameModel.setLastSuit(lastCard.getSuit());
        }
    }

}
