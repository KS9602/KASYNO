package com.example.GameService.config.websocket;

import com.example.GameService.dto.game.GameResponse;
import com.example.GameService.dto.game.GameStateResponse;
import com.example.GameService.dto.room.RoomResponse;
import com.example.GameService.entities.RoomPlayerModel;
import com.example.GameService.repositories.RoomPlayerRepository;
import com.example.GameService.services.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameWebSocketService {

    private final SimpMessagingTemplate messagingTemplate;
    private final RoomPlayerRepository roomPlayerRepository;
    private final GameService gameService;

    public void sendGameUpdate(Long roomId, Long gameId) {

        List<Long> playerIds = roomPlayerRepository
                .findAllByRoomId(roomId)
                .stream()
                .map(RoomPlayerModel::getPlayerId)
                .toList();

        for (Long playerId : playerIds) {

            GameStateResponse state = gameService.getGameStateForPlayer(roomId, gameId, playerId);

            messagingTemplate.convertAndSend(
                    "/topic/games/" + gameId + "/" + playerId,
                    state
            );
        }
    }

    public void sendRoomUpdate(Long roomId, RoomResponse room) {
        messagingTemplate.convertAndSend("/topic/rooms/" + roomId, room);
    }

    public void sendGameStarted(Long roomId, GameResponse game) {
        messagingTemplate.convertAndSend("/topic/rooms/" + roomId + "/started", game);
    }
}