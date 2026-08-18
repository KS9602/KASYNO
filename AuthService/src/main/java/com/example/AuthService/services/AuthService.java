package com.example.AuthService.services;

import com.example.AuthService.DTO.AccessTokenDTO;
import com.example.AuthService.DTO.LoginRequestDTO;
import com.example.AuthService.DTO.RegisterRequestDTO;
import com.example.AuthService.entities.BaseUserModel;
import com.example.AuthService.enums.TokenType;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@AllArgsConstructor
public class AuthService {

    private UserService userService;
    private JwtService jwtService;
    private AuthenticationManager authenticationManager;


    public String generateToken(LoginRequestDTO loginRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.username(), loginRequestDTO.password())
        );
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(loginRequestDTO.username(), TokenType.REFRESH);
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }

    public String generateAccessToken(AccessTokenDTO accessTokenDTO) {
        return jwtService.generateToken(accessTokenDTO.username(), TokenType.ACCESS);
    }

}