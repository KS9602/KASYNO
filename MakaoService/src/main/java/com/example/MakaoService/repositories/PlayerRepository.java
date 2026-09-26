package com.example.MakaoService.repositories;

import com.example.MakaoService.entities.PlayerModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<PlayerModel, Long> {
    Optional<PlayerModel> findByGlobalUserId(Long globalUserId);
}
