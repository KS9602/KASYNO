package com.example.AuthService.services;

import com.example.AuthService.DTO.AccessTokenDTO;
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

@Slf4j
@Service
@AllArgsConstructor
public class AuthService {

    private final TokenRepository tokenRepository;
    private JwtService jwtService;
    private AuthenticationManager authenticationManager;


    public String generateToken(LoginRequestDTO loginRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.username(), loginRequestDTO.password())
        );
        if (authentication.isAuthenticated()) {
            String token = jwtService.generateToken(loginRequestDTO.username(), TokenType.REFRESH);
            tokenRepository.save(new RedisToken(loginRequestDTO.username(), token));
            return token;

        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
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


    public String generateAccessToken(AccessTokenDTO accessTokenDTO) {
        return jwtService.generateToken(accessTokenDTO.username(), TokenType.ACCESS);
    }

}