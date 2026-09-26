package com.example.GameService.repositories;

import com.example.GameService.entities.CardModel;
import com.example.GameService.enums.CardLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<CardModel, Long> {

    List<CardModel> findAllByGameId(Long gameId);
    List<CardModel> findAllByPlayerIdAndGameId(Long playerId, Long gameId);
    List<CardModel> findAllByPlayerIdAndGameIdAndLocation(Long playerId, Long gameId, CardLocation location);
    long countByGameIdAndPlayerIdAndLocation(
            Long gameId,
            Long playerId,
            CardLocation location
    );
    List<CardModel> findAllByGameIdAndLocationOrderByDeckPositionAsc(
            Long gameId,
            CardLocation location
    );

    Optional<CardModel> findFirstByGameIdAndLocationOrderByDeckPositionAsc(Long gameId, CardLocation cardLocation);

    Optional<CardModel> findFirstByGameIdAndLocationOrderByDeckPositionDesc(Long gameId, CardLocation cardLocation);

    long countByGameIdAndLocation(Long gameId, CardLocation location);
}