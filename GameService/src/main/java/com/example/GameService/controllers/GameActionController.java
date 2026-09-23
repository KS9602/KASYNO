package com.example.GameService.controllers;

import com.example.GameService.dto.game.GameResponse;
import com.example.GameService.services.GameActionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/games/{gameId}/actions")
@RequiredArgsConstructor
public class GameActionController extends BaseController {

    private final GameActionService gameActionService;

//    @PostMapping("/play")
//    public GameResponse playCard(
//            @PathVariable Long gameId,
//            @RequestBody PlayCardRequest request,
//            Authentication authentication
//    ) {
//        return gameActionService.playCard(
//                gameId,
//                request,
//                authPlayer(authentication)
//        );
//    }

    @PostMapping("/draw")
    public GameResponse draw(
            @PathVariable Long gameId,
            Authentication authentication
    ) {
        return gameActionService.draw(
                gameId,
                authPlayer(authentication)
        );
    }


//            dealService.dealInitialCards(gameId, player);


}
