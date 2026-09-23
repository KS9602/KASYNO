package com.example.GameService.controllers;


import com.example.GameService.dto.game.GameResponse;
import com.example.GameService.services.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/games")
@RequiredArgsConstructor
public class GameController extends BaseController {


    private final GameService gameService;

    @GetMapping("/{gameId}")
    public GameResponse getGame(
            @PathVariable Long gameId,
            Authentication authentication
    ) {
        return gameService.getGame(
                gameId,
                authPlayer(authentication)
        );
    }
    @GetMapping("/{gameId}/state")
    public GameResponse getGameState(
            @PathVariable Long gameId,
            Authentication authentication
    ) {
        return gameService.getGameState(
                gameId,
                authPlayer(authentication)
        );
    }

}
