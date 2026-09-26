package com.example.GameService.repositories;

import com.example.GameService.entities.GameActionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameActionRepository extends JpaRepository<GameActionModel, Long> {
}
