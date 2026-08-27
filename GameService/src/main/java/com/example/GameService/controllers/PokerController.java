package com.example.GameService.controllers;

import com.example.GameService.dto.CardDTO;
import com.example.GameService.dto.DrawDTO;
import com.example.GameService.dto.StartGameDTO;
import com.example.GameService.dto.StartRoundDTO;
import com.example.GameService.engines.PokerEngine;
import com.example.GameService.exceptions.DeckNotFoundException;
import com.example.GameService.services.PokerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/poker")
@RequiredArgsConstructor
public class PokerController {

    private final PokerEngine pokerEngine;

    @PostMapping("/start-game")
    public ResponseEntity<StartGameDTO> start(@RequestBody StartGameDTO startGameDTO) {
        // pobrac userow z userservice
        return ResponseEntity.ok(pokerEngine.startGame(startGameDTO));
    }

    @PostMapping("/start-round")
    public ResponseEntity<StartRoundDTO> startRound(@RequestBody StartRoundDTO startRoundDTO) {
        return  ResponseEntity.ok(pokerEngine.startRound(startRoundDTO));
    }

    @PostMapping("/draw")
    public ResponseEntity<List<CardDTO>> draw(@RequestBody DrawDTO drawDTO) throws DeckNotFoundException {
        List<CardDTO> pack = pokerEngine.getCards(drawDTO);
        return ResponseEntity.ok(pack);
    }
}
