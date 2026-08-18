package com.example.demo.controllers;


import com.example.demo.DTO.UserRegisterDTO;
import com.example.demo.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.SimpleUserDTO;


@RestController
@RequestMapping("/users")
public class UsersController {

    private final UserService userService;
    public UsersController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/register")
    public ResponseEntity<SimpleUserDTO> registerUser(@RequestBody UserRegisterDTO userRegisterDTO){
        System.out.println("WEJSCIE");
        System.out.println(userRegisterDTO);
        SimpleUserDTO simpleUserDTO = userService.registerUser(userRegisterDTO);
        System.out.println("WYJSCIE");
        System.out.println(simpleUserDTO);
        return ResponseEntity.ok(simpleUserDTO);
    }


}
