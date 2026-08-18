package com.example.demo.controllers;

import com.example.demo.DTO.AddUserRequestDTO;
import com.example.demo.DTO.SimpleUserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class BaseControllerHTML {

    @GetMapping("/home")
    public String home() {
        return "index";
    }


    @GetMapping("/register")
    public String register() {
        return "register_page";
    }

    @GetMapping("/login")
    public String login() {return "login_page";}

//    @PostMapping("add_user")
//    public ResponseEntity<SimpleUserDTO> addUser(@RequestBody AddUserRequestDTO addUserRequestDTO){
//        return ResponseEntity.ok(baseService.saveUsers(addUserRequestDTO));
//    }
//
//    @GetMapping("/users_list")
//    public ResponseEntity<List<SimpleUserDTO>> usersList(@RequestParam Integer scope){
//        List<SimpleUserDTO> usersList = baseService.usersListScope(scope);
//        return ResponseEntity.ok(usersList);
//    }

}
