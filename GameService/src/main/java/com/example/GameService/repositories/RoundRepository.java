package com.example.GameService.repositories;

import com.example.GameService.entities.RoundModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoundRepository extends JpaRepository<RoundModel, Long> {
}
