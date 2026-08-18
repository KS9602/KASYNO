package com.example.AuthService.controllers;

import com.example.AuthService.DTO.LoginRequestDTO;
import com.example.AuthService.DTO.AccessTokenDTO;
import com.example.AuthService.DTO.RegisterRequestDTO;
import com.example.AuthService.enums.ResponseMessage;
import com.example.AuthService.services.AuthService;
import com.example.AuthService.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private AuthService authService;
    private UserService userService;



    @PostMapping("/register")
    public ResponseEntity<ResponseMessage> addNewUser(@RequestBody RegisterRequestDTO registerRequestDTO) {
        userService.addUser(registerRequestDTO);
        return  new ResponseEntity<>(ResponseMessage.USER_CREATED, HttpStatus.OK);
    }

    @PostMapping("/login")
    public String authenticateAndGetToken(@RequestBody LoginRequestDTO loginRequestDTO) {
        return authService.generateToken(loginRequestDTO);
    }

    @PostMapping("/get-refresh")
    public String getRefreshToken(@RequestBody AccessTokenDTO accessTokenDTO) {
        return "";
    }


}
