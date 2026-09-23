package com.example.GameService.repositories;

import com.example.GameService.entities.RoomPlayerModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomPlayerRepository extends JpaRepository<RoomPlayerModel, Long> {

    Optional<RoomPlayerModel> findByPlayerIdAndRoomId(Long playerId, Long roomId);
    List<RoomPlayerModel> findAllByRoomId(Long roomId);

    long countByRoomId(Long roomId);
    boolean existsByRoomIdAndPlayerId(Long roomId, Long playerId);
    void deleteByRoomIdAndPlayerId(Long roomId, Long playerId);

}
