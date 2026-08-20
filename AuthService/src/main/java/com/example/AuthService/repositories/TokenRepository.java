package com.example.AuthService.repositories;

import com.example.AuthService.entities.RedisToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TokenRepository extends CrudRepository<RedisToken, String> {
}
