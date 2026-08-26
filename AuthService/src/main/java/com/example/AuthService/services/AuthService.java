package com.example.AuthService.services;

import com.example.AuthService.DTO.AccessTokenDTO;
import com.example.AuthService.DTO.RefreshTokenDTO;
import com.example.AuthService.DTO.LoginRequestDTO;
import com.example.AuthService.entities.RedisToken;
import com.example.AuthService.enums.TokenType;
import com.example.AuthService.repositories.TokenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class AuthService {

    private final TokenRepository tokenRepository;
    private JwtService jwtService;
    private AuthenticationManager authenticationManager;


    public HashMap<String,String> login(LoginRequestDTO loginRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.username(), loginRequestDTO.password())
        );
        if (authentication.isAuthenticated()) {
            HashMap<String, String> tokens = new HashMap<>();
            String accessToken = jwtService.generateToken(loginRequestDTO.username(), TokenType.ACCESS);
            String refreshToken = jwtService.generateToken(loginRequestDTO.username(), TokenType.REFRESH);
            tokens.put("accessToken", accessToken);
            tokens.put("refreshToken", refreshToken);
            tokenRepository.save(new RedisToken(loginRequestDTO.username(), refreshToken));
            return tokens;

        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }

    public String generateAccessToken(String refreshToken) {
        if(Boolean.TRUE.equals(jwtService.isTokenExpired(refreshToken))){
            throw new RuntimeException("Token is expired!");
        }
        String username = jwtService.extractUsername(refreshToken);
        if(username == null){
            throw new RuntimeException("Invalid refresh token!");
        }
        RedisToken redisToken = tokenRepository.findById(username).orElse(null);
        if (redisToken == null) {
            throw new UsernameNotFoundException("Invalid user request!");
        }
        return jwtService.generateToken(username, TokenType.ACCESS);
    }

    public void logout(String token) {
        String username = null;
        try{
            username = jwtService.extractUsername(token);
        } catch (Exception e){
            return;
        }
        RedisToken redisToken = tokenRepository.findById(username).orElse(null);
        if (redisToken != null) {
            tokenRepository.deleteById(username);
        }
    }


}