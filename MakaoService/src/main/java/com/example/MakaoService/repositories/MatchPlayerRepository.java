package com.example.MakaoService.repositories;

import com.example.MakaoService.entities.MatchPlayerModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchPlayerRepository extends JpaRepository<MatchPlayerModel, Long> {
    List<MatchPlayerModel> findAllByMatchId(Long matchId);
    Optional<MatchPlayerModel> findByMatchIdAndPlayerId(Long matchId, Long playerId);
    long countByMatchId(Long matchId);
}
