package com.example.demo.controllers;

import com.example.demo.DTO.CardsDTO;
import com.example.demo.services.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/game")
@CrossOrigin(origins = "http://localhost:4200")
public class GameController {

    private final GameService gameService;
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/tasuj")
    public ResponseEntity<List<CardsDTO>> tasuj(){
        List<CardsDTO> list = gameService.tasuj();
        return ResponseEntity.ok(list);
    }
}
