package com.example.AuthService.controllers;

import com.example.AuthService.DTO.AccessTokenDTO;
import com.example.AuthService.DTO.LoginRequestDTO;
import com.example.AuthService.DTO.RefreshTokenDTO;
import com.example.AuthService.DTO.RegisterRequestDTO;
import com.example.AuthService.enums.ResponseMessage;
import com.example.AuthService.exceptions.UsernameAlreadyExistsException;
import com.example.AuthService.services.AuthService;
import com.example.AuthService.services.CookieService;
import com.example.AuthService.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final CookieService cookieService;
    private final String accessTokenCookie = "access_token";
    private final String refreshTokenCookie = "refresh_token";


    @PostMapping("/register")
    public ResponseEntity<ResponseMessage> register(@RequestBody RegisterRequestDTO registerRequestDTO) {
        try {userService.addUser(registerRequestDTO);}
        catch (UsernameAlreadyExistsException e) {
            return new ResponseEntity<>(ResponseMessage.USER_EXISTS,  HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(ResponseMessage.USER_CREATED, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseMessage> login(
            @RequestBody LoginRequestDTO loginRequestDTO,
            HttpServletResponse response
    ) {
        HashMap<String,String> tokens = null;
        try{
            tokens = authService.login(loginRequestDTO);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseMessage.BAD_REQUEST, HttpStatus.BAD_REQUEST);
        }
        response.addCookie(cookieService.addTokenToCookie(tokens.get("accessToken"),accessTokenCookie,600));
        response.addCookie(cookieService.addTokenToCookie(tokens.get("refreshToken"),refreshTokenCookie,3600));
        return new ResponseEntity<>(ResponseMessage.LOGGED_IN, HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<ResponseMessage> logout(
            @CookieValue(name = refreshTokenCookie, required = false) String refreshToken,
            HttpServletResponse response
    ) {
        response.addCookie(new Cookie(accessTokenCookie, null));
        response.addCookie(new Cookie(refreshTokenCookie, null));
        if (refreshToken != null){
            authService.logout(refreshToken);
        }
        return new ResponseEntity<>(ResponseMessage.LOGGED_OUT, HttpStatus.OK);
    }


    @PostMapping("/refresh")
    public ResponseEntity<ResponseMessage> refreshToken(
            @CookieValue(name = refreshTokenCookie) String refreshToken,
            HttpServletResponse response
            ) {
        String token = null;
        try{
            token = authService.generateAccessToken(refreshToken);
        }
        catch (RuntimeException e) {
            return new ResponseEntity<>(ResponseMessage.FORBIDDEN, HttpStatus.FORBIDDEN);
        }
        response.addCookie(cookieService.addTokenToCookie(token,accessTokenCookie, 600));
        return new ResponseEntity<>(ResponseMessage.REFRESHED, HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<Boolean> me() {
        return ResponseEntity.ok(true);
    }



}
