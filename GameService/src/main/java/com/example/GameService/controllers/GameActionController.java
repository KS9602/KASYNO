package com.example.GameService.controllers;

import com.example.GameService.dto.game.GameStateResponse;
import com.example.GameService.dto.game.PlayCardRequest;
import com.example.GameService.services.GameActionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/games/{gameId}/actions")
@RequiredArgsConstructor
public class GameActionController extends BaseController {

    private final GameActionService gameActionService;

    @PostMapping("/play")
    public GameStateResponse playCards(
            @PathVariable Long gameId,
            @RequestBody PlayCardRequest request,
            Authentication authentication
    ) {
        return gameActionService.playCards(
                gameId,
                request.cardIds(),
                request.requestedRank(),
                request.requestedSuit(),
                authPlayer(authentication)
        );
    }

    @PostMapping("/draw")
    public GameStateResponse draw(
            @PathVariable Long gameId,
            Authentication authentication
    ) {
        return gameActionService.draw(
                gameId,
                authPlayer(authentication)
        );
    }

    @PostMapping("/pass")
    public GameStateResponse pass(
            @PathVariable Long gameId,
            Authentication authentication
    ) {
        return gameActionService.pass(
                gameId,
                authPlayer(authentication)
        );
    }

    @PostMapping("/makao")
    public GameStateResponse callMakao(
            @PathVariable Long gameId,
            Authentication authentication
    ) {
        return gameActionService.callMakao(
                gameId,
                authPlayer(authentication)
        );
    }

    @PostMapping("/stop-makao")
    public GameStateResponse stopMakao(
            @PathVariable Long gameId,
            @RequestParam Long targetPlayerId,
            Authentication authentication
    ) {
        return gameActionService.stopMakao(
                gameId,
                targetPlayerId,
                authPlayer(authentication)
        );
    }

}
