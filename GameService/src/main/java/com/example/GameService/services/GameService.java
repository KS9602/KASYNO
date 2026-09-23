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
        drawInitialCardsTable(gameModel.getId());
        gameModel.setStatus(GameStatus.IN_PROGRESS);
        gameRepository.saveAndFlush(gameModel);
        return gameModel;
    }

    public GameModel createGame(Long roomId) {
        GameModel gameModel = new GameModel();
        gameModel.setRoomId(roomId);
        gameModel.setGameNumber(1);
        gameModel.setStatus(GameStatus.WAITING);
        gameModel.setCurrentPlayerId(randomPlayerId(roomId));
        gameRepository.saveAndFlush(gameModel);
        return gameModel;
    }

    public GameResponse getGame(Long gameId, AuthenticatedUser player) {
        return gameMapper.toResponse(findAuthorizedGame(gameId, player));
    }

    public GameResponse getGameState(Long gameId, AuthenticatedUser player) {
        return gameMapper.toResponse(findAuthorizedGame(gameId, player));
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
                deckService.getCountByGameIdAndLocation(gameId, CardLocation.DECK)
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
            for(int i = 0; i < 4; i++){
                deckService.draw(gameId, playerModel.getPlayerId(), CardLocation.HAND);
            }
        }
    }
    public void drawInitialCardsTable(Long gameId) {
        long count = deckService.getCountByGameIdAndLocation(gameId, CardLocation.DECK);
        for(int i = 0; i < count; i++){
            CardModel card = deckService.draw(gameId, null, CardLocation.TABLE);
            if(!card.getType().isFunctional()){
                break;
            }
        }
    }

}
