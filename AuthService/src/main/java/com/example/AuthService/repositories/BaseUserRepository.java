package com.example.AuthService.repositories;

import com.example.AuthService.entities.BaseUserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BaseUserRepository extends JpaRepository<BaseUserModel, Long> {

    Optional<BaseUserModel> findByUsername(String username);
    Optional<BaseUserModel> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String username);
}
