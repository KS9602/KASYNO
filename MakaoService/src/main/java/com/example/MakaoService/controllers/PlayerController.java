package com.example.MakaoService.controllers;

import com.example.MakaoService.dto.PlayerSummaryDTO;
import com.example.MakaoService.entities.PlayerModel;
import com.example.MakaoService.security.AuthenticatedUser;
import com.example.MakaoService.services.PlayerIdentityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/players")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerIdentityService playerIdentityService;

    @GetMapping("/me")
    public PlayerSummaryDTO me(Authentication authentication) {
        AuthenticatedUser user = (AuthenticatedUser) authentication.getPrincipal();
        PlayerModel player = playerIdentityService.resolvePlayer(user);
        return new PlayerSummaryDTO(player.getId(), player.getUsername());
    }
}
