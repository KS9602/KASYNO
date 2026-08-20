package com.example.AuthService.controllers;

import com.example.AuthService.DTO.LoginRequestDTO;
import com.example.AuthService.DTO.AccessTokenDTO;
import com.example.AuthService.DTO.RegisterRequestDTO;
import com.example.AuthService.enums.ResponseMessage;
import com.example.AuthService.enums.TokenType;
import com.example.AuthService.exceptions.UsernameAlreadyExistsException;
import com.example.AuthService.services.AuthService;
import com.example.AuthService.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;


    @PostMapping("/register")
    public ResponseEntity<ResponseMessage> addNewUser(@RequestBody RegisterRequestDTO registerRequestDTO) {
        try {userService.addUser(registerRequestDTO);}
        catch (UsernameAlreadyExistsException e) {
            return new ResponseEntity<>(ResponseMessage.USER_EXISTS,  HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(ResponseMessage.USER_CREATED, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseMessage> authenticateAndGetToken(
            @RequestBody LoginRequestDTO loginRequestDTO,
            HttpServletResponse response
    ) {
        String token = null;
        try{
            token =  authService.generateToken(loginRequestDTO);
        } catch (Exception e) {
            return new ResponseEntity<>(ResponseMessage.BAD_REQUEST, HttpStatus.BAD_REQUEST);
        }
        response.addCookie(addTokenToCookie(token));
        return new ResponseEntity<>(ResponseMessage.LOGGED_IN, HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<ResponseMessage> logout(
            @CookieValue(value = "token", required = false) String token,
            HttpServletResponse response
    ) {
        if (token == null) {
            return new ResponseEntity<>(ResponseMessage.LOGGED_OUT, HttpStatus.OK);
        }
        response.addCookie(new Cookie("token", null));
        authService.logout(token);
        return new ResponseEntity<>(ResponseMessage.LOGGED_OUT, HttpStatus.OK);
    }

    private Cookie addTokenToCookie (String token) {
        Cookie cookie = new Cookie("token", token);
        cookie.setMaxAge(3600);
        cookie.setHttpOnly(true);
        return cookie;
    }
}
