package com.example.MakaoService.repositories;

import com.example.MakaoService.entities.MatchModel;
import com.example.MakaoService.entities.MatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<MatchModel, Long> {
    List<MatchModel> findAllByStatusOrderByIdDesc(MatchStatus status);
}
