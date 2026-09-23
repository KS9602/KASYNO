package com.example.GameService.services;

import com.example.GameService.config.websocket.GameWebSocketService;
import com.example.GameService.dto.game.GameResponse;
import com.example.GameService.dto.room.CreateRoomRequest;
import com.example.GameService.dto.room.JoinRoomRequest;
import com.example.GameService.dto.room.RoomResponse;
import com.example.GameService.entities.GameModel;
import com.example.GameService.entities.RoomModel;
import com.example.GameService.entities.RoomPlayerModel;
import com.example.GameService.enums.RoomStatus;
import com.example.GameService.exceptions.room.*;
import com.example.GameService.mappers.GameMapper;
import com.example.GameService.mappers.RoomMapper;
import com.example.GameService.repositories.RoomRepository;
import com.example.GameService.repositories.RoomPlayerRepository;
import com.example.GameService.repositories.projections.RoomProjection;
import com.example.GameService.config.security.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final RoomPlayerRepository roomPlayerRepository;
    private final PasswordEncoder passwordEncoder;
    private final GameService gameService;
    private final RoomMapper roomMapper;
    private final GameMapper gameMapper;
    private final DeckService deckService;
    private final GameWebSocketService gameWebSocketService;

    public RoomResponse createRoom(
            CreateRoomRequest request,
            AuthenticatedUser player
    ) {
        RoomModel room = new RoomModel();

        room.setName(request.name());
        room.setPassword(
                passwordEncoder.encode(request.password())
        );
        room.setOwnerId(player.playerId());
        room.setMaxPlayers(request.maxPlayers());
        room.setStatus(RoomStatus.WAITING);

        roomRepository.save(room);

        addPlayer(room.getId(), player.playerId());

        return new RoomResponse(
                room.getId(),
                room.getName(),
                1L,
                room.getMaxPlayers(),
                room.getStatus()
        );
    }

    private void addPlayer(Long roomId, Long playerId) {
        RoomPlayerModel roomPlayer = new RoomPlayerModel();
        roomPlayer.setRoomId(roomId);
        roomPlayer.setPlayerId(playerId);
        roomPlayer.setJoinedAt(LocalDateTime.now());
        roomPlayerRepository.save(roomPlayer);
    }


    public RoomResponse joinRoom(
            Long roomId,
            JoinRoomRequest request,
            AuthenticatedUser player
    ) {

        RoomModel room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RoomNotFoundException("Room not found: " + roomId));

        if (room.getStatus() != RoomStatus.WAITING) {
            throw new RoomNotJoinableException("Room is not joinable");
        }

        Long players = roomPlayerRepository.countByRoomId(roomId);

        if (players >= room.getMaxPlayers()) {
            throw new RoomFullException("Room is full");
        }

        if (!passwordEncoder.matches(
                request.password(),
                room.getPassword()
        )) {
            throw new InvalidRoomPasswordException("Invalid password");
        }

        if (roomPlayerRepository.existsByRoomIdAndPlayerId(
                roomId,
                player.playerId()
        )) {
            throw new AlreadyInRoomException("Player is already in room");
        }

        addPlayer(roomId, player.playerId());

        return new RoomResponse(
                room.getId(),
                room.getName(),
                players + 1,
                room.getMaxPlayers(),
                room.getStatus()
        );
    }

    public List<RoomResponse> getRooms() {
        List<RoomProjection> rooms =
                roomRepository.getRooms();
        return rooms.stream()
                .map(roomMapper::projectionToResponse
                )
                .toList();
    }

    public RoomResponse getRoom(Long roomId) {
        RoomProjection roomModel = roomRepository.getRoomById(roomId).orElseThrow(() -> new RoomNotFoundException("Room not found"));
        return roomMapper.projectionToResponse(roomModel);
    }

    public void leaveRoom(Long roomId, AuthenticatedUser player) {
        roomPlayerRepository.deleteByRoomIdAndPlayerId(roomId, player.playerId());
    }



    public GameResponse startGame(Long roomId, AuthenticatedUser player) {
        RoomModel roomModel = roomRepository.findById(roomId).orElseThrow(() -> new RoomNotFoundException("Room not found"));
        if (!roomModel.getOwnerId().equals(player.playerId())) {
            throw new NotOwnerException("Only creator can start the game");
        }
        GameModel gameModel = gameService.startGame(roomId);

        roomModel.setStatus(RoomStatus.IN_GAME);
        roomRepository.save(roomModel);

        gameWebSocketService.sendGameUpdate(roomId, gameModel.getId());
        return gameMapper.toResponse(gameModel);
    }

}