package com.example.AuthService.entities;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash("Token")
@AllArgsConstructor
@Setter
@Getter
public class RedisToken implements Serializable {
    @Id
    private String id;
    private String token;
}


