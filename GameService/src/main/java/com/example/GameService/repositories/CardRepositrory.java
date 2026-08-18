package com.example.GameService.repositories;

import com.example.GameService.entities.CardModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepositrory extends JpaRepository<CardModel, Long> {

    @Query("SELECT cm FROM CardModel cm WHERE cm.gameId = :gameId AND cm.roundId = :roundId AND cm.isDraved = false")
    List<CardModel> findAllByGameIdAndRoundId(@Param("gameId") Long gameId, @Param("roundId") Long roundId);
}
